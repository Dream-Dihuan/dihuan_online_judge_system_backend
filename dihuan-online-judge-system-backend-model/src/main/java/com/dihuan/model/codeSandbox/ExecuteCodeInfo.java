package com.dihuan.model.codeSandbox;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 判题信息
 */
@Data
@AllArgsConstructor
public class ExecuteCodeInfo {

    /**
     * 程序执行状态
     */
    private Boolean success;

    /**
     * 程序执行信息
     */
    private String output;

    /**
     * 消耗内存
     */
    private Long memory;

    /**
     * 消耗时间（KB）
     */
    private Long time;
}
