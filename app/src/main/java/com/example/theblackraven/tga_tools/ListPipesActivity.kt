package com.example.theblackraven.tga_tools

import android.app.AlertDialog
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_listpipes.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class ListPipesActivity : AppCompatActivity() {

    private lateinit  var PipeViewModel1: PipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listpipes)



        val context = this

        val DaoPipes = TGA_Database_non_persistant.getDatabase(context).DaoPipes()
        val DaoPipes_persistent = TGA_RoomDatabase.getDatabase(context).DaoPipes_persistant()
        DaoPipes.insertAll(Pipes_persistant2Pipes(DaoPipes_persistent.getAllPipes()))

       // Liste für die Rohre
        var ListPipes = mutableListOf<Pipes>()
            //var data = db.Get_All_Columns_Pipes("Select * from " + Constants_DB_Pipes.TABLE_NAME_PIPES +  " WHERE " + Constants_DB_Pipes.COL_PIPEMANUFACTURER + " LIKE 'Geberit'" )

            /*
            var data = db.Get_All_Columns_Pipes()
            for (i in 0..(data.size - 1)) {
                ListPipes.add(data.get(i))
            }*/
        PipeViewModel1 = ViewModelProviders.of(this).get(PipeViewModel::class.java)
        PipeViewModel1.init()


        var adapter = PipeAdapter(this, ListPipes)
        lvPipes.adapter = adapter //Fill Listview

        PipeViewModel1.Pipes.observe(this, Observer { Pipes ->
            if (Pipes != null) {
                ListPipes = Pipes.toMutableList()
                adapter.update(ListPipes)
            }

        })
            lvPipes.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
                adapter.activate(position)
            }

        val searchItem = findViewById<SearchView>(R.id.sv_pipes)

        searchItem.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.length > 0) {
                    PipeViewModel1.search(newText)
                }
                else
                {
                    PipeViewModel1.clear()
                }
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                // task HERE
                return false
            }

        })


    }



    //Wiki for classes with kotlin: https://kotlinlang.org/docs/reference/classes.html
    inner class PipeAdapter : BaseAdapter {         //BaseAdapter implements ListAdapter, SpinnerAdapter;
                                                    // declare an explicit supertype, we place the type after a colon in the class header (: BaseAdapter)
        private var notesList = mutableListOf<Pipes>()
        private var context: Context            //? ==> Context is nullable

        constructor(context: Context, notesList : MutableList<Pipes>) : super() {       //secondary constructor, super is needed
            this.notesList = notesList
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {        //override the function getView

            val view: View?
            val vh: ViewHolder

            if (convertView == null) {      //only inflate, when view isn't inflated yet; See  https://stackoverflow.com/questions/10560624/what-is-the-purpose-of-convertview-in-listview-adapter
                view = layoutInflater.inflate(R.layout.pipes_layout, parent, false)
                vh = ViewHolder(view, position, this.context)
                view.tag = vh
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }

            vh.tvTitle.text = notesList[position].manufacturer + " " + notesList[position].typ
            vh.tvContent.text = notesList[position].typ_manufacturer + " DN:" + notesList[position].dn
            vh.db_id = notesList[position].id

            if (notesList[position].category == true) {
                vh.ivDelete.visibility = View.INVISIBLE
                vh.ivEdit.visibility = View.INVISIBLE
            }
            else {
                vh.ivDelete.visibility = View.VISIBLE
                vh.ivEdit.visibility = View.VISIBLE
            }


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

        fun update(Pipes : MutableList<Pipes>)
        {
            notesList = Pipes
            notifyDataSetChanged()
        }

        fun activate(position : Int)
        {
            val PipesRepository = PipesRepository(context)
            if (notesList[position].category == true && notesList[position].activated == false ) {
                if (notesList[position].typ_manufacturer == "") {
                    PipesRepository.setVisibleCategory(notesList[position].manufacturer)
                }
                else {
                    PipesRepository.setVisiblePipes(notesList[position].manufacturer, notesList[position].typ_manufacturer)
                }
                PipesRepository.activate(notesList[position].id, true)
            }
            else if (notesList[position].category == true && notesList[position].activated == true ) {
                if (notesList[position].typ_manufacturer == "") {
                    PipesRepository.setInvisibleCategory(notesList[position].manufacturer)
                }
                else {
                    PipesRepository.setInvisiblePipes(notesList[position].manufacturer, notesList[position].typ_manufacturer)
                }
                PipesRepository.activate(notesList[position].id, false)
            }
        }
    }

    private class ViewHolder(view: View?, position: Int, context: Context) {
        val tvTitle: TextView
        val tvContent: TextView
        val ivDelete: ImageView
        val ivEdit: ImageView
        var db_id : String = ""
        var context : Context = context




 //       init {
 //           this.tvTitle = view?.findViewById(R.id.tvTitle) as TextView
 //           this.tvContent = view?.findViewById(R.id.tvContent) as TextView
 //       }

        //  if you target API 26, you should change to:
       init {
            this.tvTitle = view?.findViewById<TextView>(R.id.tvTitle) as TextView
            this.tvContent = view?.findViewById<TextView>(R.id.tvContent) as TextView
            this.ivDelete =   view?.findViewById<ImageView>(R.id.ivDelete) as ImageView
            this.ivEdit =   view?.findViewById<ImageView>(R.id.ivEdit) as ImageView


                this.ivEdit.setOnClickListener {
                    //Toast.makeText(this.context, this.context.toString() , Toast.LENGTH_SHORT).show()
                    val intent = Intent(this.context, InsertNewPipesActivity::class.java)
                    intent.putExtra(KEY_WORDS_INSERTNEWPIPESACTIVITY.DB_ID, db_id.toString())
                    this.context.startActivity(intent)
                }

                this.ivDelete.setOnClickListener {
                    val builder = AlertDialog.Builder(this.context)

                    builder.setTitle("Bestätigen")
                    builder.setMessage("Eintrag wirklich löschen?")

                    builder.setPositiveButton("YES") { dialog, which ->
                        val DaoPipes = TGA_Database_non_persistant.getDatabase(context).DaoPipes()
                        DaoPipes.deletePipe(db_id)
                    }

                    builder.setNegativeButton("No") { dialog, which ->

                        dialog.dismiss()
                    }

                    val dialog: AlertDialog = builder.create()
                    dialog.show()

                }
            }


    }
}

class PipeViewModel(application: Application) : AndroidViewModel(application) {

    private var parentJob = Job()
    // By default all the coroutines launched in this scope should be using the Main dispatcher
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)
    private val context = application

    val PipesRepository = PipesRepository(application)

    var Pipes = PipesRepository.getAllPipes()

    fun init()  = scope.launch(Dispatchers.IO){
        CreateAppList(context)
        Pipes = PipesRepository.getAllPipes()
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun search(pattern: String){
        PipesRepository.setAllInvisible()
        PipesRepository.search(pattern)

    }

    fun clear()
    {
        PipesRepository.setAllInvisible()
        PipesRepository.setFirstCategoryVisible()
    }


}
