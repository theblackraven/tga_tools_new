package com.example.theblackraven.tga_tools

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.*
import android.content.ContentValues
import android.content.Context
import android.database.Cursor.FIELD_TYPE_STRING
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

/**
 * Created by theblackraven 2018-09-30
 */

class Constants_DB{
    companion object {
        val DATABASE_NAME_TGA_TOOLS ="DB_TGA_TOOLS"
        val COL_ID = "id"
    }
}

class Constants_DB_Pipes{
    companion object {
        val TABLE_NAME_PIPES ="pipes"
        val COL_PIPETYP = "pipetyp"
        val COL_PIPEMANUFACTURER = "pipemanufacturer"
        val COL_TYPPIPEMANUFACTURER = "typ_pipemanufacturer"
        val COL_PIPE_DO = "outer_diameter"
        val COL_PIPE_DI = "inner_diameter"
        val COL_PIPE_K   = "pipe_k"
        val COL_PIPE_DN   = "pipe_dn"
        val COL_USER_ENTRY = "User_Entry"
    }
}





class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context,Constants_DB.DATABASE_NAME_TGA_TOOLS,null,1){
    override fun onCreate(db: SQLiteDatabase?) {

        val createTable_pipes = "CREATE TABLE IF NOT EXISTS " + Constants_DB_Pipes.TABLE_NAME_PIPES + " (" +
                Constants_DB.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Constants_DB_Pipes.COL_PIPETYP + " VARCHAR(256)," +
                Constants_DB_Pipes.COL_PIPEMANUFACTURER + " VARCHAR(256)," +
                Constants_DB_Pipes.COL_TYPPIPEMANUFACTURER + " VARCHAR(256)," +
                Constants_DB_Pipes.COL_PIPE_DN + " INTEGER," +
                Constants_DB_Pipes.COL_PIPE_DO + " FLOAT," +
                Constants_DB_Pipes.COL_PIPE_DI + " FLOAT," +
                Constants_DB_Pipes.COL_PIPE_K + " FLOAT," +
                Constants_DB_Pipes.COL_USER_ENTRY + " INTEGER)"
        db?.execSQL(createTable_pipes)
    }

