package com.example.theblackraven.tga_tools

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //Funktion, um ListPipesActivity  zu starten
    fun goto_ListPipesActivity()
    {
        startActivity(Intent(this@MainActivity, ListPipesActivity::class.java))
    }

    //Funktion, um ListPipesActivity  zu starten
    fun goto_InsertNewPipesActivity()
    {
        startActivity(Intent(this@MainActivity, InsertNewPipesActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btn_goto_ListPipesActivity.setOnClickListener {
            goto_ListPipesActivity()
        }

        btn_goto_InsertNewPipesActivity.setOnClickListener {
            goto_InsertNewPipesActivity()
        }
    }
}
