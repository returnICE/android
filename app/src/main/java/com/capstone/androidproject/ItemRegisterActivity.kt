package com.capstone.androidproject

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.recyclerview.widget.*
import com.capstone.androidproject.RegisterItem.photoAdapter
import com.capstone.androidproject.RegisterItem.photoList
import com.capstone.androidproject.Response.Success
import com.capstone.androidproject.ServerConfig.ServerConnect
import com.capstone.androidproject.SharedPreferenceConfig.App
import kotlinx.android.synthetic.main.activity_item_register_photo.*
import kotlinx.android.synthetic.main.photo_list.*
import kotlinx.android.synthetic.main.activity_signup.btnNext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.lang.Exception


class ItemRegisterActivity : AppCompatActivity() {

    val OPEN_GALLERY = 1
    //val photoArray = arrayListOf<photoList>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_register_photo)

        var photoArray = arrayListOf<photoList>()


        btnSelectPhoto.setOnClickListener {
            openGallery()
/*
            val pAdapter = photoAdapter(this, photoArray)
            recyclerViewPhoto.adapter = pAdapter

            val lm = LinearLayoutManager(this)
            recyclerViewPhoto.layoutManager = lm
            recyclerViewPhoto.setHasFixedSize(true)

 */
        }






        btnNext.setOnClickListener{

            //var photo = 이미지 보내기 변수 추가
            val nextIntent = Intent(this@ItemRegisterActivity, SignupDetailActivity::class.java)

            //다음 activity로 img전송필요

            startActivity(nextIntent)
        }


    }
    fun openGallery(){
        val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent, OPEN_GALLERY)
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent? ) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == OPEN_GALLERY) {
                var currentImageURL: Uri? = data?.data

                try{
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, currentImageURL)
                    imgPhoto.setImageBitmap(bitmap)
                }
                catch(e:Exception){
                    e.printStackTrace()
                }
            }
        }
        //필요시 else로 로그보는 코드 추가
    }

    //이미지 업로드 함수 준비
    fun uploadImg(img : String){
        val serverConnect = ServerConnect(this)
        val server = serverConnect.conn()

        server.postItemImgRequest(img).enqueue(object:

            Callback<Success> {
            override fun onFailure(call: Call<Success>, t: Throwable) {
                Toast.makeText(this@ItemRegisterActivity, "회원가입 실패1", Toast.LENGTH_SHORT).show()
                println(t?.message.toString())
            }

            override fun onResponse(
                call: Call<Success>,
                response: Response<Success>
            ) {
                val success = response?.body()?.success

                if (success == false) {
                    Toast.makeText(this@ItemRegisterActivity, "사진전송 실패", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ItemRegisterActivity, "사진전송 성공", Toast.LENGTH_SHORT).show()

                }
            }
        })
    }
}
