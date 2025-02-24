package com.trackasia.android.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue

class TrackAsiaIssueRegistry : IssueRegistry() {
    override val issues: List<Issue>
        get() = listOf(KeepDetector.ISSUE_NOT_KEPT)

    override val api: Int
        get() = CURRENT_API
}
