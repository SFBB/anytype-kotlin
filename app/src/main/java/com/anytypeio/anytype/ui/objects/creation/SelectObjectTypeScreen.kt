package com.anytypeio.anytype.ui.objects.creation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.anytypeio.anytype.R
import com.anytypeio.anytype.core_ui.extensions.throttledClick
import com.anytypeio.anytype.core_ui.foundation.AlertConfig
import com.anytypeio.anytype.core_ui.foundation.Dragger
import com.anytypeio.anytype.core_ui.foundation.EmptyState
import com.anytypeio.anytype.core_ui.foundation.GRADIENT_TYPE_RED
import com.anytypeio.anytype.core_ui.foundation.Toolbar
import com.anytypeio.anytype.core_ui.views.BodyRegular
import com.anytypeio.anytype.core_ui.views.Caption1Medium
import com.anytypeio.anytype.core_ui.views.Title2
import com.anytypeio.anytype.core_ui.widgets.ListWidgetObjectIcon
import com.anytypeio.anytype.core_ui.widgets.SearchField
import com.anytypeio.anytype.presentation.objects.ObjectIcon
import com.anytypeio.anytype.presentation.objects.SelectTypeView
import com.anytypeio.anytype.presentation.objects.SelectTypeViewState

@Preview
@Composable
fun PreviewScreen() {
    SelectObjectTypeScreen(
        onTypeClicked = {},
        state = SelectTypeViewState.Loading,
        onQueryChanged = {},
        onFocused = {},
        onUnpinTypeClicked = {},
        onPinOnTopClicked = {},
        onSetDefaultTypeClicked = {},
        onMoveLeftClicked = {},
        onMoveRightClicked = {},
        title = "Create object"
    )
}

@Composable
fun SelectObjectTypeScreen(
    title: String,
    onTypeClicked: (SelectTypeView.Type) -> Unit,
    onUnpinTypeClicked: (SelectTypeView.Type) -> Unit,
    onPinOnTopClicked: (SelectTypeView.Type) -> Unit,
    onSetDefaultTypeClicked: (SelectTypeView.Type) -> Unit,
    onMoveLeftClicked: (SelectTypeView.Type) -> Unit,
    onMoveRightClicked: (SelectTypeView.Type) -> Unit,
    onQueryChanged: (String) -> Unit,
    onFocused: () -> Unit,
    state: SelectTypeViewState
) {
    Column(
        modifier = Modifier.nestedScroll(rememberNestedScrollInteropConnection())

    ) {
        Dragger(
            Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 6.dp)
                .verticalScroll(rememberScrollState())
        )
        Toolbar(title = title)
        SearchField(
            onQueryChanged = onQueryChanged,
            onFocused = onFocused
        )
        Spacer(modifier = Modifier.height(8.dp))
        ScreenContent(
            state = state,
            onTypeClicked = onTypeClicked,
            onPinOnTopClicked = onPinOnTopClicked,
            onUnpinTypeClicked = onUnpinTypeClicked,
            onSetDefaultTypeClicked = onSetDefaultTypeClicked,
            onMoveLeftClicked = onMoveLeftClicked,
            onMoveRightClicked = onMoveRightClicked
        )
    }
}

