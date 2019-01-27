package com.example.theblackraven.tga_tools

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import kotlinx.android.synthetic.main.activity_insertnewpipes.*


class KEY_WORDS_INSERTNEWPIPESACTIVITY{
    companion object {
        val DB_ID = "id"
    }
}


class InsertNewPipesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertnewpipes)


        val context = this
        var db = DataBaseHandler(context)

        val bundle=intent.extras
        var db_id : String? = null
        var pipe : Pipes?
        var manufacturers = mutableListOf<String>("Alle")
        var pipe_typ = mutableListOf<String>("Alle")
        var manufacturer_typ = mutableListOf<String>("Alle")
        var dn = mutableListOf<Int>(8, 10, 12, 15, 20)

        if(bundle!=null) {
            db_id= bundle.getString(KEY_WORDS_INSERTNEWPIPESACTIVITY.DB_ID)
            Log.i("InsertNewPipesActivity", "db_id: " + db_id)
        }


        if (db_id != null) //load data from pipe and insert values
        {
            var data =  db.Get_All_Columns_Pipes("Select * from " + Constants_DB_Pipes.TABLE_NAME_PIPES +  " WHERE " + Constants_DB_Pipes.COL_ID + " == " + db_id )
            pipe = data.get(0)
            ac_pipe_manufacturer.setText(pipe.manufacturer)
            ac_pipe__typ_manufacturer.setText(pipe.typ_manufacturer)
            ac_pipetyp.setText(pipe.typ)
            ac_pipe_dn.setText(pipe.dn.toString())
            etv_di.setText(pipe.diameter_in.toString())
            etv_do.setText(pipe.diameter_out.toString())
            etv_k.setText(pipe.k.toString())
            btn_insert_update.setText("Update")

        }


        //get existing manufactuer

        var data_manufactures = db.Get_DB_Column_String_DISTINCT(Constants_DB_Pipes.TABLE_NAME_PIPES, Constants_DB_Pipes.COL_PIPEMANUFACTURER)
        for (i in 0..(data_manufactures.size - 1)) {
            manufacturers.add(data_manufactures.get(i))
        }

        //get existing pipe_typ
        var data_typ = db.Get_DB_Column_String_DISTINCT(Constants_DB_Pipes.TABLE_NAME_PIPES, Constants_DB_Pipes.COL_PIPETYP)
        for (i in 0..(data_typ.size - 1)) {
            pipe_typ.add(data_typ.get(i))
        }

        //get existing typ of manufactuer
        var data_manufacturer_typ = db.Get_DB_Column_String_DISTINCT(Constants_DB_Pipes.TABLE_NAME_PIPES, Constants_DB_Pipes.COL_TYPPIPEMANUFACTURER)
        for (i in 0..(data_manufacturer_typ.size - 1)) {
            manufacturer_typ.add(data_manufacturer_typ.get(i))
        }


        // Initialize a new array adapter object
        val adapter_manufacturer = ArrayAdapter<String>(
                this, // Context
                android.R.layout.simple_dropdown_item_1line, // Layout
                manufacturers // Listadapter
        )

        // Set the AutoCompleteTextView adapter
        ac_pipe_manufacturer.setAdapter(adapter_manufacturer)

        // Auto complete threshold
        // The minimum number of characters to type to show the drop down
        ac_pipe_manufacturer.threshold = 1

        val adapter_manufactuer_typ = ArrayAdapter<String>(
                this, // Context
                android.R.layout.simple_dropdown_item_1line, // Layout
                manufacturer_typ // Listadapter
        )
        ac_pipe__typ_manufacturer.setAdapter(adapter_manufactuer_typ)
        ac_pipe__typ_manufacturer.threshold = 1


        val adapter_typ = ArrayAdapter<String>(
                this, // Context
                android.R.layout.simple_dropdown_item_1line, // Layout
                pipe_typ // Listadapter
        )
        ac_pipetyp.setAdapter(adapter_typ)
        ac_pipetyp.threshold = 1

        val adapter_dn = ArrayAdapter<Int>(
                this, // Context
                android.R.layout.simple_dropdown_item_1line, // Layout
                dn
        )
        ac_pipe_dn.setAdapter(adapter_dn)
        ac_pipe_dn.threshold = 1



        //String array.
        val units = arrayOf("mm", "inch")

        //Adapter for spinners
        spinner_unit_pipe_di.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, units)
        spinner_unit_pipe_do.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, units)
        spinner_unit_pipe_k.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, units)




        btn_insert_update.setOnClickListener {
            if (ac_pipetyp.text.toString().length > 0 && ac_pipe_manufacturer.text.toString().length > 0 && ac_pipe__typ_manufacturer.text.toString().length > 0 && ac_pipe_dn.text.toString().length > 0 && etv_di.text.toString().length > 0  && etv_k.text.toString().length > 0 && etv_do.text.toString().length > 0
            ) {

                    var pipe = Pipes(ac_pipetyp.text.toString(), ac_pipe_manufacturer.text.toString(), ac_pipe__typ_manufacturer.text.toString(), etv_do.text.toString().toFloat(), etv_di.text.toString().toFloat(), etv_k.text.toString().toFloat(), ac_pipe_dn.text.toString().toInt())
                if (db_id == null) { //add new pipe
                    db.InsertDataPipes(pipe)
                }
                else
                {
                    db.UpdateDataPipes(pipe, db_id.toInt())
                }
            } else {
                Toast.makeText(context,"Please Fill All Data's",Toast.LENGTH_SHORT).show()
            }
        }


        }
}
