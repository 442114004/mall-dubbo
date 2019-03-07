package com.zscat.mall.portal.controller;


import com.zscat.common.annotation.IgnoreAuth;
import com.zscat.common.result.CommonResult;
import com.zscat.mall.portal.constant.RedisKey;
import com.zscat.mall.portal.service.UmsService;
import com.zscat.mall.portal.util.JsonUtil;
import com.zscat.mall.portal.vo.IndexData;
import com.zscat.mall.portal.vo.R;
import com.zscat.mall.portal.vo.TArticleDO;
import com.zscat.oms.dto.OmsOrderQueryParam;
import com.zscat.oms.dto.OrderStatusCount;
import com.zscat.oms.model.OmsOrder;
import com.zscat.oms.service.OmsOrderService;
import com.zscat.pms.dto.PmsProductQueryParam;
import com.zscat.pms.model.PmsProductAttributeCategory;
import com.zscat.pms.service.PmsProductAttributeCategoryService;
import com.zscat.pms.service.PmsProductService;
import com.zscat.ums.model.SmsCoupon;
import com.zscat.ums.model.SmsHomeAdvertise;
import com.zscat.ums.model.UmsMember;
import com.zscat.ums.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员登录注册管理Controller
 * Created by zscat on 2018/8/3.
 */
@RestController
@Api(tags = "AppletMemberController", description = "会员登录注册管理")
@RequestMapping("/api/applet")
public class AppletMemberController extends ApiBaseAction {
    @Autowired
    private UmsService umsService;
    @Autowired
    private UmsMemberService memberService;
    @Autowired
    private SmsHomeAdvertiseService advertiseService;
    @Autowired
    private SmsCouponService couponService;
    @Autowired
    private PmsProductAttributeCategoryService productAttributeCategoryService;

    @Autowired
    private PmsProductService pmsProductService;


    @Autowired
    private OmsOrderService orderService;

    @Autowired
    private RedisService redisService;

    @Autowired
    UmsMemberCouponService umsMemberCouponService;

    @IgnoreAuth
    @ApiOperation("注册")
    @PostMapping("login_by_weixin")
    public Object loginByWeixin(HttpServletRequest req) {
        return umsService.loginByWeixin(req);

    }

