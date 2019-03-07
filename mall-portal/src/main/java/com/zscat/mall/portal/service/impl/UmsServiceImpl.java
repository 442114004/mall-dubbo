package com.zscat.mall.portal.service.impl;

import com.zscat.mall.portal.config.WxAppletProperties;
import com.zscat.mall.portal.controller.ApiBaseAction;
import com.zscat.mall.portal.service.UmsService;
import com.zscat.mall.portal.util.CharUtil;
import com.zscat.mall.portal.util.CommonUtil;
import com.zscat.mall.portal.util.JsonUtils;
import com.zscat.mall.portal.util.JwtTokenUtil;
import com.zscat.ums.model.UmsMember;
import com.zscat.ums.model.UmsMemberLevel;
import com.zscat.ums.model.UmsMemberLevelExample;
import com.zscat.ums.service.UmsMemberLevelService;
import com.zscat.ums.service.UmsMemberService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页内容管理Service实现类
 * Created by macro on 2019/1/28.
 */
@Service
public class UmsServiceImpl implements UmsService {

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


    @Autowired
    private WxAppletProperties wxAppletProperties;

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
}
