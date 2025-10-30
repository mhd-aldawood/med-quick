package com.example.kotlintest.screens.home

import com.example.kotlintest.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class HomeState(val dataHolder: DataHolder)
sealed class HomeAction {
    data class OnCardClick(val index: Int) : HomeAction()
}

sealed class HomeEvents {
    data class SelectedDevice(val deviceCategory: DeviceCategory) : HomeEvents()
}
@HiltViewModel
class HomeViewModel @Inject constructor() :
    BaseViewModel<HomeState, HomeEvents, HomeAction>(
        initialState = HomeState(dataHolder = DataHolder())
    ) {
    override fun handleAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnCardClick -> handleOnCardClicked(action.index)
        }
    }

    private fun handleOnCardClicked(index: Int) {
        sendEvent(HomeEvents.SelectedDevice(
            mutableState.value
                .dataHolder
                .cardList[index]
            .deviceCategory))

    }
}
