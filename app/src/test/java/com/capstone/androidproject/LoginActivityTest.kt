package com.capstone.androidproject

import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.capstone.androidproject.SharedPreferenceConfig.App
import org.hamcrest.core.IsEqual
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.robolectric.pluginapi.Sdk
import java.lang.Exception
import kotlin.RuntimeException
import kotlin.math.log

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(RobolectricTestRunner::class)
class LoginActivityTest {

    private lateinit var subject : LoginActivity
    private lateinit var signupButton : ImageView

    @Before
    fun setUp() {
        subject = Robolectric.setupActivity(LoginActivity::class.java)
        signupButton = subject.findViewById(R.id.btnSignup)

    }

    @Test
    fun activityFoundTest() {
        assertNotNull(subject)
    }

    @Test
    fun signupButtonTest() {

        signupButton.performClick()
        val nextIntent = shadowOf(subject).nextStartedActivity
        assertEquals(nextIntent.component!!.className, SignupActivity::class.java.name)

    }
}
