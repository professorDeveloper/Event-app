package com.azamovhudstc.eventapp.viewmodel.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azamovhudstc.eventapp.R
import com.azamovhudstc.eventapp.data.local.model.EventData
import com.azamovhudstc.eventapp.repository.EventRepository
import com.azamovhudstc.eventapp.usecase.impl.EventsUseCaseImpl
import com.azamovhudstc.eventapp.viewmodel.EventScreenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class EventScreenViewModelImpl @Inject constructor(var useCaseImpl: EventsUseCaseImpl) :ViewModel(),EventScreenViewModel{
    override val allActionsLiveData: MutableLiveData<ArrayList<String>> = MutableLiveData()
    override val allEventsLiveData: MutableLiveData<List<EventData>> = MutableLiveData()
    override val showDialogLiveData: MutableLiveData<Unit> = MutableLiveData()
    override val showShareDialogLiveData: MutableLiveData<Int> = MutableLiveData()
    override val navigateRateScreenLiveData: MutableLiveData<Unit> = MutableLiveData()
    override val quitAppLiveData: MutableLiveData<Unit> = MutableLiveData()

    init {
        loadAllEvents()
    }

    override fun onClickButtonMore() {
        showDialogLiveData.value = Unit
    }

    override fun onClickEventChange(eventId: Int, state: Boolean) {
        useCaseImpl
            .changeEventState(eventId, state)
            .onEach { list -> allEventsLiveData.value = list; loadAllActions(list) }
            .launchIn(viewModelScope)
    }

    override fun onClickEnableAllActionsButton() {
        useCaseImpl
            .enableAllEventStates()
            .onEach { list -> allEventsLiveData.value = list; loadAllActions(list) }
            .launchIn(viewModelScope)
    }

    override fun onClickDisableAllActionsButton() {
        useCaseImpl
            .disableAllEventStates()
            .onEach { list -> allEventsLiveData.value = list; loadAllActions(list) }
            .launchIn(viewModelScope)
    }

    override fun onClickShareButton() {
        showShareDialogLiveData.value = R.string.app_name
    }

    override fun onClickRateButton() {
        navigateRateScreenLiveData.value = Unit
    }

    override fun onClickQuitAppButton() {
        quitAppLiveData.value = Unit
    }

    private fun loadAllEvents() {
        useCaseImpl
            .allEvents()
            .onEach { list -> allEventsLiveData.value = list; loadAllActions(list) }
            .launchIn(viewModelScope)
    }
    private fun loadAllActions(list: List<EventData>) {
        val allActions = ArrayList<String>()
        list.forEach { eventData -> allActions.add(eventData.action) }
        allActionsLiveData.value = allActions
    }
}