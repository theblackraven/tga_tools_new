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
            if (etv.text.toString().length > 0 &&
                    etvDi.text.toString().toFloat() > 0 && etvDo.text.toString().toFloat() > 0  &&  etvk.text.toString().toFloat() > 0 ) {
                var pipe = Pipes(etvPipe.text.toString(),etvDo.text.toString().toFloat(),etvDi.text.toString().toFloat(),etvk.text.toString().toFloat())
                db.insertDataPipes(pipe)
            } else {
                Toast.makeText(context,"Please Fill All Data's",Toast.LENGTH_SHORT).show()
            }
        }

        btn_read.setOnClickListener({
            var data = db.readDataPipes()
            tvResult.text = ""
            for (i in 0..(data.size - 1)) {
                tvResult.append(data.get(i).id.toString() + " " + data.get(i). + " " + data.get(i).k + "\n")
            }
        })

        btn_delete.setOnClickListener({
            db.deleteData()
            btn_read.performClick()
        })
    }
}
