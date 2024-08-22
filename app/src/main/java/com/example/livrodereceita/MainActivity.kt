package com.example.livrodereceita

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var btntlinicial: ImageButton = findViewById(R.id.btntlinicial)
        /** var btntlinicial = findViewById<Button>(R.id.btntlinicial) **/

        btntlinicial.setOnClickListener {
            var passatela = Intent(this, ListadeReceitas::class.java)
            startActivity(passatela)
        }

    }
}