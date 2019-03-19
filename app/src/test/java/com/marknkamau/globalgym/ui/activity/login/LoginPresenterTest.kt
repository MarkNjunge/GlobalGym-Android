package com.marknkamau.globalgym.ui.activity.login

import com.marknkamau.globalgym.data.auth.AuthService
import com.marknkamau.globalgym.data.repository.UserRepository
import com.marknkamau.globalgym.helpers.Constants
import com.marknkamau.globalgym.helpers.Helpers
import com.marknkamau.globalgym.ui.login.LoginPresenter
import com.marknkamau.globalgym.ui.login.LoginView
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.HttpException

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

@RunWith(MockitoJUnitRunner::class)
class LoginPresenterTest {

    @Mock
    private lateinit var view: LoginView
    @Mock
    private lateinit var authService: AuthService
    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var presenter: LoginPresenter


    @Before
    fun setup() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        presenter = LoginPresenter(view, authService, userRepository)

        Mockito.`when`(authService.getUser()).thenReturn(Constants.authUser)
    }

    @After
    fun teardown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

    @Test
    fun sentPasswordRestEmail_success(){
        Mockito.`when`(authService.setPasswordReset(Constants.user.email)).thenReturn(Completable.complete())

        presenter.sendPasswordReset(Constants.user.email)

        Mockito.verify(view).displayMessage(Mockito.anyString())
    }

    @Test
    fun sentPasswordRestEmail_failure(){
        Mockito.`when`(authService.setPasswordReset(Constants.user.email)).thenReturn(Completable.error(Exception("")))

        presenter.sendPasswordReset(Constants.user.email)

        Mockito.verify(view).displayMessage(Mockito.anyString())
    }

    @Test
    fun login_with_validCredentialsAndRegistered() {
        Mockito.`when`(authService.getUser()).thenReturn(Constants.authUser)
        Mockito.`when`(authService.logIn(Constants.user.email, Constants.userPassword)).thenReturn(Completable.complete())
        Mockito.`when`(userRepository.getUser(Constants.user.userId)).thenReturn(Single.just(Constants.user))
        Mockito.`when`(userRepository.setCurrentUser(Constants.user)).thenReturn(Completable.complete())

        presenter.logIn(Constants.user.email, Constants.userPassword)

        Mockito.verify(view).onLoggedIn()
    }

    @Test
    fun login_with_validCredentialsAndNotRegistered() {
        Mockito.`when`(authService.logIn(Constants.user.email, Constants.userPassword)).thenReturn(Completable.complete())
        val response = Helpers.createHttpErrorResponse(404)
        Mockito.`when`(userRepository.getUser(Constants.user.userId)).thenReturn(Single.error(HttpException(response)))

        presenter.logIn(Constants.user.email, Constants.userPassword)

        Mockito.verify(view).onNotRegistered()
    }

    @Test
    fun should_failLogin_with_invalidCredentials() {
        Mockito.`when`(authService.logIn(Constants.user.email, Constants.userPassword)).thenReturn(Completable.error(Exception("error")))

        presenter.logIn(Constants.user.email, Constants.userPassword)

        Mockito.verify(view).displayMessage(Mockito.anyString())
    }
}