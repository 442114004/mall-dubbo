package com.zscat.oms.impl;


import com.zscat.oms.mapper.OmsCompanyAddressMapper;
import com.zscat.oms.model.OmsCompanyAddress;
import com.zscat.oms.model.OmsCompanyAddressExample;
import com.zscat.oms.service.OmsCompanyAddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 收货地址管理Service实现类
 * Created by zscat on 2018/10/18.
 */
@Service("omsCompanyAddressService")
public class OmsCompanyAddressServiceImpl implements OmsCompanyAddressService {
    @Resource
    private OmsCompanyAddressMapper companyAddressMapper;

    @Override
    public List<OmsCompanyAddress> list() {
        return companyAddressMapper.selectByExample(new OmsCompanyAddressExample());
    }
}
