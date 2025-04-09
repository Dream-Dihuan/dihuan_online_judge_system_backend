package com.dihuan.model.vo.question;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "题目信息概览Vo")
public class QuestionVo {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "题目状态 0-初试/1-正确/2-错误")
    private Integer status;

    @Schema(description = "题目标题")
    private String title;

    @Schema(description = "题目标签")
    private String tags;

    @Schema(description = "题目通过数")
    private Long acceptedNumber;

    @Schema(description = "题目提交数")
    private Long submitNumber;

    @Schema(description = "用户是否收藏")
    private Boolean collected;
}
