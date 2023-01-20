package com.example.rifsa_mobile.view.fragment.inventory.insert

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentInvetoryInsertDetailBinding
import com.example.rifsa_mobile.model.entity.remotefirebase.InventoryEntity
import com.example.rifsa_mobile.viewmodel.remoteviewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.userpreferences.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.time.LocalDate
import java.util.*


class InvetoryInsertFragment : Fragment() {
    private lateinit var binding : FragmentInvetoryInsertDetailBinding

    private val remoteViewModel : RemoteViewModel by viewModels{ ViewModelFactory.getInstance(requireContext()) }
    private val authViewModel : UserPrefrencesViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }


    private lateinit var currentImage : Uri
    private var isDetail = false

    private var date = LocalDate.now().toString()
    private var detailId = UUID.randomUUID().toString()
    private var fileName = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInvetoryInsertDetailBinding.inflate(layoutInflater)

        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.GONE


        try {
            val data = InvetoryInsertFragmentArgs.fromBundle(requireArguments()).detailInventory
            if (data != null){
                showDetail(data)
                isDetail = true
                detailId = data.idInventory
                date = data.dated
                fileName = data.name
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
                        binding.pgInventoryBar.visibility = View.VISIBLE
                        deleteInventory()
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
            binding.pgInventoryBar.visibility = View.VISIBLE
            uploadInventoryFile()
        }

    }

    private fun showDetail(data : InventoryEntity){
        val amount = data.amount
        binding.tvinventarisInsertName.setText(data.name)
        binding.tvinventarisInsertAmount.setText(amount)
        binding.tvinventarisInsertNote.setText(data.noted)
        "Detail Data".also { binding.tvInventoryInsertdetail.text = it }

        Log.d("Image url",data.imageUrl)

        Glide.with(requireContext())
            .asBitmap()
            .load(data.imageUrl)
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
        authViewModel.getUserId().observe(viewLifecycleOwner){ userId ->
            remoteViewModel.uploadInventoryFile(name,currentImage,userId)
                .addOnSuccessListener {
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

        val tempData = InventoryEntity(
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
                showStatus(it.message.toString())
            }
    }

    private fun deleteInventory(){
        authViewModel.getUserId().observe(viewLifecycleOwner){ userId ->
            remoteViewModel.deleteInventory(date,detailId,userId)
                .addOnSuccessListener {
                    deleteInventoryFile()
                    showStatus("berhasil mengahpus")
                }
                .addOnFailureListener {
                    showStatus(it.message.toString())
                }
        }
    }

    private fun deleteInventoryFile(){
        authViewModel.getUserId().observe(viewLifecycleOwner){ userId ->
            remoteViewModel.deleteInventoryFile(fileName,userId)
                .addOnSuccessListener {
                    findNavController().navigate(
                        InvetoryInsertFragmentDirections
                            .actionInvetoryInsertFragmentToInventoryFragment()
                    )
                }
                .addOnFailureListener {
                    showStatus("gagal menghapus")
                }
        }
    }


    private fun gotoCameraFragment(){
        findNavController().navigate(InvetoryInsertFragmentDirections.actionInvetoryInsertFragmentToCameraFragment(
            camera_key)
        )
    }

    private fun showStatus(title : String){
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