package com.doneit.ascend.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.dto.GroupType
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.common.BottomNavigationChangeListener
import com.doneit.ascend.presentation.main.create_group.CreateGroupActivity
import com.doneit.ascend.presentation.main.extensions.addWithBackStack
import com.doneit.ascend.presentation.main.extensions.replace
import com.doneit.ascend.presentation.main.extensions.replaceWithBackStack
import com.doneit.ascend.presentation.main.group_info.GroupInfoContract
import com.doneit.ascend.presentation.main.group_info.GroupInfoFragment
import com.doneit.ascend.presentation.main.group_list.GroupListContract
import com.doneit.ascend.presentation.main.group_list.GroupListFragment
import com.doneit.ascend.presentation.main.group_list.common.GroupListArgs
import com.doneit.ascend.presentation.main.home.HomeContract
import com.doneit.ascend.presentation.main.home.HomeFragment
import com.doneit.ascend.presentation.main.master_mind.MasterMindContract
import com.doneit.ascend.presentation.main.master_mind.MasterMindFragment
import com.doneit.ascend.presentation.main.master_mind.list.ListContract
import com.doneit.ascend.presentation.main.master_mind_info.MMInfoContract
import com.doneit.ascend.presentation.main.master_mind_info.MMInfoFragment
import com.doneit.ascend.presentation.main.notification.NotificationContract
import com.doneit.ascend.presentation.main.notification.NotificationFragment
import com.doneit.ascend.presentation.main.search.SearchContract
import com.doneit.ascend.presentation.main.search.SearchFragment
import com.doneit.ascend.presentation.profile.common.ProfileContract
import com.doneit.ascend.presentation.profile.crop.CropActivity
import com.doneit.ascend.presentation.profile.edit_bio.EditBioFragment
import com.doneit.ascend.presentation.profile.mm_following.MMFollowingContract
import com.doneit.ascend.presentation.profile.mm_following.MMFollowingFragment
import com.doneit.ascend.presentation.profile.mm_following.mm_add.MMAddContract
import com.doneit.ascend.presentation.profile.mm_following.mm_add.MMAddFragment
import com.doneit.ascend.presentation.profile.rating.ProfileRatingsContract
import com.doneit.ascend.presentation.profile.rating.ProfileRatingsFragment
import com.doneit.ascend.presentation.profile.regular_user.UserProfileFragment
import com.doneit.ascend.presentation.web_page.WebPageContract
import com.doneit.ascend.presentation.web_page.WebPageFragment
import com.doneit.ascend.presentation.web_page.common.WebPageArgs
import com.vrgsoft.core.presentation.fragment.argumented.ArgumentedFragment
import com.vrgsoft.core.presentation.router.FragmentRouter
import com.yalantis.ucrop.UCrop

class MainRouter(
    private val activity: MainActivity,
    private val appRouter: IMainAppRouter
) : FragmentRouter(activity.supportFragmentManager),
    BottomNavigationChangeListener,
    ProfileContract.Router,
    com.doneit.ascend.presentation.profile.master_mind.MMProfileContract.Router,
    HomeContract.Router,
    GroupInfoContract.Router,
    WebPageContract.Router,
    MMFollowingContract.Router,
    MMAddContract.Router,
    GroupListContract.Router,
    NotificationContract.Router,
    MasterMindContract.Router,
    ListContract.Router,
    MMInfoContract.Router,
    SearchContract.Router,
    ProfileRatingsContract.Router {

    override val containerId = activity.getContainerId()
    private val containerIdFull = activity.getContainerIdFull()


    override fun onBack() {
        activity.supportFragmentManager.popBackStack()
    }

    override fun navigateToLogin() {
        appRouter.goToLogin()
    }

    override fun navigateToTerms() {
        val args = WebPageArgs("Terms & Conditions", "terms_and_conditions")

        val fragment = WebPageFragment()
        (fragment as Fragment).arguments = Bundle().apply {
            putParcelable(ArgumentedFragment.KEY_ARGS, args)
        }

        activity.supportFragmentManager.replaceWithBackStack(R.id.container, fragment)
    }

    override fun navigateToPrivacyPolicy() {
        val args = WebPageArgs("Privacy Policy", "privacy_policy")

        val fragment = WebPageFragment()
        (fragment as Fragment).arguments = Bundle().apply {
            putParcelable(ArgumentedFragment.KEY_ARGS, args)
        }

        activity.supportFragmentManager.replaceWithBackStack(R.id.container, fragment)
    }

    override fun navigateToHome() {
        activity.supportFragmentManager.replace(containerId, HomeFragment())
    }

    override fun navigateToMyContent() {
        // TODO: navigate to my content screens
    }

    override fun navigateToAscensionPlan() {
        // TODO: navigate to ascension plan screen
    }



    override fun navigateToRegularUserProfile() {
        activity.supportFragmentManager.replaceWithBackStack(
            containerId,
            UserProfileFragment()
        )
    }

    override fun navigateToMMProfile() {
        activity.supportFragmentManager.replaceWithBackStack(
            containerId,
            com.doneit.ascend.presentation.profile.master_mind.MMProfileFragment()
        )
    }

    fun navigateToCreateGroup() {
        activity.startActivity(Intent(activity, CreateGroupActivity::class.java))
    }

    override fun navigateToGroupList(userId: Long?, groupType: GroupType?, isMyGroups: Boolean?) {
        val args = GroupListArgs(userId, groupType, isMyGroups)
        activity.supportFragmentManager.replaceWithBackStack(containerIdFull, GroupListFragment.newInstance(args))
    }

    override fun navigateToMMInfo(model: MasterMindEntity) {
        activity.supportFragmentManager.replaceWithBackStack(containerIdFull, MMInfoFragment.newInstance(model))
    }

    override fun navigateToSearch() {
        activity.supportFragmentManager.replaceWithBackStack(containerIdFull, SearchFragment())
    }

    override fun navigateToAllMasterMinds() {
        activity.supportFragmentManager.replaceWithBackStack(containerIdFull, MasterMindFragment())
    }

    override fun navigateToGroupInfo(model: GroupEntity) {
        activity.supportFragmentManager.replaceWithBackStack(containerIdFull, GroupInfoFragment.newInstance(model))
    }

    override fun navigateToGroupInfo(id: Long) {
        activity.supportFragmentManager.replaceWithBackStack(containerIdFull, GroupInfoFragment.newInstance(id))
    }

    override fun navigateToNotifications() {
        activity.supportFragmentManager.replaceWithBackStack(containerIdFull, NotificationFragment())
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
        activity.supportFragmentManager.addWithBackStack(containerIdFull, EditBioFragment())
    }

    override fun navigateToMMFollowed() {
        activity.supportFragmentManager.replaceWithBackStack(containerIdFull, MMFollowingFragment())
    }

    override fun navigateToAddMasterMind() {
        activity.supportFragmentManager.replaceWithBackStack(containerIdFull, MMAddFragment())
    }

    override fun navigateToRatings() {
        activity.supportFragmentManager.replaceWithBackStack(containerId, ProfileRatingsFragment())
    }
}