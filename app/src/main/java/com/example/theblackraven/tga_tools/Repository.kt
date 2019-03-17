package com.example.theblackraven.tga_tools

import android.arch.lifecycle.LiveData
import android.content.Context


class AppsRepository(private val context: Context) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    private val db = TGA_Database_non_persistant.getDatabase(context)
    private val DaoApps = db.DaoApps()
    private val Apps = DaoApps.getAllApps_LiveData()

    fun getAllApps() : LiveData<List<Apps>>  {
        return Apps
    }

    fun insert(apps:Apps){
        DaoApps.insert(apps)
    }

    fun update(app_name : String, id : Int, parent_ids : String){
        DaoApps.update(app_name, id, parent_ids)
    }

    fun used_count(app_name:String)
    {
        DaoApps.used_count(app_name)
    }


    fun setInvisible(s_id: String, n_id : Int){
        DaoApps.setInvisible(s_id, n_id)
    }

    fun setVisible(s_id: String, n_id : Int){
        DaoApps.setVisible(s_id, n_id)
    }

    fun activate(id: Int, activate : Boolean){
        DaoApps.acitvate(id, activate)
    }

    fun setAllInvisible(){
        DaoApps.setAllInvisible()
    }

    fun setFirstCategoryVisible(){
        DaoApps.setFirstCategoryVisible()
    }

    fun search(pattern : String){
        DaoApps.search(pattern)
    }


    // You must call this on a non-UI thread or your app will crash. So we're making this a
    // suspend function so the caller methods know this.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.



}

class PipesRepository(private val context: Context) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    private val db = TGA_Database_non_persistant.getDatabase(context)
    private val DaoPipes = db.DaoPipes()
    private val Pipes = DaoPipes.getAllPipes()

    fun getAllPipes() : LiveData<List<Pipes>>  {
        return Pipes
    }

    fun setVisibleCategory(manufactuer: String){
        DaoPipes.setVisibleCategory(manufactuer)
    }

    fun setInvisibleCategory(manufactuer: String){
        DaoPipes.setInvisibleCategory(manufactuer)
    }

    fun setVisiblePipes(manufactuer: String, typ_manufactuer : String){
        DaoPipes.setVisiblePipes(manufactuer, typ_manufactuer)
    }

    fun setInvisiblePipes(manufactuer: String, typ_manufactuer : String){
        DaoPipes.setInvisiblePipes(manufactuer, typ_manufactuer)
    }

    fun activate(id: String, activate : Boolean){
        DaoPipes.acitvate(id, activate)
    }

    fun setAllInvisible(){
        DaoPipes.setAllInvisible()
    }

    fun setFirstCategoryVisible(){
        DaoPipes.setFirstCategoryVisible()
    }

    fun search(pattern: String){
        DaoPipes.search(pattern)
    }

    // You must call this on a non-UI thread or your app will crash. So we're making this a
    // suspend function so the caller methods know this.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.



}

