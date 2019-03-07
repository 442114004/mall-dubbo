package com.zscat.mall.portal.service;


import com.zscat.mall.portal.entity.MemberProductCollection;
import com.zscat.ums.model.UmsMember;

import java.util.List;

/**
 * 会员收藏Service
 * Created by zscat on 2018/8/2.
 */
public interface MemberCollectionService {
    int addProduct(MemberProductCollection productCollection,UmsMember umsMember);

    int deleteProduct(Long memberId, Long productId);

    List<MemberProductCollection> listProduct(Long memberId, int type);
}
