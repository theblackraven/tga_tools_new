package com.example.theblackraven.tga_tools

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.*
import android.content.ContentValues
import android.content.Context
import android.database.Cursor.FIELD_TYPE_STRING
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE
import android.database.sqlite.SQLiteDatabase.createInMemory
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

/**
 * Created by theblackraven 2018-09-30
 */
@Dao
interface DaoPipes{
    @Query("SELECT * from table_pipes ORDER BY manufacturer ASC")
    fun getAllPipes(): LiveData<List<Pipes>>

    @Query("SELECT * from table_pipes WHERE id LIKE :id")
    fun getPipe(id:String): Pipes

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun InsertPipes(Pipes: Pipes)

    @Query("DELETE from table_pipes WHERE id LIKE :id")
    fun deletePipe(id : String)

    @Query("SELECT DISTINCT manufacturer  FROM table_pipes")
    fun getDestinctManufactuer() : List<String>

    @Query("SELECT DISTINCT typ FROM table_pipes")
    fun getDestinctTyp() : List<String>

    @Query("SELECT DISTINCT typ_manufacturer FROM table_pipes")
    fun getDestinctTypManufacturer() : List<String>



    @Update()
    fun Update (vararg pipe: Pipes)
}

@Dao
interface DaoApps {
    @Query("SELECT * from table_apps ORDER BY app_name DESC")
    fun getAllApps(): List<Apps>

    @Query("SELECT * from table_apps WHERE visible = 1 ORDER BY id, used_count DESC")
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
abstract class TGA_Apps_Database : RoomDatabase() {

    abstract fun DaoApps(): DaoApps

    companion object {
        @Volatile
        private var INSTANCE: TGA_Apps_Database? = null

        fun getDatabase(context: Context): TGA_Apps_Database {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.inMemoryDatabaseBuilder(
                        context.applicationContext,
                        TGA_Apps_Database::class.java
                ).allowMainThreadQueries().build() //#Todo run querrys not in Main Thread
                INSTANCE = instance
                return instance
            }
        }
    }
}

@Database(entities = [Apps::class, Pipes::class], version = 2)
abstract class TGA_RoomDatabase : RoomDatabase() {

    abstract fun DaoPipes(): DaoPipes

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