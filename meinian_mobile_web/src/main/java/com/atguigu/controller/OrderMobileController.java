package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisMessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Order;
import com.atguigu.service.OrderService;
import com.atguigu.util.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderMobileController {

    @Reference
    private OrderService orderService;
    @Autowired
    private JedisPool jedisPool;

    /**
     * * 旅游预约
     * * @param map
     * * @return
     *
     */
    @RequestMapping("/submit")
    public Result submitOrder(@RequestBody Map map) {
        // ① 在页面获取手机号
        String telephone = (String) map.get("telephone");
        //② 在页面获取验证码
        String validateCode = (String) map.get("validateCode");
        //从Redis中获取缓存的验证码，key为手机号+RedisConstant.SENDTYPE_ORDER
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);

        //校验手机验证码
        if (codeInRedis == null || !codeInRedis.equals(validateCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        Result result = null;
        //调用旅游预约服务
        try {
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
        // 断点调试，查看map里面封装了哪些数据
            result = orderService.order(map);
        } catch (Exception e) {
            e.printStackTrace();
        //预约失败
            return result;
        }
        if (result.isFlag()) {
        //预约成功，发送短信通知，短信通知内容可以是“预约时间”，“预约人”，“预约地点”，“预约事项”等信息。
            String orderDate = (String) map.get("orderDate");
            try {
                SMSUtils.sendShortMessage(telephone, orderDate);
            } catch (ClientException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
