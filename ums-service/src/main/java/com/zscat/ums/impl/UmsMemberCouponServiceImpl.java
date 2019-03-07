package com.zscat.ums.impl;

import com.zscat.common.result.CommonResult;
import com.zscat.ums.mapper.SmsCouponHistoryDao;
import com.zscat.ums.mapper.SmsCouponHistoryMapper;
import com.zscat.ums.mapper.SmsCouponMapper;
import com.zscat.ums.model.SmsCoupon;
import com.zscat.ums.model.SmsCouponHistory;
import com.zscat.ums.model.SmsCouponHistoryExample;
import com.zscat.ums.model.UmsMember;
import com.zscat.ums.service.UmsMemberCouponService;
import com.zscat.ums.service.UmsMemberService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 会员优惠券管理Service实现类
 * Created by zscat on 2018/8/29.
 */
@Service("umsMemberCouponService")
public class UmsMemberCouponServiceImpl implements UmsMemberCouponService {
    @Resource
    private UmsMemberService memberService;
    @Resource
    private SmsCouponMapper couponMapper;
    @Resource
    private SmsCouponHistoryMapper couponHistoryMapper;
    @Resource
    private SmsCouponHistoryDao couponHistoryDao;

    @Override
    public List<SmsCoupon> selectNotRecive(Long memberId){
        return couponMapper.selectNotRecive(memberId);
    }
    @Override
    public List<SmsCoupon> selectRecive(Long memberId){
        return  couponMapper.selectRecive(memberId);
    }


    @Override
    public CommonResult add(Long couponId,UmsMember currentMember ) {

        //获取优惠券信息，判断数量
        SmsCoupon coupon = couponMapper.selectByPrimaryKey(couponId);
        if (coupon == null) {
            return new CommonResult().failed("优惠券不存在");
        }
        if (coupon.getCount() <= 0) {
            return new CommonResult().failed("优惠券已经领完了");
        }
        Date now = new Date();
        if (now.before(coupon.getEnableTime())) {
            return new CommonResult().failed("优惠券还没到领取时间");
        }
        //判断用户领取的优惠券数量是否超过限制
        SmsCouponHistoryExample couponHistoryExample = new SmsCouponHistoryExample();
        couponHistoryExample.createCriteria().andCouponIdEqualTo(couponId).andMemberIdEqualTo(currentMember.getId());
        int count = couponHistoryMapper.countByExample(couponHistoryExample);
        if (count >= coupon.getPerLimit()) {
            return new CommonResult().failed("您已经领取过该优惠券");
        }
        //生成领取优惠券历史
        SmsCouponHistory couponHistory = new SmsCouponHistory();
        couponHistory.setCouponId(couponId);
        couponHistory.setCouponCode(generateCouponCode(currentMember.getId()));
        couponHistory.setCreateTime(now);
        couponHistory.setMemberId(currentMember.getId());
        couponHistory.setMemberNickname(currentMember.getNickname());
        //主动领取
        couponHistory.setGetType(1);
        //未使用
        couponHistory.setUseStatus(0);
        couponHistoryMapper.insert(couponHistory);
        //修改优惠券表的数量、领取数量
        coupon.setCount(coupon.getCount() - 1);
        coupon.setReceiveCount(coupon.getReceiveCount() == null ? 1 : coupon.getReceiveCount() + 1);
        couponMapper.updateByPrimaryKey(coupon);
        return new CommonResult().success("领取成功", null);
    }

    /**
     * 16位优惠码生成：时间戳后8位+4位随机数+用户id后4位
     */
    private String generateCouponCode(Long memberId) {
        StringBuilder sb = new StringBuilder();
        Long currentTimeMillis = System.currentTimeMillis();
        String timeMillisStr = currentTimeMillis.toString();
        sb.append(timeMillisStr.substring(timeMillisStr.length() - 8));
        for (int i = 0; i < 4; i++) {
            sb.append(new Random().nextInt(10));
        }
        String memberIdStr = memberId.toString();
        if (memberIdStr.length() <= 4) {
            sb.append(String.format("%04d", memberId));
        } else {
            sb.append(memberIdStr.substring(memberIdStr.length() - 4));
        }
        return sb.toString();
    }

    @Override
    public List<SmsCouponHistory> list(Integer useStatus,UmsMember currentMember) {

        SmsCouponHistoryExample couponHistoryExample = new SmsCouponHistoryExample();
        SmsCouponHistoryExample.Criteria criteria = couponHistoryExample.createCriteria();
        criteria.andMemberIdEqualTo(currentMember.getId());
        if (useStatus != null) {
            criteria.andUseStatusEqualTo(useStatus);
        }
        return couponHistoryMapper.selectByExample(couponHistoryExample);
    }



}
