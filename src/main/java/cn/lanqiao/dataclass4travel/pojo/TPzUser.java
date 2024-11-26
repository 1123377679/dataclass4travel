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
 * @since 2024-11-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_pz_user")
public class TPzUser implements Serializable {
    // 测试

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
     * 用户名
     */
    @TableField("USER_NAME")
    private String userName;

    /**
     * 密码
     */
    @TableField("PASSWORD")
    private String passWord;

    /**
     * 手机号码
     */
    @TableField("LINK_TEL")
    private String linkTel;

    /**
     * 真实姓名
     */
    @TableField("NAME")
    private String name;

    /**
     * 身份证
     */
    @TableField("IC_CODE")
    private String icCode;

    /**
     * 状态
     */
    @TableField("STATE")
    private Integer state;

    /**
     * 省份
     */
    @TableField("PROVINCE")
    private Integer province;


}
