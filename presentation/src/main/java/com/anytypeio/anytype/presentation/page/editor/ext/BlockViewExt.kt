package com.anytypeio.anytype.presentation.page.editor.ext

import com.anytypeio.anytype.core_models.Id
import com.anytypeio.anytype.presentation.page.editor.model.BlockView
import com.anytypeio.anytype.presentation.page.editor.model.BlockView.Media.Bookmark.Companion.SEARCH_FIELD_DESCRIPTION_KEY
import com.anytypeio.anytype.presentation.page.editor.model.BlockView.Media.Bookmark.Companion.SEARCH_FIELD_TITLE_KEY
import com.anytypeio.anytype.presentation.page.editor.model.BlockView.Media.Bookmark.Companion.SEARCH_FIELD_URL_KEY
import com.anytypeio.anytype.presentation.page.editor.model.BlockView.Searchable.Field.Companion.DEFAULT_SEARCH_FIELD_KEY

fun List<BlockView>.singleStylingMode(
    target: Id
): List<BlockView> = map { view ->
    val isSelected = view.id == target
    when (view) {
        is BlockView.Text.Paragraph -> view.copy(
            mode = BlockView.Mode.READ,
            isSelected = isSelected,
            isFocused = false,
            cursor = null
        )
        is BlockView.Text.Checkbox -> view.copy(
            mode = BlockView.Mode.READ,
            isSelected = isSelected,
            isFocused = false,
            cursor = null
        )
        is BlockView.Text.Bulleted -> view.copy(
            mode = BlockView.Mode.READ,
            isSelected = isSelected,
            isFocused = false,
            cursor = null
        )
        is BlockView.Text.Numbered -> view.copy(
            mode = BlockView.Mode.READ,
            isSelected = isSelected,
            isFocused = false,
            cursor = null
        )
        is BlockView.Text.Highlight -> view.copy(
            mode = BlockView.Mode.READ,
            isSelected = isSelected,
            isFocused = false,
            cursor = null
        )
        is BlockView.Text.Header.One -> view.copy(
            mode = BlockView.Mode.READ,
            isSelected = isSelected,
            isFocused = false,
            cursor = null
        )
        is BlockView.Text.Header.Two -> view.copy(
            mode = BlockView.Mode.READ,
            isSelected = isSelected,
            isFocused = false,
            cursor = null
        )
        is BlockView.Text.Header.Three -> view.copy(
            mode = BlockView.Mode.READ,
            isSelected = isSelected,
            isFocused = false,
            cursor = null
        )
        is BlockView.Text.Toggle -> view.copy(
            mode = BlockView.Mode.READ,
            isSelected = isSelected,
            isFocused = false,
            cursor = null
        )
        is BlockView.Code -> view.copy(
            mode = BlockView.Mode.READ,
            isSelected = isSelected
        )
        is BlockView.Error.File -> view.copy(
            mode = BlockView.Mode.READ,
            isSelected = isSelected
        )
        is BlockView.Error.Video -> view.copy(
            mode = BlockView.Mode.READ,
            isSelected = isSelected
        )
        is BlockView.Error.Picture -> view.copy(
            mode = BlockView.Mode.READ,
            isSelected = isSelected
        )
        is BlockView.Error.Bookmark -> view.copy(
            mode = BlockView.Mode.READ,
            isSelected = isSelected
        )
        is BlockView.Upload.File -> view.copy(
            mode = BlockView.Mode.READ,
            isSelected = isSelected
        )
        is BlockView.Upload.Video -> view.copy(
            mode = BlockView.Mode.READ,
            isSelected = isSelected
        )
        is BlockView.Upload.Picture -> view.copy(
            mode = BlockView.Mode.READ,
            isSelected = isSelected
        )
        is BlockView.MediaPlaceholder.File -> view.copy(
            mode = BlockView.Mode.READ,
            isSelected = isSelected
        )
        is BlockView.MediaPlaceholder.Video -> view.copy(
            mode = BlockView.Mode.READ,
            isSelected = isSelected
        )
        is BlockView.MediaPlaceholder.Bookmark -> view.copy(
            mode = BlockView.Mode.READ,
            isSelected = isSelected
        )
        is BlockView.MediaPlaceholder.Picture -> view.copy(
            mode = BlockView.Mode.READ,
            isSelected = isSelected
        )
        is BlockView.Media.File -> view.copy(
            mode = BlockView.Mode.READ,
            isSelected = isSelected
        )
        is BlockView.Media.Video -> view.copy(
            mode = BlockView.Mode.READ,
            isSelected = isSelected
        )
        is BlockView.Media.Bookmark -> view.copy(
            mode = BlockView.Mode.READ,
            isSelected = isSelected
        )
        is BlockView.Media.Picture -> view.copy(
            mode = BlockView.Mode.READ,
            isSelected = isSelected
        )
        is BlockView.Title.Basic -> view.copy(
            mode = BlockView.Mode.READ
        )
        is BlockView.Title.Profile -> view.copy(
            mode = BlockView.Mode.READ
        )
        is BlockView.Title.Archive -> view.copy(
            mode = BlockView.Mode.READ
        )
        is BlockView.Description -> view.copy(
            mode = BlockView.Mode.READ
        )
        else -> view.also { check(view !is BlockView.Permission) }
    }
}

