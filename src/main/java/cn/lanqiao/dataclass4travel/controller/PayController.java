package cn.lanqiao.dataclass4travel.controller;

import cn.lanqiao.dataclass4travel.pojo.UserOrder;
import cn.lanqiao.dataclass4travel.service.PayServicee;
import cn.lanqiao.dataclass4travel.service.UserOrderService;

import cn.lanqiao.dataclass4travel.utils.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @BelongsProject: TravelDream
 * @BelongsPackage: com.travel.controller
 * @CreateTime: 2021-05-25 09:31
 * @Description: TODO
 */
@Controller
public class PayController {

    @Autowired
    UserOrderService userOrderService;

    @Autowired
    PayServicee payServicee;

    /**
     * 01-跳转到支付页面,远程调用位置统一下单接口，获取微信支付的二维码地址
     *
     * @return
     */
    @RequestMapping("/user_topayOrder/{id}")
    public String user_topayOrder(@PathVariable("id") String id, Model model) {
        //要支付的订单对象
        // id 调用微信支付的订单ID
        UserOrder userOrder = userOrderService.getById(id);
        //订单金额(微信支付，订单金额以分为单位)
        //double fee = userOrder.getFee();
        String fee = "1"; //模拟1分钱
        //String fee=String.valueOf(((int)(userOrder.getFee()))*100); // 6000.0
        System.out.println("待支付的金额是:"+fee);
        String payUrl = payServicee.callWeiXinCreateOrder(id, fee, userOrder.getProductName());
        System.out.println("微信支付的地址是:" + payUrl);
        model.addAttribute("payUrl", payUrl);
        model.addAttribute("order_code", userOrder.getOrderCode()); //给用显示的订单号
        model.addAttribute("fee", (Long.valueOf(fee) * 1.0) / 100); //订单金额
        model.addAttribute("id", id); //支付使用的订单号
        return "portal/pay";
    }

    /**
     * 检查订单号是否被支付
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/user_queryPayStatus/{id}")
    public CommonResult user_queryPayStatus(@PathVariable("id") String id) {
        CommonResult result = null;
        int x=0;
        while (true) {
            // 调用查询接口
            Map<String, String> map = payServicee.queryPayStatus(id);
            System.out.println("==============================微信接口返回的结果:======================================");
            System.out.println(map);
            if (map == null) {// 出错
                result = new CommonResult(500, "支付异常");
                break;
            }
            if (map.get("trade_state").equals("SUCCESS")) {// 如果成功
                result = new CommonResult(200, "支付成功");
                //更新数据库支付状态
                UserOrder userOrder = userOrderService.getById(id);
                userOrder.setState(1L);//已付款
                userOrderService.updateById(userOrder);
                break;
            }
            try {
                Thread.sleep(3000);// 间隔三秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //为了不让循环无休止地运行，我们定义一个循环变量，如果这个变量超过了这个值则退出循环，设置时间为5分钟
            x++;
            if(x>=100){
                result = new CommonResult(505, "二维码超时");
                break;
            }
        }
        return result;
    }


    /**
     * 02-跳转支付成功页面
     *
     * @return
     */
    @RequestMapping("/user_topaySuccess")
    public String topaySuccess() {
        return "portal/pay_success";
    }

    /**
     * 03-跳转支付失败页面
     *
     * @return
     */
    @RequestMapping("/user_topayFail")
    public String topayFail() {
        return "portal/pay_Fail";
    }


}
