package com.buckstabue.stickynotes.idea.createeditstickynote

import com.buckstabue.stickynotes.base.di.AppInjector
import com.buckstabue.stickynotes.idea.createeditstickynote.CreateEditStickyNoteAnalytics.Source
import com.buckstabue.stickynotes.idea.createeditstickynote.CreateEditStickyNoteViewModel.Mode
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.ex.EditorGutterComponentEx
import com.intellij.openapi.project.DumbAware
import javax.swing.Icon

// we add @JvmOverloads to make it possible to create this action by reflection with the default constructor
class CreateStickyNoteAction @JvmOverloads constructor(
    private val codeBindingEnabledByDefaultWhenPossible: Boolean = true,
    private val source: Source = Source.CONTEXT_MENU,
    text: String? = null,
    icon: Icon? = null
) : AnAction(text, null, icon), DumbAware {
    companion object {
        private val logger = Logger.getInstance(CreateStickyNoteAction::class.java)
    }

    override fun actionPerformed(event: AnActionEvent) {
        logger.debug("new event from place = ${event.place}")
        val project = event.project
        if (project == null) {
            logger.error("Project is null")
            return
        }
        val editor = CommonDataKeys.EDITOR.getData(event.dataContext)
        if (editor == null) {
            logger.debug("Could not extract editor from event")
        }
        val gutterLineNumber: Int? =
            EditorGutterComponentEx.LOGICAL_LINE_AT_CURSOR.getData(event.dataContext)

        val createEditStickyNoteComponent = AppInjector.getProjectComponent(project)
            .plusCreateEditStickyNoteComponent()
            .create(
                mode = Mode.CREATE,
                source = source
            )
        val createStickyNoteScenario = createEditStickyNoteComponent.createStickyNoteScenario()
        createStickyNoteScenario.launch(
            editor = editor,
            lineNumber = gutterLineNumber,
            codeBindingEnabledByDefaultWhenPossible = codeBindingEnabledByDefaultWhenPossible
        )
    }
}