fun List<BlockView>.exitSingleStylingMode(
    target: Id,
    cursor: Int?
): List<BlockView> = map { view ->
    val isTarget = view.id == target
    when (view) {
        is BlockView.Text.Paragraph -> view.copy(
            mode = BlockView.Mode.EDIT,
            isSelected = false,
            isFocused = isTarget,
            cursor = if (isTarget) cursor else null
        )
        is BlockView.Text.Checkbox -> view.copy(
            mode = BlockView.Mode.EDIT,
            isSelected = false,
            isFocused = isTarget,
            cursor = if (isTarget) cursor else null
        )
        is BlockView.Text.Bulleted -> view.copy(
            mode = BlockView.Mode.EDIT,
            isSelected = false,
            isFocused = isTarget,
            cursor = if (isTarget) cursor else null
        )
        is BlockView.Text.Numbered -> view.copy(
            mode = BlockView.Mode.EDIT,
            isSelected = false,
            isFocused = isTarget,
            cursor = if (isTarget) cursor else null
        )
        is BlockView.Text.Highlight -> view.copy(
            mode = BlockView.Mode.EDIT,
            isSelected = false,
            isFocused = isTarget,
            cursor = if (isTarget) cursor else null
        )
        is BlockView.Text.Header.One -> view.copy(
            mode = BlockView.Mode.EDIT,
            isSelected = false,
            isFocused = isTarget,
            cursor = if (isTarget) cursor else null
        )
        is BlockView.Text.Header.Two -> view.copy(
            mode = BlockView.Mode.EDIT,
            isSelected = false,
            isFocused = isTarget,
            cursor = if (isTarget) cursor else null
        )
        is BlockView.Text.Header.Three -> view.copy(
            mode = BlockView.Mode.EDIT,
            isSelected = false,
            isFocused = isTarget,
            cursor = if (isTarget) cursor else null
        )
        is BlockView.Text.Toggle -> view.copy(
            mode = BlockView.Mode.EDIT,
            isSelected = false,
            isFocused = isTarget,
            cursor = if (isTarget) cursor else null
        )
        is BlockView.Code -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Error.File -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Error.Video -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Error.Picture -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Error.Bookmark -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Upload.File -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Upload.Video -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Upload.Picture -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.MediaPlaceholder.File -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.MediaPlaceholder.Video -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.MediaPlaceholder.Bookmark -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.MediaPlaceholder.Picture -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Media.File -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Media.Video -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Media.Bookmark -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Media.Picture -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Description -> view.copy(mode = BlockView.Mode.EDIT)
        is BlockView.Title.Basic -> view.copy(mode = BlockView.Mode.EDIT)
        is BlockView.Title.Profile -> view.copy(mode = BlockView.Mode.EDIT)
        is BlockView.Title.Archive -> view.copy(mode = BlockView.Mode.EDIT)
        else -> view.also { check(view !is BlockView.Permission) }
    }
}

