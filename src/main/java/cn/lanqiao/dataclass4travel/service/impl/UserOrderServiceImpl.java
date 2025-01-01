package cn.lanqiao.dataclass4travel.service.impl;

import cn.lanqiao.dataclass4travel.mapper.UserOrderMapper;
import cn.lanqiao.dataclass4travel.pojo.OrderData;
import cn.lanqiao.dataclass4travel.pojo.UserOrder;
import cn.lanqiao.dataclass4travel.service.UserOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @BelongsProject: TravelDream
 * @BelongsPackage: com.travel.service.impl
 * @CreateTime: 2021-05-21 14:37
 * @Description: TODO
 */
@Service
public class UserOrderServiceImpl extends ServiceImpl<UserOrderMapper, UserOrder> implements UserOrderService {

    @Resource
    UserOrderMapper userOrderMapper;

    @Override
    public List<OrderData> getOrderDatas() {
        return userOrderMapper.getOrderDatas();
    }
}
