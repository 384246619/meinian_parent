package com.atguigu.dao;

import com.atguigu.pojo.Member;

import java.util.List;
import java.util.Map;

public interface MemberDao {
    // 添加会员
    public void add(Member member);

    // 根据手机号查询会员信息（唯一）
    public Member findByTelephone(String telephone);

    Integer findMemberCountBeforeDate(String regTime);

    int getTodayNewMember(String date);

    int getTotalMember();

    int getThisWeekAndMonthNewMember(String date);

}
