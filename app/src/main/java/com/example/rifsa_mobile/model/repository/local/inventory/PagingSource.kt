package com.example.rifsa_mobile.model.repository.local.inventory

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rifsa_mobile.model.entity.remotefirebase.InventoryEntity
import com.example.rifsa_mobile.model.local.room.dao.InventoryDao

class PagingSource(
    private val dao : InventoryDao
): PagingSource<Int, InventoryEntity>() {

    override fun getRefreshKey(
        state: PagingState<Int, InventoryEntity>
    ): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, InventoryEntity> {
        return try{
            val position = params.key ?: 0
            val data = dao.readInventorySortNameAsc(
                1, 10
            )

            Log.d("tes Inventory",data.size.toString())
            LoadResult.Page(
                data = data,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (data.isNullOrEmpty()) null else position + 1
            )
        } catch ( e : Exception){
            LoadResult.Error(e)
        }
    }

}