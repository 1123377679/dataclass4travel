package cn.lanqiao.dataclass4travel.pojo;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_cms_insurance")
//1
public class TCmsInsurance {

  private String id;
  private String addUserId;
  private java.sql.Timestamp addTime;
  private long deleteStatus;
  private String modifyUserId;
  private java.sql.Timestamp modifyTime;
  private String title;
  private long insuranceCompany;
  private double price;
  private long type;
  private String resume;
  private long state;
  private String imgUrl;


}
