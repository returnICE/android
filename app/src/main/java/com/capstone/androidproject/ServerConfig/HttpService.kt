package com.capstone.androidproject.ServerConfig

import com.capstone.androidproject.Response.*
import retrofit2.Call
import retrofit2.http.*

data class ResponseEX(val result:String? = null)

interface HttpService {

    @FormUrlEncoded
    @POST("/login")
    fun postLoginRequest(@Field("ID")ID: String,
                        @Field("PW")PW: String)
            :Call<TokenResponse>

    @FormUrlEncoded
    @POST("/users")
    fun postSignupRequest(@Field("ID")ID: String,
                          @Field("PW")PW: String,
                          @Field("name")name: String,
                          @Field("birth")birth: String,
                          @Field("address")address: String,
                          @Field("phone")phone: String
                          )
            :Call<SignupResponse>

    @FormUrlEncoded
    @POST("/items")
    fun postItemRegisterRequest(@Field("manufactdate")manufactdate: String,
                                @Field("expirationdate")expirationdate: String,
                                @Field("type")type: String,
                                @Field("information")information: String,
                                @Field("originprice")originprice: String,
                                @Field("saleprice")saleprice: String
    )
            :Call<SignupResponse>

    @FormUrlEncoded
    @POST("/upload")
    fun postItemImgRequest(@Field("imgFile")imgFile: String)
            :Call<Success>

    @GET("/users/:id")
    fun getRetrieveRequest():Call<Success>

    @GET("/users/check")
    fun getCheckRequest(@Query("ID")ID: String):Call<Success>

    @GET("/login/logout")
    fun getLogoutRequest():Call<Success>

    @GET("/login")
    fun getGetUserRequest(@Header("x-access-token")token: String): Call<LoginResponse>

    /*
    @GET("/경로")
    fun getRequest(@Query("name")name: String):Call<ResponseEX>

    @GET("/경로/{id}")
    fun getParamRequest(@Path("id")id: String):Call<ResponseEX>

    @FormUrlEncoded
    @POST("/경로")
    fun postRequest(@Field("id")id: String,
                    @Field("pw")pw: String):Call<ResponseEX>

    @FormUrlEncoded
    @PUT("/경로/{id}")
    fun putRequest(@Path("id")id: String,
                   @Field("content")content: String):Call<ResponseEX>

    @DELETE("/경로/{id}")
    fun deleteRequest(@Path("id")id: String):Call<ResponseEX>
    */
    // 주석처리한거 http통신할 때 직접 작성해야되니까 어떻게 생겻는지 볼 것
    // https://www.youtube.com/watch?v=iGW9QHp2uMg <- 여기 참고하면 될 듯
}