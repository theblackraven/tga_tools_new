package com.example.theblackraven.tga_tools

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import kotlinx.android.synthetic.main.activity_insertnewpipes.*




class InsertNewPipesActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertnewpipes)




        //String array.
        val pipe_manufacturers = arrayOf("Geberit", "Unknown", "Three", "Four", "Five")

        // Initialize a new array adapter object
        val adapter = ArrayAdapter<String>(
                this, // Context
                android.R.layout.simple_dropdown_item_1line, // Layout
                pipe_manufacturers // Array
        )

        // Set the AutoCompleteTextView adapter
        ac_pipe_manufacturer.setAdapter(adapter)

        // Auto complete threshold
        // The minimum number of characters to type to show the drop down
        ac_pipe_manufacturer.threshold = 2



        val context = this
        var db = DataBaseHandler(context)
        btn_insert.setOnClickListener {
            if (pipetyp.text.toString().length > 0
                   ) {
                var pipe = Pipes("test",ac_pipe_manufacturer.text.toString(), "Test", 0f, 0f, 0f,0)
                db.insertDataPipes(pipe)
            } else {
                Toast.makeText(context,"Please Fill All Data's",Toast.LENGTH_SHORT).show()
            }
        }


        }
}
