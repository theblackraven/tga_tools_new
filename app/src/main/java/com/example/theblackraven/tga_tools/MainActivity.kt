package com.example.theblackraven.tga_tools

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val context = this
        var db = DataBaseHandler(context)
        btn_insert.setOnClickListener {
            if (pipetyp.text.toString().length > 0
                   ) {
                var pipe = Pipes("Test","")
                db.insertDataPipes(pipe)
            } else {
                Toast.makeText(context,"Please Fill All Data's",Toast.LENGTH_SHORT).show()
            }
        }

        btn_read.setOnClickListener({
            var data = db.readManufacturersPipes()
            tvResult.text = ""
            for (i in 0..(data.size - 1)) {
                tvResult.append(data.get(i) + "\n")
            }
        })

        var manu = arrayOf("English", "French", "Spanish", "Hindi" )
        val mySpinner = findViewById(R.id.spinner_pipe_manufacturer) as Spinner

        btn_delete.setOnClickListener({
            db.deleteData()
            btn_read.performClick()
        })
    }
}
