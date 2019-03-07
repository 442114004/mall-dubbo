package com.zscat.oms.service;

import com.zscat.oms.dto.CartProduct;
import com.zscat.oms.dto.CartPromotionItem;
import com.zscat.oms.model.OmsCartItem;
import com.zscat.pms.model.PmsProduct;
import com.zscat.ums.dto.SmsCouponHistoryDetail;
import com.zscat.ums.model.UmsMember;

import java.util.List;

/**
 * 购物车管理Service
 * Created by zscat on 2018/8/2.
 */
public interface OmsCartItemService {
    /**
     * 查询购物车中是否包含该商品，有增加数量，无添加到购物车
     */

    OmsCartItem add(OmsCartItem cartItem,PmsProduct pmsProduct,UmsMember currentMember);

    /**
     * 根据会员编号获取购物车列表
     */
    List<OmsCartItem> list(Long memberId, List<Long> ids);

    OmsCartItem selectById(Long id);

    /**
     * 获取包含促销活动信息的购物车列表
     */
    List<CartPromotionItem> listPromotion(Long memberId, List<Long> ids);

    /**
     * 修改某个购物车商品的数量
     */
    int updateQuantity(Long id, Long memberId, Integer quantity);

    /**
     * 批量删除购物车中的商品
     */
    int delete(Long memberId, List<Long> ids);

    /**
     * 获取购物车中用于选择商品规格的商品信息
     */
    CartProduct getCartProduct(Long productId);

    /**
     * 修改购物车中商品的规格
     */

    int updateAttr(OmsCartItem cartItem);

    /**
     * 清空购物车
     */
    int clear(Long memberId);

    /**
     * 根据购物车信息获取可用优惠券
     */
    List<SmsCouponHistoryDetail> listCart(List<CartPromotionItem> cartItemList, Integer type,UmsMember currentMember);

}