fun List<BlockView>.toReadMode(): List<BlockView> = map { view ->
    when (view) {
        is BlockView.Text.Paragraph -> view.copy(mode = BlockView.Mode.READ)
        is BlockView.Text.Checkbox -> view.copy(mode = BlockView.Mode.READ)
        is BlockView.Text.Bulleted -> view.copy(mode = BlockView.Mode.READ)
        is BlockView.Text.Numbered -> view.copy(mode = BlockView.Mode.READ)
        is BlockView.Text.Highlight -> view.copy(mode = BlockView.Mode.READ)
        is BlockView.Text.Header.One -> view.copy(mode = BlockView.Mode.READ)
        is BlockView.Text.Header.Two -> view.copy(mode = BlockView.Mode.READ)
        is BlockView.Text.Header.Three -> view.copy(mode = BlockView.Mode.READ)
        is BlockView.Text.Toggle -> view.copy(mode = BlockView.Mode.READ)
        is BlockView.Title.Basic -> view.copy(mode = BlockView.Mode.READ)
        is BlockView.Title.Profile -> view.copy(mode = BlockView.Mode.READ)
        is BlockView.Title.Archive -> view.copy(mode = BlockView.Mode.READ)
        is BlockView.Description -> view.copy(mode = BlockView.Mode.READ)
        is BlockView.Code -> view.copy(mode = BlockView.Mode.READ)
        is BlockView.Error.File -> view.copy(mode = BlockView.Mode.READ)
        is BlockView.Error.Video -> view.copy(mode = BlockView.Mode.READ)
        is BlockView.Error.Picture -> view.copy(mode = BlockView.Mode.READ)
        is BlockView.Error.Bookmark -> view.copy(mode = BlockView.Mode.READ)
        is BlockView.Upload.File -> view.copy(mode = BlockView.Mode.READ)
        is BlockView.Upload.Video -> view.copy(mode = BlockView.Mode.READ)
        is BlockView.Upload.Picture -> view.copy(mode = BlockView.Mode.READ)
        is BlockView.MediaPlaceholder.File -> view.copy(mode = BlockView.Mode.READ)
        is BlockView.MediaPlaceholder.Video -> view.copy(mode = BlockView.Mode.READ)
        is BlockView.MediaPlaceholder.Bookmark -> view.copy(mode = BlockView.Mode.READ)
        is BlockView.MediaPlaceholder.Picture -> view.copy(mode = BlockView.Mode.READ)
        is BlockView.Media.File -> view.copy(mode = BlockView.Mode.READ)
        is BlockView.Media.Video -> view.copy(mode = BlockView.Mode.READ)
        is BlockView.Media.Bookmark -> view.copy(mode = BlockView.Mode.READ)
        is BlockView.Media.Picture -> view.copy(mode = BlockView.Mode.READ)
        else -> view.also { check(view !is BlockView.Permission) }
    }
}

fun List<BlockView>.toEditMode(): List<BlockView> = map { view ->
    when (view) {
        is BlockView.Text.Paragraph -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Text.Checkbox -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Text.Bulleted -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Text.Numbered -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Text.Highlight -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Text.Header.One -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Text.Header.Two -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Text.Header.Three -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Text.Toggle -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Code -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Error.File -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Error.Video -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Error.Picture -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Error.Bookmark -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Upload.File -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Upload.Video -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Upload.Picture -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.MediaPlaceholder.File -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.MediaPlaceholder.Video -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.MediaPlaceholder.Bookmark -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.MediaPlaceholder.Picture -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Media.File -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Media.Video -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Media.Bookmark -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Media.Picture -> view.copy(mode = BlockView.Mode.EDIT, isSelected = false)
        is BlockView.Description -> view.copy(mode = BlockView.Mode.EDIT)
        is BlockView.Title.Basic -> view.copy(mode = BlockView.Mode.EDIT)
        is BlockView.Title.Profile -> view.copy(mode = BlockView.Mode.EDIT)
        is BlockView.Title.Archive -> view.copy(mode = BlockView.Mode.EDIT)
        else -> view.also { check(view !is BlockView.Permission) }
    }
}

