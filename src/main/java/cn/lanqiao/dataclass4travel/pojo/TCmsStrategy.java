package cn.lanqiao.dataclass4travel.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName t_cms_strategy
 */
@TableName(value ="t_cms_strategy")
@Data
@AllArgsConstructor
@NoArgsConstructor


public class TCmsStrategy implements Serializable {

    /**
     * 主键
     */
    @TableId
    private String id;

    /**
     * 添加人ID
     */
    private String addUserId;

    /**
     * 添加时间
     */
    private String addTime;

    /**
     * 删除标志
     */
    private long deleteStatus;

    /**
     * 修改人ID
     */
    private String modifyUserId;

    /**
     * 修改时间
     */
    private String modifyTime;

    /**
     * logo图片地址
     */
    private String imgUrl;

    /**
     * 主题
     */
    private String title;

    /**
     * 等级
     */
    private Integer rating;

    /**
     * 简介
     */
    private String summary;

    /**
     * 内容图片地址
     */
    private String introUrl;

    /**
     * 
     */
    private Integer state;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}