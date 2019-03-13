package com.example.theblackraven.tga_tools

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.arch.lifecycle.*
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class MainActivity() : AppCompatActivity() {


    private lateinit  var AppsViewModel1: AppsViewModel

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bundle=intent.extras

        val context = this
        var ListApps = mutableListOf<Apps>()
        AppsViewModel1 = ViewModelProviders.of(this).get(AppsViewModel::class.java)
        AppsViewModel1.init()

        var adapter = AppAdapter(this, ListApps)
        lvApps.adapter = adapter


        AppsViewModel1.Apps.observe(this, Observer { Apps ->
            if (Apps != null) {
                ListApps = Apps.toMutableList()
                adapter.update(ListApps)
            }

            })


        lvApps.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            AppsViewModel1.update(getLevel_of_Item(position,ListApps)+1)
            goto_app(getId_of_ListView(position,ListApps), context)
        }

        }


    inner class AppAdapter : BaseAdapter {         //BaseAdapter implements ListAdapter, SpinnerAdapter;
        // declare an explicit supertype, we place the type after a colon in the class header (: BaseAdapter)
        private var notesList = mutableListOf<Apps>()
        private var context: Context            // ==> Context is nullable

        constructor(context: Context, notesList : MutableList<Apps>) : super() {       //secondary constructor, super is needed
            this.notesList = notesList
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {        //override the function getView

            val view: View?
            val vh: MainActivity.ViewHolder

            if (convertView == null) {      //only inflate, when view isn't inflated yet; See  https://stackoverflow.com/questions/10560624/what-is-the-purpose-of-convertview-in-listview-adapter
                view = layoutInflater.inflate(R.layout.apps_layout, parent, false)
                vh = MainActivity.ViewHolder(view, this.context)
                view.tag = vh
            } else {
                view = convertView
                vh = view.tag as MainActivity.ViewHolder
            }

            vh.app_name.text = notesList[position].app_name
            vh.counter.text = notesList[position].used_count.toString()
            vh.ivApp.setImageResource(getImageId(notesList[position].id))
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

        fun update(Apps : MutableList<Apps>)
        {
            notesList = Apps
            notifyDataSetChanged()
        }

    }
    private class ViewHolder(view: View?, context: Context) {
        val app_name: TextView
        val counter: TextView
        val ivApp: ImageView
        var context : Context = context


        init {
            this.app_name = view?.findViewById<TextView>(R.id.tvapp_name) as TextView
            this.counter = view?.findViewById<TextView>(R.id.tvCounter) as TextView
            this.ivApp = view?.findViewById<ImageView>(R.id.ivApp) as ImageView
        }
    }
}

class AppsViewModel(application: Application) : AndroidViewModel(application) {

    private var parentJob = Job()
    // By default all the coroutines launched in this scope should be using the Main dispatcher
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)
    private val context = application

    val db = TGA_RoomDatabase.getDatabase(context)
    val DaoApps = db.DaoApps()

    val AppsRepository = AppsRepository(application)


    var Apps = AppsRepository.getAllApps()

    fun update(level : Int)
    {
        DaoApps.activate(level)
    }

    fun init()  = scope.launch(Dispatchers.IO){
          CreateAppList(context)
          Apps = AppsRepository.getAllApps()
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }


}

//Return Id of Database for the Clicked Element of the ListView
private fun getId_of_ListView(position:Int, ListofApps:List<Apps>) : Int
{
    return (ListofApps.get(position).id)
}

private fun getLevel_of_Item(position:Int, ListofApps:List<Apps>) : Int
{
    return (ListofApps.get(position).level)
}