fun List<BlockView>.clearSearchHighlights(): List<BlockView> = map { view ->
    when (view) {
        is BlockView.Text.Paragraph -> view.copy(searchFields = emptyList())
        is BlockView.Text.Numbered -> view.copy(searchFields = emptyList())
        is BlockView.Text.Bulleted -> view.copy(searchFields = emptyList())
        is BlockView.Text.Checkbox -> view.copy(searchFields = emptyList())
        is BlockView.Text.Toggle -> view.copy(searchFields = emptyList())
        is BlockView.Text.Header.One -> view.copy(searchFields = emptyList())
        is BlockView.Text.Header.Two -> view.copy(searchFields = emptyList())
        is BlockView.Text.Header.Three -> view.copy(searchFields = emptyList())
        is BlockView.Text.Highlight -> view.copy(searchFields = emptyList())
        is BlockView.Title.Basic -> view.copy(searchFields = emptyList())
        is BlockView.Title.Profile -> view.copy(searchFields = emptyList())
        is BlockView.Media.Bookmark -> view.copy(searchFields = emptyList())
        is BlockView.Media.File -> view.copy(searchFields = emptyList())
        is BlockView.Page -> view.copy(searchFields = emptyList())
        is BlockView.PageArchive -> view.copy(searchFields = emptyList())
        else -> view.also { check(view !is BlockView.Searchable) }
    }
}

fun List<BlockView>.highlight(
    highlighter: (List<Pair<String, String>>) -> List<BlockView.Searchable.Field>
) = map { view ->
    when (view) {
        is BlockView.Text.Paragraph -> {
            val fields = listOf(DEFAULT_SEARCH_FIELD_KEY to view.text)
            view.copy(searchFields = highlighter(fields))
        }
        is BlockView.Text.Numbered -> {
            val fields = listOf(DEFAULT_SEARCH_FIELD_KEY to view.text)
            view.copy(searchFields = highlighter(fields))
        }
        is BlockView.Text.Bulleted -> {
            val fields = listOf(DEFAULT_SEARCH_FIELD_KEY to view.text)
            view.copy(searchFields = highlighter(fields))
        }
        is BlockView.Text.Checkbox -> {
            val fields = listOf(DEFAULT_SEARCH_FIELD_KEY to view.text)
            view.copy(searchFields = highlighter(fields))
        }
        is BlockView.Text.Toggle -> {
            val fields = listOf(DEFAULT_SEARCH_FIELD_KEY to view.text)
            view.copy(searchFields = highlighter(fields))
        }
        is BlockView.Text.Header.One -> {
            val fields = listOf(DEFAULT_SEARCH_FIELD_KEY to view.text)
            view.copy(searchFields = highlighter(fields))
        }
        is BlockView.Text.Header.Two -> {
            val fields = listOf(DEFAULT_SEARCH_FIELD_KEY to view.text)
            view.copy(searchFields = highlighter(fields))
        }
        is BlockView.Text.Header.Three -> {
            val fields = listOf(DEFAULT_SEARCH_FIELD_KEY to view.text)
            view.copy(searchFields = highlighter(fields))
        }
        is BlockView.Text.Highlight -> {
            val fields = listOf(DEFAULT_SEARCH_FIELD_KEY to view.text)
            view.copy(searchFields = highlighter(fields))
        }
        is BlockView.Title.Basic -> {
            val fields = listOf(DEFAULT_SEARCH_FIELD_KEY to view.text.orEmpty())
            view.copy(searchFields = highlighter(fields))
        }
        is BlockView.Title.Profile -> {
            val fields = listOf(DEFAULT_SEARCH_FIELD_KEY to view.text.orEmpty())
            view.copy(searchFields = highlighter(fields))
        }
        is BlockView.Media.Bookmark -> {
            val fields = listOf(
                SEARCH_FIELD_DESCRIPTION_KEY to view.description.orEmpty(),
                SEARCH_FIELD_TITLE_KEY to view.title.orEmpty(),
                SEARCH_FIELD_URL_KEY to view.url
            )
            view.copy(searchFields = highlighter(fields))
        }
        is BlockView.Media.File -> {
            val fields = listOf(DEFAULT_SEARCH_FIELD_KEY to view.name.orEmpty())
            view.copy(searchFields = highlighter(fields))
        }
        is BlockView.Page -> {
            val fields = listOf(DEFAULT_SEARCH_FIELD_KEY to view.text.orEmpty())
            view.copy(searchFields = highlighter(fields))
        }
        is BlockView.PageArchive -> {
            val fields = listOf(DEFAULT_SEARCH_FIELD_KEY to view.text.orEmpty())
            view.copy(searchFields = highlighter(fields))
        }
        else -> view.also { check(view !is BlockView.Searchable) }
    }
}

