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
    lateinit var request: Request
    lateinit var response: Response
    lateinit var bodyData: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        keyProc()
    }


//keyProc
    fun keyProc()
{
    //        按鍵
    login.setOnClickListener {
        post_login()
    }
    devicelist.setOnClickListener {
        post_login()
        post_3rd_devivce_list()

    }

    btnHistry.setOnClickListener {
        post_login()
        post_3rd_get_history_data()
    }


    btnGreen.setOnClickListener {
        post_login()
        post3rd_get_club_greenwatter()
    }
}

//================================================================================

    //  副程式在此

    fun post_login() {
        GlobalScope.launch(Dispatchers.IO) {  //要使用Default,unconfined,IO , 用Main會當機, 奇怪
            val url = "http://202.88.100.249/SAWELLPlus_Club/php/3rd_login.php"

            client = OkHttpClient()                            //要一個實例
            JSON = MediaType.parse("application/json; charset=utf-8")
            val jsondata = "{\n" +
                    "\"account\":\"w06\",\n" +
                    "\"pw\":\"w\",\n" +
                    "\"timezone\":8 \n" +
                    "}\n"
            //          body = RequestBody.create(JSON, "{\"account\":\"w06\" ,\"pw\":\"w\" , \"timezone\":8 }")
            body = RequestBody.create(JSON, jsondata)


            request = Request.Builder()                    //建立需求
                .url(url)
                .post(body)
                .build()

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

    //=================================================================
    fun post_3rd_devivce_list() {
        GlobalScope.launch(Dispatchers.Default) {
            delay(1000)            //不加delay 好像不行 , 網路備好時間(min=300ms)
            val url = "http://202.88.100.249/SAWELLPlus_Club/php/3rd_get_device_list.php"
            client = OkHttpClient()                            //要一個實例
            JSON = MediaType.parse("application/json; charset=utf-8")
            request = Request.Builder()                    //建立需求
                .addHeader("Cookie", phpsessid)  // （這是加檔頭傳送）
                .url(url)
                .build()
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

    //=================================================================
    fun post_3rd_get_history_data() {
        GlobalScope.launch(Dispatchers.Default) {
            delay(1000)            //不加delay 好像不行 , 網路備好時間(min=300ms)
            val url = "http://202.88.100.249/SAWELLPlus/ doBind.php"
            client = OkHttpClient()                            //要一個實例
            // 記憶雙引號前加\
            val jsondata = "{\n" +
                    "\t\"value_type\":0,\n" +
                    "\t\"during_type\":4,\n" +
                    "    \"query_type\":\"\",\n" +
                    "\t\"start_date\": \"2014-07-21\",\n" +
                    "\t\"stop_date\": \"2020-09-06\"\n" +
                    "\t}\n"
            body = RequestBody.create(JSON,jsondata)
            JSON = MediaType.parse("application/json; charset=utf-8")
            request = Request.Builder()                    //建立需求
                .addHeader("Cookie", phpsessid)  // （這是加檔頭傳送）
                .url(url)
                .post(body)
                .build()
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

    //=====================================================================================
    fun post3rd_get_club_greenwatter() {
        GlobalScope.launch(Dispatchers.Default) {
            delay(1000)            //不加delay 好像不行 , 網路備好時間(min=300ms)
            val url = "  http://202.88.100.249/SAWELLPlus_Club/php/3rd_get_club_greenwatter.php"
            client = OkHttpClient()                            //要一個實例
            // 記憶雙引號前加\
            JSON = MediaType.parse("application/json; charset=utf-8")
            request = Request.Builder()                    //建立需求
                .addHeader("Cookie", phpsessid)  // （這是加檔頭傳送）
                .url(url)
                .build()
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
