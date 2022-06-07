package com.example.rifsa_mobile.view.fragment.inventory.insert

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentInvetoryInsertDetailBinding
import com.example.rifsa_mobile.model.entity.local.inventory.Inventory
import com.example.rifsa_mobile.utils.FetchResult
import com.example.rifsa_mobile.utils.Utils
import com.example.rifsa_mobile.viewmodel.LocalViewModel
import com.example.rifsa_mobile.viewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.utils.ObtainViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody


class InvetoryInsertFragment : Fragment() {
    private lateinit var binding : FragmentInvetoryInsertDetailBinding

    private val remoteViewModel : RemoteViewModel by viewModels{ ViewModelFactory.getInstance(requireContext()) }
    private lateinit var localViewModel: LocalViewModel

    private var detail : Inventory? = null
    private lateinit var currentImage : Uri
    private var randomId = Utils.randomId()
    private var isDetail = false
    private var detailId = ""
    private var sortId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInvetoryInsertDetailBinding.inflate(layoutInflater)
        localViewModel = ObtainViewModel(requireActivity())

        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.GONE


        try {
            val data = InvetoryInsertFragmentArgs.fromBundle(requireArguments()).detailInventory
            detail = data
            if (data != null){
                val pic = Uri.parse(data.urlPhoto)
                showImage(pic)
                showDetail(data)
                isDetail = true
                detailId = data.id_inventories
                sortId = data.id_sort
                currentImage = pic
                binding.btninventoryInsertDelete.visibility = View.VISIBLE
            }
            showCameraImage()
        }catch (e : Exception){ }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgInventory.setOnClickListener {
            gotoCameraFragment()
        }
        binding.btnInventoryBackhome.setOnClickListener {
            findNavController().navigate(
                InvetoryInsertFragmentDirections.actionInvetoryInsertFragmentToInventoryFragment()
            )
        }
        binding.btninventoryInsertDelete.setOnClickListener {
            AlertDialog.Builder(requireActivity()).apply {
                setTitle("Hapus data")
                setMessage("apakah anda ingin menghapus data ini ?")
                apply {
                    setPositiveButton("ya") { _, _ ->
                        deleteInventoryLocal()
                    }
                    setNegativeButton("tidak") { dialog, _ ->
                        dialog.dismiss()
                    }
                }
                create()
                show()
            }
        }


        binding.btnInventorySave.setOnClickListener {
            insertInventoryRemote()
        }

    }

    private fun insertInventoryRemote(){
        val image = Utils.uriToFile(currentImage,requireContext())
        val typeFile = image.asRequestBody("image/jpg".toMediaTypeOrNull())
        val multiPart : MultipartBody.Part = MultipartBody.Part.createFormData(
            "file",
            image.name,
            typeFile
        )

        val name = binding.tvinventarisInsertName.text.toString()
        val amount = binding.tvinventarisInsertAmount.text.toString().toInt()
        val note = binding.tvinventarisInsertNote.text.toString()

        lifecycleScope.launch {
            remoteViewModel.postInventory(name,multiPart,amount,note).observe(viewLifecycleOwner){
                when(it){
                    is FetchResult.Loading ->{

                    }
                    is FetchResult.Success->{
                        showToast(it.data.message)
                    }
                    is FetchResult.Error->{
                        showToast(it.error)
                        Log.d("Insert inventory",it.error)
                    }
                    else -> {}
                }
            }
        }
    }

    private suspend fun insertUpdateInventoryLocal(){
        val amount = binding.tvinventarisInsertAmount.text.toString()

        val tempInsert = Inventory(
            sortId,
            randomId,
            binding.tvinventarisInsertName.text.toString(),
            amount.toInt(),
            currentImage.toString(),
            binding.tvinventarisInsertNote.text.toString(),
            false
        )

        try {
            localViewModel.insertInventoryLocal(tempInsert)
            showToast("Berhasil menambahkan")
            findNavController().navigate(InvetoryInsertFragmentDirections.actionInvetoryInsertFragmentToInventoryFragment())
        }catch (e : Exception){
            showToast(e.message.toString())
        }
    }

    private fun deleteInventoryLocal(){
        try {
            localViewModel.deleteInventoryLocal(detailId)
            showToast("Item telah dihapus")
            findNavController().navigate(InvetoryInsertFragmentDirections.actionInvetoryInsertFragmentToInventoryFragment())
        }catch (e : Exception){
            showToast(e.message.toString())
        }
    }


    private fun showCameraImage(){
        val uriImage = findNavController().currentBackStackEntry?.savedStateHandle?.get<Uri>(camera_key_inventory)
        if (uriImage != null) {
            currentImage = uriImage
            showImage(uriImage)
        }
    }

    private fun gotoCameraFragment(){
        findNavController().navigate(InvetoryInsertFragmentDirections.actionInvetoryInsertFragmentToCameraFragment(
            camera_key)
        )
    }



    @SuppressLint("SetTextI18n")
    private fun showDetail(data : Inventory){
        val amount = data.amount.toString()
        binding.tvinventarisInsertName.setText(data.name)
        binding.tvinventarisInsertAmount.setText(amount)
        binding.tvinventarisInsertNote.setText(data.noted)
        binding.tvInventoryInsertdetail.text = "Detail registerData"
    }

    private fun showImage(data : Uri){
        Glide.with(requireContext())
            .asBitmap()
            .load(data)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.imgInventory)
    }


    private fun showToast(title : String){
        Toast.makeText(requireContext(),title, Toast.LENGTH_SHORT).show()
    }

    companion object{
        const val camera_key_inventory = "inventory"
        const val camera_key = "back"
    }


}