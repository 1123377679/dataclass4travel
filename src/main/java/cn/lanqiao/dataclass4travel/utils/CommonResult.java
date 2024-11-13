package cn.lanqiao.dataclass4travel.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 李某人
 * @Date: 2024/11/13/21:49
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult {
    private Integer code;
    private String msg;
    private Object data;

    public CommonResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
