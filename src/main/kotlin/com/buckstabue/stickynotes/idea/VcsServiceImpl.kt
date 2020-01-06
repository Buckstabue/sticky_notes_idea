package com.buckstabue.stickynotes.idea

import com.buckstabue.stickynotes.vcs.VcsService
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import git4idea.repo.GitRepository
import git4idea.repo.GitRepositoryChangeListener
import git4idea.repo.GitRepositoryManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class VcsServiceImpl(
    private val project: Project
) : VcsService {
    companion object {
        fun getInstance(project: Project): VcsService {
            return ServiceManager.getService(project, VcsService::class.java)
        }
    }

    private val currentBranchNameChannel = BroadcastChannel<String?>(Channel.CONFLATED).also {
        it.offer(getCurrentBranchName())
    }

    init {
        project.messageBus.connect()
            .subscribe(GitRepository.GIT_REPO_CHANGE, GitRepositoryChangeListener {
                currentBranchNameChannel.offer(getCurrentBranchName())
            })
    }

    override fun getCurrentBranchName(): String? {
        val gitRepositoryManager = GitRepositoryManager.getInstance(project)
        return gitRepositoryManager.repositories.firstOrNull()?.currentBranchName
    }

    override fun observeCurrentBranchName(): ReceiveChannel<String?> {
        return currentBranchNameChannel.openSubscription()
    }

}
