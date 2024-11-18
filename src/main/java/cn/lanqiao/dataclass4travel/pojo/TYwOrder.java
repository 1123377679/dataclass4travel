package cn.lanqiao.dataclass4travel.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TYwOrder {

  private String id;
  private String addUserId;
  private String addTime;
  private long deleteStatus;
  private String modifyUserId;
  private String modifyTime;
  private String userId;
  private String userName;
  private String productId;
  private String productName;
  private double fee;
  private long productType;
  private long state;
  private String orderCode;
  private String orderTime;
  private String setoffTime;
  private String linkTel;
  private long peopleCount;
  private String requirement;
  private String icCode;
  private String imgUrl;




}
