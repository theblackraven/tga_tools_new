package com.example.theblackraven.tga_tools

import android.content.ContentValues
import android.content.Context
import android.database.Cursor.FIELD_TYPE_STRING
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

/**
 * Created by theblackraven 2018-09-30
 */


class Constants_DB_Pipes{
    companion object {
        val DATABASE_NAME_PIPES ="DB_PIPES"
        val TABLE_NAME_PIPES ="pipes"
        val COL_PIPETYP = "pipetyp"
        val COL_PIPEMANUFACTURER = "pipemanufacturer"
        val COL_TYPPIPEMANUFACTURER = "typ_pipemanufacturer"
        val COL_PIPE_DO = "outer_diameter"
        val COL_PIPE_DI = "inner_diameter"
        val COL_PIPE_K   = "pipe_k"
        val COL_PIPE_DN   = "pipe_dn"
        val COL_ID = "id"
        val COL_USER_ENTRY = "User_Entry"
    }
}



class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context,Constants_DB_Pipes.DATABASE_NAME_PIPES,null,1){
    override fun onCreate(db: SQLiteDatabase?) {

        val createTable = "CREATE TABLE " + Constants_DB_Pipes.TABLE_NAME_PIPES +" (" +
                Constants_DB_Pipes.COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                Constants_DB_Pipes.COL_PIPETYP + " VARCHAR(256)," +
                Constants_DB_Pipes.COL_PIPEMANUFACTURER + " VARCHAR(256)," +
                Constants_DB_Pipes.COL_TYPPIPEMANUFACTURER + " VARCHAR(256)," +
                Constants_DB_Pipes.COL_PIPE_DN +" INTEGER," +
                Constants_DB_Pipes.COL_PIPE_DO +" FLOAT," +
                Constants_DB_Pipes.COL_PIPE_DI +" FLOAT," +
                Constants_DB_Pipes.COL_PIPE_K + " FLOAT," +
                Constants_DB_Pipes.COL_USER_ENTRY + " INTEGER)"
        db?.execSQL(createTable)

    }

    override fun onUpgrade(db: SQLiteDatabase?,oldVersion: Int,newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun insertDataPipes(pipes : Pipes){
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
        if(result == -1.toLong())
            Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()
    }

    fun Get_All_Columns_Pipes(query : String) : MutableList<Pipes>{
        var list : MutableList<Pipes> = ArrayList()

        val db = this.readableDatabase
        val result = db.rawQuery(query,null)
        if(result.moveToFirst()){
            do {
                var pipe = Pipes()
                pipe.id = result.getString(result.getColumnIndex(Constants_DB_Pipes.COL_ID)).toInt()
                pipe.typ = result.getString(result.getColumnIndex(Constants_DB_Pipes.COL_PIPETYP))
                pipe.manufacturer = result.getString(result.getColumnIndex(Constants_DB_Pipes.COL_PIPEMANUFACTURER))
                pipe.typ_manufacturer = result.getString(result.getColumnIndex(Constants_DB_Pipes.COL_TYPPIPEMANUFACTURER))
                pipe.dn = result.getString(result.getColumnIndex(Constants_DB_Pipes.COL_PIPE_DO)).toInt()
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