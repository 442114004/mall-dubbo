package com.zscat.ums.impl;

import com.zscat.ums.mapper.UmsMemberReceiveAddressMapper;
import com.zscat.ums.model.UmsMember;
import com.zscat.ums.model.UmsMemberReceiveAddress;
import com.zscat.ums.model.UmsMemberReceiveAddressExample;
import com.zscat.ums.service.UmsMemberReceiveAddressService;
import com.zscat.ums.service.UmsMemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户地址管理Service实现类
 * Created by zscat on 2018/8/28.
 */
@Service("umsMemberReceiveAddressService")
public class UmsMemberReceiveAddressServiceImpl implements UmsMemberReceiveAddressService {
    @Resource
    private UmsMemberService memberService;
    @Resource
    private UmsMemberReceiveAddressMapper addressMapper;

    @Override
    public int add(UmsMemberReceiveAddress address,UmsMember currentMember) {

        address.setMemberId(currentMember.getId());
        if (this.list(currentMember)!=null && this.list(currentMember).size()>0){
            address.setDefaultStatus(0);
        }else {
            address.setDefaultStatus(1);
        }
        return addressMapper.insert(address);
    }

    @Override
    public int delete(Long id,UmsMember currentMember) {

        UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(currentMember.getId()).andIdEqualTo(id);
        return addressMapper.deleteByExample(example);
    }

    @Override
    public int update(Long id, UmsMemberReceiveAddress address,UmsMember currentMember) {
        address.setId(null);

        UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(currentMember.getId()).andIdEqualTo(id);
        return addressMapper.updateByExampleSelective(address, example);
    }

    @Override
    public List<UmsMemberReceiveAddress> list(UmsMember currentMember) {

        UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(currentMember.getId());
        return addressMapper.selectByExample(example);
    }

    @Override
    public UmsMemberReceiveAddress getItem(Long id,UmsMember currentMember) {

        UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(currentMember.getId()).andIdEqualTo(id);
        List<UmsMemberReceiveAddress> addressList = addressMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(addressList)) {
            return addressList.get(0);
        }
        return null;
    }

    @Override
    public UmsMemberReceiveAddress getDefaultItem(UmsMember currentMember) {

        UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(currentMember.getId()).andDefaultStatusEqualTo(1);
        List<UmsMemberReceiveAddress> addressList = addressMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(addressList)) {
            return addressList.get(0);
        }
        return null;
    }
    @Transactional
    @Override
    public int setDefault(Long id,UmsMember currentMember) {

        addressMapper.updateStatusByMember(currentMember.getId());

        UmsMemberReceiveAddress def = new UmsMemberReceiveAddress();
        def.setId(id);
        def.setDefaultStatus(1);
        addressMapper.updateByPrimaryKeySelective(def);
        return 1;
    }
}
