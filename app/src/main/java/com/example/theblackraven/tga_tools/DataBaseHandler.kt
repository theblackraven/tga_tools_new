package com.example.theblackraven.tga_tools

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.content.Context

/**
 * Created by theblackraven 2018-09-30
 */
@Dao
interface DaoPipes{

    @Query("UPDATE table_pipes SET visible = 0, activated = 0 " )
    fun setAllInvisible()

    @Query("UPDATE table_pipes SET visible = 1 WHERE typ_manufacturer = '' " )
    fun setFirstCategoryVisible()

    @Query("UPDATE table_pipes SET visible = 1 WHERE typ_manufacturer LIKE  '%' || :pattern || '%' OR typ LIKE  '%' || :pattern || '%' OR DN LIKE  '%' || :pattern || '%' AND category = 0"  )
    fun search(pattern : String)

    @Query("SELECT * from table_pipes WHERE visible = 1 ORDER BY manufacturer ASC, typ ASC")
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

    @Query("UPDATE table_pipes SET visible = 1 WHERE manufacturer = :manufactuer AND category = 1" )
    fun setVisibleCategory(manufactuer: String)

    @Query("UPDATE table_pipes SET visible = 0, activated = 0 WHERE manufacturer = :manufactuer  AND typ_manufacturer <> '' ")
    fun setInvisibleCategory(manufactuer: String)

    @Query("UPDATE table_pipes SET visible = 1 WHERE typ_manufacturer =  :typ_manufactuer AND manufacturer = :manufactuer AND category = 0" )
    fun setVisiblePipes(manufactuer: String, typ_manufactuer : String)

    @Query("UPDATE table_pipes SET visible = 0 WHERE typ_manufacturer =  :typ_manufactuer AND manufacturer = :manufactuer AND category = 0" )
    fun setInvisiblePipes(manufactuer: String, typ_manufactuer : String)

    @Query("UPDATE table_pipes SET activated = :activate WHERE id = :id" )
    fun acitvate(id: String, activate : Boolean)

    @Update()
    fun Update (vararg pipe: Pipes)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(ListPipes: List<Pipes>)

}

@Dao
interface DaoPipes_persistent{

    @Query("SELECT * from table_pipes_persistent")
    fun getAllPipes(): List<Pipes_persistant>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun InsertPipes(Pipes: Pipes_persistant)

    @Update()
    fun Update (vararg pipe: Pipes_persistant)
}

@Dao
interface DaoApps {
    @Query("SELECT * from table_apps ORDER BY app_name DESC")
    fun getAllApps(): List<Apps>

    @Query("SELECT * from table_apps WHERE visible = 1 ORDER BY id, app_name_location DESC")
    fun getAllApps_LiveData(): LiveData<List<Apps>>

    @Query("UPDATE table_apps SET visible = 0, activated = 0 " )
    fun setAllInvisible()

    @Query("UPDATE table_apps SET visible = 1 WHERE parent_ids ='' " )
    fun setFirstCategoryVisible()

    @Query("UPDATE table_apps SET visible = 1 WHERE app_name_location LIKE  '%' || :pattern || '%' AND runable = 1"  )
    fun search(pattern : String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(Apps: Apps)

    @Query("UPDATE table_apps SET used_count = used_count + 1 WHERE app_name = :app_name")
    fun used_count(app_name:String)

    @Query("UPDATE table_apps SET visible = 0, activated = 0 WHERE parent_ids LIKE  '%ID' || :s_id || '%' AND not id = :n_id" )
    fun setInvisible(s_id: String, n_id : Int)

    @Query("UPDATE table_apps SET visible = 1 WHERE parent_ids LIKE '%' || :s_id AND not id = :n_id" )
    fun setVisible(s_id: String, n_id : Int)

    @Query("UPDATE table_apps SET activated = :activate WHERE id = :id" )
    fun acitvate(id: Int, activate : Boolean)

    @Query("UPDATE table_apps SET id = :id , parent_ids = :parent_ids WHERE app_name = :app_name")
    fun update(app_name : String, id : Int, parent_ids : String)
}

@Database(entities = [Apps::class, Pipes::class], version = 1)
abstract class TGA_Database_non_persistant : RoomDatabase() {

    abstract fun DaoApps(): DaoApps
    abstract fun DaoPipes(): DaoPipes

    companion object {
        @Volatile
        private var INSTANCE: TGA_Database_non_persistant? = null

        fun getDatabase(context: Context): TGA_Database_non_persistant {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.inMemoryDatabaseBuilder(
                        context.applicationContext,
                        TGA_Database_non_persistant::class.java
                ).allowMainThreadQueries().build() //#Todo run querrys not in Main Thread
                INSTANCE = instance
                return instance
            }
        }
    }
}

@Database(entities = [Pipes_persistant::class], version = 1)
abstract class TGA_RoomDatabase : RoomDatabase() {

    abstract fun DaoPipes_persistant(): DaoPipes_persistent

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