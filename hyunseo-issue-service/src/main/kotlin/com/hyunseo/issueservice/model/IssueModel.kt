package com.hyunseo.issueservice.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.hyunseo.issueservice.domain.Comment
import com.hyunseo.issueservice.domain.Issue
import com.hyunseo.issueservice.domain.enums.IssuePriority
import com.hyunseo.issueservice.domain.enums.IssueStatus
import com.hyunseo.issueservice.domain.enums.IssueType
import java.time.LocalDateTime

/**
 * @author ihyeonseo
 */

data class IssueRequest(
    val summary: String,
    val description: String,
    val type: IssueType,
    val priority: IssuePriority,
    val status: IssueStatus,
)

data class IssueResponse(
    val id: Long,
    val comments: List<CommentResponse> = emptyList(),
    val summary: String,
    val description: String,
    val userId: Long,
    val type: IssueType,
    val priority: IssuePriority,
    val status: IssueStatus,
    @JsonFormat(pattern = "yyyy-MM-dd MMM:mm:ss")
    val createdAt: LocalDateTime?,
    @JsonFormat(pattern = "yyyy-MM-dd MMM:mm:ss")
    val updatedAt: LocalDateTime?,
) {
   companion object {
       operator fun invoke(issue: Issue) =
           with(issue) {
               IssueResponse(
                   id = id!!,
                   comments = comments.sortedByDescending(Comment::id).map(Comment::toResponse),
                   summary = summary,
                   description = description,
                   userId = userId,
                   type = type,
                   priority = priority,
                   status = status,
                   createdAt = createdAt,
                   updatedAt = updatedAt,
               )
           }
   }
}