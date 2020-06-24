package com.agileburo.anytype.core_ui.features.page

import androidx.recyclerview.widget.DiffUtil
import com.agileburo.anytype.core_ui.common.Focusable
import com.agileburo.anytype.core_ui.common.Markup
import com.agileburo.anytype.core_ui.features.page.BlockView.Indentable
import timber.log.Timber

class BlockViewDiffUtil(
    private val old: List<BlockView>,
    private val new: List<BlockView>
) : DiffUtil.Callback() {

    override fun getOldListSize() = old.size
    override fun getNewListSize() = new.size

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ) = new[newItemPosition].id == old[oldItemPosition].id

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ) = new[newItemPosition] == old[oldItemPosition]

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {

        val oldBlock = old[oldItemPosition]
        val newBlock = new[newItemPosition]

        if (newBlock::class != oldBlock::class)
            return super.getChangePayload(oldItemPosition, newItemPosition)

        val changes = mutableListOf<Int>()

        if (newBlock is BlockView.Title && oldBlock is BlockView.Title) {
            if (newBlock.text != oldBlock.text)
                changes.add(TEXT_CHANGED)
        }

        if (newBlock is BlockView.ProfileTitle && oldBlock is BlockView.ProfileTitle) {
            if (newBlock.text != oldBlock.text)
                changes.add(TEXT_CHANGED)
        }

        if (newBlock is BlockView.Text && oldBlock is BlockView.Text) {
            if (newBlock.text != oldBlock.text)
                changes.add(TEXT_CHANGED)
            if (newBlock.color != oldBlock.color)
                changes.add(TEXT_COLOR_CHANGED)
            if (newBlock.backgroundColor != oldBlock.backgroundColor)
                changes.add(BACKGROUND_COLOR_CHANGED)
        }

        if (newBlock is Markup && oldBlock is Markup) {
            if (newBlock.marks != oldBlock.marks)
                changes.add(MARKUP_CHANGED)
        }

        if (newBlock is Focusable && oldBlock is Focusable) {
            if (newBlock.isFocused != oldBlock.isFocused)
                changes.add(FOCUS_CHANGED)
        }

        if (newBlock is BlockView.Cursor && oldBlock is BlockView.Cursor) {
            if (newBlock.cursor != oldBlock.cursor)
                changes.add(CURSOR_CHANGED)
        }

        if (newBlock is Indentable && oldBlock is Indentable) {
            if (newBlock.indent != oldBlock.indent)
                changes.add(INDENT_CHANGED)
        }

        if (newBlock is BlockView.Numbered && oldBlock is BlockView.Numbered) {
            if (newBlock.number != oldBlock.number)
                changes.add(NUMBER_CHANGED)
        }

        if (newBlock is BlockView.Toggle && oldBlock is BlockView.Toggle) {
            if (newBlock.isEmpty != oldBlock.isEmpty)
                changes.add(TOGGLE_EMPTY_STATE_CHANGED)
        }

        if (newBlock is BlockView.Selectable && oldBlock is BlockView.Selectable) {
            if (newBlock.isSelected != oldBlock.isSelected)
                changes.add(SELECTION_CHANGED)
        }

        if (newBlock is BlockView.Permission && oldBlock is BlockView.Permission) {
            if (newBlock.mode != oldBlock.mode)
                changes.add(READ_WRITE_MODE_CHANGED)
        }

        if (newBlock is BlockView.Alignable && oldBlock is BlockView.Alignable) {
            if (newBlock.alignment != oldBlock.alignment)
                changes.add(ALIGNMENT_CHANGED)
        }

        return if (changes.isNotEmpty())
            Payload(changes).also { Timber.d("Returning payload: $it") }
        else
            super.getChangePayload(oldItemPosition, newItemPosition)
    }

    /**
     * Payload of changes to apply to a block view.
     */
    data class Payload(
        val changes: List<Int>
    ) {

        val isCursorChanged : Boolean
        get() = changes.contains(CURSOR_CHANGED)

        fun markupChanged() = changes.contains(MARKUP_CHANGED)
        fun textChanged() = changes.contains(TEXT_CHANGED)
        fun textColorChanged() = changes.contains(TEXT_COLOR_CHANGED)
        fun focusChanged() = changes.contains(FOCUS_CHANGED)
        fun backgroundColorChanged() = changes.contains(BACKGROUND_COLOR_CHANGED)
        fun readWriteModeChanged() = changes.contains(READ_WRITE_MODE_CHANGED)
        fun selectionChanged() = changes.contains(SELECTION_CHANGED)
        fun alignmentChanged() = changes.contains(ALIGNMENT_CHANGED)
    }

    companion object {
        const val TEXT_CHANGED = 0
        const val MARKUP_CHANGED = 1
        const val FOCUS_CHANGED = 3
        const val TEXT_COLOR_CHANGED = 4
        const val NUMBER_CHANGED = 5
        const val BACKGROUND_COLOR_CHANGED = 6
        const val INDENT_CHANGED = 7
        const val TOGGLE_EMPTY_STATE_CHANGED = 8
        const val READ_WRITE_MODE_CHANGED = 9
        const val SELECTION_CHANGED = 10
        const val ALIGNMENT_CHANGED = 11
        const val CURSOR_CHANGED = 12
    }
}