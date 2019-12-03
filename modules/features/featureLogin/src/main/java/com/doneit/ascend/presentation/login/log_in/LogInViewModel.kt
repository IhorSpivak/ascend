package com.doneit.ascend.presentation.login.log_in

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.LogInUserModel
import com.doneit.ascend.domain.entity.SocialLogInModel
import com.doneit.ascend.domain.use_case.interactor.question.QuestionUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.models.PresentationLoginModel
import com.doneit.ascend.presentation.login.models.ValidationResult
import com.doneit.ascend.presentation.login.utils.LoginUtils
import com.doneit.ascend.presentation.login.utils.getNotNull
import com.doneit.ascend.presentation.login.utils.isPhoneValid
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.utils.LocalStorage
import com.doneit.ascend.presentation.utils.UIReturnStep
import com.facebook.AccessToken
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import com.vrgsoft.networkmanager.livedata.SingleLiveManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
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
    override val isSignInEnabled = ObservableField<Boolean>(true)
    override val facebookNeedLoginSubject = SingleLiveManager<Boolean>()
    override val googleNeedLoginSubject = SingleLiveManager<Boolean>()
    override val twitterNeedLoginSubject = SingleLiveManager<Boolean>()
    override val errorRes = MutableLiveData<Int?>()

    init {
        loginModel.phone.validator = { s ->
            val result = ValidationResult()
            if (isPhoneValid(
                    loginModel.phoneCode.getNotNull(),
                    loginModel.phone.observableField.getNotNull()
                ).not()
            ) {
                result.isSussed = false
                result.errors.add(R.string.error_phone)
            }
            result
        }

        val invalidationListener = { updateIsSignInEnabled() }
        loginModel.phone.onFieldInvalidate = invalidationListener
    }

    override fun singInClick() {
        viewModelScope.launch {
            isSignInEnabled.set(false)
            val requestEntity =
                userUseCase.signIn(
                    LogInUserModel(
                        loginModel.getPhoneNumber(),
                        loginModel.password
                    )
                )

            if (requestEntity.isSuccessful) {
                val token = requestEntity.successModel?.token
                token?.let {
                    localStorage.saveSessionToken(it)
                }

                launch(Dispatchers.Main) {
                    router.goToMain()
                }
            } else {
                errorRes.postValue(R.string.error_login)
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

        isFormValid = isFormValid and loginModel.phone.isValid

        isSignInEnabled.set(isFormValid)
    }

    override fun onFacebookLogin(accessToken: AccessToken) {
        val socialLogInModel = SocialLogInModel(
            LoginUtils.SOCIAL_TYPE_FACEBOOK,
            accessToken.token,
            null
        )

        socialLogin(socialLogInModel)
    }

    override fun onGoogleLoginClick() {
        //router.navigateToGoogleLogin()
        googleNeedLoginSubject.call(true)
    }

    override fun loginWithGoogle(idToken: String) {
        val socialLogInModel = SocialLogInModel(
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
        val socialLogInModel = SocialLogInModel(
            LoginUtils.SOCIAL_TYPE_TWITTER,
            token,
            secretToken
        )

        socialLogin(socialLogInModel)
    }

    private fun socialLogin(socialLogInModel: SocialLogInModel) {
        GlobalScope.launch {
            val requestEntity = userUseCase.socialSignIn(socialLogInModel)

            if (requestEntity.isSuccessful) {

                if (requestEntity.successModel != null) {
                    localStorage.saveSessionToken(requestEntity.successModel!!.token)

                    if (requestEntity.successModel!!.userEntity.unansweredQuestions != null &&
                        requestEntity.successModel!!.userEntity.unansweredQuestions!!.isNotEmpty()
                    ) {
                        launch(Dispatchers.Main) {
                            val questionsRequest =
                                questionUseCase.getList(requestEntity.successModel!!.token)

                            if (questionsRequest.isSuccessful) {
                                questionUseCase.insert(questionsRequest.successModel!!)

                                localStorage.saveUIReturnStep(UIReturnStep.FIRST_TIME_LOGIN)
                                router.navigateToFirstTimeLogin(questionsRequest.successModel!!)
                            }
                        }
                    } else {
                        router.goToMain()
                    }
                } else {
                    // TODO: show error message

                }
            }
        }
    }
}