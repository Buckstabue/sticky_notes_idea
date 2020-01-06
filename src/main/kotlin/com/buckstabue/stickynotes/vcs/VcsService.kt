package com.buckstabue.stickynotes.vcs

import kotlinx.coroutines.channels.ReceiveChannel

interface VcsService {
    fun observeCurrentBranchName(): ReceiveChannel<String?>
    fun getCurrentBranchName(): String?
}
