package com.zscat.ums.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户登录参数
 * Created by zscat on 2018/4/26.
 */
@Getter
@Setter
public class UmsAdminParam {

    private String username;

    private String password;

    private String icon;

    private String email;

    private String nickName;

    private String note;
}
