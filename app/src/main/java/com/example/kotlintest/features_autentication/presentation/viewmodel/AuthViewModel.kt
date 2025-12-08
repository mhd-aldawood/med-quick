package com.example.kotlintest.features_autentication.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlintest.features_autentication.data.model.CheckOtpRequest
import com.example.kotlintest.features_autentication.data.model.ClinicItemResponse
import com.example.kotlintest.features_autentication.data.model.LoginResponse
import com.example.kotlintest.features_autentication.data.model.UserNameAvailabilityResponse
import com.example.kotlintest.features_autentication.domain.model.CheckOtpReq
import com.example.kotlintest.features_autentication.domain.model.CheckUserNameReq
import com.example.kotlintest.features_autentication.domain.model.LoginReq
import com.example.kotlintest.features_autentication.domain.model.ResendOtpCodeReq
import com.example.kotlintest.features_autentication.domain.model.ResetPasswordReq
import com.example.kotlintest.features_autentication.domain.usecase.CheckOtpUseCase
import com.example.kotlintest.features_autentication.domain.usecase.CheckUserNameUseCase
import com.example.kotlintest.features_autentication.domain.usecase.GetClinicListUseCase
import com.example.kotlintest.features_autentication.domain.usecase.LoginUseCase
import com.example.kotlintest.features_autentication.domain.usecase.ResendOtpCodeUseCase
import com.example.kotlintest.features_autentication.domain.usecase.ResetPasswordUseCase
import com.example.kotlintest.features_autentication.presentation.components.AuthCardScreen
import com.example.kotlintest.features_autentication.presentation.events.AuthScreenEvent
import com.example.kotlintest.features_autentication.presentation.states.AuthScreenState
import com.example.kotlintest.features_autentication.utils.Const
import com.example.kotlintest.features_autentication.utils.data.local.SharedPreferanceRepository
import com.example.kotlintest.features_autentication.utils.data.model.AppAuthState
import com.example.kotlintest.features_autentication.utils.data.model.MainResources
import com.example.kotlintest.features_autentication.utils.data.model.RequestStates
import com.example.kotlintest.features_autentication.utils.data.model.ResendCodeType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val getClinicListUseCase: GetClinicListUseCase,
    private val checkUserNameUseCase: CheckUserNameUseCase,
    private val checkOtpUseCase: CheckOtpUseCase,
    private val resendOtpCodeUseCase: ResendOtpCodeUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val loginUseCase: LoginUseCase,
    private val sharedPreferanceRepository: SharedPreferanceRepository

):ViewModel() {
//    private val _clinicListReq =  MutableLiveData<RequestStates<ArrayList<ClinicItemResponse>>>(
//        RequestStates(data = ArrayList()))
//    val clinicListReq = _clinicListReq

    private val _authScreenState = MutableStateFlow(AuthScreenState())
    val authScreenState: StateFlow<AuthScreenState> = _authScreenState.asStateFlow()

    fun onEvent(event: AuthScreenEvent){
        when(event){
            is AuthScreenEvent.SaveSelectedProviderEvent->{
                sharedPreferanceRepository.saveObjectToSharedPreferences(Const.PROVIDER,event.provider)
                val currentAppState =sharedPreferanceRepository.getObjectFromSharedPreferences<AppAuthState>(Const.App_Auth_State,
                    AppAuthState::class.java)?:AppAuthState.FirstTime
                if (currentAppState == AppAuthState.FirstTime) {
                    _authScreenState.update {
                        it.copy(
                            screen = AuthCardScreen.CheckUserNameScreen
                        )
                    }
                }
                else if (currentAppState == AppAuthState.UserNameChecked)
                {
                    _authScreenState.update {
                        it.copy(
                            screen = AuthCardScreen.SignInScreen
                        )
                    }

                }
            }
            is AuthScreenEvent.ChangeAuthScreenCardEvent->{
                _authScreenState.update { it.copy(screen = event.screenCard) }
            }
            is AuthScreenEvent.OnCheckUserNameChangedEvent->{
                _authScreenState.update { it.copy(checkUserName = event.checkUserName) }
            }
            is AuthScreenEvent.CheckUserNameEvent->{
                checkUserName(_authScreenState.value.checkUserName)
            }
            is AuthScreenEvent.ResetEvent->{
                _authScreenState.update { currentState ->
                    currentState.copy(
                        checkUserNameObj = RequestStates(data = UserNameAvailabilityResponse()),
                        checkUserName = "",
                    )
                }
            }
            is AuthScreenEvent.CheckOtpCodeEvent->{
                checkOtpCode(authScreenState.value.checkUserName,event.otpCode)
            }
            is AuthScreenEvent.ResendOtpCodeEvent->{
                resendOtpCode(authScreenState.value.checkUserName)
            }
            is AuthScreenEvent.OnPasswordChangedEvent->{
                _authScreenState.update { it.copy(password = event.password) }
            }
            is AuthScreenEvent.OnConfirmPasswordChangedEvent->{
                _authScreenState.update { it.copy(confirmPassword = event.confirmPassword) }
            }
            is AuthScreenEvent.OnResetPasswordClickEvent->{
                resetPassword()
            }
            is AuthScreenEvent.OnSignInUserNameChangedEvent->{
                _authScreenState.update { it.copy(signInUserName = event.signInUserName) }
            }
            is AuthScreenEvent.OnSignInPasswordChangedEvent->{
                _authScreenState.update { it.copy(signInPassword = event.signInPassword) }
            }
            is AuthScreenEvent.OnSignInClickEvent->{
                signIn()
            }
            else->{

        }
        }
    }
    init {
        getClinicList()
    }
    fun getClinicList(){
        getClinicListUseCase(Unit).onEach {
            when(it){
                is MainResources.Sucess->{
                    _authScreenState.update { currentState ->
                        currentState.copy(
                            providerList =RequestStates(isSuccess = true, data = it.data?: ArrayList())
                        )
                    }
                }
                is MainResources.isLoading->{
                    _authScreenState.update { currentState ->
                        currentState.copy(
                            providerList = RequestStates(isLoading = true , data = ArrayList())
                        )
                    }
                }
                is MainResources.isError->{
                    _authScreenState.update { currentState ->
                        currentState.copy(
                            providerList =RequestStates(isError = true , error = it.message?:"",data = ArrayList())
                        )
                    }
                }
                else->{

                }
            }
        }.launchIn(viewModelScope)
    }

    fun checkUserName(userName:String){
        checkUserNameUseCase(CheckUserNameReq(userName)).onEach {
            when(it){
                is MainResources.Sucess->{
                    _authScreenState.update { currentState ->

                        val checkUserNameObj = it.data?:UserNameAvailabilityResponse()

                        if (!(checkUserNameObj.isAvailable)) {
                            // If the condition is met, return the NEW state with the updated name
                            if (checkUserNameObj.isAccountActive) {
                                currentState.copy(
                                    checkUserNameObj = RequestStates(isSuccess = true, data = it.data?: UserNameAvailabilityResponse()),
                                    screen = AuthCardScreen.SignInScreen)
                            }
                            else
                            {
                                currentState.copy(
                                    checkUserNameObj = RequestStates(isSuccess = true, data = it.data?: UserNameAvailabilityResponse()),
                                    screen = AuthCardScreen.OtpScreen)
                            }

                        } else {
                            // If the condition is NOT met, return the CURRENT state unchanged
                            // (e.g., perhaps you want to show a Snackbar here in a real app via a one-off event)
                            currentState.copy(
                                checkUserNameObj = RequestStates(isSuccess = true, data = it.data?: UserNameAvailabilityResponse()),
                                screen = AuthCardScreen.CheckUserNameScreen)
                        }
                    }


                }
                is MainResources.isLoading->{
                    _authScreenState.update { currentState ->
                        currentState.copy(
                            checkUserNameObj = RequestStates(isLoading = true, data =  UserNameAvailabilityResponse())
                        )
                    }

                }
                is MainResources.isError->{
                    _authScreenState.update { currentState ->
                        currentState.copy(
                            checkUserNameObj = RequestStates(isError = true, error = it.message?:"",data =  UserNameAvailabilityResponse())
                        )
                    }
                }
                else->{

                }
            }
        }.launchIn(viewModelScope)
    }

    fun checkOtpCode(userName:String,otpCode:String){
        checkOtpUseCase(CheckOtpReq(userName , otpCode)).onEach {
            when(it){
                is MainResources.isLoading-> {
                    _authScreenState.update { currentState ->
                        currentState.copy(
                            checkOtpRequest = RequestStates(isLoading = true, data = CheckOtpRequest())
                        )
                    }
                }
                is MainResources.isError->{
                    _authScreenState.update { currentState ->
                        currentState.copy(
                            checkOtpRequest = RequestStates(isError = true, error = it.message?:"", data = CheckOtpRequest())
                        )
                    }
                }
                is MainResources.Sucess->{
                    _authScreenState.update { currentState ->
                        val otpValid = it.data?.isTokenValid?:false

                        currentState.copy(
                            checkOtpRequest = RequestStates(isSuccess = true,  data = it.data?:CheckOtpRequest()),
                            otpCode = otpCode,
                            screen =  if (otpValid) AuthCardScreen.ResetPasswordScreen else currentState.screen
                        )
                    }
                }
                else->{

                }
            }
        }.launchIn(viewModelScope)

    }

    fun resendOtpCode(userName:String){
        resendOtpCodeUseCase(ResendOtpCodeReq(userName, ResendCodeType.SignupVerification.type)).onEach {
            when(it){
                is MainResources.isLoading-> {
                    _authScreenState.update { currentState ->
                        currentState.copy(
                            resendOtpCodeReq =  RequestStates(isLoading = true, data = Unit)
                        )
                    }
                }
                is MainResources.isError->{
                    _authScreenState.update { currentState ->
                        currentState.copy(
                            resendOtpCodeReq = RequestStates(isError = true, error = it.message?:"",data = Unit)
                        )
                    }
                }
                is MainResources.Sucess->{
                    _authScreenState.update { currentState ->
                        currentState.copy(
                            resendOtpCodeReq = RequestStates(isSuccess = true, data = Unit)
                        )
                    }
                }
                else->{

                }
            }
        }.launchIn(viewModelScope)

    }
    fun resetPassword(){
        resetPasswordUseCase(
            ResetPasswordReq(
                email = _authScreenState.value.checkUserName,
                password = _authScreenState.value.password,
                confirmPassword = _authScreenState.value.confirmPassword,
                token = _authScreenState.value.otpCode
            )
        ).onEach {
            when(it){
                is MainResources.isLoading-> {
                    _authScreenState.update { currentState ->
                        currentState.copy(
                            resetPasswordReq =  RequestStates(isLoading = true, data = Unit)
                        )
                    }
                }
                is MainResources.isError->{
                    _authScreenState.update { currentState ->
                        currentState.copy(
                            resetPasswordReq = RequestStates(isError = true, error = it.message?:"",data = Unit)
                        )
                    }
                }
                is MainResources.Sucess->{
                    sharedPreferanceRepository.saveObjectToSharedPreferences(Const.App_Auth_State,AppAuthState.UserNameChecked)
                    _authScreenState.update { currentState ->
                        currentState.copy(
                            resetPasswordReq = RequestStates(isSuccess = true, data = Unit),
                            screen =  AuthCardScreen.SignInScreen
                        )
                    }

                }
                else->{

                }
            }
        }.launchIn(viewModelScope)

    }

    fun signIn(){
        loginUseCase(
            LoginReq(
                clientSecret = Const.clientSecretTest,
                clientId = Const.clientIdTest,
                userName = _authScreenState.value.signInUserName,
                password = _authScreenState.value.signInPassword,
                platform = 0
            )
        ).onEach {
            when(it){
                is MainResources.isLoading-> {
                    _authScreenState.update { currentState ->
                        currentState.copy(
                            signInRequset =  RequestStates(isLoading = true, data = LoginResponse())
                        )
                    }
                }
                is MainResources.isError->{
                    _authScreenState.update { currentState ->
                        currentState.copy(
                            signInRequset = RequestStates(isError = true, error = it.message?:"",data = LoginResponse())
                        )
                    }
                }
                is MainResources.Sucess->{
                    if(it.data?.userNameAvailability?.isAvailable == false && it.data.userNameAvailability.isAccountActive){
                        sharedPreferanceRepository.saveObjectToSharedPreferences(Const.App_Auth_State,AppAuthState.LogedIn)
                        sharedPreferanceRepository.saveString(Const.TOKEN,it.data.loginResponse.accessToken)
                        sharedPreferanceRepository.saveString(Const.REFRESH_TOKEN,it.data.loginResponse.refreshToken)
                        sharedPreferanceRepository.saveInt(Const.EXPIRE_DATE,it.data.loginResponse.expiresIn)
                        sharedPreferanceRepository.saveObjectToSharedPreferences(Const.USER,it.data.user)
                    }
                    if(it.data?.isBasicInfoFilled==true){
                        sharedPreferanceRepository.saveBoolean(Const.COMPLETE,true)
                    }
                    _authScreenState.update { currentState ->
                        currentState.copy(
                            signInRequset = RequestStates(isSuccess = true,isSubmit = true, data = it.data?:LoginResponse()),
                            screen =  AuthCardScreen.HomeScreen
                        )
                    }
                }
                else->{

                }
            }
        }.launchIn(viewModelScope)
    }
}