@Composable
private fun ScreenContent(
    state: SelectTypeViewState,
    onTypeClicked: (SelectTypeView.Type) -> Unit,
    onUnpinTypeClicked: (SelectTypeView.Type) -> Unit,
    onPinOnTopClicked: (SelectTypeView.Type) -> Unit,
    onSetDefaultTypeClicked: (SelectTypeView.Type) -> Unit,
    onMoveLeftClicked: (SelectTypeView.Type) -> Unit,
    onMoveRightClicked: (SelectTypeView.Type) -> Unit,
) {
    when (state) {
        is SelectTypeViewState.Content -> {
            FlowRowContent(
                views = state.views,
                onTypeClicked = onTypeClicked,
                onPinOnTopClicked = onPinOnTopClicked,
                onUnpinTypeClicked = onUnpinTypeClicked,
                onSetDefaultTypeClicked = onSetDefaultTypeClicked,
                onMoveRightClicked = onMoveRightClicked,
                onMoveLeftClicked = onMoveLeftClicked
            )
        }
        SelectTypeViewState.Empty -> {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(500)),
                exit = fadeOut(animationSpec = tween(500))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    EmptyState(
                        modifier = Modifier.align(Alignment.Center),
                        title = stringResource(id = R.string.nothing_found),
                        description = stringResource(id = R.string.nothing_found_object_types),
                        icon = AlertConfig.Icon(
                            gradient = GRADIENT_TYPE_RED,
                            icon = R.drawable.ic_alert_error
                        )
                    )
                }
            }
        }

        SelectTypeViewState.Loading -> {}
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun FlowRowContent(
    views: List<SelectTypeView>,
    onTypeClicked: (SelectTypeView.Type) -> Unit,
    onUnpinTypeClicked: (SelectTypeView.Type) -> Unit,
    onPinOnTopClicked: (SelectTypeView.Type) -> Unit,
    onSetDefaultTypeClicked: (SelectTypeView.Type) -> Unit,
    onMoveLeftClicked: (SelectTypeView.Type) -> Unit,
    onMoveRightClicked: (SelectTypeView.Type) -> Unit,
) {
    FlowRow(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        views.forEach { view ->
            when (view) {
                is SelectTypeView.Type -> {
                    val isMenuExpanded = remember {
                        mutableStateOf(false)
                    }
                    val haptic = LocalHapticFeedback.current
                    Box {
                        ObjectTypeItem(
                            name = view.name,
                            icon = view.icon,
                            onItemClicked = throttledClick(
                                onClick = { onTypeClicked(view) }
                            ),
                            onItemLongClicked = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                isMenuExpanded.value = !isMenuExpanded.value
                            },
                            modifier = Modifier,
                            isSelected = view.isDefault
                        )
                        ObjectTypeItemMenu(
                            view = view,
                            isMenuExpanded = isMenuExpanded,
                            onPinOnTopClicked = onPinOnTopClicked,
                            onUnpinTypeClicked = onUnpinTypeClicked,
                            onSetDefaultTypeClicked = onSetDefaultTypeClicked,
                            onMoveLeftClicked = onMoveLeftClicked,
                            onMoveRightClicked = onMoveRightClicked
                        )
                    }
                }
                is SelectTypeView.Section.Pinned -> {
                    Section(
                        title = stringResource(id = R.string.create_object_section_pinned),
                    )
                }
                is SelectTypeView.Section.Groups -> {
                    Section(
                        title = stringResource(id = R.string.create_object_section_lists),
                    )
                }
                is SelectTypeView.Section.Objects -> {
                    Section(
                        title = stringResource(id = R.string.create_object_section_objects)
                    )
                }
                is SelectTypeView.Section.Library -> {
                    Section(
                        title = stringResource(id = R.string.create_object_section_library),
                    )
                }
            }

        }
    }
}

