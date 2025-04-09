package com.dihuan.model.dto.question;

import com.baomidou.mybatisplus.annotation.TableField;
import com.dihuan.model.enums.question.JudgeStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "提交题目信息Dto")
public class QuestionSubmitDto {

    @Schema(description = "题目ID")
    private Long questionId;

    @Schema(description = "作答用户ID")
    private Long userId;

    @Schema(description = "作答代码语言名称")
    private String language;

    @Schema(description = "用户代码")
    private String code;
}
