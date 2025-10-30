package com.example.kotlintest.screens.users

import com.example.kotlintest.core.BaseViewModel
import com.example.kotlintest.util.Logger
import javax.inject.Inject

class TestViewModel @Inject constructor() :
BaseViewModel<TestState, TestEvents, TestAction>(
initialState = TestState()
){
    override fun handleAction(action: TestAction) {
        when(action){
            TestAction.Test -> {
                Logger.i("","ViewModel Action")
                sendEvent(TestEvents.Send_Events)
            }

        }

    }
}

sealed class TestAction {
    data object Test : TestAction()
}

sealed class TestEvents {
    data object Send_Events : TestEvents()
}

data class TestState (val p:Int=0)

