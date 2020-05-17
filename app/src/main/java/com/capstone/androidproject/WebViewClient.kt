package com.capstone.androidproject

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import java.net.URISyntaxException

class WebViewClient(act : Activity) : WebViewClient(){

    var activity: Activity = act

    //페이지 이동
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("javascript:")) {
            var intent: Intent? = null

            try {
                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME) //IntentURI처리
                val uri = Uri.parse(intent!!.dataString)

                activity.startActivity(Intent(Intent.ACTION_VIEW, uri)) //해당되는 Activity 실행
                return true
            } catch (ex: URISyntaxException) {
                return false
            } catch (e: ActivityNotFoundException) {
                if (intent == null) return false

                if (handleNotFoundPaymentScheme(intent.scheme)) return true //설치되지 않은 앱에 대해 사전 처리(Google Play이동 등 필요한 처리)

                val packageName = intent.getPackage()
                if (packageName != null) { //packageName이 있는 경우에는 Google Play에서 검색을 기본
                    activity.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=$packageName")
                        )
                    )
                    return true
                }
                return false
            }
        }
        return false
    }
    protected fun handleNotFoundPaymentScheme(scheme: String?): Boolean {
        //PG사에서 호출하는 url에 package정보가 없어 ActivityNotFoundException이 난 후 market 실행이 안되는 경우
        if (PaymentScheme.ISP.equals(scheme)) {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_ISP)
                )
            )
            return true
        } else if (PaymentScheme.BANKPAY.equals(scheme)) {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_BANKPAY)
                )
            )
            return true
        }

        return false
    }
}