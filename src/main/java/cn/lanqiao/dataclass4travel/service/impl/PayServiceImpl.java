package cn.lanqiao.dataclass4travel.service.impl;

import cn.lanqiao.dataclass4travel.service.PayServicee;
import cn.lanqiao.dataclass4travel.utils.WeixinHttpClient;
import com.github.wxpay.sdk.WXPayUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @BelongsProject: TravelDream
 * @BelongsPackage: com.travel.service.impl
 * @CreateTime: 2021-05-25 10:55
 * @Description: TODO
 */
@Service
public class PayServiceImpl implements PayServicee {

    @Value("${appid}")
    private String appid;

    @Value("${partner}")
    private String partner;

    @Value("${partnerkey}")
    private String partnerkey;

    @Value("${notifyurl}")
    private String notifyurl;

    /**
     * 远程调用微信支付的统一下单接口
     *
     * @param order_id
     * @param total_fee
     * @return
     */
    @Override
    public String callWeiXinCreateOrder(String order_id, String total_fee, String description) {
        // 1.创建参数
        Map<String, String> param = new HashMap();// 创建参数
        param.put("appid", appid);// 公众号
        param.put("mch_id", partner);// 商户号
        param.put("nonce_str", WXPayUtil.generateNonceStr());// 随机字符串
        param.put("body", description);// 商品描述
        param.put("out_trade_no", order_id);// 商户订单号
        param.put("total_fee", total_fee);// 总金额（分）
        param.put("spbill_create_ip", "127.0.0.1");// IP
        param.put("notify_url", notifyurl);// 回调地址(随便写)
        param.put("trade_type", "NATIVE");// 交易类型
        try {
            // 2.使用微信SDK工具类把Map格式的参数转为XML格式，因为微信支付接口接收XML格式！
            String xmlParam = WXPayUtil.generateSignedXml(param, partnerkey);
            WeixinHttpClient client = new WeixinHttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            client.setHttps(true);
            client.setXmlParam(xmlParam);
            client.post();

            // 3.获得微信接口返回结果（微信返回的结果是XML格式的，程序方便使用就把XXML再转为MAP）
            String result = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(result);
            return resultMap.get("code_url");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public Map<String, String> queryPayStatus(String id) {
        Map param = new HashMap();
        param.put("appid", appid);// 公众账号ID
        param.put("mch_id", partner);// 商户号
        param.put("out_trade_no", id);// 订单号
        param.put("nonce_str", WXPayUtil.generateNonceStr());// 随机字符串

        String url = "https://api.mch.weixin.qq.com/pay/orderquery";
        try {
            String xmlParam = WXPayUtil.generateSignedXml(param, partnerkey);
            WeixinHttpClient client = new WeixinHttpClient(url);
            client.setHttps(true);
            client.setXmlParam(xmlParam);
            client.post();
            //接口的返回结果
            String result = client.getContent();
            Map<String, String> map = WXPayUtil.xmlToMap(result);
            System.out.println(map);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
