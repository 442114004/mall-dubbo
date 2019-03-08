package com.zscat.oms.impl;


import com.zscat.oms.dto.CartProduct;
import com.zscat.oms.dto.CartPromotionItem;
import com.zscat.oms.mapper.OmsCartItemMapper;
import com.zscat.oms.mapper.PortalProductDao;
import com.zscat.oms.model.OmsCartItem;
import com.zscat.oms.model.OmsCartItemExample;
import com.zscat.oms.service.OmsCartItemService;
import com.zscat.oms.service.OmsPromotionService;
import com.zscat.pms.model.PmsProduct;
import com.zscat.ums.dto.SmsCouponHistoryDetail;
import com.zscat.ums.model.SmsCouponProductCategoryRelation;
import com.zscat.ums.model.SmsCouponProductRelation;
import com.zscat.ums.model.UmsMember;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 购物车管理Service实现类
 * Created by zscat on 2018/8/2.
 */
@Service("omsCartItemService")
public class OmsCartItemServiceImpl implements OmsCartItemService {
    @Resource
    private OmsCartItemMapper cartItemMapper;
    @Resource
    private PortalProductDao productDao;
    @Resource
    private OmsPromotionService promotionService;


    @Override
    public OmsCartItem add(OmsCartItem cartItem,PmsProduct pmsProduct,UmsMember currentMember) {
        cartItem.setMemberId(currentMember.getId());
        cartItem.setMemberNickname(currentMember.getNickname());
        cartItem.setDeleteStatus(0);
    //    PmsProduct pmsProduct = pmsProductMapper.selectByPrimaryKey(cartItem.getProductId());
        if (StringUtils.isEmpty(cartItem.getProductPic())){
            cartItem.setProductPic(pmsProduct.getPic());
        }
        cartItem.setProductBrand(pmsProduct.getBrandName());
        cartItem.setProductName(pmsProduct.getName());
        cartItem.setProductSn(pmsProduct.getProductSn());
        cartItem.setProductSubTitle(pmsProduct.getSubTitle());
        cartItem.setProductCategoryId(pmsProduct.getProductCategoryId());
        OmsCartItem existCartItem = getCartItem(cartItem);
        if (existCartItem == null) {
            cartItem.setCreateDate(new Date());
            cartItemMapper.insert(cartItem);
        } else {
            cartItem.setModifyDate(new Date());
            existCartItem.setQuantity(existCartItem.getQuantity() + cartItem.getQuantity());
            cartItemMapper.updateByPrimaryKey(existCartItem);
            return existCartItem;
        }
        return cartItem;
    }

