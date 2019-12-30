package com.doneit.ascend.presentation.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.dto.GroupType
import com.doneit.ascend.presentation.main.group_info.GroupInfoActivity
import com.doneit.ascend.presentation.main.group_list.GroupListActivity
import com.doneit.ascend.presentation.main.group_list.common.GroupListArgs
import com.doneit.ascend.presentation.main.master_mind_info.MMInfoActivity

fun AppCompatActivity.openMMInfo(model: MasterMindEntity) {
    val intent = Intent(this, MMInfoActivity::class.java)
    intent.putExtra(MMInfoActivity.MM_ENTITY, model)
    startActivity(intent)
}

fun AppCompatActivity.openGroupInfo(model: GroupEntity) {
    val intent = Intent(this, GroupInfoActivity::class.java)
    intent.putExtra(GroupInfoActivity.GROUP_ENTITY, model)
    startActivity(intent)
}

fun AppCompatActivity.openGroupInfo(id: Long) {
    val intent = Intent(this, GroupInfoActivity::class.java)
    intent.putExtra(GroupInfoActivity.GROUP_ID, id)
    startActivity(intent)
}

fun AppCompatActivity.openGroupList(
    userId: Long? = null,
    groupType: GroupType? = null,
    isMyGroups: Boolean? = null
) {
    val intent = Intent(this, GroupListActivity::class.java)
    val filter = GroupListArgs(userId, groupType, isMyGroups)
    intent.putExtra(GroupListActivity.ARG_GROUP_FILTER, filter)

    startActivity(intent)
}