package com.anytypeio.anytype.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.anytypeio.anytype.R
import com.anytypeio.anytype.core_models.Id
import com.anytypeio.anytype.core_models.ObjectWrapper
import com.anytypeio.anytype.core_models.primitives.SpaceId
import com.anytypeio.anytype.core_ui.extensions.throttledClick
import com.anytypeio.anytype.core_utils.ext.arg
import com.anytypeio.anytype.core_utils.ext.argOrNull
import com.anytypeio.anytype.core_utils.ext.toast
import com.anytypeio.anytype.core_utils.intents.ActivityCustomTabsHelper
import com.anytypeio.anytype.core_utils.tools.FeatureToggles
import com.anytypeio.anytype.core_utils.ui.BaseComposeFragment
import com.anytypeio.anytype.di.common.componentManager
import com.anytypeio.anytype.other.DefaultDeepLinkResolver
import com.anytypeio.anytype.presentation.home.Command
import com.anytypeio.anytype.presentation.home.HomeScreenViewModel
import com.anytypeio.anytype.presentation.home.HomeScreenViewModel.Navigation
import com.anytypeio.anytype.presentation.home.HomeScreenViewModel.ViewerSpaceSettingsState
import com.anytypeio.anytype.presentation.spaces.SpaceIconView
import com.anytypeio.anytype.presentation.widgets.DropDownMenuAction
import com.anytypeio.anytype.presentation.widgets.WidgetView
import com.anytypeio.anytype.ui.base.navigation
import com.anytypeio.anytype.ui.gallery.GalleryInstallationFragment
import com.anytypeio.anytype.ui.multiplayer.LeaveSpaceWarning
import com.anytypeio.anytype.ui.multiplayer.RequestJoinSpaceFragment
import com.anytypeio.anytype.ui.multiplayer.ShareQrCodeSpaceInviteFragment
import com.anytypeio.anytype.ui.multiplayer.ShareSpaceFragment
import com.anytypeio.anytype.ui.objects.creation.ObjectTypeSelectionFragment
import com.anytypeio.anytype.ui.objects.creation.WidgetObjectTypeFragment
import com.anytypeio.anytype.ui.objects.creation.WidgetSourceTypeFragment
import com.anytypeio.anytype.ui.objects.types.pickers.ObjectTypeSelectionListener
import com.anytypeio.anytype.ui.objects.types.pickers.WidgetObjectTypeListener
import com.anytypeio.anytype.ui.objects.types.pickers.WidgetSourceTypeListener
import com.anytypeio.anytype.ui.payments.MembershipFragment
import com.anytypeio.anytype.ui.settings.space.SpaceSettingsFragment
import com.anytypeio.anytype.ui.settings.typography
import com.anytypeio.anytype.ui.widgets.SelectWidgetSourceFragment
import com.anytypeio.anytype.ui.widgets.SelectWidgetTypeFragment
import com.anytypeio.anytype.ui_settings.space.new_settings.ViewerSpaceSettings
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeScreenFragment : BaseComposeFragment(),
    ObjectTypeSelectionListener,
    WidgetObjectTypeListener,
    WidgetSourceTypeListener {

    private val deepLink: String? get() = argOrNull(DEEP_LINK_KEY)

    private val space: Id get() = arg<Id>(SPACE_ID_KEY)

    private var isMnemonicReminderDialogNeeded: Boolean
        get() = argOrNull<Boolean>(SHOW_MNEMONIC_KEY) == true
        set(value) { arguments?.putBoolean(SHOW_MNEMONIC_KEY, value) }


    @Inject
    lateinit var featureToggles: FeatureToggles

    @Inject
    lateinit var factory: HomeScreenViewModel.Factory

    private val vm by viewModels<HomeScreenViewModel> { factory }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            MaterialTheme(
                typography = typography,
                shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(16.dp)),
                colors = MaterialTheme.colors.copy(
                    surface = colorResource(id = R.color.background_secondary)
                )
            ) {
                val view = (vm.views.collectAsStateWithLifecycle().value.find {
                    it is WidgetView.SpaceWidget.View
                } as? WidgetView.SpaceWidget.View)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    HomeScreenToolbar(
                        spaceIconView = view?.icon ?: SpaceIconView.Loading,
                        onSpaceIconClicked = { vm.onSpaceSettingsClicked(space = SpaceId(space)) },
                        membersCount = view?.membersCount ?: 0,
                        name = view?.space?.name.orEmpty(),
                        onBackButtonClicked = {
                            vm.onBackClicked(
                                isSpaceRoot = isSpaceRootScreen()
                            )
                        },
                        onSettingsClicked = { vm.onSpaceSettingsClicked(space = SpaceId(space)) }
                    )
                    PageWithWidgets(
                        modifier = Modifier.weight(1f),
                        showSpaceWidget = false
                    )
                }
            }

            val spaceSettingsState = vm.viewerSpaceSettingsState.collectAsStateWithLifecycle().value

            if (spaceSettingsState is ViewerSpaceSettingsState.Visible) {
                ModalBottomSheet(
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 32.dp),
                    containerColor = colorResource(R.color.background_secondary),
                    onDismissRequest = vm::onDismissViewerSpaceSettings,
                    dragHandle = null,
                    content = {
                        ViewerSpaceSettings(
                            title = spaceSettingsState.name,
                            icon = spaceSettingsState.icon,
                            description = spaceSettingsState.description,
                            info = spaceSettingsState.techInfo,
                            uiEvent = {
                                vm.onViewerSpaceSettingsUiEvent(
                                    space = SpaceId(space),
                                    uiEvent = it
                                )
                            }
                        )
                    }
                )
            }

            BackHandler {
                vm.onBackClicked(
                    isSpaceRoot = isSpaceRootScreen()
                )
            }
        }
    }

    @Composable
    fun PageWithWidgets(
        modifier: Modifier = Modifier,
        showSpaceWidget: Boolean = true
    ) {
        HomeScreen(
            modifier = modifier,
            widgets = if (showSpaceWidget) vm.views.collectAsState().value else vm.views.collectAsState().value.filter { it !is WidgetView.SpaceWidget },
            mode = vm.mode.collectAsState().value,
            onExpand = { path -> vm.onExpand(path) },
            onCreateWidget = vm::onCreateWidgetClicked,
            onEditWidgets = vm::onEditWidgets,
            onExitEditMode = vm::onExitEditMode,
            onWidgetMenuAction = { widget: Id, action: DropDownMenuAction ->
                vm.onDropDownMenuAction(widget, action)
            },
            onWidgetElementClicked = vm::onWidgetElementClicked,
            onWidgetSourceClicked = vm::onWidgetSourceClicked,
            onChangeWidgetView = vm::onChangeCurrentWidgetView,
            onToggleExpandedWidgetState = vm::onToggleCollapsedWidgetState,
            onSearchClicked = vm::onSearchIconClicked,
            onCreateNewObjectClicked = throttledClick(
                onClick = { vm.onCreateNewObjectClicked() }
            ),
            onCreateNewObjectLongClicked = throttledClick(
                onClick = { vm.onCreateNewObjectLongClicked() }
            ),
            onSpaceWidgetClicked = throttledClick(
                onClick = vm::onSpaceWidgetClicked
            ),
            onBundledWidgetClicked = vm::onBundledWidgetClicked,
            onMove = vm::onMove,
            onObjectCheckboxClicked = vm::onObjectCheckboxClicked,
            onSpaceWidgetShareIconClicked = vm::onSpaceWidgetShareIconClicked,
            onSeeAllObjectsClicked = vm::onSeeAllObjectsClicked,
            onCreateObjectInsideWidget = vm::onCreateObjectInsideWidget,
            onCreateDataViewObject = vm::onCreateDataViewObject,
            onNavBarShareButtonClicked = vm::onNavBarShareIconClicked,
            navPanelState = vm.navPanelState.collectAsStateWithLifecycle().value,
            onHomeButtonClicked = vm::onHomeButtonClicked,
            onCreateElement = vm::onCreateWidgetElementClicked,
            onWidgetMenuTriggered = vm::onWidgetMenuTriggered
        )
    }

    override fun onStart() {
        Timber.d("onStart")
        super.onStart()
        vm.onStart()
    }

    override fun onStop() {
        super.onStop()
        vm.onStop()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { vm.commands.collect { command -> proceed(command) } }
                launch { vm.navigation.collect { command -> proceed(command) } }
                launch { vm.toasts.collect { toast(it) } }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isMnemonicReminderDialogNeeded)
            showMnemonicReminderAlert()
        proceedWithDeepLinks()
    }

    private fun proceedWithDeepLinks() {
        val deepLinkFromFragmentArgs = deepLink
        if (deepLinkFromFragmentArgs != null) {
            Timber.d("Deeplink  from fragment args")
            vm.onResume(DefaultDeepLinkResolver.resolve(deepLinkFromFragmentArgs))
            arguments?.putString(DEEP_LINK_KEY, null)
        } else {
            vm.onResume(null)
        }
    }

    private fun proceed(command: Command) {
        when (command) {
            is Command.ChangeWidgetSource -> {
                runCatching {
                    findNavController().navigate(
                        R.id.selectWidgetSourceScreen,
                        args = SelectWidgetSourceFragment.args(
                            ctx = command.ctx,
                            widget = command.widget,
                            source = command.source,
                            type = command.type,
                            isInEditMode = command.isInEditMode,
                            spaceId = command.space
                        )
                    )
                }.onFailure {
                    Timber.e(it, "Error while navigation")
                }
            }
            is Command.SelectWidgetSource -> {
                runCatching {
                    findNavController().navigate(
                        R.id.selectWidgetSourceScreen,
                        args = SelectWidgetSourceFragment.args(
                            ctx = command.ctx,
                            target = command.target,
                            isInEditMode = command.isInEditMode,
                            spaceId = command.space
                        )
                    )
                }.onFailure {
                    Timber.e(it, "Error while navigation")
                }
            }
            is Command.ChangeWidgetType -> {
                runCatching {
                    findNavController().navigate(
                        R.id.selectWidgetTypeScreen,
                        args = SelectWidgetTypeFragment.args(
                            ctx = command.ctx,
                            widget = command.widget,
                            source = command.source,
                            type = command.type,
                            layout = command.layout,
                            isInEditMode = command.isInEditMode
                        )
                    )
                }.onFailure {
                    Timber.e(it, "Error while navigation")
                }
            }
            is Command.SelectWidgetType -> {
                runCatching {
                    findNavController().navigate(
                        R.id.selectWidgetTypeScreen,
                        args = SelectWidgetTypeFragment.args(
                            ctx = command.ctx,
                            source = command.source,
                            layout = command.layout,
                            target = command.target,
                            isInEditMode = command.isInEditMode
                        )
                    )
                }.onFailure {
                    Timber.e(it, "Error while navigation")
                }
            }
            is Command.Deeplink.Invite -> {
                findNavController().navigate(
                    R.id.requestJoinSpaceScreen,
                    RequestJoinSpaceFragment.args(link = command.link)
                )
            }
            is Command.Deeplink.GalleryInstallation -> {
                findNavController().navigate(
                    R.id.galleryInstallationScreen,
                    GalleryInstallationFragment.args(
                        deepLinkType = command.deepLinkType,
                        deepLinkSource = command.deepLinkSource
                    )
                )
            }
            is Command.Deeplink.MembershipScreen -> {
                runCatching {
                    findNavController().navigate(
                        R.id.paymentsScreen,
                        MembershipFragment.args(command.tierId),
                        NavOptions.Builder().setLaunchSingleTop(true).build()
                    )
                }.onFailure {
                    Timber.e(it, "Error while opening membership screen")
                }
            }
            is Command.Deeplink.DeepLinkToObjectNotWorking -> {
                toast(
                    getString(R.string.multiplayer_deeplink_to_your_object_error)
                )
            }
            is Command.ShareSpace -> {
                runCatching {
                    findNavController().navigate(
                        R.id.shareSpaceScreen,
                        args = ShareSpaceFragment.args(command.space)
                    )
                }.onFailure {
                    Timber.e(it, "Error while opening share screen")
                }
            }
            is Command.ShowInviteLinkQrCode -> {
                runCatching {
                    findNavController().navigate(
                        R.id.shareSpaceInviteQrCodeScreen,
                        ShareQrCodeSpaceInviteFragment.args(link = command.link)
                    )
                }.onFailure {
                    Timber.w(it, "Error while showing invite QR code from space settings in widgets")
                }
            }
            is Command.ShowLeaveSpaceWarning -> {
                val dialog = LeaveSpaceWarning.new()
                dialog.onLeaveSpaceAccepted = {
                    dialog.dismiss()
                    vm.onLeaveSpaceAcceptedClicked(SpaceId(space))
                }
                dialog.show(childFragmentManager, null)
            }
            is Command.CreateSourceForNewWidget -> {
                val dialog = WidgetSourceTypeFragment.new(
                    space = command.space.id,
                    widgetId = command.widgets
                )
                dialog.show(childFragmentManager, null)
            }
            is Command.CreateObjectForWidget -> {
                val dialog = WidgetObjectTypeFragment.new(
                    space = command.space.id,
                    widgetId = command.widget,
                    source = command.source
                )
                dialog.show(childFragmentManager, null)
            }
            is Command.OpenSpaceSettings -> {
                runCatching {
                    findNavController().navigate(
                        R.id.action_open_space_settings,
                        SpaceSettingsFragment.args(space = command.spaceId)
                    )
                }.onFailure { e ->
                    Timber.e(e, "Error while opening space settings")
                }
            }
            is Command.OpenObjectCreateDialog -> {
                val dialog = ObjectTypeSelectionFragment.new(
                    space = command.space.id
                )
                dialog.show(childFragmentManager, "object-create-dialog")
            }
            is Command.OpenGlobalSearchScreen -> {
                runCatching {
                    navigation().openGlobalSearch(
                        space = command.space
                    )
                }.onFailure {
                    Timber.e(it, "Error while opening global search screen")
                }
            }
            is Command.OpenVault -> {
                runCatching {
                    findNavController().navigate(R.id.action_open_vault)
                }.onFailure {
                    Timber.e(it, "Error while opening vault from home screen")
                }
            }
            is Command.Exit -> {
                runCatching {
                    findNavController().popBackStack()
                }.onFailure {
                    Timber.e(it, "Error exiting home screen")
                }
            }
            is Command.ShowWidgetAutoCreatedToast -> {
                toast(
                    msg = getString(
                        R.string.widget_auto_created_toast,
                        command.name
                    ),
                    duration = Toast.LENGTH_LONG
                )
            }
        }
    }

    private fun proceed(destination: Navigation) {
        Timber.d("New destination: $destination")
        when (destination) {
            is Navigation.OpenObject -> runCatching {
                navigation().openDocument(
                    target = destination.ctx,
                    space = destination.space
                )
            }
            is Navigation.OpenSet -> runCatching {
                navigation().openObjectSet(
                    target = destination.ctx,
                    space = destination.space,
                    view = destination.view
                )
            }
            is Navigation.OpenChat -> runCatching {
                navigation().openChat(
                    target = destination.ctx,
                    space = destination.space
                )
            }
            is Navigation.ExpandWidget -> runCatching {
                navigation().launchCollections(
                    subscription = destination.subscription,
                    space = destination.space
                )
            }
            is Navigation.OpenAllContent -> {
                runCatching {
                    navigation().openAllContent(space = destination.space)
                }.onFailure { e ->
                    Timber.e(e, "Error while opening all content from widgets")
                }
            }
            is Navigation.OpenSpaceSwitcher -> {
                findNavController().navigate(R.id.actionOpenSpaceSwitcher)
            }
            is Navigation.OpenDateObject -> {
                runCatching {
                    navigation().openDateObject(
                        objectId = destination.ctx,
                        space = destination.space
                    )
                }.onFailure { e ->
                    Timber.e(e, "Error while opening date object from widgets")
                }
            }
            is Navigation.OpenParticipant -> {
                runCatching {
                    navigation().openParticipantObject(
                        objectId = destination.objectId,
                        space = destination.space
                    )
                    }.onFailure { e ->
                    Timber.e(e, "Error while opening participant from widgets")
                }
            }
            is Navigation.OpenType -> {
                runCatching {
                    navigation().openObjectType(
                        objectId = destination.target,
                        space = destination.space
                    )
                }.onFailure { e ->
                    Timber.e(e, "Error while opening participant from widgets")
                }
            }
            is Navigation.OpenOwnerOrEditorSpaceSettings -> {
                runCatching {
                    findNavController()
                        .navigate(
                            R.id.actionOpenSpaceSettings,
                            SpaceSettingsFragment.args(
                                space = SpaceId(space)
                            )
                        )
                }.onFailure {
                    Timber.e(it, "Error while opening space settings")
                }
            }
            is Navigation.OpenBookmarkUrl -> {
                try {
                    ActivityCustomTabsHelper.openUrl(
                        activity = requireActivity(),
                        url = destination.url
                    )
                } catch (e: Throwable) {
                    Timber.e(e, "Error opening bookmark URL: ${destination.url}")
                    toast("Failed to open URL")
                }
            }
        }
    }

    private fun showMnemonicReminderAlert() {
        isMnemonicReminderDialogNeeded = false
        findNavController().navigate(R.id.dashboardKeychainDialog)
    }

    override fun onCreateWidgetObject(
        objType: ObjectWrapper.Type,
        widgetId: Id,
        source: Id
    ) {
        vm.onCreateObjectForWidget(type = objType, source = source)
    }

    override fun onSetNewWidgetSource(objType: ObjectWrapper.Type, widgetId: Id) {
        vm.onNewWidgetSourceTypeSelected(type = objType, widgets = widgetId)
    }

    override fun onSelectObjectType(objType: ObjectWrapper.Type) {
        vm.onCreateNewObjectClicked(objType = objType)
    }

    override fun injectDependencies() {
        componentManager().homeScreenComponent.get().inject(this)

    }

    override fun releaseDependencies() {
        componentManager().homeScreenComponent.release()
    }

    companion object {
        const val SHOW_MNEMONIC_KEY = "arg.home-screen.show-mnemonic"
        const val DEEP_LINK_KEY = "arg.home-screen.deep-link"
        const val SPACE_ID_KEY = "arg.home-screen.space-id"

        fun args(
            space: Id,
            deeplink: String? = null
        ) : Bundle = bundleOf(
            DEEP_LINK_KEY to deeplink,
            SPACE_ID_KEY to space
        )
    }
}

fun Fragment.isSpaceRootScreen() : Boolean {
    val previous = findNavController().previousBackStackEntry
    return previous?.destination?.id == R.id.vaultScreen
}