package com.zscat.mall.portal.service;

import javax.servlet.http.HttpServletRequest;

/**
 * 会员相关Service
 * Created by macro on 2019/1/28.
 */
public interface UmsService {

    Object loginByWeixin(HttpServletRequest req);

}
