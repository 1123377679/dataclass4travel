package cn.lanqiao.dataclass4travel.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: 李某人
 * @Date: 2024/11/13/21:53
 * @Description:
 */
public class DateUtils {
    /**
     * 获取当前系统时间
     */
    public static String getNowTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }
}
