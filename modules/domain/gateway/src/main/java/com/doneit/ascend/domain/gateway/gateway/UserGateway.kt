package com.doneit.ascend.domain.gateway.gateway

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.doneit.ascend.domain.entity.*
import com.doneit.ascend.domain.entity.common.RequestEntity
import com.doneit.ascend.domain.gateway.common.mapper.toRequestEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toUserEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toUserLocal
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
    private val context: Context
) : BaseGateway(errors), IUserGateway {

    override fun <T> calculateMessage(error: T): String {
        return ""//todo, not required for now
    }

    override suspend fun signIn(logInModel: LogInUserModel): RequestEntity<AuthEntity, List<String>> {
        return executeRemote { remote.signIn(logInModel.toLoginRequest()) }.toRequestEntity(
            {
                it?.toEntity()
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun signOut(): RequestEntity<Unit, List<String>> {
        val result = executeRemote { remote.signOut() }.toRequestEntity(
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

    override suspend fun socialSignIn(socialLoginModel: SocialLogInModel): RequestEntity<AuthEntity, List<String>> {
        return executeRemote { remote.socialSignIn(socialLoginModel.toSocialLoginRequest()) }.toRequestEntity(
            {

                it?.toEntity()
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun signUp(signUpModel: SignUpModel): RequestEntity<AuthEntity, List<String>> {
        return executeRemote { remote.signUp(signUpModel.toSignUpRequest()) }.toRequestEntity(
            {
                it?.toEntity()
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun deleteAccount(): RequestEntity<Unit, List<String>> {
        val result =  executeRemote { remote.deleteAccount() }.toRequestEntity(
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

    override suspend fun signUpValidation(signUpModel: SignUpModel): RequestEntity<Unit, List<String>> {
        return executeRemote { remote.signUpValidation(signUpModel.toSignUpRequest()) }.toRequestEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun getConfirmationCode(phone: String): RequestEntity<Unit, List<String>> {
        return executeRemote { remote.getConfirmationCode(PhoneRequest(phone)) }.toRequestEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun forgotPassword(phone: String): RequestEntity<Unit, List<String>> {
        return executeRemote { remote.forgotPassword(PhoneRequest(phone)) }.toRequestEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun resetPassword(resetModel: ResetPasswordModel): RequestEntity<Unit, List<String>> {
        return executeRemote { remote.resetPassword(resetModel.toResetPasswordRequest()) }.toRequestEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun insert(user: UserEntity, token: String) {
        local.insert(user.toUserLocal())

        // save token
        val account = Account(user.name, context.packageName)

        removeAccounts()

        accountManager.addAccountExplicitly(account, ARG_AM_PASSWORD, null)
        accountManager.setAuthToken(account, "Bearer", token)
    }

    override fun getUser(): LiveData<UserEntity?> {
        return liveData {
            val userLive = MutableLiveData<UserEntity>()
            emitSource(userLive)

            val user = local.getFirstUser()

            userLive.postValue(user?.toUserEntity())
        }
    }

    private fun removeAccounts() {
        val accounts = accountManager.getAccountsByType(context.packageName)

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