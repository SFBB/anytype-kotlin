package com.anytypeio.anytype.core_ui.widgets.dv

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import coil3.load
import coil3.request.CachePolicy
import coil3.request.transformations
import coil3.transform.CircleCropTransformation
import com.anytypeio.anytype.core_models.Hash
import com.anytypeio.anytype.core_models.Url
import com.anytypeio.anytype.core_ui.R
import com.anytypeio.anytype.core_ui.databinding.WidgetGalleryViewDefaultTitleIconBinding
import com.anytypeio.anytype.core_utils.ext.gone
import com.anytypeio.anytype.core_utils.ext.visible
import com.anytypeio.anytype.emojifier.Emojifier
import com.anytypeio.anytype.presentation.objects.ObjectIcon
import timber.log.Timber

class GalleryViewDefaultTitleIcon @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    val binding = WidgetGalleryViewDefaultTitleIconBinding.inflate(
        LayoutInflater.from(context), this
    )

    fun bind(icon: ObjectIcon) = with(binding) {
        when (icon) {
            is ObjectIcon.Basic.Emoji -> {
                bindBasicEmoji(icon)
            }
            is ObjectIcon.Basic.Image -> {
                bindBasicImage(icon.hash)
            }
            is ObjectIcon.Profile.Image -> {
                bindProfileImage(icon)
            }
            is ObjectIcon.Bookmark -> {
                bindBasicImage(icon.image)
            }
            is ObjectIcon.Profile.Avatar -> {
                bindProfileAvatar(icon)
            }
            is ObjectIcon.Task -> {
                bindTask(icon)
            }
            else -> {
                Timber.e("Ignoring icon: $icon")
            }
        }
    }

    private fun WidgetGalleryViewDefaultTitleIconBinding.bindTask(
        icon: ObjectIcon.Task
    ) {
        ivIconEmoji.setImageDrawable(null)
        ivIconEmoji.gone()
        tvAvatar.text = null
        tvAvatar.gone()
        ivIconImage.visible()
        if (icon.isChecked) {
            ivIconImage.setImageResource(R.drawable.ic_gallery_view_task_checked)
        } else {
            ivIconImage.setImageResource(R.drawable.ic_gallery_view_task_unchecked)
        }
    }

    private fun WidgetGalleryViewDefaultTitleIconBinding.bindProfileAvatar(
        icon: ObjectIcon.Profile.Avatar
    ) {
        ivIconEmoji.setImageDrawable(null)
        ivIconEmoji.gone()
        ivIconImage.setImageDrawable(null)
        ivIconImage.gone()
        tvAvatar.text = if (icon.name.isNotEmpty())
            icon.name.first().toString()
        else
            resources.getString(R.string.u)
        tvAvatar.visible()
    }

    private fun WidgetGalleryViewDefaultTitleIconBinding.bindBasicEmoji(
        icon: ObjectIcon.Basic.Emoji
    ) {
        ivIconImage.setImageDrawable(null)
        ivIconImage.gone()
        tvAvatar.text = null
        tvAvatar.gone()
        setEmoji(icon.unicode)
    }

    private fun WidgetGalleryViewDefaultTitleIconBinding.bindProfileImage(
        icon: ObjectIcon.Profile.Image
    ) {
        prepareImage()
        setCircularImage(icon.hash)
    }

    private fun WidgetGalleryViewDefaultTitleIconBinding.bindBasicImage(hash: Hash) {
        prepareImage()
        setImage(hash)
    }

    private fun WidgetGalleryViewDefaultTitleIconBinding.prepareImage() {
        ivIconEmoji.setImageDrawable(null)
        ivIconEmoji.gone()
        tvAvatar.text = null
        tvAvatar.gone()
    }

    private fun setEmoji(emoji: String) = with(binding) {
        if (emoji.isNotBlank()) {
            try {
                ivIconEmoji.load(Emojifier.uri(emoji)) {
                    diskCachePolicy(CachePolicy.ENABLED)
                    memoryCachePolicy(CachePolicy.ENABLED)
                }
            } catch (e: Throwable) {
                Timber.w(e, "Error while setting emoji icon for: $emoji")
            }
        } else {
            ivIconEmoji.setImageDrawable(null)
        }
        ivIconEmoji.visible()
    }

    private fun setImage(image: Url) = with(binding) {
        if (image.isNotBlank()) {
            ivIconImage.load(image)
        } else {
            ivIconImage.setImageDrawable(null)
        }
        ivIconImage.visible()
    }

    private fun setCircularImage(image: Url) = with(binding) {
        if (image.isNotBlank()) {
            ivIconImage.load(image) {
                transformations(CircleCropTransformation())
            }
        } else {
            ivIconImage.setImageDrawable(null)
        }
        ivIconImage.visible()
    }
}