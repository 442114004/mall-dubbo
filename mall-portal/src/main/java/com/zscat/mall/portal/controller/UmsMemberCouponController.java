package com.zscat.mall.portal.controller;

import com.zscat.common.result.CommonResult;
import com.zscat.oms.dto.CartPromotionItem;
import com.zscat.oms.service.OmsCartItemService;
import com.zscat.oms.service.OmsOrderItemService;
import com.zscat.ums.dto.SmsCouponHistoryDetail;
import com.zscat.ums.model.SmsCoupon;
import com.zscat.ums.model.SmsCouponHistory;
import com.zscat.ums.model.UmsMember;
import com.zscat.ums.service.UmsMemberCouponService;
import com.zscat.ums.service.UmsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * 用户优惠券管理Controller
 * Created by zscat on 2018/8/29.
 */
@Controller
@Api(tags = "UmsMemberCouponController", description = "用户优惠券管理")
@RequestMapping("/api/member/coupon")
public class UmsMemberCouponController extends  ApiBaseAction{
    @Autowired
    private UmsMemberCouponService memberCouponService;
    @Autowired
    private OmsCartItemService cartItemService;
    @Autowired
    private UmsMemberService memberService;

    @Autowired
    UmsMemberCouponService umsMemberCouponService;

    @Resource
    private OmsCartItemService omsCartItemService;
    @ApiOperation("领取指定优惠券")
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add( Long couponId) {
        return memberCouponService.add(couponId,this.getCurrentMember());
    }

    @ApiOperation("获取用户优惠券列表")
    @ApiImplicitParam(name = "useStatus", value = "优惠券筛选类型:0->未使用；1->已使用；2->已过期",
            allowableValues = "0,1,2", paramType = "query", dataType = "integer")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(@RequestParam(value = "useStatus", required = false) Integer useStatus) {
        List<SmsCouponHistory> couponHistoryList = memberCouponService.list(useStatus,this.getCurrentMember());
        return new CommonResult().success(couponHistoryList);
    }

    /**
     * 所有可领取的优惠券
     * @return
     */
    @RequestMapping(value = "/alllist", method = RequestMethod.GET)
    @ResponseBody
    public Object alllist() {
        List<SmsCoupon> couponList = new ArrayList<>();
        UmsMember umsMember = this.getCurrentMember();
        if (umsMember != null && umsMember.getId() != null) {
            couponList = umsMemberCouponService.selectNotRecive(umsMember.getId());
        }
        return new CommonResult().success(couponList);
    }


    @ApiOperation("获取登录会员购物车的相关优惠券")
    @ApiImplicitParam(name = "type", value = "使用可用:0->不可用；1->可用",
            defaultValue = "1", allowableValues = "0,1", paramType = "query", dataType = "integer")
    @RequestMapping(value = "/list/cart/{type}", method = RequestMethod.GET)
    @ResponseBody
    public Object listCart(@PathVariable Integer type) {
        List<CartPromotionItem> cartPromotionItemList = cartItemService.listPromotion(this.getCurrentMember().getId(),null);
        List<SmsCouponHistoryDetail> couponHistoryList = omsCartItemService.listCart(cartPromotionItemList, type,this.getCurrentMember());
        return new CommonResult().success(couponHistoryList);
    }
}
