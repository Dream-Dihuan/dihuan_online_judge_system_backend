package com.dihuan.model.entity.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JudgeConfig implements Serializable {

    private String language;

    private Long timeLimit;

    private Long memoryLimit;
}
