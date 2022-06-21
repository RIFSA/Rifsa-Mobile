package com.example.rifsa_mobile.model.entity.remote.finance

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class FinanceResultResponse(

	@field:SerializedName("data")
	val financeResponseData: List<FinanceResponseData>,
	@field:SerializedName("message")
	val message: String,
	@field:SerializedName("status")
	val status: Int
)

@Parcelize
data class FinanceResponseData(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("kegiatan")
	val kegiatan: String,

	@field:SerializedName("id_keuangan")
	val idKeuangan: Int,

	@field:SerializedName("jumlah")
	val jumlah: String,

	@field:SerializedName("jenis")
	val jenis: String,

	@field:SerializedName("catatan")
	val catatan: String,

	@field:SerializedName("tanggal")
	val tanggal: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
): Parcelable
