package com.dihuan.model.vo.question;

import lombok.Data;

@Data
public class QuestionSubmitListItemVo {

    private Long questionId;


    private Long questionSubmitId;


    private String questionTitle;


    private String userName;


    private String language;


    private Integer judgeStatus;


    private Integer questionResult;


    private String createTime;

}
