package com.example.rifsa_mobile.model.local.room.dao

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.FinancialEntity

@Dao
interface DiseaseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertDiseaseLocal(data : DiseaseEntity)

    @Query("select * from DiseaseTable")
    fun getDiseaseLocal(): LiveData<List<DiseaseEntity>>

    @Query("delete from DiseaseTable where diseaseId like :id")
    fun deleteDiseaseLocal(id: String)

    @Query("select*from DiseaseTable where isUploaded = 0")
    fun getDiseaseNotUploaded(): LiveData<List<DiseaseEntity>>

    @Query("select*from DiseaseTable where isUploaded = 0 and uploadReminderId =:alarmId")
    fun getDataNotUploaded(alarmId : Int): DiseaseEntity

    @Query("select*from DiseaseTable where isUploaded = 0")
    fun getNotUploaded(): List<DiseaseEntity>

    @Query("update DiseaseTable set imageUrl=:imageUri,isUploaded = 1 where diseaseId=:idDisease")
    fun updateUploadStatus(imageUri : String, idDisease : String)

    //sort data
    @Query("select * from DiseaseTable order by nameDisease asc")
    fun readDiseaseSortNameAsc(): DataSource.Factory<Int, DiseaseEntity>

    @Query("select * from DiseaseTable order by nameDisease desc")
    fun readDiseaseSortNameDesc(): DataSource.Factory<Int, DiseaseEntity>

    @Query("select * from DiseaseTable order by nameDisease asc")
    fun readDiseaseSortDateAsc(): DataSource.Factory<Int, DiseaseEntity>

    @Query("select * from DiseaseTable order by nameDisease desc")
    fun readDiseaseSortDateDesc(): DataSource.Factory<Int, DiseaseEntity>

}