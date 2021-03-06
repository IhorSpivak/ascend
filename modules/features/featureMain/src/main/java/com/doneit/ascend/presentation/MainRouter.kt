package com.doneit.ascend.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.ascension.goal.GoalEntity
import com.doneit.ascend.domain.entity.ascension.spiritual_action_step.SpiritualActionStepEntity
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.community_feed.Attachment
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.GroupStatus
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.ascension_plan.AscensionPlanContract
import com.doneit.ascend.presentation.main.ascension_plan.AscensionPlanFragment
import com.doneit.ascend.presentation.main.ascension_plan.create_goal.CreateGoalsContract
import com.doneit.ascend.presentation.main.ascension_plan.create_goal.CreateGoalsFragment
import com.doneit.ascend.presentation.main.ascension_plan.create_spiritual.CreateSpiritualContract
import com.doneit.ascend.presentation.main.ascension_plan.create_spiritual.CreateSpiritualFragment
import com.doneit.ascend.presentation.main.ascension_plan.goals.list.GoalsListContract
import com.doneit.ascend.presentation.main.ascension_plan.spiritual_action_steps.SpiritualActionStepsContract
import com.doneit.ascend.presentation.main.ascension_plan.spiritual_action_steps.SpiritualActionStepsFragment
import com.doneit.ascend.presentation.main.ascension_plan.spiritual_action_steps.list.SpiritualActionListContract
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.bio.BioContract
import com.doneit.ascend.presentation.main.chats.MyChatsContract
import com.doneit.ascend.presentation.main.chats.MyChatsFragment
import com.doneit.ascend.presentation.main.chats.chat.ChatContract
import com.doneit.ascend.presentation.main.chats.chat.ChatFragment
import com.doneit.ascend.presentation.main.chats.chat.common.ChatType
import com.doneit.ascend.presentation.main.chats.chat.livestream_user_actions.LivestreamUserActionsFragment
import com.doneit.ascend.presentation.main.chats.chat_members.ChatMembersContract
import com.doneit.ascend.presentation.main.chats.chat_members.ChatMembersFragment
import com.doneit.ascend.presentation.main.chats.new_chat.NewChatContract
import com.doneit.ascend.presentation.main.chats.new_chat.NewChatFragment
import com.doneit.ascend.presentation.main.chats.new_chat.add_members.AddMemberFragment
import com.doneit.ascend.presentation.main.common.BottomNavigationChangeListener
import com.doneit.ascend.presentation.main.create_group.CreateGroupArgs
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostContract
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostFragment
import com.doneit.ascend.presentation.main.create_group.select_group_type.SelectGroupTypeContract
import com.doneit.ascend.presentation.main.create_group.select_group_type.SelectGroupTypeFragment
import com.doneit.ascend.presentation.main.goals.GoalsContract
import com.doneit.ascend.presentation.main.goals.GoalsFragment
import com.doneit.ascend.presentation.main.group_info.GroupInfoContract
import com.doneit.ascend.presentation.main.group_info.GroupInfoFragment
import com.doneit.ascend.presentation.main.group_info.attendees.AttendeesContract
import com.doneit.ascend.presentation.main.group_info.attendees.AttendeesFragment
import com.doneit.ascend.presentation.main.groups.GroupsArg
import com.doneit.ascend.presentation.main.groups.GroupsContract
import com.doneit.ascend.presentation.main.groups.GroupsFragment
import com.doneit.ascend.presentation.main.groups.common.toGroupListArgs
import com.doneit.ascend.presentation.main.groups.daily_group_list.GroupDailyListContract
import com.doneit.ascend.presentation.main.groups.daily_group_list.GroupDailyListFragment
import com.doneit.ascend.presentation.main.groups.group_list.GroupListContract
import com.doneit.ascend.presentation.main.home.HomeContract
import com.doneit.ascend.presentation.main.home.HomeFragment
import com.doneit.ascend.presentation.main.home.community_feed.CommunityFeedContract
import com.doneit.ascend.presentation.main.home.community_feed.CommunityFeedFragment
import com.doneit.ascend.presentation.main.home.community_feed.channels.ChannelsContract
import com.doneit.ascend.presentation.main.home.community_feed.channels.ChannelsFragment
import com.doneit.ascend.presentation.main.home.community_feed.channels.create_channel.CreateChannelContract
import com.doneit.ascend.presentation.main.home.community_feed.channels.create_channel.CreateChannelFragment
import com.doneit.ascend.presentation.main.home.community_feed.channels.create_channel.add_members.AddMembersFragment
import com.doneit.ascend.presentation.main.home.community_feed.comments_view.CommentsViewContract
import com.doneit.ascend.presentation.main.home.community_feed.create_post.CreatePostContract
import com.doneit.ascend.presentation.main.home.community_feed.create_post.CreatePostFragment
import com.doneit.ascend.presentation.main.home.community_feed.post_details.PostDetailsContract
import com.doneit.ascend.presentation.main.home.community_feed.post_details.PostDetailsFragment
import com.doneit.ascend.presentation.main.home.community_feed.preview.PreviewActivity
import com.doneit.ascend.presentation.main.home.community_feed.share_post.SharePostBottomSheetFragment
import com.doneit.ascend.presentation.main.home.community_feed.share_post.SharePostContract
import com.doneit.ascend.presentation.main.home.daily.DailyContract
import com.doneit.ascend.presentation.main.home.webinars.WebinarsContract
import com.doneit.ascend.presentation.main.master_mind.MasterMindContract
import com.doneit.ascend.presentation.main.master_mind.MasterMindFragment
import com.doneit.ascend.presentation.main.master_mind.list.ListContract
import com.doneit.ascend.presentation.main.master_mind_info.MMInfoContract
import com.doneit.ascend.presentation.main.master_mind_info.MMInfoFragment
import com.doneit.ascend.presentation.main.master_mind_info.mm_content.livestreams.MMLiveStreamsContract
import com.doneit.ascend.presentation.main.notification.NotificationContract
import com.doneit.ascend.presentation.main.notification.NotificationFragment
import com.doneit.ascend.presentation.main.search.SearchContract
import com.doneit.ascend.presentation.main.search.SearchFragment
import com.doneit.ascend.presentation.profile.block_list.BlockedUsersContract
import com.doneit.ascend.presentation.profile.block_list.BlockedUsersFragment
import com.doneit.ascend.presentation.profile.change_location.ChangeLocationContract
import com.doneit.ascend.presentation.profile.change_location.ChangeLocationFragment
import com.doneit.ascend.presentation.profile.change_password.ChangePasswordContract
import com.doneit.ascend.presentation.profile.change_password.ChangePasswordFragment
import com.doneit.ascend.presentation.profile.common.ProfileContract
import com.doneit.ascend.presentation.profile.crop.CropActivity
import com.doneit.ascend.presentation.profile.edit_bio.EditBioFragment
import com.doneit.ascend.presentation.profile.edit_email.EditEmailContract
import com.doneit.ascend.presentation.profile.edit_email.EditEmailFragment
import com.doneit.ascend.presentation.profile.edit_phone.EditPhoneContract
import com.doneit.ascend.presentation.profile.edit_phone.EditPhoneFragment
import com.doneit.ascend.presentation.profile.edit_phone.verify_phone.VerifyChangePhoneFragment
import com.doneit.ascend.presentation.profile.master_mind.MMProfileFragment
import com.doneit.ascend.presentation.profile.mm_following.MMFollowingContract
import com.doneit.ascend.presentation.profile.mm_following.MMFollowingFragment
import com.doneit.ascend.presentation.profile.mm_following.mm_add.MMAddContract
import com.doneit.ascend.presentation.profile.mm_following.mm_add.MMAddFragment
import com.doneit.ascend.presentation.profile.notification_settings.NotificationSettingsContract
import com.doneit.ascend.presentation.profile.notification_settings.NotificationSettingsFragment
import com.doneit.ascend.presentation.profile.payments.PaymentsContract
import com.doneit.ascend.presentation.profile.payments.PaymentsFragment
import com.doneit.ascend.presentation.profile.payments.earnings.EarningsContract
import com.doneit.ascend.presentation.profile.payments.my_transactions.MyTransactionsContract
import com.doneit.ascend.presentation.profile.payments.payment_methods.PaymentMethodsContract
import com.doneit.ascend.presentation.profile.payments.payment_methods.add_payment.AddPaymentContract
import com.doneit.ascend.presentation.profile.payments.payment_methods.add_payment.AddPaymentFragment
import com.doneit.ascend.presentation.profile.rating.ProfileRatingsContract
import com.doneit.ascend.presentation.profile.rating.ProfileRatingsFragment
import com.doneit.ascend.presentation.profile.regular_user.UserProfileFragment
import com.doneit.ascend.presentation.profile.regular_user.age.AgeFragment
import com.doneit.ascend.presentation.profile.regular_user.community.CommunityFragment
import com.doneit.ascend.presentation.utils.GroupAction
import com.doneit.ascend.presentation.utils.extensions.*
import com.doneit.ascend.presentation.video_chat.VideoChatActivity
import com.doneit.ascend.presentation.video_chat_webinar.WebinarVideoChatActivity
import com.doneit.ascend.presentation.web_page.WebPageContract
import com.doneit.ascend.presentation.web_page.WebPageFragment
import com.vrgsoft.core.presentation.router.FragmentRouter
import com.yalantis.ucrop.UCrop
import com.doneit.ascend.presentation.main.home.channels.ChannelsContract as ChannelsContactTab

