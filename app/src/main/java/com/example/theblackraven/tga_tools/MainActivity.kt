package com.example.theblackraven.tga_tools

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_listpipes.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.apps_layout.*
import org.jetbrains.anko.doAsync

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


    fun goto_app(id:Int, context:Context)
    {

        TGA_RoomDatabase.getDatabase(context).DaoApps().used_count(id)// Count everytime app is started

        if (id == 1)
        {
            startActivity(Intent(this@MainActivity, ListPipesActivity::class.java))
        }

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val context = this
        doAsync {
            CreateAppList(context)
        }

        val db = TGA_RoomDatabase.getDatabase(context)
        val DaoApps = db.DaoApps()
        val ListApps = mutableListOf<Apps>()

       //Get applist
        var data = DaoApps.getAllApps()
        if (data != null) {
            for (i in 0..(data.size - 1)) {
                ListApps.add(data.get(i))
            }
        }


        lvApps.adapter = AppAdapter(this, ListApps)

        lvApps.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            val  tvCount = view.findViewById<TextView>(R.id.tvapp_id).text.toString().toInt()
            goto_app(tvCount, context)
        }


        }

    inner class AppAdapter : BaseAdapter {         //BaseAdapter implements ListAdapter, SpinnerAdapter;
        // declare an explicit supertype, we place the type after a colon in the class header (: BaseAdapter)
        private var notesList = mutableListOf<Apps>()
        private var context: Context            //? ==> Context is nullable

        constructor(context: Context, notesList : MutableList<Apps>) : super() {       //secondary constructor, super is needed
            this.notesList = notesList
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {        //override the function getView

            val view: View?
            val vh: MainActivity.ViewHolder

            if (convertView == null) {      //only inflate, when view isn't inflated yet; See  https://stackoverflow.com/questions/10560624/what-is-the-purpose-of-convertview-in-listview-adapter
                view = layoutInflater.inflate(R.layout.apps_layout, parent, false)
                vh = MainActivity.ViewHolder(view, position, this.context)
                view.tag = vh
            } else {
                view = convertView
                vh = view.tag as MainActivity.ViewHolder
            }

            vh.app_name.text = notesList[position].app_name
            vh.counter.text = notesList[position].used_count.toString()
            vh.ivApp.setImageResource(getImageId(notesList[position].id))
            vh.app_id.text = notesList[position].id.toString()

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
    private class ViewHolder(view: View?, position: Int, context: Context) {
        val app_name: TextView
        val counter: TextView
        val ivApp: ImageView
        val app_id : TextView
        var context : Context = context


        init {
            this.app_name = view?.findViewById<TextView>(R.id.tvapp_name) as TextView
            this.app_id = view?.findViewById<TextView>(R.id.tvapp_id) as TextView
            this.counter = view?.findViewById<TextView>(R.id.tvCounter) as TextView
            this.ivApp = view?.findViewById<ImageView>(R.id.ivApp) as ImageView
        }
    }
}

