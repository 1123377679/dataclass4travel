package cn.lanqiao.dataclass4travel.pojo;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_cms_car")
public class TCmsCar {

  private String id;
  private String addUserId;
  private String addTime;
  private long deleteStatus;
  private String modifyUserId;
  private String modifyTime;
  private String title;
  private String startPlace;
  private String endPlace;
  private String startDateAndTime;
  private double needTime;
  private String gatherPlace;
  private long type;
  private String imgUrl;
  private long state;
  private String remark;
  private double price;

}