    /**
     * 根据会员id,商品id和规格获取购物车中商品
     */
    private OmsCartItem getCartItem(OmsCartItem cartItem) {
        OmsCartItemExample example = new OmsCartItemExample();
        OmsCartItemExample.Criteria criteria = example.createCriteria().andMemberIdEqualTo(cartItem.getMemberId())
                .andProductIdEqualTo(cartItem.getProductId()).andDeleteStatusEqualTo(0);
        if (!StringUtils.isEmpty(cartItem.getSp1())) {
            criteria.andSp1EqualTo(cartItem.getSp1());
        }
        if (!StringUtils.isEmpty(cartItem.getSp2())) {
            criteria.andSp2EqualTo(cartItem.getSp2());
        }
        if (!StringUtils.isEmpty(cartItem.getSp3())) {
            criteria.andSp3EqualTo(cartItem.getSp3());
        }
        List<OmsCartItem> cartItemList = cartItemMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(cartItemList)) {
            return cartItemList.get(0);
        }
        return null;
    }

    @Override
    public List<OmsCartItem> list(Long memberId,List<Long> ids) {

        OmsCartItemExample example = new OmsCartItemExample();
        OmsCartItemExample.Criteria criteria = example.createCriteria().andDeleteStatusEqualTo(0).andMemberIdEqualTo(memberId);

        if (ids!=null && ids.size()>0){
            criteria.andIdIn(ids);
        }
        return cartItemMapper.selectByExample(example);
    }

    @Override
    public OmsCartItem selectById(Long id) {
        return cartItemMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<CartPromotionItem> listPromotion(Long memberId, List<Long> ids) {
        List<OmsCartItem> cartItemList = list(memberId,ids);
        List<CartPromotionItem> cartPromotionItemList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cartItemList)) {
            cartPromotionItemList = promotionService.calcCartPromotion(cartItemList);
        }
        return cartPromotionItemList;
    }

    @Override
    public int updateQuantity(Long id, Long memberId, Integer quantity) {
        OmsCartItem cartItem = new OmsCartItem();
        cartItem.setQuantity(quantity);
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andDeleteStatusEqualTo(0)
                .andIdEqualTo(id).andMemberIdEqualTo(memberId);
        return cartItemMapper.updateByExampleSelective(cartItem, example);
    }

    @Override
    public int delete(Long memberId, List<Long> ids) {
        OmsCartItem record = new OmsCartItem();
        record.setDeleteStatus(1);
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andIdIn(ids).andMemberIdEqualTo(memberId);
        return cartItemMapper.updateByExampleSelective(record, example);
    }

    @Override
    public CartProduct getCartProduct(Long productId) {
        return productDao.getCartProduct(productId);
    }

    @Override
    public int updateAttr(OmsCartItem cartItem) {
        //删除原购物车信息
        OmsCartItem updateCart = new OmsCartItem();
        updateCart.setId(cartItem.getId());
        updateCart.setModifyDate(new Date());
        updateCart.setDeleteStatus(1);
        cartItemMapper.updateByPrimaryKeySelective(updateCart);
        cartItem.setId(null);
        add(cartItem,null,null);
        return 1;
    }

    @Override
    public int clear(Long memberId) {
        OmsCartItem record = new OmsCartItem();
        record.setDeleteStatus(1);
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andMemberIdEqualTo(memberId);
        return cartItemMapper.updateByExampleSelective(record, example);
    }
    @Override
    public List<SmsCouponHistoryDetail> listCart(List<CartPromotionItem> cartItemList, Integer type,UmsMember currentMember) {
        Date now = new Date();
        //获取该用户所有优惠券
        List<SmsCouponHistoryDetail> allList = null;
                //couponHistoryDao.getDetailList(currentMember.getId());
        //根据优惠券使用类型来判断优惠券是否可用
        List<SmsCouponHistoryDetail> enableList = new ArrayList<>();
        List<SmsCouponHistoryDetail> disableList = new ArrayList<>();
        for (SmsCouponHistoryDetail couponHistoryDetail : allList) {
            Integer useType = couponHistoryDetail.getCoupon().getUseType();
            BigDecimal minPoint = couponHistoryDetail.getCoupon().getMinPoint();
            Date endTime = couponHistoryDetail.getCoupon().getEndTime();
            if (useType.equals(0)) {
                //0->全场通用
                //判断是否满足优惠起点
                //计算购物车商品的总价
                BigDecimal totalAmount = calcTotalAmount(cartItemList);
                if (now.before(endTime) && totalAmount.subtract(minPoint).intValue() >= 0) {
                    enableList.add(couponHistoryDetail);
                } else {
                    disableList.add(couponHistoryDetail);
                }
            } else if (useType.equals(1)) {
                //1->指定分类
                //计算指定分类商品的总价
                List<Long> productCategoryIds = new ArrayList<>();
                for (SmsCouponProductCategoryRelation categoryRelation : couponHistoryDetail.getCategoryRelationList()) {
                    productCategoryIds.add(categoryRelation.getProductCategoryId());
                }
                BigDecimal totalAmount = calcTotalAmountByproductCategoryId(cartItemList, productCategoryIds);
                if (now.before(endTime) && totalAmount.intValue() > 0 && totalAmount.subtract(minPoint).intValue() >= 0) {
                    enableList.add(couponHistoryDetail);
                } else {
                    disableList.add(couponHistoryDetail);
                }
            } else if (useType.equals(2)) {
                //2->指定商品
                //计算指定商品的总价
                List<Long> productIds = new ArrayList<>();
                for (SmsCouponProductRelation productRelation : couponHistoryDetail.getProductRelationList()) {
                    productIds.add(productRelation.getProductId());
                }
                BigDecimal totalAmount = calcTotalAmountByProductId(cartItemList, productIds);
                if (now.before(endTime) && totalAmount.intValue() > 0 && totalAmount.subtract(minPoint).intValue() >= 0) {
                    enableList.add(couponHistoryDetail);
                } else {
                    disableList.add(couponHistoryDetail);
                }
            }
        }
        if (type.equals(1)) {
            return enableList;
        } else {
            return disableList;
        }
    }

    private BigDecimal calcTotalAmount(List<CartPromotionItem> cartItemList) {
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartItemList) {
            BigDecimal realPrice = item.getPrice().subtract(item.getReduceAmount());
            total = total.add(realPrice.multiply(new BigDecimal(item.getQuantity())));
        }
        return total;
    }

    private BigDecimal calcTotalAmountByproductCategoryId(List<CartPromotionItem> cartItemList, List<Long> productCategoryIds) {
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartItemList) {
            if (productCategoryIds.contains(item.getProductCategoryId())) {
                BigDecimal realPrice = item.getPrice().subtract(item.getReduceAmount());
                total = total.add(realPrice.multiply(new BigDecimal(item.getQuantity())));
            }
        }
        return total;
    }

    private BigDecimal calcTotalAmountByProductId(List<CartPromotionItem> cartItemList, List<Long> productIds) {
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartItemList) {
            if (productIds.contains(item.getProductId())) {
                BigDecimal realPrice = item.getPrice().subtract(item.getReduceAmount());
                total = total.add(realPrice.multiply(new BigDecimal(item.getQuantity())));
            }
        }
        return total;
    }
}
