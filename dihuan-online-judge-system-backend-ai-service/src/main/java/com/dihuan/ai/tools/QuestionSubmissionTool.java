package com.dihuan.ai.tools;

import com.dihuan.common.result.DihuanPage;
import com.dihuan.common.localThread.TokenInfoHolder;
import com.dihuan.common.utils.jsonWebTokenUtils.TokenInfo;
import com.dihuan.model.vo.question.QuestionSubmitListItemVo;
import com.dihuan.model.vo.question.QuestionSubmitVo;
import com.dihuan.serviceClient.service.QuestionFeignClient;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

@Component
public class QuestionSubmissionTool {

	private static final Logger log = LoggerFactory.getLogger(QuestionSubmissionTool.class);

	private static final int MAX_LIMIT = 100;

	/**
	 * ToolContext 中存放当前登录用户ID的键名。
	 * 由 AIThirdAPIService 在每次请求时写入。
	 */
	public static final String USER_ID_KEY = "userId";

	private final QuestionFeignClient questionFeignClient;

	public QuestionSubmissionTool(QuestionFeignClient questionFeignClient) {
		this.questionFeignClient = questionFeignClient;
	}

	@Tool(description = "查询当前登录用户针对指定题目的最近提交记录。当用户询问提交历史、最近提交或提交列表时使用")
	public String getRecentQuestionSubmissions(@ToolParam(description = "题目ID") Long questionId, @ToolParam(description = "获取条数") Integer limit, ToolContext toolContext) {
		Long userId = currentUserId(toolContext);
		log.info("[AI][TOOL] getRecentQuestionSubmissions start: userId={}, questionId={}, limit={}", userId, questionId, limit);
		validateArguments(questionId, userId, limit);
		String result = formatAsMarkdown(questionId, queryRecentQuestionSubmissions(questionId, userId, limit));
		log.info("[AI][TOOL] getRecentQuestionSubmissions complete: questionId={}, resultLength={}, result={}", questionId, result.length(), result);
		return result;
	}

	@Tool(description = "查询当前登录用户的一条提交详情，包括代码、判题状态、判题结果和判题用例信息。当用户询问某个提交为什么出错时使用")
	public QuestionSubmitVo getQuestionSubmissionDetail(@ToolParam(description = "提交记录ID") Long submissionId, ToolContext toolContext) {
		Long userId = currentUserId(toolContext);
		log.info("[AI][TOOL] getQuestionSubmissionDetail start: submissionId={}, userId={}", submissionId, userId);
		if (submissionId == null || submissionId <= 0) {
			throw new IllegalArgumentException("submissionId must be a positive number");
		}
		QuestionSubmitVo submission = questionFeignClient.getQuestionSubmitInfo(submissionId);
		if (submission == null || !userId.equals(submission.getUserId())) {
			throw new IllegalArgumentException("提交记录不存在或无权访问");
		}
		log.info("[AI][TOOL] getQuestionSubmissionDetail complete: submissionId={}, found=true", submissionId);
		return submission;
	}

	/**
	 * 解析当前登录用户ID。
	 *
	 * <p>优先从 Spring AI 的 {@link ToolContext} 获取：流式接口下工具回调运行在
	 * {@code Schedulers.boundedElastic()} 线程，无法读取 Tomcat 请求线程的 ThreadLocal。
	 * ThreadLocal 仅作为阻塞式调用的兜底。</p>
	 */
	private Long currentUserId(ToolContext toolContext) {
		Long userId = userIdFromToolContext(toolContext);
		if (userId != null) {
			return userId;
		}
		TokenInfo tokenInfo = TokenInfoHolder.getTokenInfo();
		if (tokenInfo != null && tokenInfo.getId() != null && tokenInfo.getId() > 0) {
			return tokenInfo.getId();
		}
		throw new IllegalStateException("当前请求未获取到登录用户信息");
	}

	private Long userIdFromToolContext(ToolContext toolContext) {
		if (toolContext == null || toolContext.getContext() == null) {
			return null;
		}
		Object value = toolContext.getContext().get(USER_ID_KEY);
		if (value instanceof Number number) {
			long userId = number.longValue();
			return userId > 0 ? userId : null;
		}
		if (value instanceof String text && !text.isBlank()) {
			try {
				long userId = Long.parseLong(text.trim());
				return userId > 0 ? userId : null;
			} catch (NumberFormatException exception) {
				log.warn("[AI][TOOL] invalid userId in tool context: {}", text);
			}
		}
		return null;
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

	private String formatAsMarkdown(Long questionId, List<QuestionSubmitListItemVo> submissions) {
		StringBuilder markdown = new StringBuilder("题目ID ")
				.append(questionId)
				.append(" 最近的提交记录：\n\n")
				.append("| 提交ID | 语言 | 判题状态 | 结果 | 提交时间 |\n")
				.append("| --- | --- | --- | --- | --- |\n");
		if (submissions.isEmpty()) {
			return markdown.append("| - | - | - | 暂无提交记录 | - |").toString();
		}

		for (QuestionSubmitListItemVo submission : submissions) {
			markdown.append("| ")
					.append(valueOrDash(submission.getQuestionSubmitId()))
					.append(" | ")
					.append(valueOrDash(submission.getLanguage()))
					.append(" | ")
					.append(judgeStatusText(submission.getJudgeStatus()))
					.append(" | ")
					.append(questionResultText(submission.getQuestionResult()))
					.append(" | ")
					.append(valueOrDash(submission.getCreateTime()))
					.append(" |\n");
		}
		return markdown.toString();
	}

	private String judgeStatusText(Integer status) {
		return switch (status == null ? -1 : status) {
			case 0 -> "等待判题";
			case 1 -> "判题中";
			case 2 -> "判题完成";
			default -> "未知状态";
		};
	}

	private String questionResultText(Integer result) {
		return switch (result == null ? -1 : result) {
			case 0 -> "未尝试";
			case 1 -> "通过";
			case 2 -> "未通过";
			default -> "未知结果";
		};
	}

	private String valueOrDash(Object value) {
		return value == null ? "-" : value.toString();
	}
}
