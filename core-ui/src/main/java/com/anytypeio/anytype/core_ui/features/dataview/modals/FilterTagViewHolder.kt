package com.anytypeio.anytype.core_ui.features.dataview.modals

import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.anytypeio.anytype.core_ui.R
import com.anytypeio.anytype.core_ui.extensions.color
import com.anytypeio.anytype.core_ui.extensions.dark
import com.anytypeio.anytype.core_ui.extensions.light
import com.anytypeio.anytype.core_utils.ext.gone
import com.anytypeio.anytype.core_utils.ext.invisible
import com.anytypeio.anytype.core_utils.ext.visible
import com.anytypeio.anytype.presentation.editor.editor.ThemeColor
import com.anytypeio.anytype.presentation.extension.hasValue
import com.anytypeio.anytype.presentation.sets.model.FilterView
import com.anytypeio.anytype.presentation.sets.model.TagView
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.item_dv_viewer_filter_tag.view.*
import kotlinx.android.synthetic.main.item_dv_viewer_filter_tag_value.view.*


class FilterTagViewHolder(view: View) : FilterViewHolder(view) {

    override val textTitle: TextView get() = itemView.tvTitle
    override val textCondition: TextView get() = itemView.tvCondition
    override val iconFormat: ImageView get() = itemView.iconFormat
    override val iconArrow: ImageView get() = itemView.iconArrow
    override val iconRemove: ImageView get() = itemView.iconRemoveTag

    private val chip1 = itemView.chip1
    private val chip2 = itemView.chip2
    private val chipDots = itemView.chipDots
    private val chips = listOf<Chip>(chip1, chip2, chipDots)

    fun bind(
        item: FilterView.Expression.Tag
    ) {
        setup(
            isEditMode = item.isInEditMode,
            title = item.title,
            condition = item.condition.title,
            format = item.format
        )
        chips.forEach { it.gone() }
        if (item.condition.hasValue()) {
            item.filterValue.value.forEachIndexed { index, s ->
                when (index) {
                    0 -> bindChip(chip1, s)
                    1 -> bindChip(chip2, s)
                    2 -> chipDots.visible()
                }
            }
        } else {
            bindChip(chip1, null)
            bindChip(chip2, null)
            chipDots.invisible()
        }
    }

    private fun bindChip(chip: Chip, value: TagView?) {
        with(chip) {
            val defaultTextColor = itemView.resources.getColor(R.color.text_primary, null)
            val defaultBackground = itemView.resources.getColor(R.color.shape_primary, null)
            val color = ThemeColor.values().find { v -> v.title == value?.color }
            if (color != null && color != ThemeColor.DEFAULT) {
                chipBackgroundColor = ColorStateList.valueOf(resources.light(color, defaultBackground))
                setTextColor(resources.dark(color, defaultTextColor))
            } else {
                setChipBackgroundColorResource(R.color.default_filter_tag_background_color)
                setTextColor(context.color(R.color.default_filter_tag_text_color))
            }
            text = value?.tag
            if (value != null) {
                visible()
            } else {
                invisible()
            }
        }
    }
}