@Composable
private fun ObjectTypeItemMenu(
    view: SelectTypeView.Type,
    isMenuExpanded: MutableState<Boolean>,
    onPinOnTopClicked: (SelectTypeView.Type) -> Unit,
    onUnpinTypeClicked: (SelectTypeView.Type) -> Unit,
    onSetDefaultTypeClicked: (SelectTypeView.Type) -> Unit,
    onMoveLeftClicked: (SelectTypeView.Type) -> Unit,
    onMoveRightClicked: (SelectTypeView.Type) -> Unit
) {
    if (view.isPinnable) {
        DropdownMenu(
            expanded = isMenuExpanded.value,
            onDismissRequest = { isMenuExpanded.value = false },
            offset = DpOffset(x = 0.dp, y = 6.dp)
        ) {
            if (!view.isPinned) {
                DropdownMenuItem(
                    onClick = {
                        isMenuExpanded.value = false
                        onPinOnTopClicked(view)
                    }
                ) {
                    Text(
                        text = stringResource(R.string.any_object_creation_menu_pin_on_top),
                        style = BodyRegular,
                        color = colorResource(id = R.color.text_primary)
                    )
                }
            }
            if (view.isPinned) {
                DropdownMenuItem(
                    onClick = {
                        isMenuExpanded.value = false
                        onUnpinTypeClicked(view)
                    }
                ) {
                    Text(
                        text = stringResource(R.string.any_object_creation_menu_unpin),
                        style = BodyRegular,
                        color = colorResource(id = R.color.text_primary)
                    )
                }
            }
            if (view.canBeDefault && !view.isDefault && !view.isFromLibrary) {
                DropdownMenuItem(
                    onClick = {
                        isMenuExpanded.value = false
                        onSetDefaultTypeClicked(view)
                    }
                ) {
                    Text(
                        text = stringResource(R.string.any_object_creation_menu_set_as_default),
                        style = BodyRegular,
                        color = colorResource(id = R.color.text_primary)
                    )
                }
            }
            if (view.isPinned) {
                if (!view.isFirstInSection && !view.isLastInSection) {
                    DropdownMenuItem(
                        onClick = {
                            isMenuExpanded.value = false
                            onMoveLeftClicked(view)
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.any_object_creation_menu_move_left),
                            style = BodyRegular,
                            color = colorResource(id = R.color.text_primary)
                        )
                    }
                    DropdownMenuItem(
                        onClick = {
                            isMenuExpanded.value = false
                            onMoveRightClicked(view)
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.any_object_creation_menu_move_right),
                            style = BodyRegular,
                            color = colorResource(id = R.color.text_primary)
                        )
                    }
                }
                if (view.isFirstInSection && !view.isLastInSection) {
                    DropdownMenuItem(
                        onClick = {
                            isMenuExpanded.value = false
                            onMoveRightClicked(view)
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.any_object_creation_menu_move_right),
                            style = BodyRegular,
                            color = colorResource(id = R.color.text_primary)
                        )
                    }
                }
                if (view.isLastInSection && !view.isFirstInSection) {
                    DropdownMenuItem(
                        onClick = {
                            isMenuExpanded.value = false
                            onMoveLeftClicked(view)
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.any_object_creation_menu_move_left),
                            style = BodyRegular,
                            color = colorResource(id = R.color.text_primary)
                        )
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun LazyColumnContent(
    views: List<SelectTypeView>,
    onTypeClicked: (SelectTypeView.Type) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(
            start = 20.dp,
            end = 20.dp
        )
    ) {
        views.forEach { view ->
            when (view) {
                is SelectTypeView.Section.Groups -> {
                    item(
                        key = view.javaClass.name,
                        span = { GridItemSpan(maxLineSpan) }
                    ) {
                        Section(
                            title = stringResource(id = R.string.create_object_section_lists),
                        )
                    }
                }
                is SelectTypeView.Section.Objects -> {
                    item(
                        key = view.javaClass.name,
                        span = { GridItemSpan(maxLineSpan) }
                    ) {
                        Section(
                            title = stringResource(id = R.string.create_object_section_objects)
                        )
                    }
                }
                is SelectTypeView.Section.Pinned -> {
                    item(
                        key = view.javaClass.name,
                        span = { GridItemSpan(maxLineSpan) }
                    ) {
                        Section(
                            title = stringResource(id = R.string.create_object_section_pinned)
                        )
                    }
                }
                is SelectTypeView.Section.Library -> {
                    item(
                        key = view.javaClass.name,
                        span = { GridItemSpan(maxLineSpan) }
                    ) {
                        Section(
                            title = stringResource(id = R.string.create_object_section_library)
                        )
                    }
                }
                is SelectTypeView.Type -> {
                    item(
                        key = "select-object-type-${view.typeKey}"
                    ) {
                        ObjectTypeItem(
                            name = view.name,
                            icon = view.icon,
                            onItemClicked = throttledClick(
                                onClick = {
                                    onTypeClicked(view)
                                }
                            ),
                            onItemLongClicked = {

                            },
                            modifier = Modifier.animateItem(),
                            isSelected = view.isDefault
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun ObjectTypeItem(
    modifier: Modifier,
    name: String,
    icon: ObjectIcon,
    isSelected: Boolean,
    onItemClicked: () -> Unit,
    onItemLongClicked: () -> Unit
) {
    Row(
        modifier = modifier
            .height(48.dp)
            .border(
                width = 1.dp,
                color = if (isSelected)
                    colorResource(id = R.color.palette_system_amber_50)
                else colorResource(
                    id =
                    R.color.shape_primary
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .combinedClickable(
                onClick = {
                    onItemClicked()
                },
                onLongClick = {
                    onItemLongClicked()
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier.width(14.dp)
        )
        ListWidgetObjectIcon(
            icon = icon,
            iconSize = 18.dp,
            modifier = Modifier,
            iconWithoutBackgroundMaxSize = 200.dp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = name,
            style = Title2,
            color = colorResource(id = R.color.text_primary),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(end = 16.dp)
        )
    }
}

@Composable
private fun Section(title: String) {
    Box(
        modifier = Modifier
            .height(44.dp)
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.BottomStart),
            text = title,
            color = colorResource(id = R.color.text_secondary),
            style = Caption1Medium
        )
    }
}



@Preview
@Composable
fun ClipboardCreateObjectPreview() {
    ClipboardBottomToolbar(
        type = CLIPBOARD_TYPE_OBJECT,
        onToolbarClicked = {}
    )
}

@Preview
@Composable
fun ClipboardCreateBookmarkPreview() {
    ClipboardBottomToolbar(
        type = CLIPBOARD_TYPE_BOOKMARK,
        onToolbarClicked = {}
    )
}

@Composable
fun ClipboardBottomToolbar(
    type: ClipboardDataType,
    modifier: Modifier = Modifier,
    onToolbarClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(color = colorResource(id = R.color.background_secondary))
            .clickable { onToolbarClicked() }
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_clipboard_bottom_toolbar),
            contentDescription = "Clipboard icon",
            modifier = Modifier
                .padding(start = 20.dp)
                .align(Alignment.CenterStart)
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 50.dp),
            style = Caption1Medium,
            color = colorResource(id = R.color.text_secondary),
            text = when(type) {
                CLIPBOARD_TYPE_OBJECT -> stringResource(R.string.clipboard_panel_create_object_from_clipboard)
                CLIPBOARD_TYPE_BOOKMARK -> stringResource(R.string.clipboard_panel_create_bookmark_from_clipboard)
                else -> throw IllegalStateException("Unexpected type")
            }
        )
    }
}

const val CLIPBOARD_TYPE_OBJECT = 0
const val CLIPBOARD_TYPE_BOOKMARK = 1
typealias ClipboardDataType = Int