class MainRouter(
    private val activity: MainActivity,
    private val appRouter: IMainAppRouter
) : FragmentRouter(activity.supportFragmentManager),
    BottomNavigationChangeListener,
    MainContract.Router,
    BioContract.Router,
    ProfileContract.Router,
    com.doneit.ascend.presentation.profile.master_mind.MMProfileContract.Router,
    HomeContract.Router,
    AscensionPlanContract.Router,
    CreateSpiritualContract.Router,
    CreateGoalsContract.Router,
    com.doneit.ascend.presentation.main.home.groups_list.GroupsListContract.Router,
    DailyContract.Router,
    CreateGroupHostContract.Router,
    GroupInfoContract.Router,
    WebPageContract.Router,
    MMFollowingContract.Router,
    MMAddContract.Router,
    GroupsContract.Router,
    GroupListContract.Router,
    GroupDailyListContract.Router,
    NotificationContract.Router,
    MasterMindContract.Router,
    ListContract.Router,
    MMInfoContract.Router,
    SearchContract.Router,
    ProfileRatingsContract.Router,
    EditPhoneContract.Router,
    ChangeLocationContract.Router,
    ChangePasswordContract.Router,
    EditEmailContract.Router,
    NotificationSettingsContract.Router,
    PaymentsContract.Router,
    EarningsContract.Router,
    PaymentMethodsContract.Router,
    AddPaymentContract.Router,
    MyTransactionsContract.Router,
    SelectGroupTypeContract.Router,
    SpiritualActionStepsContract.Router,
    SpiritualActionListContract.Router,
    GoalsContract.Router,
    GoalsListContract.Router,
    AttendeesContract.Router,
    WebinarsContract.Router,
    MMLiveStreamsContract.Router,
    com.doneit.ascend.presentation.main.home.groups.GroupsContract.Router,
    MyChatsContract.Router,
    NewChatContract.Router,
    ChatContract.Router,
    ChatMembersContract.Router,
    BlockedUsersContract.Router,
    CommunityFeedContract.Router,
    SharePostContract.Router,
    CreatePostContract.Router,
    ChannelsContract.Router,
    PostDetailsContract.Router,
    CommentsViewContract.Router,
    CreateChannelContract.Router,
    ChannelsContactTab.Router {
    override fun navigateToEditGoal(goal: GoalEntity) {
        //add later
    }

    override fun navigateToEditActionStep(actionStep: SpiritualActionStepEntity) {
        //todo create fragment
    }

    override val containerId = activity.getContainerId()
    private val containerIdFull = activity.getContainerIdFull()
    override fun onBackWithOpenChat(chat: ChatEntity, user: UserEntity, chatType: ChatType) {
        manager.popBackStack()
        replaceFullWithMainUpdate(ChatFragment.getInstance(chat, user, chatType))
    }


    override fun onBack() {
        manager.popBackStack(SharePostBottomSheetFragment::class.java.simpleName, 0)
        manager.popBackStack()
    }

    override fun navigateToPostDetails(user: UserEntity, post: Post) {
        manager.replaceWithBackStack(containerIdFull, PostDetailsFragment.newInstance(user, post))
    }

    override fun goToDetailedUser(id: Long) {
        manager.replaceWithBackStack(
            containerIdFull,
            MMInfoFragment.newInstance(id)
        )
    }

    override fun goToChatMembers(
        chatId: Long,
        chatOwner: Long,
        chatType: com.doneit.ascend.domain.entity.dto.ChatType,
        members: List<MemberEntity>,
        user: UserEntity
    ) {
        manager.replaceWithBackStack(
            containerIdFull,
            ChatMembersFragment.newInstance(chatId, chatOwner, chatType, members, user)
        )
    }

    override fun goToLiveStreamUser(member: MemberEntity) {
        manager.add(
            containerIdFull,
            LivestreamUserActionsFragment.newInstance(member)
        )
    }

    override fun navigateToChat(chat: ChatEntity, user: UserEntity, chatType: ChatType) {
        replaceFullWithMainUpdate(ChatFragment.getInstance(chat, user, chatType))
    }

    override fun navigateToNewChat() {
        replaceFullWithMainUpdate(NewChatFragment())
    }


    override fun navigateToAddChatMember() {
        manager.addWithBackStack(containerIdFull, AddMemberFragment())
    }

    override fun navigateToDetails(group: GroupEntity) {
        replaceFullWithMainUpdate(GroupInfoFragment.newInstance(group.id))
    }

    override fun navigateToDetailsNoBackStack(group: GroupEntity) {
        manager.popBackStackImmediate()
        replaceFullNoBackStack(GroupInfoFragment.newInstance(group.id))
    }

    override fun navigateToLogin() {
        appRouter.goToLogin()
    }

    override fun navigateToMyChats() {
        replaceFullWithMainUpdate(MyChatsFragment())
    }

    override fun navigateToCommunityFeedFragment(user: UserEntity) {
        popOrReplaceWithBackStack { CommunityFeedFragment.newInstance(user) }
    }

    override fun navigateToHome() {
        popOrReplace { HomeFragment() }
    }

    override fun navigateToMyContent() {
        // TODO: navigate to my content screens
    }

    override fun navigateToAscensionPlan() {
        popOrReplaceWithBackStack { AscensionPlanFragment() }
    }


    override fun navigateToRegularUserProfile() {
        popOrReplaceWithBackStack { UserProfileFragment() }
    }

    override fun navigateToMMProfile() {
        popOrReplaceWithBackStack { MMProfileFragment() }
    }

    override fun navigateToCreateGroupMM() {
        replaceFullWithMainUpdate(SelectGroupTypeFragment())
    }

    override fun navigateToGroupList(
        userId: Long?,
        groupType: GroupType?,
        isMyGroups: Boolean?,
        mmName: String?
    ) {
        val args =
            GroupsArg(userId, groupType, isMyGroups)
        manager.replaceWithBackStack(
            containerIdFull,
            GroupsFragment.newInstance(args, mmName)
        )
    }

    override fun navigateToDailyGroupList(
        userId: Long?,
        groupType: GroupType?,
        isMyGroups: Boolean?
    ) {
        val args =
            GroupsArg(userId, groupType, isMyGroups)

        manager.replaceWithBackStack(
            containerIdFull,
            GroupDailyListFragment.newInstance(
                args.toGroupListArgs(
                    SortType.DESC,
                    GroupStatus.UPCOMING
                )
            )
        )
    }

    override fun navigateToMMInfo(id: Long) {
        replaceFullWithMainUpdate(MMInfoFragment.newInstance(id))
    }

    override fun navigateToShare(
        id: Long,
        user: UserEntity,
        shareType: SharePostBottomSheetFragment.ShareType
    ) {
        SharePostBottomSheetFragment.newInstance(
            id,
            user,
            shareType
        ).show(
            manager,
            SharePostBottomSheetFragment::class.java.simpleName
        )
    }

    override fun navigateToViewAttendees(attendees: List<AttendeeEntity>, group: GroupEntity) {
        replaceFullWithMainUpdate(AttendeesFragment(attendees.toMutableList(), group))
    }

    override fun navigateToEditGroup(group: GroupEntity) {
        val type = GroupType.values()
            .getOrNull(group.groupType!!.ordinal)
        val args = CreateGroupArgs(type!!)
        replaceFullWithMainUpdate(CreateGroupHostFragment().apply {
            arguments = Bundle().apply {
                putParcelable(
                    com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment.KEY_ARGS,
                    args
                )
                putParcelable(GroupAction.EDIT.toString(), group)
            }
        })
    }

    override fun navigateToDuplicateGroup(group: GroupEntity) {
        val type = GroupType.values()
            .getOrNull(group.groupType!!.ordinal)
        val args = CreateGroupArgs(type!!)
        replaceFullWithMainUpdate(CreateGroupHostFragment().apply {
            arguments = Bundle().apply {
                putParcelable(
                    com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment.KEY_ARGS,
                    args
                )
                putParcelable(GroupAction.DUPLICATE.toString(), group)
            }
        })
    }

    override fun navigateToSearch() {
        replaceFullWithMainUpdate(SearchFragment())
    }

    override fun navigateToAllMasterMinds() {
        replaceFullWithMainUpdate(MasterMindFragment())
    }

    override fun navigateToGroupInfo(id: Long) {
        replaceFullWithMainUpdate(GroupInfoFragment.newInstance(id))
    }

    override fun navigateToNotifications() {
        replaceFullWithMainUpdate(NotificationFragment())
    }

    override fun navigateToAvatarUCropActivity(
        sourceUri: Uri,
        destinationUri: Uri,
        fragmentToReceiveResult: Fragment//todo remove passing fragment
    ) {
        val bundle = Bundle()
        bundle.putParcelable(CropActivity.ARG_SOURCE, sourceUri)
        bundle.putParcelable(CropActivity.ARG_DESTINATION, destinationUri)

        val cropIntent = Intent(activity, CropActivity::class.java)
        cropIntent.putExtras(bundle)

        fragmentToReceiveResult.startActivityForResult(cropIntent, UCrop.REQUEST_CROP)
    }

    override fun navigateToEditBio() {
        manager.addWithBackStack(containerIdFull, EditBioFragment())
    }

    override fun navigateToMMFollowed() {
        manager.replaceWithBackStack(containerIdFull, MMFollowingFragment())
    }

    override fun navigateToAddMasterMind() {
        manager.replaceWithBackStack(containerIdFull, MMAddFragment())
    }

    override fun navigateToRatings() {
        manager.replaceWithBackStack(containerIdFull, ProfileRatingsFragment())
    }

    override fun navigateToChangePhone() {
        manager.replaceWithoutBackStack(
            containerIdFull,
            EditPhoneFragment()
        )
    }

    override fun navigateToVerifyPhone() {
        manager.addWithBackStack(
            containerIdFull,
            VerifyChangePhoneFragment()
        )
    }

    override fun navigateToChangeLocation(currentLocation: String?) {
        val instance = ChangeLocationFragment.newInstance(currentLocation)
        manager.replaceWithBackStack(containerIdFull, instance)
    }

    override fun navigateToChangePassword() {
        manager.replaceWithBackStack(containerIdFull, ChangePasswordFragment())
    }

    override fun navigateToEditEmail() {
        manager.replaceWithBackStack(containerIdFull, EditEmailFragment())
    }

    override fun navigateToNotificationSettings() {
        manager.replaceWithBackStack(
            containerIdFull,
            NotificationSettingsFragment()
        )
    }

    override fun navigateToPayments(isMasterMind: Boolean) {
        manager.replaceWithBackStack(containerIdFull, PaymentsFragment.newInstance(isMasterMind))
    }

    override fun navigateToTerms() {
        manager.replaceWithBackStack(
            containerIdFull,
            WebPageFragment.newInstance(R.string.terms_and_conditions, "terms_and_conditions")
        )
    }

    override fun navigateToPrivacyPolicy() {
        manager.replaceWithBackStack(
            containerIdFull,
            WebPageFragment.newInstance(R.string.privacy, "privacy_policy")
        )
    }

    override fun navigateToBlockedUsers() {
        manager.replaceWithBackStack(
            containerIdFull,
            BlockedUsersFragment()
        )
    }

    override fun navigateToAddPaymentMethod() {
        manager.replaceWithBackStack(containerIdFull, AddPaymentFragment())
    }

    override fun navigateToSetAge() {
        manager.replaceWithBackStack(containerIdFull, AgeFragment())
    }

    override fun navigateToSetCommunity() {
        manager.replaceWithBackStack(containerIdFull, CommunityFragment())
    }

    override fun navigateToVideoChat(groupId: Long, groupType: GroupType) {
        if (groupType == GroupType.WEBINAR) {
            val intent = Intent(activity, WebinarVideoChatActivity::class.java).apply {
                putExtras(
                    Bundle().apply {
                        putLong(WebinarVideoChatActivity.GROUP_ID_ARG, groupId)
                    }
                )
            }

            activity.startActivityForResult(intent, WebinarVideoChatActivity.RESULT_CODE)
        } else {
            val intent = Intent(activity, VideoChatActivity::class.java).apply {
                putExtras(
                    Bundle().apply {
                        putLong(VideoChatActivity.GROUP_ID_ARG, groupId)
                    }
                )
            }

            activity.startActivityForResult(intent, VideoChatActivity.RESULT_CODE)
        }
    }

    override fun navigateToCreateGroup(type: GroupType) {
        val args = CreateGroupArgs(type)

        val fragment = CreateGroupHostFragment()

        fragment.arguments = Bundle().apply {
            putParcelable(
                com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment.KEY_ARGS,
                args
            )
        }

        replaceFullWithMainUpdate(fragment)
    }

    override fun navigateToSpiritualActionSteps() {
        manager.replaceWithBackStack(
            containerIdFull,
            SpiritualActionStepsFragment()
        )
    }

    override fun navigateToMyGoals() {
        manager.replaceWithBackStack(containerIdFull, GoalsFragment())
    }

    override fun navigateToCreateSpiritualActionSteps() {
        manager.replaceWithBackStack(
            containerIdFull,
            CreateSpiritualFragment()
        )
    }

    override fun navigateToCreateGoal() {
        manager.replaceWithBackStack(containerIdFull, CreateGoalsFragment())
    }

    private fun replaceFullWithMainUpdate(fragment: Fragment) {
        manager.beginTransaction()
            .replace(containerId, Fragment())//in order to force fragment's view recreation
            .replace(containerIdFull, fragment, fragment::class.java.simpleName)
            .addToBackStack(fragment::class.java.simpleName)
            .commit()
    }

    private inline fun <reified T : BaseFragment<*>> popOrReplace(factory: () -> T) {
        with(manager.findFragmentByTag(T::class.java.simpleName)) {
            if (this == null)
                manager.replace(containerId, factory())
            else manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    private inline fun <reified T : BaseFragment<*>> popOrReplaceWithBackStack(factory: () -> T) {
        with(manager.findFragmentByTag(T::class.java.simpleName)) {
            if (this == null)
                manager.replaceWithBackStack(containerId, factory())
            else manager.popBackStack(T::class.java.simpleName, 0)
        }
    }

    private inline fun <reified T : Fragment> replaceFullNoBackStack(fragment: T) {
        manager.popBackStackImmediate()
        manager.beginTransaction()
            .replace(containerId, Fragment())//in order to force fragment's view recreation
            .replace(containerIdFull, fragment, T::class.java.simpleName)
            .addToBackStack(fragment::class.java.simpleName)
            .commit()
    }

    override fun navigateToCreatePost(post: Post?) {
        manager.replaceWithBackStack(containerIdFull, CreatePostFragment.newInstance(post))
    }

    override fun navigateToPreview(attachments: List<Attachment>, selected: Int) {
        activity.startActivity(PreviewActivity.createIntent(activity, attachments, selected))
    }

    override fun navigateToSharedPostChat(chat: ChatEntity, user: UserEntity, chatType: ChatType) {
        replaceFullWithMainUpdate(ChatFragment.getInstance(chat, user, chatType))
    }

    override fun navigateToSharedPostChannel(channelId: Long) {
        //TODO
    }


    override fun navigateToChannel(channel: ChatEntity, userEntity: UserEntity) {
        manager.replaceWithBackStack(
            containerIdFull,
            ChatFragment.getInstance(channel, user = userEntity, chatType = ChatType.CHAT)
        )
    }

    override fun navigateToNewChannel() {
        manager.replaceWithBackStack(containerIdFull, CreateChannelFragment.newInstance())
    }

    override fun navigateToEditChannel(channel: ChatEntity) {
        manager.replaceWithBackStack(containerIdFull, CreateChannelFragment.newInstance(channel))
    }


    override fun navigateToAddChannelMembers() {
        manager.replaceWithBackStack(containerIdFull, AddMembersFragment())
    }

    override fun navigateToChannels() {
        replaceFullWithMainUpdate(ChannelsFragment())
    }

}