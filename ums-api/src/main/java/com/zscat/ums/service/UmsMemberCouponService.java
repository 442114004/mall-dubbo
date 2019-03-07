package com.zscat.ums.service;

import com.zscat.common.result.CommonResult;
import com.zscat.ums.model.SmsCoupon;
import com.zscat.ums.model.SmsCouponHistory;
import com.zscat.ums.model.UmsMember;

import java.util.List;

/**
 * 用户优惠券管理Service
 * Created by zscat on 2018/8/29.
 */
public interface UmsMemberCouponService {
    /**
     * 会员添加优惠券
     */

    CommonResult add(Long couponId,UmsMember currentMember);

    /**
     * 获取优惠券列表
     *
     * @param useStatus 优惠券的使用状态
     */
    List<SmsCouponHistory> list(Integer useStatus,UmsMember currentMember);

    List<SmsCoupon> selectNotRecive(Long memberId);
    List<SmsCoupon> selectRecive(Long memberId);


}
