package cn.lanqiao.dataclass4travel.mapper;

import cn.lanqiao.dataclass4travel.pojo.OrderData;
import cn.lanqiao.dataclass4travel.pojo.UserOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


import java.util.List;

/**
 * @BelongsProject: TravelDream
 * @BelongsPackage: com.travel.mapper
 * @CreateTime: 2021-05-21 14:36
 * @Description: TODO
 */
public interface UserOrderMapper extends BaseMapper<UserOrder> {

    List<OrderData> getOrderDatas();
}
