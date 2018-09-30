package com.example.theblackraven.tga_tools

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

/**
 * Created by theblackraven 2018-09-30
 */

val DATABASE_NAME ="MyDB"
val TABLE_NAME="Rohre"
val COL_NAME = "Rohrname"
val COL_DA = "Aussendurchmesser"
val COL_DI = "Innendurchmesser"
val COL_K   = "Rohrrauhigkeit"
val COL_ID = "id"

class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context,DATABASE_NAME,null,1){
    override fun onCreate(db: SQLiteDatabase?) {

        val createTable = "CREATE TABLE " + TABLE_NAME +" (" +
                COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME + " VARCHAR(256)," +
                COL_DA +" INTEGER," +
                COL_DI +" INTEGER," +
                COL_K + " INTEGER)"

        db?.execSQL(createTable)

    }

    override fun onUpgrade(db: SQLiteDatabase?,oldVersion: Int,newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun insertData(pipes : Pipes){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_NAME,pipes.pipe)
        cv.put(COL_DA,pipes.diameter_out)
        cv.put(COL_DI, pipes.diameter_in)
        cv.put(COL_K, pipes.k)
        var result = db.insert(TABLE_NAME,null,cv)
        if(result == -1.toLong())
            Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()
    }

    fun readData() : MutableList<Pipes>{
        var list : MutableList<Pipes> = ArrayList()

        val db = this.readableDatabase
        val query = "Select * from " + TABLE_NAME
        val result = db.rawQuery(query,null)
        if(result.moveToFirst()){
            do {
                var pipe = Pipes()
                pipe.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                pipe.pipe = result.getString(result.getColumnIndex(COL_NAME))
                pipe.diameter_in = result.getString(result.getColumnIndex(COL_DI)).toInt()
                pipe.diameter_out = result.getString(result.getColumnIndex(COL_DA)).toInt()
                pipe.k = result.getString(result.getColumnIndex(COL_K)).toInt()
                list.add(pipe)
            }while (result.moveToNext())
        }

        result.close()
        db.close()
        return list
    }

    fun deleteData(){
        val db = this.writableDatabase
        db.delete(TABLE_NAME,null,null)
        db.close()
    }


    fun updateData() {
        val db = this.writableDatabase
        val query = "Select * from " + TABLE_NAME
        val result = db.rawQuery(query,null)
        if(result.moveToFirst()){
            do {
                var cv = ContentValues()
                cv.put(COL_K,(result.getInt(result.getColumnIndex(COL_K))+1))
                db.update(TABLE_NAME,cv,COL_ID + "=? AND " + COL_NAME + "=?",
                        arrayOf(result.getString(result.getColumnIndex(COL_ID)),
                                result.getString(result.getColumnIndex(COL_NAME))))
            }while (result.moveToNext())
        }

        result.close()
        db.close()
    }


}