fun BlockView.setHighlight(
    highlights: List<BlockView.Searchable.Field>
): BlockView = when (this) {
    is BlockView.Text.Paragraph -> copy(searchFields = highlights)
    is BlockView.Text.Numbered -> copy(searchFields = highlights)
    is BlockView.Text.Bulleted -> copy(searchFields = highlights)
    is BlockView.Text.Checkbox -> copy(searchFields = highlights)
    is BlockView.Text.Toggle -> copy(searchFields = highlights)
    is BlockView.Text.Header.One -> copy(searchFields = highlights)
    is BlockView.Text.Header.Two -> copy(searchFields = highlights)
    is BlockView.Text.Header.Three -> copy(searchFields = highlights)
    is BlockView.Text.Highlight -> copy(searchFields = highlights)
    is BlockView.Title.Basic -> copy(searchFields = highlights)
    is BlockView.Title.Profile -> copy(searchFields = highlights)
    is BlockView.Media.Bookmark -> copy(searchFields = highlights)
    is BlockView.Media.File -> copy(searchFields = highlights)
    is BlockView.Page -> copy(searchFields = highlights)
    is BlockView.PageArchive -> copy(searchFields = highlights)
    else -> this.also { check(this !is BlockView.Searchable) }
}

fun BlockView.setGhostEditorSelection(
    ghostEditorSelection: IntRange?
): BlockView = when (this) {
    is BlockView.Text.Paragraph -> copy(ghostEditorSelection = ghostEditorSelection)
    is BlockView.Text.Numbered -> copy(ghostEditorSelection = ghostEditorSelection)
    is BlockView.Text.Bulleted -> copy(ghostEditorSelection = ghostEditorSelection)
    is BlockView.Text.Checkbox -> copy(ghostEditorSelection = ghostEditorSelection)
    is BlockView.Text.Toggle -> copy(ghostEditorSelection = ghostEditorSelection)
    is BlockView.Text.Header.One -> copy(ghostEditorSelection = ghostEditorSelection)
    is BlockView.Text.Header.Two -> copy(ghostEditorSelection = ghostEditorSelection)
    is BlockView.Text.Header.Three -> copy(ghostEditorSelection = ghostEditorSelection)
    is BlockView.Text.Highlight -> copy(ghostEditorSelection = ghostEditorSelection)
    else -> this.also { check(this !is BlockView.SupportGhostEditorSelection) }
}

fun List<BlockView>.nextSearchTarget(): List<BlockView> {
    val currentTargetView = find { view ->
        view is BlockView.Searchable && view.searchFields.any { it.isTargeted }
    }
    if (currentTargetView == null) {
        val nextCandidate = find { view ->
            view is BlockView.Searchable && view.searchFields.any { it.highlights.isNotEmpty() }
        }
        if (nextCandidate == null) {
            return this
        } else {
            check(nextCandidate is BlockView.Searchable)
            val nextFieldCandidateIndex = nextCandidate.searchFields.indexOfFirst { field ->
                field.highlights.isNotEmpty()
            }
            val highlights = nextCandidate.searchFields.mapIndexed { index, field ->
                if (index == nextFieldCandidateIndex) {
                    field.copy(target = field.highlights.first())
                } else {
                    field
                }
            }
            return map { view ->
                if (view.id == nextCandidate.id) {
                    view.setHighlight(highlights)
                } else {
                    view
                }
            }
        }
    } else {
        check(currentTargetView is BlockView.Searchable)
        val currentField = currentTargetView.searchFields.first { it.isTargeted }
        val currentHighlightIndex = currentField.highlights.indexOf(currentField.target)
        if (currentHighlightIndex < currentField.highlights.size.dec()) {
            val highlights = currentTargetView.searchFields.map { field ->
                if (field.key == currentField.key) {
                    field.copy(target = currentField.highlights[currentHighlightIndex.inc()])
                } else {
                    field
                }
            }
            return map { view ->
                if (view.id == currentTargetView.id) {
                    view.setHighlight(highlights)
                } else {
                    view
                }
            }
        } else {
            val currentTargetFieldIndex = currentTargetView.searchFields.indexOf(currentField)
            val nextFields = currentTargetView.searchFields.subList(
                currentTargetFieldIndex.inc(),
                currentTargetView.searchFields.size
            )
            val nextFieldTargetCandidate = nextFields.find { it.highlights.isNotEmpty() }
            if (nextFieldTargetCandidate != null) {
                val highlights = currentTargetView.searchFields.map { field ->
                    when (field.key) {
                        currentField.key -> field.copy(target = IntRange.EMPTY)
                        nextFieldTargetCandidate.key -> {
                            field.copy(target = nextFieldTargetCandidate.highlights.first())
                        }
                        else -> field
                    }
                }
                return map { view ->
                    if (view.id == currentTargetView.id) {
                        view.setHighlight(highlights)
                    } else {
                        view
                    }
                }
            } else {
                val nextViews = subList(indexOf(currentTargetView).inc(), size)
                val nextCandidate = nextViews.find { view ->
                    view is BlockView.Searchable && view.searchFields.any { it.highlights.isNotEmpty() }
                }
                if (nextCandidate == null) {
                    return this
                } else {
                    check(nextCandidate is BlockView.Searchable)
                    val nextFieldCandidateIndex = nextCandidate.searchFields.indexOfFirst { field ->
                        field.highlights.isNotEmpty()
                    }
                    return map { view ->
                        when (view.id) {
                            nextCandidate.id -> view.setHighlight(
                                nextCandidate.searchFields.mapIndexed { index, field ->
                                    if (index == nextFieldCandidateIndex) {
                                        field.copy(target = field.highlights.first())
                                    } else {
                                        field
                                    }
                                }
                            )
                            currentTargetView.id -> view.setHighlight(
                                currentTargetView.searchFields.map { field ->
                                    field.copy(target = IntRange.EMPTY)
                                }
                            )
                            else -> view
                        }
                    }
                }
            }
        }
    }
}

