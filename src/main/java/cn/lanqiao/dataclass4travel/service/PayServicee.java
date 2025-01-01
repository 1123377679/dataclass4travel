package cn.lanqiao.dataclass4travel.service;

import java.util.Map;

/**
 * @BelongsProject: TravelDream
 * @BelongsPackage: com.travel.service
 * @CreateTime: 2021-05-25 10:54
 * @Description: TODO
 */
public interface PayServicee {

    /**
     * 远程调用微信支付的统一下单接口
     * @param order_id
     * @param total_fee
     * @return
     */
    public String callWeiXinCreateOrder(String order_id, String total_fee,String description);

    /**
     * 检查订单是否支付 调用微信远程查询订单状态
     * @param id
     * @return
     */
    public Map<String, String> queryPayStatus(String id);

}
