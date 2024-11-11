package cn.lanqiao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName t_pz_admin_user
 */
@TableName(value ="t_pz_admin_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TPzAdminUser implements Serializable {
    /**
     * 主键
     */
    @TableId
    private String ID;

    /**
     * 添加人ID
     */
    private String ADD_USER_ID;

    /**
     * 添加时间
     */
    private Date ADD_TIME;

    /**
     * 删除标志
     */
    private Integer DELETE_STATUS;

    /**
     * 修改人ID
     */
    private String MODIFY_USER_ID;

    /**
     * 修改时间
     */
    private Date MODIFY_TIME;

    /**
     * 用户名
     */
    private String USER_NAME;

    /**
     * 密码
     */
    private String PASSWORD;

    /**
     * 手机号码
     */
    private String LINK_TEL;

    /**
     * 真实姓名
     */
    private String NAME;

    /**
     * 状态
     */
    private Integer STATE;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}