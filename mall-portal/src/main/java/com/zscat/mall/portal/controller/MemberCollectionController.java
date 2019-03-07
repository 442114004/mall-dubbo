package com.zscat.mall.portal.controller;

import com.zscat.common.result.CommonResult;
import com.zscat.mall.portal.entity.MemberProductCollection;
import com.zscat.mall.portal.service.MemberCollectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;



/**
 * 会员收藏管理Controller
 * Created by zscat on 2018/8/2.
 */
@Controller
@Api(tags = "MemberCollectionController", description = "会员收藏管理")
@RequestMapping("/api/collection")
public class MemberCollectionController extends ApiBaseAction{
    @Autowired
    private MemberCollectionService memberCollectionService;

    @ApiOperation("添加商品收藏")
    @ResponseBody
    @RequestMapping("favorite-add")
    public Object addProduct(MemberProductCollection productCollection) {
        int count = memberCollectionService.addProduct(productCollection,this.getCurrentMember());
        if (count > 0) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @ApiOperation("删除收藏商品")
    @RequestMapping("/favorite-remove")
    @ResponseBody
    public Object deleteProduct(Long memberId, Long productId) {
        int count = memberCollectionService.deleteProduct(memberId, productId);
        if (count > 0) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @ApiOperation("显示关注列表")
    @RequestMapping(value = "/listCollect", method = RequestMethod.GET)
    @ResponseBody
    public Object listProduct( Long memberId) {
        List<MemberProductCollection> memberProductCollectionList = memberCollectionService.listProduct(memberId,1);
        return new CommonResult().success(memberProductCollectionList);
    }
    @ApiOperation("显示关注列表")
    @RequestMapping(value = "/topic-listCollect", method = RequestMethod.GET)
    @ResponseBody
    public Object listTopic( Long memberId) {
        List<MemberProductCollection> memberProductCollectionList = memberCollectionService.listProduct(memberId,2);
        return new CommonResult().success(memberProductCollectionList);
    }
}