fun List<BlockView>.previousSearchTarget(): List<BlockView> {
    val currentTargetView = find { view ->
        view is BlockView.Searchable && view.searchFields.any { it.isTargeted }
    }
    if (currentTargetView == null) {
        return this
    } else {
        check(currentTargetView is BlockView.Searchable)
        val currentField = currentTargetView.searchFields.first { it.isTargeted }
        val currentHighlightIndex = currentField.highlights.indexOf(currentField.target)
        if (currentHighlightIndex > 0) {
            val highlights = currentTargetView.searchFields.map { field ->
                if (field.key == currentField.key) {
                    field.copy(target = currentField.highlights[currentHighlightIndex.dec()])
                } else {
                    field
                }
            }
            return map { view ->
                if (view.id == currentTargetView.id) {
                    view.setHighlight(highlights)
                } else {
                    view
                }
            }
        } else {
            val currentTargetFieldIndex = currentTargetView.searchFields.indexOf(currentField)
            val previousFields = currentTargetView.searchFields.subList(
                0,
                currentTargetFieldIndex
            )
            val previousFieldTargetCandidate =
                previousFields.findLast { it.highlights.isNotEmpty() }
            if (previousFieldTargetCandidate != null) {
                val highlights = currentTargetView.searchFields.map { field ->
                    when (field.key) {
                        currentField.key -> field.copy(target = IntRange.EMPTY)
                        previousFieldTargetCandidate.key -> {
                            field.copy(target = previousFieldTargetCandidate.highlights.last())
                        }
                        else -> field
                    }
                }
                return map { view ->
                    if (view.id == currentTargetView.id) {
                        view.setHighlight(highlights)
                    } else {
                        view
                    }
                }
            } else {
                val previousViews = subList(0, indexOf(currentTargetView))
                val previousCandidate = previousViews.findLast { view ->
                    view is BlockView.Searchable && view.searchFields.any { it.highlights.isNotEmpty() }
                }
                if (previousCandidate == null) {
                    return this
                } else {
                    check(previousCandidate is BlockView.Searchable)
                    return map { view ->
                        when (view.id) {
                            previousCandidate.id -> view.setHighlight(
                                previousCandidate.searchFields.mapIndexed { index, field ->
                                    if (index == previousCandidate.searchFields.size.dec()) {
                                        field.copy(target = field.highlights.last())
                                    } else {
                                        field
                                    }
                                }
                            )
                            currentTargetView.id -> view.setHighlight(
                                currentTargetView.searchFields.map { field ->
                                    field.copy(target = IntRange.EMPTY)
                                }
                            )
                            else -> view
                        }
                    }
                }
            }
        }
    }
}