    override fun onUpgrade(db: SQLiteDatabase?,oldVersion: Int,newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun InsertDataPipes(pipes : Pipes){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(Constants_DB_Pipes.COL_PIPETYP,pipes.typ)
        cv.put(Constants_DB_Pipes.COL_PIPEMANUFACTURER,pipes.manufacturer)
        cv.put(Constants_DB_Pipes.COL_TYPPIPEMANUFACTURER,pipes.typ_manufacturer)
        cv.put(Constants_DB_Pipes.COL_PIPE_DO,pipes.diameter_out)
        cv.put(Constants_DB_Pipes.COL_PIPE_DI, pipes.diameter_in)
        cv.put(Constants_DB_Pipes.COL_PIPE_K, pipes.k)
        cv.put(Constants_DB_Pipes.COL_PIPE_DN,pipes.dn)
        cv.put(Constants_DB_Pipes.COL_USER_ENTRY,pipes.UserEntry)
        var result = db.insert(Constants_DB_Pipes.TABLE_NAME_PIPES,null,cv)
        if(result == -1.toLong()){
            Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show()}
        else{
            Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()}
        db.close()
    }

    fun UpdateDataPipes(pipes : Pipes, id : Int){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(Constants_DB_Pipes.COL_PIPETYP,pipes.typ)
        cv.put(Constants_DB_Pipes.COL_PIPEMANUFACTURER,pipes.manufacturer)
        cv.put(Constants_DB_Pipes.COL_TYPPIPEMANUFACTURER,pipes.typ_manufacturer)
        cv.put(Constants_DB_Pipes.COL_PIPE_DO,pipes.diameter_out)
        cv.put(Constants_DB_Pipes.COL_PIPE_DI, pipes.diameter_in)
        cv.put(Constants_DB_Pipes.COL_PIPE_K, pipes.k)
        cv.put(Constants_DB_Pipes.COL_PIPE_DN,pipes.dn)
        cv.put(Constants_DB_Pipes.COL_USER_ENTRY,pipes.UserEntry)
        var result = db.update(Constants_DB_Pipes.TABLE_NAME_PIPES, cv, Constants_DB.COL_ID + " = " + id.toString(), null)
        if(result >=1){
            Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()}
        else {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    fun DeleteDataPipes(id:Int)
    {
        val db = this.writableDatabase
        var result = db.delete(Constants_DB_Pipes.TABLE_NAME_PIPES, Constants_DB.COL_ID + " = " + id.toString(), null)
        if(result >=1){
            Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()}

        else {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    fun Get_All_Columns_Pipes(WhereClause : String ="") : MutableList<Pipes>{
        var list : MutableList<Pipes> = ArrayList()
        var query : String
        if (WhereClause!="")
        {
            query = "Select * from " + Constants_DB_Pipes.TABLE_NAME_PIPES + " where " + WhereClause
        }
        else
        {
            query = "Select * from " + Constants_DB_Pipes.TABLE_NAME_PIPES
        }
        val db = this.readableDatabase
        val result = db.rawQuery(query,null)
        if(result.moveToFirst()){
            do {
                var pipe = Pipes()
                pipe.id = result.getString(result.getColumnIndex(Constants_DB.COL_ID)).toInt()
                pipe.typ = result.getString(result.getColumnIndex(Constants_DB_Pipes.COL_PIPETYP))
                pipe.manufacturer = result.getString(result.getColumnIndex(Constants_DB_Pipes.COL_PIPEMANUFACTURER))
                pipe.typ_manufacturer = result.getString(result.getColumnIndex(Constants_DB_Pipes.COL_TYPPIPEMANUFACTURER))
                pipe.dn = result.getString(result.getColumnIndex(Constants_DB_Pipes.COL_PIPE_DN)).toInt()
                pipe.diameter_in = result.getString(result.getColumnIndex(Constants_DB_Pipes.COL_PIPE_DI)).toFloat()
                pipe.diameter_out = result.getString(result.getColumnIndex(Constants_DB_Pipes.COL_PIPE_DO)).toFloat()
                pipe.k = result.getString(result.getColumnIndex(Constants_DB_Pipes.COL_PIPE_K)).toFloat()
                list.add(pipe)
            }while (result.moveToNext())
        }

        result.close()
        db.close()
        return list
    }



    fun Get_DB_Column_String(DB: String, Column:String) : MutableList<String>{


        var list = mutableListOf<String>()
        val db = this.readableDatabase
        val query = "SELECT " + Column +" from " + DB
        val result = db.rawQuery(query,null)

        if(result.moveToFirst()){
            do {
                if (result.getColumnIndex(Column) != -1 &&  result.getType(result.getColumnIndex(Column)) == FIELD_TYPE_STRING) {
                    list.add(result.getString(result.getColumnIndex(Column)).toString())
                }
            }while (result.moveToNext())
        }



        result.close()
        db.close()
        return list
    }

    fun Get_DB_Value_String(DB: String, Column:String, Id:Int, CloseDB:Boolean = true) : String{


        var value: String = "Error"
        val db = this.readableDatabase
        val query = "SELECT " + Column +" from " + DB + " where " + Constants_DB.COL_ID + " = " + Id.toString()
        val result = db.rawQuery(query,null)

        if(result.moveToFirst()){
            do {
                if (result.getColumnIndex(Column) != -1) {
                    value =result.getString(result.getColumnIndex(Column)).toString()
                }
            }while (result.moveToNext())
        }



        result.close()
        if (CloseDB) {
            db.close()
        }
        return value
    }

    // Returns only every occurence once
    fun Get_DB_Column_String_DISTINCT(DB: String, Column:String) : MutableList<String>{


        var list = mutableListOf<String>()
        val db = this.readableDatabase
        val query = "SELECT DISTINCT  " + Column +" from " + DB
        val result = db.rawQuery(query,null)

        if(result.moveToFirst()){
            do {
                if (result.getColumnIndex(Column) != -1 &&  result.getType(result.getColumnIndex(Column)) == FIELD_TYPE_STRING) {
                    list.add(result.getString(result.getColumnIndex(Column)).toString())
                }
            }while (result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }

    fun deleteAllData(DB : String){
        val db = this.writableDatabase
        db.delete( DB,null,null)
        db.close()
    }





}

@Dao
interface DaoApps {
    @Query("SELECT * from table_apps ORDER BY app_name ASC")
    fun getAllApps(): List<Apps>

    @Query("SELECT * from table_apps WHERE visible = 1 ORDER BY id, used_count ASC")
    fun getAllApps_LiveData(): LiveData<List<Apps>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun InsertDataApps(Apps: Apps)

    @Query("UPDATE table_apps SET used_count = used_count + 1 WHERE app_name = :app_name")
    fun used_count(app_name:String)

    @Query("UPDATE table_apps SET visible = 0, activated = 0 WHERE parent_ids LIKE  '%ID' || :s_id || '%' AND not id = :n_id" )
    fun invisible(s_id: String, n_id : Int)

    @Query("UPDATE table_apps SET visible = 1 WHERE parent_ids LIKE '%' || :s_id AND not id = :n_id" )
    fun visible(s_id: String, n_id : Int)

    @Query("UPDATE table_apps SET activated = :activate WHERE id = :id" )
    fun acitvate(id: Int, activate : Boolean)

    @Query("UPDATE table_apps SET id = :id , parent_ids = :parent_ids WHERE app_name = :app_name")
    fun update(app_name : String, id : Int, parent_ids : String)
}

@Database(entities = [Apps::class], version = 2)
abstract class TGA_RoomDatabase : RoomDatabase() {

    abstract fun DaoApps(): DaoApps

    companion object {
        @Volatile
        private var INSTANCE: TGA_RoomDatabase? = null

        fun getDatabase(context: Context): TGA_RoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        TGA_RoomDatabase::class.java,
                        "TGA-Tools_DB"
                ).allowMainThreadQueries().build() //#Todo run querrys not in Main Thread
                INSTANCE = instance
                return instance
            }
        }
    }
}