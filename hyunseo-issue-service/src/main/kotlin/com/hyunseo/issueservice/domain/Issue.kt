package com.hyunseo.issueservice.domain

import com.hyunseo.issueservice.domain.enums.IssuePriority
import com.hyunseo.issueservice.domain.enums.IssueStatus
import com.hyunseo.issueservice.domain.enums.IssueType
import jakarta.persistence.*
import jakarta.persistence.GenerationType.*


/**
 * @author ihyeonseo
 */

@Entity
@Table
class Issue(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    var id: Long? = null,

    @Column
    var userId: Long,

    @OneToMany(fetch = FetchType.EAGER)
    val comments: MutableList<Comment> = mutableListOf(),

    @Column
    var summary: String,

    @Column
    var description: String,

    @Column
    @Enumerated(EnumType.STRING)
    var type: IssueType,

    @Column
    @Enumerated(EnumType.STRING)
    var priority: IssuePriority,

    @Column
    @Enumerated(EnumType.STRING)
    var status: IssueStatus,
) : BaseEntity()