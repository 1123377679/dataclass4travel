package cn.lanqiao.dataclass4travel.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: 李某人
 * @Date: 2024/11/12/23:14
 * @Description: 分页工具类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageHelper<T> {
    //当前页码
    private Long pageNumber;
    //页面大小
    private Long pageSize;
    //总页数
    private Long pageCount;
    //总条数
    private Long totalCount;
    //通过sql查询出来的数据
    private List<T> records;

}
