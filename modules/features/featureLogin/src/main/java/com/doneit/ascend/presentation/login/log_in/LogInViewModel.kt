package com.doneit.ascend.presentation.login.log_in

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.dto.LogInUserDTO
import com.doneit.ascend.domain.entity.dto.SocialLogInDTO
import com.doneit.ascend.domain.use_case.interactor.question.QuestionUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.models.PresentationLoginModel
import com.doneit.ascend.presentation.login.models.ValidationResult
import com.doneit.ascend.presentation.login.utils.LoginUtils
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.utils.*
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.facebook.AccessToken
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import com.vrgsoft.networkmanager.livedata.SingleLiveManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class LogInViewModel(
    private val router: LogInContract.Router,
    private val userUseCase: UserUseCase,
    private val questionUseCase: QuestionUseCase,
    private val localStorage: LocalStorage
) : BaseViewModelImpl(), LogInContract.ViewModel {

    override val loginModel = PresentationLoginModel()
    override val isSignInEnabled = ObservableField<Boolean>(false)
    override val facebookNeedLoginSubject = SingleLiveManager<Boolean>()
    override val twitterNeedLoginSubject = SingleLiveManager<Boolean>()
    override val errorRes = MutableLiveData<Int?>()

    override val user = userUseCase.getUserLive()

    init {
        loginModel.phone.validator = { _ ->
            val result = ValidationResult()
            if (isPhoneValid(
                    loginModel.phoneCode.getNotNullString(),
                    loginModel.phone.observableField.getNotNullString()
                ).not()
            ) {
                result.isSussed = false
                result.errors.add(R.string.error_phone)
            }
            result
        }

        loginModel.password.validator = { s ->
            val result = ValidationResult()

            if (s.isValidPassword().not()) {
                result.isSussed = false
                result.errors.add(R.string.error_password)
            }

            result
        }

        val invalidationListener = { updateIsSignInEnabled() }
        loginModel.phone.onFieldInvalidate = invalidationListener
        loginModel.password.onFieldInvalidate = invalidationListener
    }

    override fun singInClick() {
        viewModelScope.launch {
            isSignInEnabled.set(false)
            val requestEntity =
                userUseCase.signIn(
                    LogInUserDTO(
                        loginModel.getPhoneNumber(),
                        loginModel.password.observableField.getNotNullString()
                    )
                )

            if (requestEntity.isSuccessful) {
                requestEntity.successModel?.let {

                    if (requestEntity.successModel!!.userEntity.unansweredQuestions != null &&
                        requestEntity.successModel!!.userEntity.unansweredQuestions!!.isNotEmpty() &&
                        it.userEntity.isMasterMind.not()
                    ) {
                        launch(Dispatchers.Main) {
                            val questionsRequest =
                                questionUseCase.getList()

                            if (questionsRequest.isSuccessful) {
                                questionUseCase.insert(questionsRequest.successModel!!)

                                localStorage.saveUIReturnStep(UIReturnStep.FIRST_TIME_LOGIN)
                                router.navigateToFirstTimeLogin(questionsRequest.successModel!!)
                            }
                        }
                    } else {
                        launch(Dispatchers.Main) {
                            router.goToMain()
                        }
                    }
                }
            } else {
                showDefaultErrorMessage(requestEntity.errorModel!!.toErrorMessage())
                //errorRes.postValue(R.string.error_login)
            }
            isSignInEnabled.set(true)
        }
    }

    override fun signUpClick() {
        router.navigateToSignUp()
    }

    override fun termsClick() {
        router.navigateToTerms()
    }

    override fun privacyPolicyClick() {
        router.navigateToPrivacyPolicy()
    }

    override fun forgotPasswordClick() {
        router.navigateToForgotPassword()
    }

    override fun onFacebookLoginClick() {
        facebookNeedLoginSubject.call(true)
    }

    private fun updateIsSignInEnabled() {
        var isFormValid = true

        isFormValid = isFormValid and loginModel.password.isValid
        isFormValid = isFormValid and loginModel.phone.isValid

        isSignInEnabled.set(isFormValid)
    }

    override fun onFacebookLogin(accessToken: AccessToken) {
        val socialLogInModel = SocialLogInDTO(
            LoginUtils.SOCIAL_TYPE_FACEBOOK,
            accessToken.token,
            null
        )

        socialLogin(socialLogInModel)
    }

    override fun onGoogleLoginClick() {
        router.navigateToGoogleLogin()
    }

    override fun loginWithGoogle(idToken: String) {
        val socialLogInModel = SocialLogInDTO(
            LoginUtils.SOCIAL_TYPE_GOOGLE,
            idToken,
            null
        )

        socialLogin(socialLogInModel)
    }

    override fun onTwitterLoginClick() {
        twitterNeedLoginSubject.call(true)
    }

    override fun loginWithTwitter(token: String, secretToken: String) {
        val socialLogInModel = SocialLogInDTO(
            LoginUtils.SOCIAL_TYPE_TWITTER,
            token,
            secretToken
        )

        socialLogin(socialLogInModel)
    }

    private fun socialLogin(socialLogInDTO: SocialLogInDTO) {
        viewModelScope.launch {
            val requestEntity = userUseCase.socialSignIn(socialLogInDTO)

            if (requestEntity.isSuccessful) {

                if (requestEntity.successModel != null) {
                    requestEntity.successModel?.let {

                        if (requestEntity.successModel!!.userEntity.unansweredQuestions != null &&
                            requestEntity.successModel!!.userEntity.unansweredQuestions!!.isNotEmpty() &&
                            it.userEntity.isMasterMind.not()
                        ) {
                            launch(Dispatchers.Main) {
                                val questionsRequest =
                                    questionUseCase.getList()

                                if (questionsRequest.isSuccessful) {
                                    questionUseCase.insert(questionsRequest.successModel!!)

                                    localStorage.saveUIReturnStep(UIReturnStep.FIRST_TIME_LOGIN)
                                    router.navigateToFirstTimeLogin(questionsRequest.successModel!!)
                                }
                            }
                        } else {
                            router.goToMain()
                        }
                    }
                } else {
                    // TODO: show error message
                }
            }
        }
    }
}