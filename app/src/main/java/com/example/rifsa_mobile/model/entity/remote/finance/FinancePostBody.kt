package com.example.rifsa_mobile.model.entity.remote.finance

import com.example.rifsa_mobile.model.entity.remote.harvestresult.HarvestResponData
import com.google.gson.annotations.SerializedName

data class FinancePostBody(
	@SerializedName("tanggal")
	val tanggal: String,
	@SerializedName("kegiatan")
	val kegiatan: String,
	@SerializedName("jenis")
	val jenis: String,
	@SerializedName("jumlah")
	val jumlah: String,
	@SerializedName("catatan")
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
