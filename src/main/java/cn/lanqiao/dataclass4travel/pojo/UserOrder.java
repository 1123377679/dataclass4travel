package cn.lanqiao.dataclass4travel.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_yw_order")
public class UserOrder {

  @TableId(value = "id",type = IdType.ASSIGN_UUID)
  private String id;
  private String addUserId;
  private String addTime;
  private Long deleteStatus;
  private String modifyUserId;
  private String modifyTime;
  private String userId;
  private String userName;
  private String productId;
  private String productName;
  private double fee;
  private Long productType;
  private Long state;
  private String orderCode;
  private String orderTime;
  private String setoffTime;
  private String linkTel;
  private Long peopleCount;
  private String requirement;
  private String icCode;
  private String imgUrl;

}
