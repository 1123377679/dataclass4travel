package cn.lanqiao.dataclass4travel.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("t_cms_travel_route")
public class TCmsTravelRoute {

  private String id;
  private String addUserId;
  private String addTime;
  private long deleteStatus;
  private String modifyUserId;
  private java.sql.Timestamp modifyTime;
  private String title;
  private String startSite;
  private String endSite;
  private String endTime;
  private String startTime;
  private long day;
  private String productCode;
  private double price;
  private long state;
  private String imgUrl;
  private String intro;


}
