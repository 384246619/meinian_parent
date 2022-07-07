package com.atguigu.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.TravelGroupDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.service.TravelGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = TravelGroupService.class)
@Transactional
public class TravelGroupServiceImpl implements TravelGroupService {

    @Autowired
    private TravelGroupDao travelGroupDao;

    @Override
    public void add(TravelGroup travelGroup, Integer[] travelItemIds) {
        // 1 新增跟团游，想t_travelgroup中添加数据，新增后返回新增的id
        travelGroupDao.add(travelGroup);
        // 2 新增跟团游和自由行中间表t_travelgroup_travelitem新增数据(新增几条，由travelItemIds决定)
        setTravelGroupAndTravelItem(travelGroup.getId(), travelItemIds);
    }

    private void setTravelGroupAndTravelItem(Integer travelGroupId, Integer[] travelItemIds) {
        // 新增几条数据，由travelItemIds的长度决定
        if (travelItemIds != null && travelItemIds.length > 0){
            for (Integer travelItemId : travelItemIds) {
                // 如果有多个参数使用map
                Map<String, Integer> map = new HashMap<>();
                map.put("travelGroup", travelGroupId);
                map.put("travelItem", travelItemId);
                travelGroupDao.setTravelGroupAndTravelItem(map);
            }
        }
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        // 使用分页插件PageHelper，设置当前页，每页最多显示的记录数
        PageHelper.startPage(currentPage,pageSize);
        // 响应分页插件的Page对象
        Page<TravelGroup> page = travelGroupDao.findPage(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public TravelGroup findById(Integer id) {
        return travelGroupDao.findById(id);
    }

    @Override
    public List<Integer> findTravelItemIdByTravelgroupId(Integer id) {
        return travelGroupDao.findTravelItemIdByTravelgroupId(id);
    }

    @Override
    public void edit(Integer[] travelitemIds, TravelGroup travelGroup) {
        // 1：修改跟团游的基本信息
        travelGroupDao.edit(travelGroup);
        /**
         * 2：修改跟团游和自由行的中间表（先删除，再创建）
         * 之前的数据删除
         * 再新增页面选中的数据
         */
        // 删除之前中间表的数据
        travelGroupDao.deleteTravelGroupAndTravelItemByTravelGroupId(travelGroup.getId());
        // 再新增页面选中的数据
        setTravelGroupAndTravelItem(travelGroup.getId(), travelitemIds);
    }

    @Override
    public void deleteById(Integer id) {
        // 在删除根游之前，先判断自由行的id，在中间表中是否存在数据
        long count =  travelGroupDao.findCountByTravelGroupId(id);
        // 中间表如果有数据，不要往后面执行，直接抛出异常
        // 如果非要删除也可以：delete from t_travelgroup_travelitem where travelitem_id = 1
        if (count > 0){
            throw new RuntimeException("不允许删除");
        }
        // 使用自由行的id进行删除
        travelGroupDao.deleteById(id);
    }

    @Override
    public List<TravelGroup> findAll() {
        return travelGroupDao.findAll();
    }
}
