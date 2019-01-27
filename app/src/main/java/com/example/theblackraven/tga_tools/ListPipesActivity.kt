package com.example.theblackraven.tga_tools

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_listpipes.*
import android.content.DialogInterface



class ListPipesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listpipes)


        val context = this
        var db = DataBaseHandler(context)



        // Liste für die Rohre
        val ListPipes = mutableListOf<Pipes>()
            //var data = db.Get_All_Columns_Pipes("Select * from " + Constants_DB_Pipes.TABLE_NAME_PIPES +  " WHERE " + Constants_DB_Pipes.COL_PIPEMANUFACTURER + " LIKE 'Geberit'" )
            var data = db.Get_All_Columns_Pipes("Select * from " + Constants_DB_Pipes.TABLE_NAME_PIPES )
            for (i in 0..(data.size - 1)) {
                ListPipes.add(data.get(i))
            }

            var PipeAdapter = PipeAdapter(this, ListPipes)
            lvPipes.adapter = PipeAdapter //Fill Listview
            lvPipes.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
                Toast.makeText(this, "Click on " + ListPipes[position].typ_manufacturer, Toast.LENGTH_SHORT).show()
            }


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
            vh.db_id = notesList[position].id.toInt()

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
        val tvTitle: TextView
        val tvContent: TextView
        val ivDelete: ImageView
        val ivEdit: ImageView
        var db_id : Int = 0
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
            this.ivDelete.setOnClickListener{
                Log.i("Delete", "Delete pressed Postion: " + db_id)

            }

            this.ivEdit.setOnClickListener{
                //Toast.makeText(this.context, this.context.toString() , Toast.LENGTH_SHORT).show()
                val intent = Intent(this.context, InsertNewPipesActivity::class.java)
                intent.putExtra(KEY_WORDS_INSERTNEWPIPESACTIVITY.DB_ID, db_id.toString())
                this.context.startActivity(intent)
            }

            this.ivDelete.setOnClickListener{
                val builder = AlertDialog.Builder(this.context)

                builder.setTitle("Bestätigen")
                builder.setMessage("Eintrag wirklich löschen?")

                builder.setPositiveButton("YES"){dialog, which ->
                    var db = DataBaseHandler(context)
                    db.DeleteDataPipes(db_id)
                    (context as Activity).finish()

                    //Reload activity to refresh Data
                    val intent = Intent(this.context, ListPipesActivity::class.java)
                    this.context.startActivity(intent)
                }

                builder.setNegativeButton("No"){dialog, which ->

                    dialog.dismiss()
                }

                val dialog: AlertDialog = builder.create()
                dialog.show()

            }
        }
    }
}
