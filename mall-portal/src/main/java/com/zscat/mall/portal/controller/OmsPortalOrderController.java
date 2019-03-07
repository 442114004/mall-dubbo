package com.zscat.mall.portal.controller;

import com.macro.mall.dto.OmsOrderDetail;
import com.macro.mall.dto.OmsOrderQueryParam;
import com.zscat.cms.mapper.OmsOrderItemMapper;
import com.zscat.cms.model.OmsOrder;
import com.zscat.cms.model.OmsOrderItem;
import com.zscat.cms.model.OmsOrderItemExample;
import com.macro.mall.portal.constant.RedisKey;
import com.macro.mall.portal.domain.CommonResult;
import com.macro.mall.portal.domain.ConfirmOrderResult;
import com.macro.mall.portal.domain.OrderParam;
import com.macro.mall.portal.service.OmsOrderService;
import com.macro.mall.portal.service.OmsPortalOrderService;
import com.macro.mall.portal.service.RedisService;
import com.macro.mall.portal.service.UmsMemberService;
import com.macro.mall.portal.util.JsonUtil;
import com.macro.mall.portal.vo.TbThanks;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 订单管理Controller
 * Created by macro on 2018/8/30.
 */
@Controller
@Api(tags = "OmsPortalOrderController", description = "订单管理")
@RequestMapping("/api/order")
public class OmsPortalOrderController {
    @Autowired
    private OmsPortalOrderService portalOrderService;
    @Autowired
    private OmsOrderService orderService;
    @Autowired
    private UmsMemberService umsMemberService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private OmsOrderItemMapper orderItemMapper;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(OmsOrderQueryParam queryParam,
                       @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        queryParam.setMemberId(umsMemberService.getCurrentMember().getId());
        List<OmsOrder> orderList = orderService.list(queryParam, pageSize, pageNum);
        for (OmsOrder order : orderList){
            OmsOrderItemExample orderItemExample = new OmsOrderItemExample();
            orderItemExample.createCriteria().andOrderIdEqualTo(order.getId());
            List<OmsOrderItem> orderItemList = orderItemMapper.selectByExample(orderItemExample);
            order.setOrderItemList(orderItemList);
        }
        return new com.macro.mall.dto.CommonResult().pageSuccess(orderList);
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

        return new com.macro.mall.dto.CommonResult().success(orderDetailResult);
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
        ConfirmOrderResult confirmOrderResult = portalOrderService.generateConfirmOrder();
        return new CommonResult().success(confirmOrderResult);
    }


    @ResponseBody
    @GetMapping("/submitPreview")
    public Object submitPreview(@RequestParam Map<String, Object> params){
        try {
            ConfirmOrderResult result = portalOrderService.submitPreview(params);
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
        return portalOrderService.generateOrder(orderParam);
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
