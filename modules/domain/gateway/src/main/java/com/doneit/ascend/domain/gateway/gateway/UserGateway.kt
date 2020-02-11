package com.doneit.ascend.domain.gateway.gateway

import android.accounts.Account
import android.accounts.AccountManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AuthEntity
import com.doneit.ascend.domain.entity.RateEntity
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.*
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toUserEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_locale.toUserLocal
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.*
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.gateway.gateway.data_source.RateDataSource
import com.doneit.ascend.domain.use_case.gateway.IUserGateway
import com.doneit.ascend.source.storage.remote.data.request.PhoneRequest
import com.doneit.ascend.source.storage.remote.repository.master_minds.IMasterMindRepository
import com.google.firebase.iid.FirebaseInstanceId
import com.vrgsoft.networkmanager.NetworkManager
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

    override suspend fun signIn(logInModel: LogInUserModel): ResponseEntity<AuthEntity, List<String>> {
        val res =
            executeRemote { remote.signIn(logInModel.toLoginRequest(getToken())) }.toResponseEntity(
                {
                    it?.toEntity()
                },
                {
                    it?.errors
                }
            )

        if (res.isSuccessful) {
            updateUserLocal(res.successModel!!)
        }

        return res
    }

    override suspend fun socialSignIn(socialLoginModel: SocialLogInModel): ResponseEntity<AuthEntity, List<String>> {
        val res =
            executeRemote { remote.socialSignIn(socialLoginModel.toSocialLoginRequest(getToken())) }.toResponseEntity(
                {

                    it?.toEntity()
                },
                {
                    it?.errors
                }
            )

        if (res.isSuccessful) {
            updateUserLocal(res.successModel!!)
        }

        return res
    }

    override suspend fun signUp(signUpModel: SignUpModel): ResponseEntity<AuthEntity, List<String>> {
        val res = executeRemote { remote.signUp(signUpModel.toSignUpRequest()) }.toResponseEntity(
            {
                it?.toEntity()
            },
            {
                it?.errors
            }
        )

        if (res.isSuccessful) {
            updateUserLocal(res.successModel!!)
        }

        return res
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

    override suspend fun signUpValidation(signUpModel: SignUpModel): ResponseEntity<Unit, List<String>> {
        return executeRemote { remote.signUpValidation(signUpModel.toSignUpRequest()) }.toResponseEntity(
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

    override suspend fun resetPassword(resetModel: ResetPasswordModel): ResponseEntity<Unit, List<String>> {
        return executeRemote { remote.resetPassword(resetModel.toResetPasswordRequest()) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun changePassword(model: ChangePasswordModel): ResponseEntity<Unit, List<String>> {
        return executeRemote { remote.changePassword(model.toRequest()) }.toResponseEntity(
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

    override suspend fun report(content: String, id: Long): ResponseEntity<Unit, List<String>> {
        return executeRemote { remote.report(content, id) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun getProfile(): ResponseEntity<UserEntity, List<String>> {
        val res = executeRemote { remote.getProfile() }.toResponseEntity(
            {
                it?.currrentUser?.toEntity()
            },
            {
                it?.errors
            }
        )

        if (res.isSuccessful) {
            updateUserLocal(res.successModel!!)
        }

        return res
    }

    override suspend fun updateProfile(groupModel: UpdateProfileModel): ResponseEntity<UserEntity, List<String>> {
        val file = if (groupModel.imagePath == null) null else File(groupModel.imagePath!!)

        val res =
            executeRemote { remote.updateProfile(file, groupModel.toRequest()) }.toResponseEntity(
                {
                    it?.currrentUser?.toEntity()
                },
                {
                    it?.errors
                }
            )

        if (res.isSuccessful) {
            updateUserLocal(res.successModel!!)
        }

        return res
    }

    override suspend fun getRating(ratingsModel: RatingsModel): PagedList<RateEntity> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(ratingsModel.perPage ?: 10)
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

    override suspend fun changePhone(model: ChangePhoneModel): ResponseEntity<Unit, List<String>> {
        val res = executeRemote { remote.changePhone(model.toRequest()) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )

        if (res.isSuccessful) {
            val user = getUser()
            val newUser = user!!.copy(phone = model.phoneNumber)
            updateUserLocal(newUser)
        }

        return res
    }

    override suspend fun changeEmail(model: ChangeEmailModel): ResponseEntity<Unit, List<String>> {
        val res = executeRemote { remote.changeEmail(model.toRequest()) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )

        if (res.isSuccessful) {
            val user = getUser()
            val newUser = user!!.copy(email = model.email)
            updateUserLocal(newUser)
        }

        return res
    }

    private suspend fun updateUserLocal(authEntity: AuthEntity) {
        updateUserLocal(authEntity.userEntity)

        // save token
        val account = Account(authEntity.userEntity.fullName, packageName)

        removeAccounts()

        accountManager.addAccountExplicitly(account, ARG_AM_PASSWORD, null)
        accountManager.setAuthToken(account, "Bearer", authEntity.token)
    }

    private suspend fun updateUserLocal(userEntity: UserEntity) {
        local.remove()//only single user at local storage allowed
        local.insert(userEntity.toUserLocal())
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

    private suspend fun getToken(): String {
        val result = Channel<String>()
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                val token = task.result?.token
                GlobalScope.launch {
                    result.send(token ?: "")
                }
            }
        return result.receive()
    }

    private fun removeAccounts() {
        val accounts = accountManager.getAccountsByType(packageName)

        if (accounts.isNotEmpty()) {
            for (accountItem in accounts) {
                try {
                    if (android.os.Build.VERSION.SDK_INT >= 22) {
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