package com.example.theblackraven.tga_tools

import android.app.Application
import android.arch.lifecycle.*
import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import android.widget.RelativeLayout
import android.util.TypedValue
import android.util.DisplayMetrics






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
            adapter.activate(position)
            adapter.open_app(position)
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

            vh.app_name.text = getStringResource(notesList[position].app_name)
            val level = notesList[position].parent_ids.split("ID").size - 1
            try {
                val lp = LinearLayout.LayoutParams(vh.app_name.getLayoutParams())
                lp.setMargins(convertDpToPx(level * 15, vh.app_name.getResources().getDisplayMetrics()),lp.topMargin, lp.rightMargin, lp.bottomMargin)
                if (!notesList[position].runable) {
                    if (level == 0) {
                        vh.app_name.setBackgroundColor(Color.parseColor("#aaaaaa"))
                        vh.counter.setBackgroundColor(Color.parseColor("#aaaaaa"))
                        vh.ivApp.setBackgroundColor(Color.parseColor("#aaaaaa"))
                    }
                    else if (level == 1) {
                        vh.app_name.setBackgroundColor(Color.parseColor("#00bbbb"))
                        vh.counter.setBackgroundColor(Color.parseColor("#00bbbb"))
                        vh.ivApp.setBackgroundColor(Color.parseColor("#00bbbb"))
                    }
                }
                else{
                    vh.app_name.setBackgroundColor(Color.parseColor("#ffffff"))
                    vh.counter.setBackgroundColor(Color.parseColor("#ffffff"))
                    vh.ivApp.setBackgroundColor(Color.parseColor("#ffffff"))
                }

                vh.app_name.setLayoutParams(lp)
                vh.counter.setLayoutParams(lp)
            }
            catch (e: Throwable){

            }
            vh.counter.text = notesList[position].used_count.toString()
            vh.ivApp.setImageResource(getImageId(notesList[position].app_name))

            return view
        }

        private fun convertDpToPx(dp: Int, displayMetrics: DisplayMetrics): Int {
            val pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), displayMetrics)
            return Math.round(pixels)
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

        fun activate(position : Int)
        {
            val db = TGA_RoomDatabase.getDatabase(context)
            val DaoApps = db.DaoApps()
            if (notesList[position].runable == false && notesList[position].activated == false ) {
                DaoApps.visible(notesList[position].id.toString(), notesList[position].id)
                DaoApps.acitvate(notesList[position].id, true)
            }
            else if (notesList[position].runable == false && notesList[position].activated == true ) {
                DaoApps.invisible(notesList[position].id.toString(), notesList[position].id)
                DaoApps.acitvate(notesList[position].id, false)
            }
        }

        fun open_app(position: Int)
        {
            apps_open_app(notesList[position].app_name, context)
        }

        //Get String Reosource
        private fun getStringResource(string:String) : String
        {
            var stringResource : String = string
            try {
               stringResource = getResources().getString(getResources().getIdentifier(string, "string", getPackageName()))
                return stringResource
            }
            catch (e: Throwable ) {
                return string
            }

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

    val AppsRepository = AppsRepository(application)


    var Apps = AppsRepository.getAllApps()

    fun init()  = scope.launch(Dispatchers.IO){
          CreateAppList(context)
          Apps = AppsRepository.getAllApps()
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }


}





