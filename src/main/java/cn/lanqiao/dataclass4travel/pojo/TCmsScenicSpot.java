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
@TableName("t_cms_scenic_spot")
public class TCmsScenicSpot {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    private String addUserId;
    private String addTime;
    private long deleteStatus;
    private String modifyUserId;
    private String modifyTime;
    private String spotName;
    private String spotIntro;
    private long spotStar;
    private String spotAddress;
    private String openTime;
    private long ticketsMessage;
    private long state;
    private String imgUrl;
}
