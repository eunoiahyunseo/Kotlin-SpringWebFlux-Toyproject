package com.hyunseo.issueservice.service

import com.hyunseo.issueservice.domain.Comment
import com.hyunseo.issueservice.domain.CommentRepository
import com.hyunseo.issueservice.domain.IssueRepository
import com.hyunseo.issueservice.exception.NotFoundException
import com.hyunseo.issueservice.model.CommentRequest
import com.hyunseo.issueservice.model.CommentResponse
import com.hyunseo.issueservice.model.toResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author ihyeonseo
 */

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val issueRepository: IssueRepository,
) {
    @Transactional
    fun create(issueId: Long, userId: Long, username: String, request: CommentRequest): CommentResponse {
        val issue = issueRepository.findByIdOrNull(issueId) ?: throw NotFoundException("이슈가 존재하지 않습니다.")

        val comment = Comment(
            issue = issue,
            userId = userId,
            username = username,
            body = request.body,
        )

        issue.comments.add(comment)
        return commentRepository.save(comment).toResponse()
    }

    @Transactional
    fun edit(
        id: Long,
        userId: Long,
        request: CommentRequest
    ): CommentResponse? {
        return commentRepository.findByIdAndUserId(id, userId)?.run {
            body = request.body
            commentRepository.save(this).toResponse()
        }
    }

    @Transactional
    fun delete(
        issueId: Long,
        id: Long,
        userId: Long
    ) {
        val issue = issueRepository.findByIdOrNull(issueId) ?: throw NotFoundException("이슈가 존재하지 않습니다.")
        commentRepository.findByIdAndUserId(id, userId)
            ?.let(commentRepository::delete)
    }
}