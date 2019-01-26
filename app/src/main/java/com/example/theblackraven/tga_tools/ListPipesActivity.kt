package com.example.theblackraven.tga_tools

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_listpipes.*

class ListPipesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listpipes)


        val context = this
        var db = DataBaseHandler(context)



        // Liste für die Rohre
        val ListPipes = mutableListOf<Pipes>()
            var data = db. Get_All_Columns_Pipes("Select * from " + Constants_DB_Pipes.TABLE_NAME_PIPES +  " WHERE " + Constants_DB_Pipes.COL_PIPEMANUFACTURER + " LIKE 'Unknown'" )
            for (i in 0..(data.size - 1)) {
                ListPipes.add(data.get(i))
            }

            var PipeAdapter = PipeAdapter(this, ListPipes)
            lvPipes.adapter = PipeAdapter
            lvPipes.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
                Toast.makeText(this, "Click on " + ListPipes[position].typ_manufacturer, Toast.LENGTH_SHORT).show()
            }



    }

    inner class PipeAdapter : BaseAdapter {

        private var notesList = mutableListOf<Pipes>()
        private var context: Context? = null

        constructor(context: Context, notesList : MutableList<Pipes>) : super() {
            this.notesList = notesList
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

            val view: View?
            val vh: ViewHolder

            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.pips_layout, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
                Log.i("JSA", "set Tag for ViewHolder, position: " + position)
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }

            vh.tvTitle.text = notesList[position].manufacturer + " " + notesList[position].typ
            vh.tvContent.text = notesList[position].typ_manufacturer + " DN:" + notesList[position].dn

            return view
        }

        override fun getItem(position: Int): Any {
            return notesList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return notesList.size
        }
    }

    private class ViewHolder(view: View?) {
        val tvTitle: TextView
        val tvContent: TextView

 //       init {
 //           this.tvTitle = view?.findViewById(R.id.tvTitle) as TextView
 //           this.tvContent = view?.findViewById(R.id.tvContent) as TextView
 //       }

        //  if you target API 26, you should change to:
       init {
          this.tvTitle = view?.findViewById<TextView>(R.id.tvTitle) as TextView
            this.tvContent = view?.findViewById<TextView>(R.id.tvContent) as TextView
        }
    }
}
