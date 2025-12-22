package com.example.kotlintest.screens.home

import com.example.kotlintest.core.BaseViewModel
import com.example.kotlintest.screens.home.models.DataHolder
import com.example.kotlintest.screens.home.models.DeviceCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class ExaminationState(val dataHolder: DataHolder)
sealed class ExaminationAction {
    data class OnCardClick(val index: Int) : ExaminationAction()
}

sealed class ExaminationEvents {
    data class SelectedDevice(val deviceCategory: DeviceCategory) : ExaminationEvents()
}
@HiltViewModel
class ExaminationViewModel @Inject constructor() :
    BaseViewModel<ExaminationState, ExaminationEvents, ExaminationAction>(
        initialState = ExaminationState(dataHolder = DataHolder())
    ) {
    override fun handleAction(action: ExaminationAction) {
        when (action) {
            is ExaminationAction.OnCardClick -> handleOnCardClicked(action.index)
        }
    }

    private fun handleOnCardClicked(index: Int) {
        sendEvent(ExaminationEvents.SelectedDevice(
            mutableState.value
                .dataHolder
                .cardList[index]
            .deviceCategory))

    }
}
