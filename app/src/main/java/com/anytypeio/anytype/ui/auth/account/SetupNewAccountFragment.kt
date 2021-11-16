package com.anytypeio.anytype.ui.auth.account

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.anytypeio.anytype.R
import com.anytypeio.anytype.core_utils.ext.gone
import com.anytypeio.anytype.core_utils.ext.showSnackbar
import com.anytypeio.anytype.core_utils.ext.toast
import com.anytypeio.anytype.core_utils.ext.visible
import com.anytypeio.anytype.di.common.componentManager
import com.anytypeio.anytype.presentation.auth.account.SetupNewAccountViewModel
import com.anytypeio.anytype.presentation.auth.account.SetupNewAccountViewModelFactory
import com.anytypeio.anytype.presentation.auth.account.SetupNewAccountViewState
import com.anytypeio.anytype.ui.base.NavigationFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_setup_new_account.*
import javax.inject.Inject

class SetupNewAccountFragment : NavigationFragment(R.layout.fragment_setup_new_account),
    Observer<SetupNewAccountViewState> {

    @Inject
    lateinit var factory: SetupNewAccountViewModelFactory

    private val vm : SetupNewAccountViewModel by viewModels { factory }

    private val animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.rotation)
    }

    lateinit var callBack : OnBackPressedCallback

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callBack = requireActivity().onBackPressedDispatcher.addCallback(this) {}
        setupNavigation()
        vm.state.observe(viewLifecycleOwner, this)
    }

    private fun setupNavigation() {
        vm.observeNavigation().observe(viewLifecycleOwner, navObserver)
    }

    override fun onChanged(state: SetupNewAccountViewState) {
        when (state) {
            is SetupNewAccountViewState.Loading -> {
                tvError.gone()
                disableBackNavigation()
                icon.startAnimation(animation)
            }
            is SetupNewAccountViewState.Success -> {
                tvError.gone()
                enableBackNavigation()
                animation.cancel()
            }
            is SetupNewAccountViewState.Error -> {
                enableBackNavigation()
                animation.cancel()
                tvError.text = state.message
                tvError.visible()
            }
            is SetupNewAccountViewState.InvalidCodeError -> {
                enableBackNavigation()
                animation.cancel()
                tvError.gone()
                requireActivity().toast(state.message)
            }
        }
    }

    private fun disableBackNavigation() {
        callBack.isEnabled = true
    }

    private fun enableBackNavigation() {
        callBack.isEnabled = false
    }

    override fun injectDependencies() {
        componentManager().setupNewAccountComponent.get().inject(this)
    }

    override fun releaseDependencies() {
        componentManager().setupNewAccountComponent.release()
    }
}