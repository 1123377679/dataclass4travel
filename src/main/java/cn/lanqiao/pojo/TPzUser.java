package cn.lanqiao.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TPzUser {

  private String id;
  private String addUserId;
  private String addTime;
  private long deleteStatus;
  private String modifyUserId;
  private String modifyTime;
  private String userName;
  private String password;
  private String linkTel;
  private String name;
  private String icCode;
  private long state;
  private long province;

}
