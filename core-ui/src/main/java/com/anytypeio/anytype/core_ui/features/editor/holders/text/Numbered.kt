package com.anytypeio.anytype.core_ui.features.editor.holders.text

import android.graphics.drawable.Drawable
import android.text.Editable
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.anytypeio.anytype.core_ui.R
import com.anytypeio.anytype.core_ui.features.editor.BlockViewDiffUtil
import com.anytypeio.anytype.core_ui.features.editor.SupportNesting
import com.anytypeio.anytype.core_ui.features.editor.marks
import com.anytypeio.anytype.core_ui.widgets.text.TextInputWidget
import com.anytypeio.anytype.core_utils.ext.addDot
import com.anytypeio.anytype.core_utils.ext.dimen
import com.anytypeio.anytype.presentation.editor.editor.ThemeColor
import com.anytypeio.anytype.presentation.editor.editor.listener.ListenerType
import com.anytypeio.anytype.presentation.editor.editor.mention.MentionEvent
import com.anytypeio.anytype.presentation.editor.editor.model.BlockView
import com.anytypeio.anytype.presentation.editor.editor.slash.SlashEvent
import kotlinx.android.synthetic.main.item_block_numbered.view.*

class Numbered(
    view: View,
    onContextMenuStyleClick: (IntRange) -> Unit
) : Text(view), SupportNesting {

    private val container = itemView.numberedBlockContentContainer
    val number = itemView.number
    override val content: TextInputWidget = itemView.numberedListContent
    override val root: View = itemView

    private val mentionIconSize: Int
    private val mentionIconPadding: Int
    private val mentionCheckedIcon: Drawable?
    private val mentionUncheckedIcon: Drawable?
    private val mentionInitialsSize: Float

    init {
        setup(onContextMenuStyleClick)
        with(itemView.context) {
            mentionIconSize =
                resources.getDimensionPixelSize(R.dimen.mention_span_image_size_default)
            mentionIconPadding =
                resources.getDimensionPixelSize(R.dimen.mention_span_image_padding_default)
            mentionUncheckedIcon = ContextCompat.getDrawable(this, R.drawable.ic_task_0_text_16)
            mentionCheckedIcon = ContextCompat.getDrawable(this, R.drawable.ic_task_1_text_16)
            mentionInitialsSize = resources.getDimension(R.dimen.mention_span_initials_size_default)
        }
    }

    fun bind(
        item: BlockView.Text.Numbered,
        onTextBlockTextChanged: (BlockView.Text) -> Unit,
        clicked: (ListenerType) -> Unit,
        onMentionEvent: (MentionEvent) -> Unit,
        onSlashEvent: (SlashEvent) -> Unit,
        onSplitLineEnterClicked: (String, Editable, IntRange) -> Unit,
        onEmptyBlockBackspaceClicked: (String) -> Unit,
        onNonEmptyBlockBackspaceClicked: (String, Editable) -> Unit,
        onBackPressedCallback: () -> Boolean
    ) = super.bind(
        item = item,
        onTextChanged = { _, editable ->
            item.apply {
                text = editable.toString()
                marks = editable.marks()
            }
            onTextBlockTextChanged(item)
        },
        clicked = clicked,
        onEmptyBlockBackspaceClicked = onEmptyBlockBackspaceClicked,
        onSplitLineEnterClicked = onSplitLineEnterClicked,
        onNonEmptyBlockBackspaceClicked = onNonEmptyBlockBackspaceClicked,
        onBackPressedCallback = onBackPressedCallback
    ).also {
        setNumber(item)
        setupMentionWatcher(onMentionEvent)
        setupSlashWatcher(onSlashEvent, item.getViewType())
    }

    private fun setNumber(item: BlockView.Text.Numbered) {
        number.gravity = when (item.number) {
            in 1..19 -> Gravity.CENTER_HORIZONTAL
            else -> Gravity.START
        }
        number.text = item.number.addDot()
    }

    override fun getMentionIconSize(): Int = mentionIconSize
    override fun getMentionIconPadding(): Int = mentionIconPadding
    override fun getMentionCheckedIcon(): Drawable? = mentionCheckedIcon
    override fun getMentionUncheckedIcon(): Drawable? = mentionUncheckedIcon
    override fun getMentionInitialsSize(): Float = mentionInitialsSize

    override fun processChangePayload(
        payloads: List<BlockViewDiffUtil.Payload>,
        item: BlockView,
        onTextChanged: (BlockView.Text) -> Unit,
        onSelectionChanged: (String, IntRange) -> Unit,
        clicked: (ListenerType) -> Unit,
        onMentionEvent: (MentionEvent) -> Unit,
        onSlashEvent: (SlashEvent) -> Unit
    ) {
        super.processChangePayload(payloads, item, onTextChanged, onSelectionChanged, clicked, onMentionEvent, onSlashEvent)
        payloads.forEach { payload ->
            if (payload.changes.contains(BlockViewDiffUtil.NUMBER_CHANGED))
                number.text = (item as BlockView.Text.Numbered).number.addDot()
        }
    }

    override fun setTextColor(color: String) {
        super.setTextColor(color)
        val value = ThemeColor.values().find { value -> value.title == color }
        if (value != null && value != ThemeColor.DEFAULT) {
            number.setTextColor(value.text)
        } else {
            number.setTextColor(itemView.context.getColor(R.color.anytype_text_default))
        }
    }

    override fun indentize(item: BlockView.Indentable) {
        number.updateLayoutParams<LinearLayout.LayoutParams> {
            setMargins(
                item.indent * dimen(R.dimen.indent),
                0,
                0,
                0
            )
        }
    }

    override fun select(item: BlockView.Selectable) {
        container.isSelected = item.isSelected
    }
}