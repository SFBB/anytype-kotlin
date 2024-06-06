package com.anytypeio.anytype.ui.onboarding.screens.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anytypeio.anytype.R
import com.anytypeio.anytype.core_ui.ColorButtonRegular
import com.anytypeio.anytype.core_ui.MnemonicPhrasePaletteColors
import com.anytypeio.anytype.core_ui.OnBoardingTextPrimaryColor
import com.anytypeio.anytype.core_ui.foundation.noRippleClickable
import com.anytypeio.anytype.core_ui.views.ButtonSize
import com.anytypeio.anytype.core_ui.views.ConditionLogin
import com.anytypeio.anytype.core_ui.views.OnBoardingButtonPrimary
import com.anytypeio.anytype.core_ui.views.OnBoardingButtonSecondary
import com.anytypeio.anytype.core_ui.views.TitleLogin
import com.anytypeio.anytype.core_utils.ext.toast
import com.anytypeio.anytype.presentation.onboarding.login.OnboardingMnemonicLoginViewModel
import com.anytypeio.anytype.presentation.onboarding.login.OnboardingMnemonicLoginViewModel.SetupState
import com.anytypeio.anytype.ui.onboarding.OnboardingMnemonicInput

@Composable
fun RecoveryScreenWrapper(
    vm: OnboardingMnemonicLoginViewModel,
    onBackClicked: () -> Unit,
    onScanQrClick: () -> Unit
) {
    RecoveryScreen(
        onBackClicked = onBackClicked,
        onNextClicked = vm::onLoginClicked,
        onActionDoneClicked = vm::onActionDone,
        onScanQrClicked = onScanQrClick,
        isLoading = vm.state.collectAsState().value is SetupState.InProgress
    )
}

@Composable
fun RecoveryScreen(
    onBackClicked: () -> Unit,
    onNextClicked: (Mnemonic) -> Unit,
    onActionDoneClicked: (Mnemonic) -> Unit,
    onScanQrClicked: () -> Unit,
    isLoading: Boolean
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 21.dp)
            ,
            text = stringResource(id = R.string.onboarding_enter_my_vault),
            style = TitleLogin.copy(
                color = OnBoardingTextPrimaryColor
            )
        )

        val text = remember {
            mutableStateOf("")
        }

        val focus = LocalFocusManager.current
        val context = LocalContext.current

        val emptyRecoveryPhraseError = stringResource(R.string.onboarding_your_key_can_t_be_empty)

        LazyColumn(
            content = {
                item {
                    OnboardingMnemonicInput(
                        modifier = Modifier
                            .padding(
                                start = 18.dp,
                                end = 18.dp,
                                top = 71.dp,
                                bottom = 18.dp
                            )
                            .height(165.dp)
                            .fillMaxWidth()
                            .background(
                                color = Color(0x26DAD7CA),
                                shape = RoundedCornerShape(24.dp)
                            )
                            .padding(
                                horizontal = 8.dp,
                                vertical = 4.dp
                            )
                        ,
                        text = text,
                        singleLine = false,
                        placeholder = stringResource(id = R.string.onboarding_type_your_key),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                val input = text.value
                                if (input.isNotEmpty()) {
                                    onActionDoneClicked(text.value).also {
                                        focus.clearFocus()
                                    }
                                } else {
                                    context.toast(emptyRecoveryPhraseError)
                                }
                            }
                        )
                    )
                }
                item {
                    OnBoardingButtonPrimary(
                        text = stringResource(id = R.string.onboarding_enter_my_vault),
                        onClick = {
                            onNextClicked.invoke(text.value).also {
                                focus.clearFocus()
                            }
                        },
                        enabled = text.value.isNotEmpty(),
                        size = ButtonSize.Large,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp),
                        isLoading = isLoading
                    )
                }
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 12.dp),
                        textAlign = TextAlign.Center,
                        text = stringResource(id = R.string.onboarding_login_or),
                        style = ConditionLogin.copy(
                            color = OnBoardingTextPrimaryColor
                        )
                    )
                }
                item {
                    OnBoardingButtonSecondary(
                        text = stringResource(id = R.string.or_scan_qr_code),
                        onClick = {
                            onScanQrClicked.invoke()
                        },
                        enabled = !isLoading,
                        disabledBackgroundColor = Color.Transparent,
                        size = ButtonSize.Large,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 18.dp, end = 18.dp, bottom = 24.dp),
                        textColor = ColorButtonRegular
                    )
                }
            }
        )
        Image(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 16.dp, start = 9.dp)
                .noRippleClickable {
                    focus.clearFocus()
                    onBackClicked()
                },
            painter = painterResource(id = R.drawable.ic_back_onboarding_32),
            contentDescription = "Back button"
        )
    }
}

typealias Mnemonic = String

object MnemonicPhraseFormatter : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val transformed = buildAnnotatedString {
            var colorIndex = 0
            var isPreviousLetterOrDigit = false
            text.forEachIndexed { index, char ->
                if (char.isLetterOrDigit()) {
                    withStyle(
                        style = SpanStyle(
                            color = MnemonicPhrasePaletteColors[colorIndex]
                        )
                    ) {
                        append(char)
                    }
                    isPreviousLetterOrDigit = true
                } else {
                    if (isPreviousLetterOrDigit) {
                        colorIndex = colorIndex.inc()
                        isPreviousLetterOrDigit = false
                    }
                    append(char)
                }
                if (colorIndex > MnemonicPhrasePaletteColors.lastIndex) {
                    colorIndex = 0
                }
            }
        }
        return TransformedText(
            transformed,
            OffsetMapping.Identity
        )
    }
}

@Preview
@Composable
fun RecoveryScreenPreview() {
    RecoveryScreen(
        onBackClicked = {},
        onNextClicked = {},
        onActionDoneClicked = {},
        onScanQrClicked = {},
        isLoading = false
    )
}

@Preview
@Composable
fun RecoveryScreenLoadingPreview() {
    RecoveryScreen(
        onBackClicked = {},
        onNextClicked = {},
        onActionDoneClicked = {},
        onScanQrClicked = {},
        isLoading = true
    )
}