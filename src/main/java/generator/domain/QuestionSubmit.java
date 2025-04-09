package generator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 题目答案提交记录表
 * @TableName question_submit
 */
@TableName(value ="question_submit")
@Data
public class QuestionSubmit implements Serializable {
    /**
     * 题目提交记录ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 题目ID
     */
    @TableField(value = "question_id")
    private Long question_id;

    /**
     * 作答用户ID
     */
    @TableField(value = "user_id")
    private Long user_id;

    /**
     * 作答代码语言名称
     */
    @TableField(value = "language")
    private String language;

    /**
     * 用户代码
     */
    @TableField(value = "code")
    private String code;

    /**
     * 判题状态
     */
    @TableField(value = "judge_status")
    private String judge_status;

    /**
     * 判题结果信息
     */
    @TableField(value = "judge_result_info")
    private String judge_result_info;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date create_time;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date update_time;

    /**
     * 是否删除
     */
    @TableField(value = "is_deleted")
    private Integer is_deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}