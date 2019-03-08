package com.zscat.mall.portal.service.impl;

import com.zscat.common.result.CommonResult;
import com.zscat.mall.portal.config.WxAppletProperties;
import com.zscat.mall.portal.controller.ApiBaseAction;
import com.zscat.mall.portal.service.RedisService;
import com.zscat.mall.portal.service.UmsService;
import com.zscat.mall.portal.util.CharUtil;
import com.zscat.mall.portal.util.CommonUtil;
import com.zscat.mall.portal.util.JsonUtils;
import com.zscat.mall.portal.util.JwtTokenUtil;
import com.zscat.ums.model.UmsMember;
import com.zscat.ums.model.UmsMemberExample;
import com.zscat.ums.model.UmsMemberLevel;
import com.zscat.ums.model.UmsMemberLevelExample;
import com.zscat.ums.service.UmsMemberLevelService;
import com.zscat.ums.service.UmsMemberService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 首页内容管理Service实现类
 * Created by zscat on 2019/1/28.
 */
@Service
public class UmsServiceImpl implements UmsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UmsServiceImpl.class);

    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Value("${redis.key.prefix.authCode}")
    private String REDIS_KEY_PREFIX_AUTH_CODE;
    @Value("${authCode.expire.seconds}")
    private Long AUTH_CODE_EXPIRE_SECONDS;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Resource
    private UmsMemberService memberService;
    @Resource
    private UmsMemberLevelService memberLevelService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Override
    public Object loginByWeixin(HttpServletRequest req) {
        try {
            String code = req.getParameter("code");
            if (StringUtils.isEmpty(code)) {
                System.out.println("code is empty");
            }
            String userInfos = req.getParameter("userInfo");

            String signature = req.getParameter("signature");

            Map<String, Object> me = JsonUtils.readJsonToMap(userInfos);
            if (null == me) {
                return ApiBaseAction.toResponsFail("登录失败");
            }

            Map<String, Object> resultObj = new HashMap<String, Object>();
            //
            //获取openid
            String requestUrl = this.getWebAccess(code);//通过自定义工具类组合出小程序需要的登录凭证 code

            JSONObject sessionData = CommonUtil.httpsRequest(requestUrl, "GET", null);

            if (null == sessionData || StringUtils.isEmpty(sessionData.getString("openid"))) {
                return ApiBaseAction.toResponsFail("登录失败");
            }
            //验证用户信息完整性
            String sha1 = CommonUtil.getSha1(userInfos + sessionData.getString("session_key"));
            if (!signature.equals(sha1)) {
                return ApiBaseAction.toResponsFail("登录失败");
            }
            UmsMember userVo = memberService.queryByOpenId(sessionData.getString("openid"));
            String token = null;
            if (null == userVo) {
                UmsMember umsMember = new UmsMember();
                umsMember.setUsername("wxapplet" + CharUtil.getRandomString(12));
                umsMember.setSourceType(1);
                umsMember.setPassword(passwordEncoder.encodePassword("123456", null));
                umsMember.setCreateTime(new Date());
                umsMember.setStatus(1);
                umsMember.setWeixinOpenid(sessionData.getString("openid"));
                if (StringUtils.isEmpty(me.get("avatarUrl").toString())) {
                    //会员头像(默认头像)
                    umsMember.setIcon("/upload/img/avatar/01.jpg");
                } else {
                    umsMember.setIcon(me.get("avatarUrl").toString());
                }
                // umsMember.setGender(Integer.parseInt(me.get("gender")));
                umsMember.setNickname(me.get("nickName").toString());
                //获取默认会员等级并设置
                UmsMemberLevelExample levelExample = new UmsMemberLevelExample();
                levelExample.createCriteria().andDefaultStatusEqualTo(1);
                List<UmsMemberLevel> memberLevelList = memberLevelService.selectByExample(levelExample);
                if (!CollectionUtils.isEmpty(memberLevelList)) {
                    umsMember.setMemberLevelId(memberLevelList.get(0).getId());
                }
                memberService.insert(umsMember);
                token = jwtTokenUtil.generateToken(umsMember.getUsername());
                resultObj.put("userId", umsMember.getId());
            }else {
                token = jwtTokenUtil.generateToken(userVo.getUsername());
                resultObj.put("userId", userVo.getId());
            }


            if (StringUtils.isEmpty(token)) {
                return ApiBaseAction.toResponsFail("登录失败");
            }
            resultObj.put("tokenHead", tokenHead);
            resultObj.put("token", token);
            resultObj.put("userInfo", me);

            return ApiBaseAction.toResponsSuccess(resultObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Resource
    private RedisService redisService;

    @Autowired
    private WxAppletProperties wxAppletProperties;

    @Override
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> tokenMap = new HashMap<>();
        String token = null;
        //密码需要客户端加密后传递
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, passwordEncoder.encodePassword(password, null));
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UmsMember member = memberService.getByUsername(username);
            token = jwtTokenUtil.generateToken(userDetails);
            tokenMap.put("userInfo",member);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            LOGGER.warn("登录异常:{}", e.getMessage());
        }

        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);

        return tokenMap;

    }
    //替换字符串
    public String getCode(String APPID, String REDIRECT_URI, String SCOPE) {
        return String.format(wxAppletProperties.getGetCode(), APPID, REDIRECT_URI, SCOPE);
    }

    //替换字符串
    public String getWebAccess(String CODE) {

        return String.format(wxAppletProperties.getWebAccessTokenhttps(),
                wxAppletProperties.getAppId(),
                wxAppletProperties.getSecret(),
                CODE);
    }

    //替换字符串
    public String getUserMessage(String access_token, String openid) {
        return String.format(wxAppletProperties.getUserMessage(), access_token, openid);
    }

    @Override
    public CommonResult register(String username, String password, String telephone, String authCode) {

        //没有该用户进行添加操作
        UmsMember umsMember = new UmsMember();
        umsMember.setUsername(username);
        umsMember.setPhone(telephone);
        umsMember.setPassword(password);
        this.register(umsMember);
        return new CommonResult().success("注册成功", null);
    }
    @Override
    public CommonResult register(UmsMember user) {
        //验证验证码
        /*if (!verifyAuthCode(authCode, telephone)) {
            return new CommonResult().failed("验证码错误");
        }*/
        if (!user.getPassword().equals(user.getConfimpassword())){
            return new CommonResult().failed("密码不一致");
        }
        //查询是否已有该用户
        UmsMemberExample example = new UmsMemberExample();
        example.createCriteria().andUsernameEqualTo(user.getUsername());
        example.or(example.createCriteria().andPasswordEqualTo(passwordEncoder.encodePassword(user.getPassword(), null)));
        List<UmsMember> umsMembers = memberService.selectByExample(example);
        if (!CollectionUtils.isEmpty(umsMembers)) {
            return new CommonResult().failed("该用户已经存在");
        }
        //没有该用户进行添加操作
        UmsMember umsMember = new UmsMember();
        umsMember.setUsername(user.getUsername());
        umsMember.setPhone(user.getPhone());
        umsMember.setPassword(passwordEncoder.encodePassword(user.getPassword(), null));
        umsMember.setCreateTime(new Date());
        umsMember.setStatus(1);
        //获取默认会员等级并设置
        UmsMemberLevelExample levelExample = new UmsMemberLevelExample();
        levelExample.createCriteria().andDefaultStatusEqualTo(1);
        List<UmsMemberLevel> memberLevelList = memberLevelService.selectByExample(levelExample);
        if (!CollectionUtils.isEmpty(memberLevelList)) {
            umsMember.setMemberLevelId(memberLevelList.get(0).getId());
        }
        memberService.insert(umsMember);
        umsMember.setPassword(null);
        return new CommonResult().success("注册成功", null);
    }

    @Override
    public String refreshToken(String oldToken) {
        String token = oldToken.substring(tokenHead.length());
        if (jwtTokenUtil.canRefresh(token)) {
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }

    @Override
    public CommonResult generateAuthCode(String telephone) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        //验证码绑定手机号并存储到redis
        redisService.set(REDIS_KEY_PREFIX_AUTH_CODE + telephone, sb.toString());
        redisService.expire(REDIS_KEY_PREFIX_AUTH_CODE + telephone, AUTH_CODE_EXPIRE_SECONDS);
        return new CommonResult().success("获取验证码成功", sb.toString());
    }

    @Override
    public CommonResult updatePassword(String telephone, String password, String authCode) {
        UmsMemberExample example = new UmsMemberExample();
        example.createCriteria().andPhoneEqualTo(telephone);
        List<UmsMember> memberList = memberService.selectByExample(example);
        if (CollectionUtils.isEmpty(memberList)) {
            return new CommonResult().failed("该账号不存在");
        }
        //验证验证码
       /* if (!verifyAuthCode(authCode, telephone)) {
            return new CommonResult().failed("验证码错误");
        }*/
        UmsMember umsMember = memberList.get(0);
        umsMember.setPassword(passwordEncoder.encodePassword(password, null));
        memberService.updateByPrimaryKeySelective(umsMember);
        return new CommonResult().success("密码修改成功", null);
    }
}
