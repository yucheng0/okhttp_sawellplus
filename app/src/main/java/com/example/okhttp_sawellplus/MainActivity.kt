package com.example.okhttp_sawellplus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.okhttp.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    var headerfiltercookie = ""
    var client = OkHttpClient()                            //要一個實例
    var JSON = MediaType.parse("application/json; charset=utf-8")
    var body = RequestBody.create(JSON, "{\"account\":\"w06\" ,\"pw\":\"w\" , \"timezone\":8 }")
    lateinit  var request:Request
    lateinit var response: Response
    lateinit var bodyData: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        按鍵
        login.setOnClickListener {
            post_login()
        }
        devicelist.setOnClickListener {
            post_3rd_devivce_list()
        }
    }


    fun post_login() {

        val url = "http://202.88.100.249/SAWELLPlus_Club/php/3rd_login.php"

         client = OkHttpClient()                            //要一個實例
         JSON = MediaType.parse("application/json; charset=utf-8")
         body = RequestBody.create(JSON, "{\"account\":\"w06\" ,\"pw\":\"w\" , \"timezone\":8 }")

         request = Request.Builder()                    //建立需求
            // 這個有問題 （這是加檔頭傳送）
            .url(url)
            .post(body)
            .build()

     GlobalScope.launch(Dispatchers.Default) {
        abcde()
        }
    }
//=================================================================
fun post_3rd_devivce_list() {

    val url = "http://202.88.100.249/SAWELLPlus_Club/php/3rd_get_device_list.php"

   client = OkHttpClient()                            //要一個實例
     JSON = MediaType.parse("application/json; charset=utf-8")
     body = RequestBody.create(JSON, "{\"account\":\"w06\" ,\"pw\":\"w\" , \"timezone\":8 }")
     request = Request.Builder()                    //建立需求
        .addHeader("Cookie",headerfiltercookie)  // 這個有問題 （這是加檔頭傳送）
        .url(url)
        .post(body)
        .build()

    GlobalScope.launch(Dispatchers.Main) {
        try {
            val response: Response = client.newCall(request).execute()            // 取得回應到response 來
   //         abc(response)
    //        println(response.request())
     //       println(response.body()!!.string())                    //本文字串


        }catch (e:Exception) {
            println ("Error!!!!")
        }
    }
}

    private suspend fun abcde() {
        withContext(Dispatchers.IO) {
            try {
                response = client.newCall(request).execute()            // 取得回應到response 來
   //             println(response.body().string())                    //本文字串
                headerfiltercookie = response.header("Set-Cookie")  //PHPSESSID
                bodyData = response.body()!!.string()
                println(bodyData)
               println(headerfiltercookie)
                runOnUiThread {
                                 textView.text = "${bodyData}"
                }
            } catch (e: Exception) {
                println("Error!!!!")
            }



        }
    }
}