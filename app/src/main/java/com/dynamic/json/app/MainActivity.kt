package com.dynamic.json.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dynamic.json.app.databinding.ActivityMainBinding
import com.dynamic.json.viewer.util.DyAssetser
import com.dynamic.json.viewer.util.json.JSONViewer

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        binding.clickMeButton.setOnClickListener {

            val jsonViewer: JSONViewer = JSONViewer()
            val jsonObject = DyAssetser.getAssetsAsJson("json/example.json")
            jsonViewer.show("example", jsonObject, null)

        }
    }
}
