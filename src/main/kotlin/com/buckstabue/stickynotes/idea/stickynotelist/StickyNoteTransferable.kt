package com.buckstabue.stickynotes.idea.stickynotelist

import com.buckstabue.stickynotes.StickyNote
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable

class StickyNoteTransferable(
    private val stickyNotes: List<StickyNote>
) : Transferable {
    companion object {
        val STICKY_NOTE_DATA_FLAVOR = DataFlavor(
            StickyNote::class.java,
            "StickyNote"
        )
        private val SUPPORTED_FLAVORS = arrayOf(STICKY_NOTE_DATA_FLAVOR)
    }

    override fun getTransferData(flavor: DataFlavor): Any {
        return stickyNotes
    }

    override fun isDataFlavorSupported(flavor: DataFlavor): Boolean {
        return flavor == STICKY_NOTE_DATA_FLAVOR
    }

    override fun getTransferDataFlavors(): Array<DataFlavor> {
        return SUPPORTED_FLAVORS
    }
}
