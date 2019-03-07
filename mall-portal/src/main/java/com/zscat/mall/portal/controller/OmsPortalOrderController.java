package com.zscat.mall.portal.controller;


import com.zscat.common.result.CommonResult;
import com.zscat.mall.portal.constant.RedisKey;
import com.zscat.mall.portal.service.OmsPortalOrderService;
import com.zscat.mall.portal.util.JsonUtil;
import com.zscat.oms.dto.*;
import com.zscat.oms.model.OmsOrder;
import com.zscat.oms.model.OmsOrderItem;
import com.zscat.oms.model.OmsOrderItemExample;
import com.zscat.oms.service.OmsOrderItemService;
import com.zscat.oms.service.OmsOrderService;
import com.zscat.ums.service.RedisService;
import com.zscat.ums.service.UmsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 订单管理Controller
 * Created by zscat on 2018/8/30.
 */
@Controller
@Api(tags = "OmsPortalOrderController", description = "订单管理")
@RequestMapping("/api/order")
public class OmsPortalOrderController extends ApiBaseAction{

    @Autowired
    private OmsOrderService orderService;
    @Autowired
    private UmsMemberService umsMemberService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private OmsOrderItemService orderItemService;

    @Resource
    private  OmsPortalOrderService portalOrderService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(OmsOrderQueryParam queryParam,
                       @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        queryParam.setMemberId(this.getCurrentMember().getId());
        List<OmsOrder> orderList = orderService.list(queryParam, pageSize, pageNum);
        for (OmsOrder order : orderList){
            OmsOrderItemExample orderItemExample = new OmsOrderItemExample();
            orderItemExample.createCriteria().andOrderIdEqualTo(order.getId());
            List<OmsOrderItem> orderItemList = orderItemService.selectByExample(orderItemExample);
            order.setOrderItemList(orderItemList);
        }
        return this.pageSuccess(orderList);
    }

    @ApiOperation("获取订单详情:订单信息、商品信息、操作记录")
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ResponseBody
    public Object detail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        OmsOrderDetail orderDetailResult = null;
        String bannerJson = redisService.get(RedisKey.PmsProductResult+id);
        if(bannerJson!=null){
            orderDetailResult = JsonUtil.jsonToPojo(bannerJson,OmsOrderDetail.class);
        }else {
            orderDetailResult = orderService.detail(id);
            redisService.set(RedisKey.PmsProductResult+id,JsonUtil.objectToJson(orderDetailResult));
            redisService.expire(RedisKey.PmsProductResult+id,10*60);
        }

        return new CommonResult().success(orderDetailResult);
    }

    /**
     *
     * @return
     */
    @Deprecated
    @ApiOperation("根据购物车信息生成确认单信息")
    @RequestMapping(value = "/generateConfirmOrder", method = RequestMethod.POST)
    @ResponseBody
    public Object generateConfirmOrder() {
        ConfirmOrderResult confirmOrderResult = portalOrderService.generateConfirmOrder(this.getCurrentMember());
        return new CommonResult().success(confirmOrderResult);
    }


    @ResponseBody
    @GetMapping("/submitPreview")
    public Object submitPreview(@RequestParam Map<String, Object> params){
        try {
            ConfirmOrderResult result = portalOrderService.submitPreview(params,this.getCurrentMember());
            return new CommonResult().success(result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 提交订单
     * @param orderParam
     * @return
     */
    @ApiOperation("根据购物车信息生成订单")
    @RequestMapping(value = "/generateOrder")
    @ResponseBody
    public Object generateOrder(OrderParam orderParam) {
        return portalOrderService.generateOrder(orderParam,this.getCurrentMember());
    }


    @RequestMapping(value = "/payOrder")
    @ApiOperation(value = "支付订单")
    @ResponseBody
    public Object payOrder(TbThanks tbThanks){
        int result=portalOrderService.payOrder(tbThanks);
        return new CommonResult().success(result);
    }

    @ApiOperation("自动取消超时订单")
    @RequestMapping(value = "/cancelTimeOutOrder", method = RequestMethod.POST)
    @ResponseBody
    public Object cancelTimeOutOrder() {
        return portalOrderService.cancelTimeOutOrder();
    }

    @ApiOperation("取消单个超时订单")
    @RequestMapping(value = "/cancelOrder", method = RequestMethod.POST)
    @ResponseBody
    public Object cancelOrder(Long orderId) {
        portalOrderService.sendDelayMessageCancelOrder(orderId);
        return new CommonResult().success(null);
    }
}
