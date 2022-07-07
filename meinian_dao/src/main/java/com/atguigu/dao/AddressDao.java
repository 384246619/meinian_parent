package com.atguigu.dao;

import com.atguigu.pojo.Address;
import com.atguigu.pojo.Member;
import com.github.pagehelper.Page;

import java.util.List;

public interface AddressDao {
    List<Address> findAll();
    void addAddress(Address address);
    Page<Member> selectByCondition(String queryString);
    void deleteById(Integer id);
}
