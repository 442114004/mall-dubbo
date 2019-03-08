package com.macro.mall.portal;


import com.zscat.ums.model.UmsMember;
import com.zscat.ums.service.UmsMemberService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zscat on 2018/8/27.
 * 前台商品查询逻辑单元测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PortalProductDaoTests {
    @Resource
    private UmsMemberService memberService;

    @Test
    public void testGetPromotionProductList() {
        List<Long> ids = new ArrayList<>();
        ids.add(26L);
        ids.add(27L);
        ids.add(28L);
        ids.add(29L);
        UmsMember umsMember = memberService.selectByPrimaryKey(1L);
        System.out.println(umsMember);
    }
}
