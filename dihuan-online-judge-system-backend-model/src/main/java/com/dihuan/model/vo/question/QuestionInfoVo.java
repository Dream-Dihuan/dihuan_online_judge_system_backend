package com.dihuan.model.vo.question;

import com.baomidou.mybatisplus.annotation.TableField;
import com.dihuan.model.entity.question.JudgeConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "题目具体信息Vo")
public class QuestionInfoVo {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "题目标题")
    private String title;

    @Schema(description = "题目内容")
    private String content;

    @Schema(description = "题目标签")
    private String tags;

    @Schema(description = "判题配置")
    private List<JudgeConfig> judgeConfig;

    @Schema(description = "作者id")
    private Long authorId;

}
