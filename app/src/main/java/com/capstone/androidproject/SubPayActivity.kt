package com.capstone.androidproject

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.capstone.androidproject.SharedPreferenceConfig.App
import android.content.Intent
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri


class SubPayActivity : AppCompatActivity() {

    val APP_SCHEME="payment://"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_pay)

        val mWebView: WebView = findViewById(R.id.webView)

        val subId = intent.getIntExtra("subId",0)

        mWebView.addJavascriptInterface(PayCheckFactory(this),"AndroidBridge")
        //https://zladnrms.tistory.com/60
        mWebView.webViewClient = WebViewClient(this)// 클릭시 새창 안뜨게

        val mWebSettings = mWebView.getSettings() //세부 세팅 등록

        mWebSettings.allowFileAccessFromFileURLs = true
        mWebSettings.allowUniversalAccessFromFileURLs = true
        mWebSettings.domStorageEnabled = true
        mWebSettings.javaScriptEnabled = true// 웹페이지 자바스크립트 허용 여부
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW)
            val cookieManager = CookieManager.getInstance()
            cookieManager.setAcceptCookie(true)
            cookieManager.setAcceptThirdPartyCookies(mWebView, true)
        }

        val intent = intent
        val intentData = intent.data

        if (intentData == null) {
            val url: String = this.getString(R.string.URL)
            mWebView.loadUrl(url+"pay/customer?subId="+subId + "&customerId="+ App.prefs.id)
        } else {
            //isp 인증 후 복귀했을 때 결제 후속조치
            val url = intentData.toString()
            if (url.startsWith(APP_SCHEME)) {
                val redirectURL = url.substring(APP_SCHEME.length + 3)
                val i = Intent(Intent.ACTION_VIEW, Uri.parse(redirectURL))
                startActivity(i)
            }
        }
    }
}
