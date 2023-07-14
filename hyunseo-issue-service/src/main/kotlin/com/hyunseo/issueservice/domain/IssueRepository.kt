package com.hyunseo.issueservice.domain

import com.hyunseo.issueservice.domain.enums.IssueStatus
import org.springframework.data.jpa.repository.JpaRepository

/**
 * @author ihyeonseo
 */
interface IssueRepository: JpaRepository<Issue, Long> {
    fun findAllByStatusOrderByCreatedAtDesc(status: IssueStatus): List<Issue>?
}