    /**
     * 小程序主页
     *
     * @param
     * @return
     */
    @IgnoreAuth
    @GetMapping("/index")
    public R index() {

        List<TArticleDO> model_list = new ArrayList<>();
        List<TArticleDO> nav_icon_list = new ArrayList<>();
        IndexData data = new IndexData();
        R r = new R();
        try {
            TArticleDO a = new TArticleDO("banner");
            TArticleDO a1 = new TArticleDO("search");
            TArticleDO a2 = new TArticleDO("nav");
            TArticleDO a3 = new TArticleDO("cat");
            TArticleDO a4 = new TArticleDO("coupon");
            TArticleDO a5 = new TArticleDO("topic");
            TArticleDO b2 = new TArticleDO("block", "3");
            TArticleDO b1 = new TArticleDO("block", "4");
            TArticleDO b3 = new TArticleDO("block", "5");
            model_list.add(a);
            model_list.add(a1);
            model_list.add(a2);
            model_list.add(a3);
            model_list.add(a4);
            model_list.add(a5);
            model_list.add(b1);
            model_list.add(b2);
            model_list.add(b3);
            List<SmsHomeAdvertise> bannerList = null;
            String bannerJson = redisService.get(RedisKey.appletBannerKey+"2");
            if(bannerJson!=null){
                bannerList = JsonUtil.jsonToList(bannerJson,SmsHomeAdvertise.class);
            }else {
                bannerList = advertiseService.list(null, 2, null, 5, 1);
                redisService.set(RedisKey.appletBannerKey+"2",JsonUtil.objectToJson(bannerList));
                redisService.expire(RedisKey.appletBannerKey+"2",24*60*60);
            }
            List<SmsCoupon> couponList = new ArrayList<>();
            UmsMember umsMember = this.getCurrentMember();
            if (umsMember != null && umsMember.getId() != null) {
                 couponList = umsMemberCouponService.selectNotRecive(umsMember.getId());
            }


            TArticleDO c1 = new TArticleDO("我的公告", "/pages/topic-list/topic-list", "navigate", "http://www.91weiyi.xyz/addons/zjhj_mall/core/web/uploads/image/86/863a7db352a936743faf8edd5162bb5c.png");
            TArticleDO c2 = new TArticleDO("商品分类", "/pages/cat/cat", "switchTab", "http://www.91weiyi.xyz/addons/zjhj_mall/core/web/uploads/image/35/3570994c06e61b1f0cf719bdb52a0053.png");
            TArticleDO c3 = new TArticleDO("购物车", "/pages/cart/cart", "switchTab", "http://www.91weiyi.xyz/addons/zjhj_mall/core/web/uploads/image/c2/c2b01cf78f79cbfba192d5896eeaecbe.png");
            TArticleDO c4 = new TArticleDO("我的订单", "/pages/order/order?status=9", "navigate", "http://www.91weiyi.xyz/addons/zjhj_mall/core/web/uploads/image/7c/7c80acbbd479b099566cc6c3d34fbcb8.png");
            TArticleDO c5 = new TArticleDO("用户中心", "/pages/user/user", "switchTab", "http://www.91weiyi.xyz/addons/zjhj_mall/core/web/uploads/image/46/46eabbff1e7dc5e416567fc45d4d5df3.png");
            TArticleDO c6 = new TArticleDO("优惠劵", "/pages/coupon/coupon?status=0", "navigate", "http://www.91weiyi.xyz/addons/zjhj_mall/core/web/uploads/image/13/13312a6d56c202330f8c282d8cf84ada.png");
            TArticleDO c7 = new TArticleDO("我的收藏", "/pages/favorite/favorite", "navigate", "http://www.91weiyi.xyz/addons/zjhj_mall/core/web/uploads/image/ca/cab6d8d4785e43bd46dcbb52ddf66f61.png");
            TArticleDO c8 = new TArticleDO("售后订单", "/pages/order/order?status=4", "navigate", "http://www.91weiyi.xyz/addons/zjhj_mall/core/web/uploads/image/cf/cfb32a65d845b4e9a9778020ed2ccac6.png");
            nav_icon_list.add(c1);
            nav_icon_list.add(c2);
            nav_icon_list.add(c3);
            nav_icon_list.add(c4);
            nav_icon_list.add(c5);
            nav_icon_list.add(c6);
            nav_icon_list.add(c7);
            nav_icon_list.add(c8);


            List<PmsProductAttributeCategory> productAttributeCategoryList = null;
            String catJson = redisService.get(RedisKey.appletCategoryKey);
            if(catJson!=null){
                productAttributeCategoryList = JsonUtil.jsonToList(catJson,PmsProductAttributeCategory.class);
            }else {
                 productAttributeCategoryList = productAttributeCategoryService.getList(6, 1);
                for (PmsProductAttributeCategory gt : productAttributeCategoryList) {
                    PmsProductQueryParam productQueryParam = new PmsProductQueryParam();
                    productQueryParam.setProductAttributeCategoryId(gt.getId());
                    productQueryParam.setPageNum(1);productQueryParam.setPageSize(4);
                    gt.setGoodsList(pmsProductService.list(productQueryParam));
                }
                redisService.set(RedisKey.appletCategoryKey,JsonUtil.objectToJson(productAttributeCategoryList));
                redisService.expire(RedisKey.appletCategoryKey,24*60*60);
            }
          //  List<CmsSubject> subjectList = subjectService.listAll();
         //   data.setSubjectList(subjectList);
            data.setCat_goods_cols(2);
            data.setCat_list(productAttributeCategoryList);
            data.setNav_icon_list(nav_icon_list);
            data.setBanner_list(bannerList);
            data.setCoupon_list(couponList);
            data.setModule_list(model_list);
            r.put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
        return r;
    }

    @IgnoreAuth
    @GetMapping("/user")
    public Object user() {

        UmsMember umsMember = this.getCurrentMember();
        if (umsMember != null && umsMember.getId() != null) {
            OmsOrderQueryParam param = new OmsOrderQueryParam();
            param.setMemberId(umsMember.getId());
            List<OmsOrder> list = orderService.list(param,10000,1);
            int status0 =0;
            int status1= 0;
            int status2 =0;
            int status3 =0;
            int status4 =0;
            int status5 =0;
            OrderStatusCount count = new OrderStatusCount();
            for (OmsOrder consult : list){
                if (consult.getStatus()==0){
                    status0++;
                }
                    if (consult.getStatus()==1){
                        status1++;
                    }
                    if (consult.getStatus()==2){
                        status2++;
                    }
                    if (consult.getStatus()==3){
                        status2++;
                    }
                if (consult.getStatus()==4){
                    status4++;
                }
                if (consult.getStatus()==5){
                    status5++;
                }
            }
            count.setStatus0(status0);count.setStatus1(status1);count.setStatus2(status2);
            count.setStatus3(status3);count.setStatus4(status4);count.setStatus5(status5);
            Map<String,Object> objectMap = new HashMap<>();
            objectMap.put("user",umsMember);
            objectMap.put("count",count);
            return new CommonResult().success(objectMap);
        }
        return new CommonResult().failed();

    }
}
