package com.example.okhttp_sawellplus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.okhttp.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    var phpsessid = ""
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

     GlobalScope.launch(Dispatchers.Default) {  //要使用Default,unconfined,IO , 用Main會當機, 奇怪
         login()
        }
    }
//=================================================================
fun post_3rd_devivce_list() {
    val url = "http://202.88.100.249/SAWELLPlus_Club/php/3rd_get_device_list.php"
    client = OkHttpClient()                            //要一個實例
    JSON = MediaType.parse("application/json; charset=utf-8")
    request = Request.Builder()                    //建立需求
        .addHeader("Cookie", phpsessid)  // （這是加檔頭傳送）
        .url(url)
        .build()

    GlobalScope.launch(Dispatchers.Default) {
        deviceList()
    }

}


    //================Login 取得PHPSESSID , Respoose = {Status:1} =========================
    suspend fun login() {
        withContext(Dispatchers.IO) {
            try {
                response = client.newCall(request).execute()            // 取得回應到response 來
                phpsessid = response.header("Set-Cookie")  //PHPSESSID
                bodyData = response.body()!!.string()
          //      println(bodyData)
          //     println(phpsessid)
                runOnUiThread {
                    textView.text = "${bodyData} \n ${phpsessid}"
                }
            } catch (e: Exception) {
                println("Error!!!!")
            }
        }
        }

       //=======================================================
    suspend fun deviceList() {
        withContext(Dispatchers.IO) {
            try {
                response = client.newCall(request).execute()            // 取得回應到response 來
                bodyData = response.body()!!.string()
               println(bodyData)
                runOnUiThread {
                  textView.text = "${bodyData}"
             }
            } catch (e: Exception) {
                println("Error!!!!")
            }
        }
    }
    }
