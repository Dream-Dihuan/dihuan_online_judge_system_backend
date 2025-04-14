package com.dihuan.model.vo.question;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class QuestionNumberVo {
    @Schema(description = "题目ID")
    private Long questionId;

    @Schema(description = "题目标题")
    private String questionTitle;
}
