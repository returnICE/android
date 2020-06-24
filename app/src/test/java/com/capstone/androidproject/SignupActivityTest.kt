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
import org.robolectric.shadows.ShadowToast
import java.lang.Exception
import kotlin.RuntimeException
import kotlin.math.log

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(RobolectricTestRunner::class)
class SignupActivityTest {

    private lateinit var subject : SignupActivity
    private lateinit var username : EditText
    private lateinit var password : EditText
    private lateinit var passwordConfirtm : EditText
    private lateinit var nextButton : ImageView

    @Before
    fun setUp() {
        subject = Robolectric.setupActivity(SignupActivity::class.java)
        username = subject.findViewById(R.id.textId)
        password = subject.findViewById(R.id.textPassword)
        passwordConfirtm = subject.findViewById(R.id.textPasswordConfirm)
        nextButton = subject.findViewById(R.id.btnNext)

    }


    @Test
    fun activityFoundTest() {
        assertNotNull(subject)
    }

    @Test
    fun PasswordCheckSuccess() {

        username.setText("testid1234")
        password.setText("testpw1234")
        passwordConfirtm.setText("testpw1234")


        nextButton.performClick()
        val nextIntent = shadowOf(subject).nextStartedActivity
        assertEquals(nextIntent.component!!.className, SignupDetailActivity::class.java.name)

    }


    @Test
    fun PasswordCheckFail() {

        username.setText("testid1234")
        password.setText("testpw1234")
        passwordConfirtm.setText("testpw123")

        nextButton.performClick()

        assertEquals(ShadowToast.getTextOfLatestToast().toString(), "비밀번호를 다시 확인해주세요")

    }


}
