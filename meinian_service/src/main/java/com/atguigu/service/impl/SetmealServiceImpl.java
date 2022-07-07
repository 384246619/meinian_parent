package com.atguigu.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.RedisConstant;
import com.atguigu.dao.SetmealDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.SetmealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;




    //将图片名称保存到Redis
    private void savePic2Redis(String pic){
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, pic);
    }

    //绑定套餐和跟团游的多对多关系
    private void setSetmealAndTravelGroup(Integer id, Integer[] travelgroupId) {
        for (Integer checkgroupId : travelgroupId) {
            Map<String, Integer> map = new HashMap<>();
            map.put("travelgroup_id", checkgroupId);
            map.put("setmeal_id", id);
            setmealDao.setSetmealAndTravelGroup(map);
        }
    }

    //新增套餐
    @Override
    public void add(Setmeal setmeal, Integer[] travelgroupIds) {
        setmealDao.add(setmeal);
        if(travelgroupIds != null && travelgroupIds.length > 0){
            //绑定套餐和跟团游的多对多关系
            setSetmealAndTravelGroup(setmeal.getId(), travelgroupIds);
        }
        //将图片名称保存到Redis
        savePic2Redis(setmeal.getImg());
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page<Setmeal> page = setmealDao.findPage(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    @Override
    public Setmeal findById(int id) {
        return setmealDao.findById(id);
    }

    @Override
    public List<Setmeal> getSetmeal() {
        return setmealDao.getSetmeal();
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }


}
