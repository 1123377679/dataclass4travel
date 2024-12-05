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
@TableName("t_pz_province")
public class Province {

  @TableId(value = "id",type = IdType.ASSIGN_UUID)
  private long id;

  private String province;

}
