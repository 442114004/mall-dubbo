package com.macro.mall.portal.controller;

import com.macro.mall.dto.PmsProductCategoryWithChildrenItem;
import com.macro.mall.dto.PmsProductQueryParam;
import com.macro.mall.dto.PmsProductResult;
import com.macro.mall.model.*;
import com.macro.mall.portal.domain.CommonResult;
import com.macro.mall.portal.domain.HomeContentResult;
import com.macro.mall.portal.service.*;
import com.macro.mall.portal.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 首页内容管理Controller
 * Created by macro on 2019/1/28.
 */
@RestController
@Api(tags = "HomeController", description = "首页内容管理")
@RequestMapping("/home")
public class HomeController {
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

    @ApiOperation("首页内容页信息展示")
    @RequestMapping(value = "/content", method = RequestMethod.GET)
    public Object content() {
        HomeContentResult contentResult = homeService.content();
        return new CommonResult().success(contentResult);
    }

    @ApiOperation("分页获取推荐商品")
    @RequestMapping(value = "/recommendProductList", method = RequestMethod.GET)
    public Object recommendProductList(@RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsProduct> productList = homeService.recommendProductList(pageSize, pageNum);
        return new CommonResult().success(productList);
    }

    @ApiOperation("获取首页商品分类")
    @RequestMapping(value = "/productCateList/{parentId}", method = RequestMethod.GET)
    public Object getProductCateList(@PathVariable Long parentId) {
        List<PmsProductCategory> productCategoryList = homeService.getProductCateList(parentId);
        return new CommonResult().success(productCategoryList);
    }

    @ApiOperation("根据分类获取专题")
    @RequestMapping(value = "/subjectList", method = RequestMethod.GET)
    public Object getSubjectList(@RequestParam(required = false) Long cateId,
                                 @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                                 @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<CmsSubject> subjectList = homeService.getSubjectList(cateId, pageSize, pageNum);
        return new CommonResult().success(subjectList);
    }

    @GetMapping(value = "/subjectDetail")
    @ApiOperation(httpMethod = "get", value = "据分类获取专题")
    public Object subjectDetail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        CmsSubject cmsSubject = subjectService.selectByPrimaryKey(id);
        return new com.macro.mall.dto.CommonResult().success(cmsSubject);
    }

    @PostMapping(value = "/product/queryProductList")
    @ApiOperation(httpMethod = "POST", value = "查询商品列表")
    public R queryProductList(@RequestBody PmsProductQueryParam productQueryParam) {
        R r = new R();
        r.put("data", pmsProductService.list(productQueryParam, productQueryParam.getPageSize(), productQueryParam.getPageNum()));
        return r;
    }
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
    @GetMapping("/listWithChildren")
    public Object listWithChildren() {
        List<PmsProductCategoryWithChildrenItem> list = productCategoryService.listWithChildren();
        return new CommonResult().success(list);
    }

    /**
     * banner
     *
     * @return
     */
    @GetMapping("/bannerList")
    public Object bannerList(@RequestParam(value = "type", required = false, defaultValue = "10") Integer type) {
        List<SmsHomeAdvertise> bannerList = advertiseService.list(null, type, null, 5, 1);
        return new CommonResult().success(bannerList);
    }


    @GetMapping(value = "/product/queryProductDetail")
    @ApiOperation(httpMethod = "get", value = "查询商品详情信息")
    public Object queryProductDetail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        PmsProductResult productResult = pmsProductService.getUpdateInfo(id);
        return new com.macro.mall.dto.CommonResult().success(productResult);
    }

    @GetMapping(value = "/attr/list")
    public Object getList(@RequestParam(value = "cid", required = false, defaultValue = "0") Long cid,
                          @RequestParam(value = "type") Integer type,
                          @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsProductAttribute> productAttributeList = productAttributeService.getList(cid, type, pageSize, pageNum);
        return new com.macro.mall.dto.CommonResult().pageSuccess(productAttributeList);
    }
}
