package com.marknkamau.globalgym.ui.activity.main

import com.marknkamau.globalgym.data.auth.AuthService
import com.marknkamau.globalgym.data.models.AuthUser
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

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

@RunWith(MockitoJUnitRunner::class)
class MainPresenterTest {

    @Mock
    private lateinit var view: MainView
    @Mock
    private lateinit var authService: AuthService

    private lateinit var presenter: MainPresenter

    @Before
    fun setup() {
        presenter = MainPresenter(view, authService)
    }

    @Test
    fun checkIfSignedIn_when_signedIn(){
        Mockito.`when`(authService.isSignedIn()).thenReturn(true)

        presenter.checkIfSignedIn()

        Mockito.verify(view).onSignedIn()
    }

    @Test
    fun checkIfSignedIn_when_notSignedIn(){
        Mockito.`when`(authService.isSignedIn()).thenReturn(false)

        presenter.checkIfSignedIn()

        Mockito.verify(view).onNotSignedIn()
    }

}