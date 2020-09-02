package com.doneit.ascend.domain.gateway.gateway

import android.accounts.Account
import android.accounts.AccountManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.RateEntity
import com.doneit.ascend.domain.entity.common.BaseCallback
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.*
import com.doneit.ascend.domain.entity.user.AuthEntity
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.gateway.common.mapper.Constants
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toUserEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_locale.merge
import com.doneit.ascend.domain.gateway.common.mapper.to_locale.toUserLocal
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.*
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.gateway.gateway.data_source.RateDataSource
import com.doneit.ascend.domain.use_case.gateway.IUserGateway
import com.doneit.ascend.source.storage.local.data.UserLocal
import com.doneit.ascend.source.storage.remote.data.request.PhoneRequest
import com.doneit.ascend.source.storage.remote.repository.master_minds.IMasterMindRepository
import com.google.firebase.iid.FirebaseInstanceId
import com.vrgsoft.networkmanager.NetworkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.io.File
import com.doneit.ascend.source.storage.local.repository.user.IUserRepository as LocalRepository
import com.doneit.ascend.source.storage.remote.repository.user.IUserRepository as RemoteRepository

internal class UserGateway(
    errors: NetworkManager,
    private val remote: RemoteRepository,
    private val mmRemote: IMasterMindRepository,
    private val local: LocalRepository,
    private val accountManager: AccountManager,
    private val packageName: String
) : BaseGateway(errors), IUserGateway {

    override fun <T> calculateMessage(error: T): String {
        return ""//todo, not required for now
    }

    override suspend fun signIn(logInUserDTO: LogInUserDTO): ResponseEntity<AuthEntity, List<String>> {
        val res =
            executeRemote { remote.signIn(logInUserDTO.toLoginRequest(getToken())) }.toResponseEntity(
                {
                    it?.toEntity()
                },
                {
                    it?.errors
                }
            )

        handleAuthResponse(res)

        return res
    }

    override suspend fun socialSignIn(socialLoginDTO: SocialLogInDTO): ResponseEntity<AuthEntity, List<String>> {
        val res =
            executeRemote { remote.socialSignIn(socialLoginDTO.toSocialLoginRequest(getToken())) }.toResponseEntity(
                {

                    it?.toEntity()
                },
                {
                    it?.errors
                }
            )

        handleAuthResponse(res)

        return res
    }

    override suspend fun signUp(signUpDTO: SignUpDTO): ResponseEntity<AuthEntity, List<String>> {
        val res = executeRemote { remote.signUp(signUpDTO.toSignUpRequest()) }.toResponseEntity(
            {
                it?.toEntity()
            },
            {
                it?.errors
            }
        )

        if (res.isSuccessful) {
            handleAuthResponse(res)
            updateFirebase(getToken())
        }

        return res
    }

    private suspend fun handleAuthResponse(authResponse: ResponseEntity<AuthEntity, List<String>>) {
        if (authResponse.isSuccessful) {
            val authEntity = authResponse.successModel!!
            updateCredentials(authEntity)
            updateUserLocal(authEntity.userEntity.toUserLocal())
        }
    }

    override suspend fun signOut(): ResponseEntity<Unit, List<String>> {
        val result = executeRemote { remote.signOut() }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )

        if (result.isSuccessful) {
            removeAccounts()
        }

        return result
    }

    override suspend fun deactivateAccount(): ResponseEntity<Unit, List<String>> {
        val result = executeRemote { remote.deactivateAccount() }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )

        if (result.isSuccessful) {
            removeAccounts()
        }

        return result
    }

    override suspend fun deleteAccount(): ResponseEntity<Unit, List<String>> {
        val result = executeRemote { remote.deleteAccount() }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )

        if (result.isSuccessful) {
            removeAccounts()
        }

        return result
    }

    override suspend fun signUpValidation(signUpDTO: SignUpDTO): ResponseEntity<Unit, List<String>> {
        return executeRemote { remote.signUpValidation(signUpDTO.toSignUpRequest()) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun getConfirmationCode(phone: String): ResponseEntity<Unit, List<String>> {
        return executeRemote { remote.getConfirmationCode(PhoneRequest(phone)) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun forgotPassword(phone: String): ResponseEntity<Unit, List<String>> {
        return executeRemote { remote.forgotPassword(PhoneRequest(phone)) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun resetPassword(resetDTO: ResetPasswordDTO): ResponseEntity<Unit, List<String>> {
        return executeRemote { remote.resetPassword(resetDTO.toResetPasswordRequest()) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun changePassword(dto: ChangePasswordDTO): ResponseEntity<Unit, List<String>> {
        return executeRemote { remote.changePassword(dto.toRequest()) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun update(user: UserEntity) {
        local.update(user.toUserLocal())
    }

    override fun getUserLive(): LiveData<UserEntity?> {
        return local.getFirstUserLive().map { it?.toUserEntity() }
    }

    override suspend fun getUser(): UserEntity? {
        return local.getFirstUser()?.toUserEntity()
    }

    override suspend fun hasSignedInUser(): Boolean {
        return accountManager.getAccountsByType(packageName).isNotEmpty()
                && (local.getFirstUser() != null)
    }

    override suspend fun report(content: String, id: String): ResponseEntity<Unit, List<String>> {
        return executeRemote { remote.report(content, id) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun updateCurrentUserData() {
        val res = executeRemote { remote.getProfile() }

        if (res.isSuccessful) {
            local.getFirstUser()?.let {
                updateUserLocal(it.merge(res.successModel!!.currrentUser))
            }
        }
    }

    override suspend fun updateProfile(groupDTO: UpdateProfileDTO): ResponseEntity<UserEntity, List<String>> {
        val file = if (groupDTO.imagePath == null) null else File(groupDTO.imagePath!!)

        val response = executeRemote { remote.updateProfile(file, groupDTO.toRequest()) }

        if (response.isSuccessful) {
            local.getFirstUser()?.let {
                updateUserLocal(it.merge(response.successModel!!.currrentUser))
            }
        }

        return response.toResponseEntity(
            {
                it?.currrentUser?.toEntity()
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun getRating(ratingsModel: RatingsDTO): PagedList<RateEntity> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(ratingsModel.perPage ?: Constants.PER_PAGE)
            .build()

        val dataSource = RateDataSource(
            GlobalScope,
            remote,
            ratingsModel
        )
        val executor = MainThreadExecutor()

        return PagedList.Builder<Int, RateEntity>(dataSource, config)
            .setFetchExecutor(executor)
            .setNotifyExecutor(executor)
            .build()
    }

    override suspend fun changePhone(dto: ChangePhoneDTO): ResponseEntity<Unit, List<String>> {
        val res = executeRemote { remote.changePhone(dto.toRequest()) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )

        if (res.isSuccessful) {
            local.getFirstUser()?.let {
                val newUser = it.copy(phone = dto.phoneNumber)
                updateUserLocal(newUser)
            }
        }

        return res
    }

    override suspend fun changeEmail(dto: ChangeEmailDTO): ResponseEntity<Unit, List<String>> {
        val res = executeRemote { remote.changeEmail(dto.toRequest()) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )

        if (res.isSuccessful) {
            local.getFirstUser()?.let {
                val newUser = it!!.copy(email = dto.email)
                updateUserLocal(newUser)
            }
        }

        return res
    }

    private suspend fun updateCredentials(authEntity: AuthEntity) {
        // save token
        val account = Account(authEntity.userEntity.fullName, packageName)

        removeAccounts()

        accountManager.addAccountExplicitly(account, ARG_AM_PASSWORD, null)
        accountManager.setAuthToken(account, "Bearer", authEntity.token)
    }

    private suspend fun updateUserLocal(userLocal: UserLocal) {
        local.remove()//only single user at local storage allowed
        local.insert(userLocal)
    }

    override suspend fun updateFirebase(firebaseId: String): ResponseEntity<Unit, List<String>> {
        return executeRemote { remote.updateFirebase(firebaseId) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }

    override fun shareUser(
        scope: CoroutineScope,
        userId: Long,
        shareDTO: ShareDTO,
        baseCallback: BaseCallback<Unit>
    ) {
        scope.launch(Dispatchers.IO) {
            val response = remote.shareUser(userId, shareDTO.toRequest())
            if (response.isSuccessful) {
                baseCallback.onSuccess(Unit)
            } else {
                baseCallback.onError(response.message)
            }
        }
    }

    private suspend fun getToken(): String {
        val result = Channel<String>()
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                val token = task.result?.token
                GlobalScope.launch {
                    result.send(token.orEmpty())
                }
            }
        return result.receive()
    }

    override fun removeAccounts() {
        val accounts = accountManager.getAccountsByType(packageName)

        if (accounts.isNotEmpty()) {
            for (accountItem in accounts) {
                try {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                        accountManager.removeAccountExplicitly(accountItem)
                    } else {
                        accountManager.removeAccount(accountItem, null, null)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    companion object {
        private const val ARG_AM_PASSWORD = "7ds9f87jF*J(SDFM(7"
    }
}