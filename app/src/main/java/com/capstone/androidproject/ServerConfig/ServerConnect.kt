package com.capstone.androidproject.ServerConfig

import android.content.Context
import com.capstone.androidproject.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ServerConnect(context:Context){
/*
    private val commonNetworkInterceptor = object : Interceptor{
        override fun intercept(chain: Interceptor.Chain): Response {
            val gson= GsonBuilder().create()

            val newRequest = chain.request().newBuilder().build()
            val response = chain.proceed(newRequest)

            val rawJson = response.body()?.string() ?:"{}"

            val type = object : TypeToken<UserWrapper<*>>() {}.type
            val res = try{
                val r = gson.fromJson<UserWrapper<*>>(rawJson, type) ?: throw JsonSyntaxException("Parse Fail")
                if(!r.success)
                    UserWrapper<Any>(false,null)
                else
                    r
            } catch(e:JsonSyntaxException){
                UserWrapper<Any>(false,null)
            }catch(t: Throwable){
                UserWrapper<Any>(false,null)
            }

            val userJson = gson.toJson(res.user)
            val JSON = MediaType.parse("application/json; charset=utf-8")
            val userBody = ResponseBody.create(JSON,userJson)

            return response.newBuilder().body(userBody)
                .build()
        }
    }*/
    /*private val client = OkHttpClient.Builder()
        .addNetworkInterceptor(commonNetworkInterceptor)
        .build()*/ // 주석처리한거는 지금 안 쓰는거라 안봐도 됨
    // retrofit의 interceptor
    // https://medium.com/mj-studio/%EC%84%9C%EB%B2%84-%EC%9D%91%EB%8B%B5-cherry-pick-okhttp-interceptor-eaafa476dc4d
    // https://gun0912.tistory.com/50  interceptor 참고하면 좋은 사이트

    val url: String = context.getString(R.string.URL)

    var retrofit = Retrofit.Builder()
            .baseUrl(url)
            //.client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    fun conn(): HttpService {
        return retrofit.create(HttpService::class.java)
    }
    // retrofit 자료
    // https://zzdd1558.tistory.com/241?category=790049
}