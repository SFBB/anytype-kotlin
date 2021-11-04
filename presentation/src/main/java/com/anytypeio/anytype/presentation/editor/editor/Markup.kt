package com.anytypeio.anytype.presentation.editor.editor

import android.os.Parcelable
import android.text.Spannable
import kotlinx.android.parcel.Parcelize

/**
 * Classes implementing this interface should support markup rendering.
 */
interface Markup {

    /**
     * A text body that this markup should be applied to.
     */
    val body: String

    /**
     * List of marks associated with the text body.
     */
    var marks: List<Mark>

    /**
     * @property from character index where this markup starts (inclusive)
     * @property to character index where this markup ends (inclusive)
     * @property type markup's type
     */
    @Parcelize
    data class Mark(
        val from: Int,
        val to: Int,
        val type: Type,
        val param: String? = null,
        val extras: Map<String, String?> = emptyMap()
    ) : Parcelable {

        private val default = extras.withDefault { null }

        val image: String? by default
        val emoji: String? by default
        val isLoading: String? by default
        val isDeleted: String? by default

        fun color(): Int? = ThemeColor.values().find { color -> color.title == param }?.text
        fun background(): Int? =
            ThemeColor.values().find { color -> color.title == param }?.background

        companion object {
            const val IS_LOADING_VALUE = "true"
            const val IS_NOT_LOADING_VALUE = "false"
            const val IS_DELETED_VALUE = "true"
            const val IS_NOT_DELETED_VALUE = "false"

            const val KEY_IS_LOADING = "isLoading"
            const val KEY_IS_DELETED = "isDeleted"
        }
    }

    /**
     * Markup types.
     */
    enum class Type {
        ITALIC,
        BOLD,
        STRIKETHROUGH,
        TEXT_COLOR,
        BACKGROUND_COLOR,
        LINK,
        KEYBOARD,
        MENTION,
        OBJECT
    }

    companion object {
        const val DEFAULT_SPANNABLE_FLAG = Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        const val MENTION_SPANNABLE_FLAG = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        const val SPAN_MONOSPACE = "monospace"
        const val NON_EXISTENT_OBJECT_MENTION_NAME = "Non-existent object"
    }
}