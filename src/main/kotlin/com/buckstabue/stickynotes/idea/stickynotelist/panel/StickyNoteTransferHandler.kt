package com.buckstabue.stickynotes.idea.stickynotelist.panel

import com.buckstabue.stickynotes.StickyNote
import com.buckstabue.stickynotes.base.di.AppInjector
import com.buckstabue.stickynotes.idea.MainScope
import com.buckstabue.stickynotes.idea.stickynotelist.StickyNoteListAnalytics
import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.PlatformDataKeys
import kotlinx.coroutines.launch
import java.awt.datatransfer.Transferable
import javax.swing.JComponent
import javax.swing.JList
import javax.swing.SwingUtilities
import javax.swing.TransferHandler

class StickyNoteTransferHandler(
    private val analytics: StickyNoteListAnalytics
) : TransferHandler() {
    override fun getSourceActions(c: JComponent): Int {
        return MOVE
    }

    override fun createTransferable(c: JComponent): Transferable? {
        val stickyNotes = (c as JList<StickyNoteViewModel>).selectedValuesList.map { it.stickyNote }
        return StickyNoteTransferable(
            stickyNotes
        )
    }

    override fun canImport(support: TransferSupport): Boolean {
        // TODO check if we can restrict DnD by a project
        return support.dataFlavors.any { it == StickyNoteTransferable.STICKY_NOTE_DATA_FLAVOR }
    }

    override fun importData(support: TransferSupport): Boolean {
        if (!canImport(support)) {
            return false
        }
        val stickyNotesList = support.transferable
            .getTransferData(StickyNoteTransferable.STICKY_NOTE_DATA_FLAVOR) as List<StickyNote>
        val dropLocation = support.dropLocation as JList.DropLocation
        val project = DataManager.getInstance().getDataContext(support.component)
            .getData(PlatformDataKeys.PROJECT)!!

        val projectComponent = AppInjector.getProjectComponent(project)
        val stickyNoteInteractor = projectComponent.stickyNoteInteractor()
        MainScope().launch {
            val movedStickyNotesIndicesRange =
                stickyNoteInteractor.moveStickyNotes(stickyNotesList, dropLocation.index)
            val stickyNoteJList = support.component as JList<StickyNoteViewModel>
            SwingUtilities.invokeLater {
                stickyNoteJList.setSelectionInterval(
                    movedStickyNotesIndicesRange.first,
                    movedStickyNotesIndicesRange.last
                )
            }
        }
        analytics.dragAndDropItems(stickyNotesList.size)
        return true
    }
}
