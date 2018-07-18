package com.marknkamau.globalgym.ui.activity.login

import com.marknkamau.globalgym.data.auth.AuthService
import com.marknkamau.globalgym.data.models.AuthUser
import com.marknkamau.globalgym.data.models.User
import com.marknkamau.globalgym.data.repository.UserRepository
import com.marknkamau.globalgym.helpers.Helpers
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
    private val authUser = AuthUser("", "test@mail.com")
    private val user = User("", "", "", "test@mail.com", "", "", 0, "", "", 0, 0, null)

    @Before
    fun setup() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        presenter = LoginPresenter(view, authService, userRepository)

        Mockito.`when`(authService.getUser()).thenReturn(authUser)
    }

    @After
    fun teardown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

    @Test
    fun sentPasswordRestEmail_success(){
        Mockito.`when`(authService.setPasswordReset("")).thenReturn(Completable.complete())

        presenter.sendPasswordReset("")

        Mockito.verify(view).displayMessage(Mockito.anyString())
    }

    @Test
    fun sentPasswordRestEmail_failure(){
        Mockito.`when`(authService.setPasswordReset("")).thenReturn(Completable.error(Exception("")))

        presenter.sendPasswordReset("")

        Mockito.verify(view).displayMessage(Mockito.anyString())
    }

    @Test
    fun login_with_validCredentialsAndRegistered() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        Mockito.`when`(authService.logIn("", "")).thenReturn(Completable.complete())
        Mockito.`when`(userRepository.getUser("")).thenReturn(Single.just(user))

        presenter.logIn("", "")

        Mockito.verify(view).onLoggedIn()
    }

    @Test
    fun login_with_validCredentialsAndNotRegistered() {
        Mockito.`when`(authService.logIn("", "")).thenReturn(Completable.complete())
        val response = Helpers.createHttpErrorResponse(404)
        Mockito.`when`(userRepository.getUser("")).thenReturn(Single.error(HttpException(response)))

        presenter.logIn("", "")

        Mockito.verify(view).onNotRegistered()
    }

    @Test
    fun should_failLogin_with_invalidCredentials() {
        Mockito.`when`(authService.logIn("", "")).thenReturn(Completable.error(Exception("error")))

        presenter.logIn("", "")

        Mockito.verify(view).displayMessage(Mockito.anyString())
    }
}