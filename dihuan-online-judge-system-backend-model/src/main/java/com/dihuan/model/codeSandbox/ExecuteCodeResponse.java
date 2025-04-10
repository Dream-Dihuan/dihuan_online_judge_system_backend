package com.dihuan.model.codeSandbox;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeResponse {

    private String message;

    private ExecuteCodeStatusEnum status;

    private List<ExecuteCodeInfo> executeCodeInfo;
}
