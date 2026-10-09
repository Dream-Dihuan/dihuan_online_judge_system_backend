package com.dihuan.ai.tools;

import com.dihuan.common.result.DihuanPage;
import com.dihuan.common.localThread.TokenInfoHolder;
import com.dihuan.common.utils.jsonWebTokenUtils.TokenInfo;
import com.dihuan.model.vo.question.QuestionSubmitListItemVo;
import com.dihuan.model.vo.question.QuestionSubmitVo;
import com.dihuan.serviceClient.service.QuestionFeignClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class QuestionSubmissionTool {

	private static final int MAX_LIMIT = 100;

	private final QuestionFeignClient questionFeignClient;

	public QuestionSubmissionTool(QuestionFeignClient questionFeignClient) {
		this.questionFeignClient = questionFeignClient;
	}

	@Tool(description = "查询当前登录用户针对指定题目的最近提交记录。当用户询问提交历史、最近提交或提交列表时使用")
	public List<QuestionSubmitListItemVo> getRecentQuestionSubmissions(@ToolParam(description = "题目ID") Long questionId, @ToolParam(description = "获取条数") Integer limit) {
		Long userId = currentUserId();
		validateArguments(questionId, userId, limit);
		return queryRecentQuestionSubmissions(questionId, userId, limit);
	}

	@Tool(description = "查询当前登录用户的一条提交详情，包括代码、判题状态、判题结果和判题用例信息。当用户询问某个提交为什么出错时使用")
	public QuestionSubmitVo getQuestionSubmissionDetail(@ToolParam(description = "提交记录ID") Long submissionId) {
		if (submissionId == null || submissionId <= 0) {
			throw new IllegalArgumentException("submissionId must be a positive number");
		}
		QuestionSubmitVo submission = questionFeignClient.getQuestionSubmitInfo(submissionId);
		if (submission == null || !currentUserId().equals(submission.getUserId())) {
			throw new IllegalArgumentException("提交记录不存在或无权访问");
		}
		return submission;
	}

	private Long currentUserId() {
		TokenInfo tokenInfo = TokenInfoHolder.getTokenInfo();
		if (tokenInfo == null || tokenInfo.getId() == null || tokenInfo.getId() <= 0) {
			throw new IllegalStateException("当前请求未获取到登录用户信息");
		}
		return tokenInfo.getId();
	}

	private List<QuestionSubmitListItemVo> queryRecentQuestionSubmissions(Long questionId, Long userId, Integer limit) {
		DihuanPage<QuestionSubmitListItemVo> page = questionFeignClient.getQuestionSubmitList(userId, questionId, 0, limit);
		if (page == null || page.getRecords() == null) {
			return Collections.emptyList();
		}
		return page.getRecords();
	}

	private void validateArguments(Long questionId, Long userId, Integer limit) {
		if (questionId == null || questionId <= 0) {
			throw new IllegalArgumentException("questionId must be a positive number");
		}
		if (userId == null || userId <= 0) {
			throw new IllegalArgumentException("userId must be a positive number");
		}
		if (limit == null || limit <= 0 || limit > MAX_LIMIT) {
			throw new IllegalArgumentException("limit must be between 1 and " + MAX_LIMIT);
		}
	}
}
