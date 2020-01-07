package com.buckstabue.stickynotes.base.di.project

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

interface ProjectScope : CoroutineScope

@PerProject
class ProjectScopeImpl @Inject constructor(
) : ProjectScope, CoroutineScope by CoroutineScope(Dispatchers.Default)
