package cn.lanqiao.dataclass4travel.service;

import cn.lanqiao.dataclass4travel.pojo.OrderData;
import cn.lanqiao.dataclass4travel.pojo.UserOrder;
import com.baomidou.mybatisplus.extension.service.IService;


import java.util.List;

/**
 * @BelongsProject: TravelDream
 * @BelongsPackage: com.travel.service
 * @CreateTime: 2021-05-21 14:37
 * @Description: TODO
 */
public interface UserOrderService extends IService<UserOrder> {

    List<OrderData> getOrderDatas();
}
