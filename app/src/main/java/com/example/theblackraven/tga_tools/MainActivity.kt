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
            if (etvPipe.text.toString().length > 0 &&
                    etvPipe.text.toString().length > 0) {
                var user = Pipes(etvPipe.text.toString(),0,0,etvk.text.toString().toInt())
                db.insertData(user)
            } else {
                Toast.makeText(context,"Please Fill All Data's",Toast.LENGTH_SHORT).show()
            }
        }

        btn_read.setOnClickListener({
            var data = db.readData()
            tvResult.text = ""
            for (i in 0..(data.size - 1)) {
                tvResult.append(data.get(i).id.toString() + " " + data.get(i).pipe + " " + data.get(i).k + "\n")
            }
        })

        btn_update.setOnClickListener({
            db.updateData()
            btn_read.performClick()
        })

        btn_delete.setOnClickListener({
            db.deleteData()
            btn_read.performClick()
        })
    }
}
