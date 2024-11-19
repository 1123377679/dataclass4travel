package cn.lanqiao.dataclass4travel.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
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
    @TableId(type = IdType.ASSIGN_UUID)
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
}
