package com.azamovhudstc.eventapp.viewmodel

import androidx.lifecycle.LiveData
import com.azamovhudstc.eventapp.data.local.model.EventData

interface EventScreenViewModel {
    val allActionsLiveData: LiveData<ArrayList<String>>
    val allEventsLiveData: LiveData<List<EventData>>
    val showDialogLiveData: LiveData<Unit>
    val showShareDialogLiveData: LiveData<Int>
    val navigateRateScreenLiveData: LiveData<Unit>
    val quitAppLiveData: LiveData<Unit>
    fun onClickButtonMore()
    fun onClickEventChange(eventId: Int, state: Boolean)
    fun onClickEnableAllActionsButton()
    fun onClickDisableAllActionsButton()
    fun onClickShareButton()
    fun onClickRateButton()
    fun onClickQuitAppButton()
}