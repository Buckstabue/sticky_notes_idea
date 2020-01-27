package com.buckstabue.stickynotes.idea.gutter

import com.buckstabue.stickynotes.FileBoundStickyNote
import com.buckstabue.stickynotes.StickyNote
import com.buckstabue.stickynotes.base.di.project.PerProject
import com.buckstabue.stickynotes.idea.IdeaFileLocation
import com.buckstabue.stickynotes.idea.MainScope
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.ex.MarkupModelEx
import com.intellij.openapi.editor.ex.RangeHighlighterEx
import com.intellij.openapi.editor.impl.DocumentMarkupModel
import com.intellij.openapi.editor.markup.HighlighterLayer
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import kotlinx.coroutines.launch
import javax.inject.Inject

@PerProject
class StickyNotesGutterManager @Inject constructor(
    private val project: Project
) {
    private val currentHighlighters = mutableMapOf<FileBoundStickyNote, RangeHighlighterEx>()

    fun onStickyNotesChanged(stickyNotes: List<StickyNote>) {
        MainScope().launch {
            val activeFileBoundStickyNotes = stickyNotes.filter { !it.isArchived }
                .filterIsInstance<FileBoundStickyNote>()
            val diff = StickyNotesDiff.calculate(
                oldNotes = currentHighlighters.keys,
                newNotes = activeFileBoundStickyNotes
            )
            diff.removedStickyNotes.forEach {
                currentHighlighters[it]?.dispose()
                currentHighlighters.remove(it)
            }
            diff.newStickyNotes.forEach { showStickerNoteGutterIcons(it) }
        }
    }

    private fun showStickerNoteGutterIcons(stickyNote: FileBoundStickyNote) {
        val cachedDocument = getCachedDocument(stickyNote) ?: return
        val markup = DocumentMarkupModel.forDocument(cachedDocument, project, true) as MarkupModelEx
        val highlighter =
            markup.addPersistentLineHighlighter(
                stickyNote.fileLocation.lineNumber,
                HighlighterLayer.ERROR + 1,
                null
            ) ?: return
        highlighter.gutterIconRenderer =
            StickyNoteGutterIconRenderer(
                stickyNote
            )
        currentHighlighters[stickyNote] = highlighter
    }

    private fun getCachedDocument(stickyNote: FileBoundStickyNote): Document? {
        val file = (stickyNote.fileLocation as IdeaFileLocation).openFileDescriptor
            ?.file ?: return null
        return FileDocumentManager.getInstance().getCachedDocument(file)
    }

    private data class StickyNotesDiff(
        val newStickyNotes: List<FileBoundStickyNote>,
        val removedStickyNotes: List<FileBoundStickyNote>
    ) {
        companion object {
            fun calculate(
                oldNotes: Collection<FileBoundStickyNote>,
                newNotes: Collection<FileBoundStickyNote>
            ): StickyNotesDiff {
                val newElements = newNotes.minus(oldNotes)
                val removedElements = oldNotes.minus(newNotes)
                return StickyNotesDiff(
                    newStickyNotes = newElements,
                    removedStickyNotes = removedElements
                )
            }
        }
    }
}

