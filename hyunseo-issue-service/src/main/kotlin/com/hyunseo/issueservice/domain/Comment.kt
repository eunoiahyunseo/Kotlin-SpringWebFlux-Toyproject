package com.hyunseo.issueservice.domain

import jakarta.persistence.*
import jakarta.persistence.GenerationType.*

/**
 * @author ihyeonseo
 */

@Entity
@Table
class Comment(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id")
    val issue: Issue,

    @Column
    val userId: Long,

    @Column
    val username: String,

    @Column
    var body: String,
) : BaseEntity()