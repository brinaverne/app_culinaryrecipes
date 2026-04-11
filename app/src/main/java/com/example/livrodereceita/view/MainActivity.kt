package com.example.livrodereceita.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.example.livrodereceita.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var btntlinicial: Button = findViewById(R.id.btntlinicial)
        /** var btntlinicial = findViewById<Button>(R.id.btntlinicial) **/

        btntlinicial.setOnClickListener {
            var passatela = Intent(this, ListadeReceitas::class.java)
            startActivity(passatela)
        }

    }
}