package com.example.rifsa_mobile.view.fragment.inventory.insert

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
import com.example.rifsa_mobile.model.entity.remote.inventory.InventoryResultResponData
import com.example.rifsa_mobile.model.entity.remotefirebase.InventoryFirebaseEntity
import com.example.rifsa_mobile.utils.FetchResult
import com.example.rifsa_mobile.view.fragment.inventory.InventoryFragmentDirections
import com.example.rifsa_mobile.viewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*


class InvetoryInsertFragment : Fragment() {
    private lateinit var binding : FragmentInvetoryInsertDetailBinding

    private val remoteViewModel : RemoteViewModel by viewModels{ ViewModelFactory.getInstance(requireContext()) }
    private val authViewModel : UserPrefrencesViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }


    private var detail : InventoryResultResponData? = null
    private lateinit var currentImage : Uri
    private var isDetail = false

    private var date = LocalDate.now().toString()
    private var detailId = UUID.randomUUID().toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInvetoryInsertDetailBinding.inflate(layoutInflater)

        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.GONE


        try {
            val data = InvetoryInsertFragmentArgs.fromBundle(requireArguments()).detailInventory
            detail = data
            if (data != null){
                showDetail(data)
                isDetail = true
                detailId = data.idInventaris.toString()
                binding.btninventoryInsertDelete.visibility = View.VISIBLE
                binding.btnInventorySave.visibility = View.GONE
            }
            showCameraImage()
        }catch (e : Exception){ }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isDetail){
            showCameraImage()
        }

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
                        deleteInventoryRemote()
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
            uploadInventoryFile()
        }

    }

    private fun showDetail(data : InventoryResultResponData){
        val amount = data.jumlah
        binding.tvinventarisInsertName.setText(data.nama)
        binding.tvinventarisInsertAmount.setText(amount)
        binding.tvinventarisInsertNote.setText(data.catatan)
        "Detail Data".also { binding.tvInventoryInsertdetail.text = it }

        Glide.with(requireContext())
            .asBitmap()
            .load("http://34.101.50.17:5000/images/${data.image}")
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.imgInventory)
    }

    private fun showCameraImage(){
        if (!isDetail){
            val uriImage = findNavController().currentBackStackEntry?.savedStateHandle?.get<Uri>(camera_key_inventory)
            if (uriImage != null) {
                currentImage = uriImage
                binding.imgInventory.setImageURI(uriImage)
            }
        }

    }



    private fun uploadInventoryFile(){
        val name = binding.tvinventarisInsertName.text.toString()
        authViewModel.getUserToken().observe(viewLifecycleOwner){ userId ->
            remoteViewModel.uploadInventoryFile(name,currentImage,userId).addOnSuccessListener {
                    it.storage.downloadUrl
                        .addOnSuccessListener { url ->
                           insertInventory(url,name, userId)
                        }
                        .addOnFailureListener {
                            showStatus("gagal menadapatkan link")
                        }
                }
                .addOnFailureListener{
                    showStatus(it.message.toString())
                }
        }
    }

    private fun insertInventory(imageUrl : Uri,name : String,userId : String){
        val amount = binding.tvinventarisInsertAmount.text.toString()
        val note = binding.tvinventarisInsertNote.text.toString()

        val tempData = InventoryFirebaseEntity(
            detailId,
            name,
            note,
            imageUrl.toString(),
            amount,
            date
        )
        remoteViewModel.insertInventory(tempData,userId)
            .addOnSuccessListener {
                showStatus("berhasil menambahkan")
                findNavController().navigate(
                    InvetoryInsertFragmentDirections.actionInvetoryInsertFragmentToInventoryFragment()
                )
            }
            .addOnFailureListener {
                showStatus("gagal menambahkan")
            }
    }

    private fun deleteInventoryRemote(){
        authViewModel.getUserToken().observe(viewLifecycleOwner){ token ->

        }

    }


    private fun gotoCameraFragment(){
        findNavController().navigate(InvetoryInsertFragmentDirections.actionInvetoryInsertFragmentToCameraFragment(
            camera_key)
        )
    }

    private fun showStatus(title : String){
        Toast.makeText(requireContext(),title, Toast.LENGTH_SHORT).show()

        if (title.isNotEmpty()){
            binding.pgInventoryBar.visibility = View.GONE
            binding.pgInventoryTitle.apply {
                visibility = View.VISIBLE
                text = title
            }
        }

        Log.d("InsertInventoryFragment",title)
    }

    companion object{
        const val camera_key_inventory = "inventory"
        const val camera_key = "back"
    }


}