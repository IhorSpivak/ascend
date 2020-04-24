package com.doneit.ascend.presentation.profile.block_list

import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.chats.BlockedUserEntity
import com.doneit.ascend.domain.entity.dto.BlockedUsersDTO
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.use_case.interactor.chats.ChatUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class BlockedUsersViewModel(
    private val router: BlockedUsersContract.Router,
    private val chatUseCase: ChatUseCase,
    private val userUseCase: UserUseCase
) : BaseViewModelImpl(),
    BlockedUsersContract.ViewModel {
    override val blockedUsers = chatUseCase.getBlockedUsers(BlockedUsersDTO(
        perPage = 10,
        sortType = SortType.ASC
    ))
    override fun onBackPressed() {
        router.onBack()
    }

    override fun onUnblockUser(user: BlockedUserEntity) {
        viewModelScope.launch {
            chatUseCase.unblockUser(user.id).let {
                if (it.isSuccessful){
                    chatUseCase.removeBlockedUser(user)
                    userUseCase.getUser()?.let {
                        userUseCase.update(it.apply { blockedUsersCount -= 1 })
                    }
                }
            }
        }
    }
}