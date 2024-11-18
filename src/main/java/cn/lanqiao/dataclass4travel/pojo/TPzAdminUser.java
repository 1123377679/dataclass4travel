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
@TableName("t_pz_admin_user")
public class TPzAdminUser {

  @TableId(type = IdType.ASSIGN_UUID)
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
    private long state;

}