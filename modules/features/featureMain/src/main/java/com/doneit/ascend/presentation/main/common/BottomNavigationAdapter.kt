package com.doneit.ascend.presentation.main.common

import com.doneit.ascend.domain.entity.dto.GroupType
import com.doneit.ascend.domain.entity.dto.toStringValue
import com.doneit.ascend.presentation.main.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigationAdapter(
    val listener: BottomNavigationChangeListener,
    private val userRole: String?
) {
    fun attach(navigationView: BottomNavigationView) {
        navigationView.setOnNavigationItemSelectedListener {
            
            when (it.itemId) {
                R.id.home -> {
                    listener.navigateToHome()
                    true
                }
                R.id.my_content -> {
                    listener.navigateToMyContent()
                    true
                }
                R.id.ascension_plan -> {
                    listener.navigateToAscensionPlan()
                    true
                }
                R.id.profile -> {
                    when(userRole) {
                        GroupType.MASTER_MIND.toStringValue() -> listener.navigateToMMProfile()
                        else -> listener.navigateToRegularUserProfile()
                    }

                    true
                }
                else -> false
            }
        }
    }
}