package com.zscat.mall.portal.controller;

import com.macro.mall.annotation.IgnoreAuth;
import com.macro.mall.dto.ConsultTypeCount;
import com.macro.mall.dto.PmsProductCategoryWithChildrenItem;
import com.macro.mall.dto.PmsProductQueryParam;
import com.macro.mall.dto.PmsProductResult;
import com.zscat.cms.model.PmsProductAttribute;
import com.zscat.cms.model.PmsProductAttributeCategory;
import com.zscat.cms.model.PmsProductConsult;
import com.zscat.cms.model.UmsMember;
import com.macro.mall.portal.constant.RedisKey;
import com.macro.mall.portal.domain.CommonResult;
import com.macro.mall.portal.domain.MemberProductCollection;
import com.macro.mall.portal.repository.MemberProductCollectionRepository;
import com.macro.mall.portal.service.*;
import com.macro.mall.portal.util.JsonUtil;
import com.macro.mall.portal.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页内容管理Controller
 * Created by macro on 2019/1/28.
 */
@RestController
@Api(tags = "GoodsController", description = "首页内容管理")
@RequestMapping("/api/pms")
public class PmsGoodsController {
    @Autowired
    private HomeService homeService;
    @Autowired
    private PmsProductAttributeCategoryService productAttributeCategoryService;
    @Autowired
    private SmsHomeAdvertiseService advertiseService;
    @Autowired
    private PmsProductService pmsProductService;
    @Autowired
    private PmsProductAttributeService productAttributeService;

    @Autowired
    private PmsProductCategoryService productCategoryService;
    @Autowired
    private CmsSubjectService subjectService;
    @Autowired
    private UmsMemberService memberService;
    @Autowired
    private PmsProductConsultService pmsProductConsultService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private MemberCollectionService memberCollectionService;
    @Autowired
    private MemberProductCollectionRepository productCollectionRepository;

    @IgnoreAuth
    @PostMapping(value = "/product/queryProductList")
    @ApiOperation(value = "查询商品列表")
    public R queryProductList(@RequestBody PmsProductQueryParam productQueryParam) {
        R r = new R();
        r.put("data", pmsProductService.list(productQueryParam, productQueryParam.getPageSize(), productQueryParam.getPageNum()));
        return r;
    }
    @IgnoreAuth
    @GetMapping(value = "/product/queryProductList1")
    public R queryProductList1(PmsProductQueryParam productQueryParam) {
        R r = new R();
        r.put("data", pmsProductService.list(productQueryParam, productQueryParam.getPageSize(), productQueryParam.getPageNum()));
        return r;
    }
    /**
     * 或者分类和分类下的商品
     *
     * @return
     */
    @IgnoreAuth
    @GetMapping("/getProductCategoryDto")
    public R getProductCategoryDtoByPid() {
        R r = new R();
        List<PmsProductAttributeCategory> productAttributeCategoryList = productAttributeCategoryService.getList(6, 1);
        for (PmsProductAttributeCategory gt : productAttributeCategoryList) {
            PmsProductQueryParam productQueryParam = new PmsProductQueryParam();
            productQueryParam.setProductAttributeCategoryId(gt.getId());
            gt.setGoodsList(pmsProductService.list(productQueryParam, 4, 1));
        }
        r.put("data", productAttributeCategoryList);
        return r;
    }

    /**
     * 查询所有一级分类及子分类
     *
     * @return
     */
    @IgnoreAuth
    @GetMapping("/listWithChildren")
    public Object listWithChildren() {
        List<PmsProductCategoryWithChildrenItem> list = productCategoryService.listWithChildren();
        return new CommonResult().success(list);
    }


    @IgnoreAuth
    @GetMapping(value = "/product/queryProductDetail")
    @ApiOperation( value = "查询商品详情信息")
    public Object queryProductDetail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        PmsProductResult productResult = pmsProductService.getUpdateInfo(id);
        UmsMember umsMember = memberService.getCurrentMember();
        if (umsMember != null && umsMember.getId() != null) {
            MemberProductCollection findCollection = productCollectionRepository.findByMemberIdAndProductId(
                    umsMember.getId(), id);
            if(findCollection!=null){
                productResult.setIs_favorite(1);
            }else{
                productResult.setIs_favorite(2);
            }
        }
        return new com.macro.mall.dto.CommonResult().success(productResult);
    }
    @IgnoreAuth
    @GetMapping(value = "/attr/list")
    public Object getList(@RequestParam(value = "cid", required = false, defaultValue = "0") Long cid,
                          @RequestParam(value = "type") Integer type,
                          @RequestParam(value = "pageSize", required = false,defaultValue = "5") Integer pageSize,
                          @RequestParam(value = "pageNum", required = false,defaultValue = "1") Integer pageNum) {
        List<PmsProductAttribute> productAttributeList = productAttributeService.getList(cid, type, pageSize, pageNum);
        return new com.macro.mall.dto.CommonResult().pageSuccess(productAttributeList);
    }

    @IgnoreAuth
    @ApiOperation("获取某个商品的评价")
    @RequestMapping(value = "/consult/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(@RequestParam(value = "goodsId", required = false, defaultValue = "0") Long goodsId,
                       @RequestParam(value = "pageNum", required = false,defaultValue = "1") Integer pageNum,
                       @RequestParam(value = "pageSize", required = false,defaultValue = "5") Integer pageSize) {

            PmsProductConsult productConsult = new PmsProductConsult();
            productConsult.setGoodsId(goodsId);
        List<PmsProductConsult> list = null;
        String consultJson = redisService.get(RedisKey.PmsProductConsult+goodsId);
        if(consultJson!=null){
            list = JsonUtil.jsonToList(consultJson,PmsProductConsult.class);
        }else {
            list = pmsProductConsultService.list(productConsult, 1,100000);
            redisService.set(RedisKey.PmsProductConsult+goodsId,JsonUtil.objectToJson(list));
            redisService.expire(RedisKey.PmsProductConsult+goodsId,24*60*60);
        }

        int goods =0;
        int general= 0;
        int bad =0;
        ConsultTypeCount count = new ConsultTypeCount();
        for (PmsProductConsult consult : list){
            if (consult.getStoreId()!=null){
                if (consult.getStoreId()==1){
                    goods++;
                }
                if (consult.getStoreId()==2){
                    general++;
                }
                if (consult.getStoreId()==3){
                    bad++;
                }
            }
        }
           count.setAll(goods+general+bad);
            count.setBad(bad);count.setGeneral(general);count.setGoods(goods);
            List<PmsProductConsult> productConsults = pmsProductConsultService.list(productConsult, pageNum,pageSize);
           Map<String,Object> objectMap = new HashMap<>();
           objectMap.put("list",productConsults);
           objectMap.put("count",count);


            return new CommonResult().success(objectMap);
    }
}
