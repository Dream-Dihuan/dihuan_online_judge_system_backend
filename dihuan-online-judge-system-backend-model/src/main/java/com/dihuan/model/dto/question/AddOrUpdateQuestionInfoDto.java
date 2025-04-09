package com.dihuan.model.dto.question;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dihuan.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 题目信息Dto
 * @TableName question
 */
@Data
@Schema(description = "题目信息Dto")
public class AddOrUpdateQuestionInfoDto extends BaseEntity {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "题目标题")
    private String title;

    @Schema(description = "题目内容")
    private String content;

    @Schema(description = "题目标签")
    private String tags;

    @Schema(description = "判题检查点")
    private String judgeCase;

    @Schema(description = "判题配置")
    private String judgeConfig;
}