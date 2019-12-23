package com.doneit.ascend.domain.gateway.gateway

import android.accounts.Account
import android.accounts.AccountManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.doneit.ascend.domain.entity.AuthEntity
import com.doneit.ascend.domain.entity.ProfileEntity
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.*
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
    private val mmRemote: com.doneit.ascend.source.storage.remote.repository.master_minds.IMasterMindRepository
    private val local: LocalRepository,
    private val accountManager: AccountManager,
    private val packageName: String
) : BaseGateway(errors), IUserGateway {

    override fun <T> calculateMessage(error: T): String {
        return ""//todo, not required for now
    }

    override suspend fun signIn(logInModel: LogInUserModel): ResponseEntity<AuthEntity, List<String>> {
         val res = executeRemote { remote.signIn(logInModel.toLoginRequest()) }.toResponseEntity(
            {
                it?.toEntity()
            },
            {
                it?.errors
            }
        )

        if(res.isSuccessful) {
            updateUserLocal(res.successModel!!)
        }

        return res
    }

    override suspend fun socialSignIn(socialLoginModel: SocialLogInModel): ResponseEntity<AuthEntity, List<String>> {
        val res = executeRemote { remote.socialSignIn(socialLoginModel.toSocialLoginRequest()) }.toResponseEntity(
            {

                it?.toEntity()
            },
            {
                it?.errors
            }
        )

        if(res.isSuccessful) {
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

        if(res.isSuccessful) {
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

        if(result.isSuccessful) {
            removeAccounts()
        }

        return result
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

    override suspend fun update(user: UserEntity) {
        local.update(user.toUserLocal())
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
                && (local.getFirstUser() != null)
    }

    override suspend fun report(content: String, id: Long): ResponseEntity<Unit, List<String>> {
        return executeRemote { remote.report(content,id)}.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }

    private suspend fun updateUserLocal(authEntity: AuthEntity) {
        local.remove()//only single user at local storage allowed

        local.insert(authEntity.userEntity.toUserLocal())

        // save token
        val account = Account(authEntity.userEntity.name, packageName)

        removeAccounts()

        accountManager.addAccountExplicitly(account, ARG_AM_PASSWORD, null)
        accountManager.setAuthToken(account, "Bearer", authEntity.token)
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

    override suspend fun report(content: String, id: Long): ResponseEntity<Unit, List<String>> {
        return executeRemote { remote.report(content,id)}.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun getProfile(): ProfileEntity {

        val user = local.getFirstUser()?.toUserEntity()

        return if(user?.role == GroupType.MASTER_MIND.toStringValue()) {
            executeRemote { mmRemote.getMMProfile(user.id)}.toResponseEntity(
                {
                    it?.toProfileEntity()
                },
                {
                    it?.errors
                }
            )
        }
        else {
            // get profile request
            executeRemote { remote.getProfile(user.id)}.toResponseEntity(
                {
                    it?.toProfileEntity()
                },
                {
                    it?.errors
                }
            )
        }
    }

    companion object {
        private const val ARG_AM_PASSWORD = "7ds9f87jF*J(SDFM(7"
    }
}