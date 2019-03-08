package com.zscat.mall.portal.service;

import com.zscat.common.result.CommonResult;
import com.zscat.ums.model.UmsMember;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 会员相关Service
 * Created by zscat on 2019/1/28.
 */
public interface UmsService {

    Object loginByWeixin(HttpServletRequest req);
    /**
     * 用户注册
     */

    String refreshToken(String token);

    CommonResult register(String username, String password, String telephone, String authCode);

    Object register(UmsMember umsMember);
    /**
     * 生成验证码
     */
    CommonResult generateAuthCode(String telephone);

    /**
     * 修改密码
     */
    CommonResult updatePassword(String telephone, String password, String authCode);

    Map<String, Object> login(String username, String password);
}
