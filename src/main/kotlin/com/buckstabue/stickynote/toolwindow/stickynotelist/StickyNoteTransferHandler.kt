package com.buckstabue.stickynote.toolwindow.stickynotelist

import com.buckstabue.stickynote.AppInjector
import com.buckstabue.stickynote.StickyNote
import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.PlatformDataKeys
import kotlinx.coroutines.launch
import java.awt.datatransfer.Transferable
import javax.swing.JComponent
import javax.swing.JList
import javax.swing.TransferHandler

class StickyNoteTransferHandler : TransferHandler() {
    override fun getSourceActions(c: JComponent): Int {
        return MOVE
    }

    override fun createTransferable(c: JComponent): Transferable? {
        val stickyNotes = (c as JList<StickyNoteViewModel>).selectedValuesList.map { it.stickyNote }
        return StickyNoteTransferable(stickyNotes)
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
        val projectScope = projectComponent.projectScope()
        projectScope.launch {
            stickyNoteInteractor.moveStickyNotes(stickyNotesList, dropLocation.index)
        }
        return true
    }
}
