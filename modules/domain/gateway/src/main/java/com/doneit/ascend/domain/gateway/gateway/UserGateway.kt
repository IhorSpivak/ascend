package com.doneit.ascend.domain.gateway.gateway

import android.accounts.Account
import android.accounts.AccountManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.doneit.ascend.domain.entity.AuthEntity
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.LogInUserModel
import com.doneit.ascend.domain.entity.dto.ResetPasswordModel
import com.doneit.ascend.domain.entity.dto.SignUpModel
import com.doneit.ascend.domain.entity.dto.SocialLogInModel
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toUserEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_locale.toUserLocal
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toLoginRequest
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toResetPasswordRequest
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toSignUpRequest
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toSocialLoginRequest
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.use_case.gateway.IUserGateway
import com.doneit.ascend.source.storage.remote.data.request.PhoneRequest
import com.vrgsoft.networkmanager.NetworkManager
import com.doneit.ascend.source.storage.local.repository.user.IUserRepository as LocalRepository
import com.doneit.ascend.source.storage.remote.repository.user.IUserRepository as RemoteRepository

internal class UserGateway(
    errors: NetworkManager,
    private val remote: RemoteRepository,
    private val local: LocalRepository,
    private val accountManager: AccountManager,
    private val packageName: String
) : BaseGateway(errors), IUserGateway {

    override fun <T> calculateMessage(error: T): String {
        return ""//todo, not required for now
    }

    override suspend fun signIn(logInModel: LogInUserModel): ResponseEntity<AuthEntity, List<String>> {
        return executeRemote { remote.signIn(logInModel.toLoginRequest()) }.toResponseEntity(
            {
                it?.toEntity()
            },
            {
                it?.errors
            }
        )
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

        if(result.isSuccessful) {
            local.remove()
            removeAccounts()
        }

        return result
    }

    override suspend fun socialSignIn(socialLoginModel: SocialLogInModel): ResponseEntity<AuthEntity, List<String>> {
        return executeRemote { remote.socialSignIn(socialLoginModel.toSocialLoginRequest()) }.toResponseEntity(
            {

                it?.toEntity()
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun signUp(signUpModel: SignUpModel): ResponseEntity<AuthEntity, List<String>> {
        return executeRemote { remote.signUp(signUpModel.toSignUpRequest()) }.toResponseEntity(
            {
                it?.toEntity()
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun deleteAccount(): ResponseEntity<Unit, List<String>> {
        val result =  executeRemote { remote.deleteAccount() }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )

        if(result.isSuccessful) {
            local.remove()
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

    //todo do it on login responses
    override suspend fun insert(user: UserEntity, token: String) {
        local.insert(user.toUserLocal())

        // save token
        val account = Account(user.name, packageName)

        removeAccounts()

        accountManager.addAccountExplicitly(account, ARG_AM_PASSWORD, null)
        accountManager.setAuthToken(account, "Bearer", token)
    }

    override fun getUserLive(): LiveData<UserEntity?> {
        return liveData {
            val userLive = MutableLiveData<UserEntity>()
            emitSource(userLive)

            val user = local.getFirstUser()

            userLive.postValue(user?.toUserEntity())
        }
    }

    override suspend fun geUser(): UserEntity? {
        return local.getFirstUser()?.toUserEntity()
    }
    
    override suspend fun hasSignedInUser(): Boolean {
        return accountManager.getAccountsByType(packageName).isNotEmpty()
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