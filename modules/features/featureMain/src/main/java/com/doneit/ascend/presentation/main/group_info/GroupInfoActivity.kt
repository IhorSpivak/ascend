package com.doneit.ascend.presentation.main.group_info

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.dialog.DeleteDialog
import com.doneit.ascend.presentation.dialog.QuestionButtonType
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseActivity
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.databinding.ActivityGroupInfoBindingImpl
import com.doneit.ascend.presentation.utils.CalendarPickerUtil
import com.doneit.ascend.presentation.utils.toDayMonthYear
import kotlinx.android.synthetic.main.activity_group_info.*
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import java.text.SimpleDateFormat
import java.util.*


class GroupInfoActivity : BaseActivity() {
    override fun diModule() = Kodein.Module("GroupInfoActivity") {
        bind<GroupInfoRouter>() with singleton {
            GroupInfoRouter(
                this@GroupInfoActivity
            )
        }

        bind<GroupInfoContract.Router>() with singleton { instance<GroupInfoRouter>() }

        bind<ViewModelProvider.Factory>() with singleton { CommonViewModelFactory(kodein.direct) }
        bind<ViewModel>(tag = GroupInfoViewModel::class.java.simpleName) with provider {
            GroupInfoViewModel(
                instance(),
                instance(),
                instance()
            )
        }
        bind<GroupInfoContract.ViewModel>() with provider { vm<GroupInfoViewModel>(instance()) }
    }

    fun getContainerId() = R.id.container

    private val viewModel: GroupInfoContract.ViewModel by instance()
    private lateinit var binding: ActivityGroupInfoBindingImpl
    private var currentDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_info)
        binding.lifecycleOwner = this
        binding.model = viewModel

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        viewModel.group.observe(this, Observer {group ->
            binding.tvStartDate.text = group.startTime?.toDayMonthYear()

            val builder = StringBuilder()

            if(group.daysOfWeek != null) {//todo refactor
                val list = group.daysOfWeek!!.toMutableList()
                val calendarUtil = CalendarPickerUtil(applicationContext)//todo move to di

                list.sortBy { it.ordinal }
                for ((index, value) in list.iterator().withIndex()) {
                    if (index != list.size - 1) {
                        builder.append("${calendarUtil.getString(value)}, ")
                    } else {
                        builder.append("${calendarUtil.getString(value)} ")
                    }
                }

                val formatter = SimpleDateFormat("hh:mm aa", Locale.getDefault())
                builder.append(formatter.format(group.startTime))
                binding.tvSchedule.text = builder.toString()
            }
        })

        val groupId = intent.getLongExtra(GROUP_ID, -1)
        viewModel.loadData(groupId)

        btnDelete.setOnClickListener {
            currentDialog = DeleteDialog.create(
                this,
                "Delete this group?",
                R.string.delete_content,
                R.string.btn_delete,
                R.string.btn_negative
            ) {
                when(it) {
                    QuestionButtonType.NEGATIVE -> currentDialog?.dismiss()
                    QuestionButtonType.POSITIVE -> viewModel.deleteGroup()
                }
            }
        }
    }

    companion object {
        const val GROUP_ID = "GROUP_ID"
    }
}