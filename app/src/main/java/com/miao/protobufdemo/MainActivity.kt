package com.miao.protobufdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val stringServiceClient = StringServiceClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 在主程序中调用协程
        MainScope().launch {
            val result = withContext(Dispatchers.Default) {
                stringServiceClient.getStringValue("Your request value")
            }

            // 在协程中获取结果后，可以在此处进行处理
            // 例如，更新 UI 或打印结果
            updateUI(result)
        }
    }

    private fun updateUI(result: String) {
        // 更新 UI 以显示结果
        // 示例：textView.text = result
    }
}