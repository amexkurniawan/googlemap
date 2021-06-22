package com.example.googlemap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.googlemap.ui.lesson1.AddMapActivity
import com.example.googlemap.ui.lesson1.AddMapKtxActivity
import com.example.googlemap.ui.lesson2.NearbyPlaceActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onClickAction()
    }

    private fun onClickAction() {
        findViewById<Button>(R.id.btnGMap1).setOnClickListener {
            val intent = Intent(this, AddMapActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.btnGMap2).setOnClickListener {
            val intent = Intent(this, AddMapKtxActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.btnGMap3).setOnClickListener {
            val intent = Intent(this, NearbyPlaceActivity::class.java)
            startActivity(intent)
        }
    }
}