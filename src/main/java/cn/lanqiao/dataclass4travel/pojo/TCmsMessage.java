package cn.lanqiao.dataclass4travel.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zyh
 * @since 2024-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_cms_message")
public class TCmsMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private String id;

    /**
     * 添加人ID
     */
    @TableField("ADD_USER_ID")
    private String addUserId;

    /**
     * 添加时间
     */
    @TableField("ADD_TIME")
    private LocalDateTime addTime;

    /**
     * 删除标志
     */
    @TableField("DELETE_STATUS")
    private Integer deleteStatus;

    /**
     * 修改人ID
     */
    @TableField("MODIFY_USER_ID")
    private String modifyUserId;

    /**
     * 修改时间
     */
    @TableField("MODIFY_TIME")
    private LocalDateTime modifyTime;

    /**
     * 用户ID
     */
    @TableField("USER_ID")
    private String userId;

    /**
     * 用户名
     */
    @TableField("USER_NAME")
    private String userName;

    /**
     * 真实姓名
     */
    @TableField("NAME")
    private String name;

    /**
     * 标题
     */
    @TableField("TITLE")
    private String title;

    /**
     * 内容
     */
    @TableField("CONTENT")
    private String content;

    /**
     * 状态
     */
    @TableField("STATE")
    private Integer state;


}
