package com.atguigu.service;


import com.atguigu.entity.PageResult;
import com.atguigu.pojo.TravelGroup;

import java.util.List;

public interface TravelGroupService {
    void add(TravelGroup travelGroup, Integer[] travelItemIds);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    List<Integer> findTravelItemIdByTravelgroupId(Integer id);

    TravelGroup findById(Integer id);

    void edit(Integer[] travelItemIds, TravelGroup travelGroup);

    void deleteById(Integer id);

    List<TravelGroup> findAll();
}
