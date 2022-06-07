package com.example.rifsa_mobile.model.entity.remote.finance

import com.example.rifsa_mobile.model.entity.remote.harvestresult.HarvestResponData
import com.google.gson.annotations.SerializedName

data class FinancePostBody(

	@field:SerializedName("tanggal")
	val tanggal: String,

	@field:SerializedName("kegiatan")
	val kegiatan: String,

	@field:SerializedName("jenis")
	val jenis: String,

	@field:SerializedName("jumlah")
	val jumlah: String,

	@field:SerializedName("catatan")
	val catatan: String
)

data class FinancePostResponse(
	@SerializedName("data")
	val financeResponseData: FinanceResponseData,

	@SerializedName("message")
	val message: String,

	@SerializedName("status")
	val status: Int
)
