package com.atguigu.dao;


import com.atguigu.pojo.TravelGroup;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface TravelGroupDao {

    void add(TravelGroup travelGroup);

    void setTravelGroupAndTravelItem(Map<String, Integer> map);

    Page<TravelGroup> findPage(String queryString);

    List<Integer> findTravelItemIdByTravelgroupId(Integer id);

    void deleteTravelGroupAndTravelItemByTravelGroupId(Integer id);

    void edit(TravelGroup travelGroup);

    TravelGroup findById(Integer id);

    long findCountByTravelGroupId(Integer id);

    void deleteById(Integer id);

    List<TravelGroup> findAll();

    List<TravelGroup> findTravelGroupListById(Integer id);
}
