package cn.lanqiao.dataclass4travel.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("t_cms_hotel")
public class Hotel {

  private String id;
  private String addUserId;
  private String addTime;
  private long deleteStatus;
  private String modifyUserId;
  private String modifyTime;
  private String hotelName;
  private String hotelIntro;
  private long hotelStar;
  private String linkPhone;
  private String address;
  private long state;
  private String imgUrl;
  private double price;

    /**
     *
     * @TableName t_cms_strategy
     */
    @TableName(value ="t_cms_strategy")
    @Data
    public static class TCmsStrategy implements Serializable {
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
        private Date addTime;

        /**
         * 删除标志
         */
        private Integer deleteStatus;

        /**
         * 修改人ID
         */
        private String modifyUserId;

        /**
         * 修改时间
         */
        private Date modifyTime;

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
}
