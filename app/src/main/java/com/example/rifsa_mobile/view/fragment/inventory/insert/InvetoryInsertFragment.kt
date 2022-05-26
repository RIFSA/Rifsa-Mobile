package com.example.rifsa_mobile.view.fragment.inventory.insert

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentInvetoryInsertDetailBinding
import com.example.rifsa_mobile.model.entity.inventory.Inventory
import com.example.rifsa_mobile.utils.Utils
import com.example.rifsa_mobile.viewmodel.LocalViewModel
import com.example.rifsa_mobile.viewmodel.utils.ObtainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch


class InvetoryInsertFragment : Fragment() {
    private lateinit var binding : FragmentInvetoryInsertDetailBinding
    private lateinit var viewModel: LocalViewModel

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
        viewModel = ObtainViewModel(requireActivity())

        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.GONE

        binding.imgInventory.setOnClickListener {
            setFragmentCamera()
        }
        binding.btnInventoryBackhome.setOnClickListener {
            findNavController().navigate(
                InvetoryInsertFragmentDirections.actionInvetoryInsertFragmentToInventoryFragment()
            )
        }
        binding.btninventoryInsertDelete.setOnClickListener {
            deleteInventory()
        }


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
        }catch (e : Exception){

        }

        binding.btnInventorySave.setOnClickListener {
            lifecycleScope.launch {
                insertInventoryLocal()
            }
        }

        return binding.root
    }

    private fun showCameraImage(){

        val file = findNavController().currentBackStackEntry?.savedStateHandle?.get<Uri>(camera_key_inventory)

        if (file != null) {
            currentImage = file
            showImage(file)
        }

    }

    private fun showImage(data : Uri){
        Glide.with(requireContext())
            .asBitmap()
            .load(data)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.imgInventory)
    }

    private fun setFragmentCamera(){
        findNavController().navigate(InvetoryInsertFragmentDirections.actionInvetoryInsertFragmentToCameraFragment(
            camera_key)
        )
    }

    private suspend fun insertInventoryLocal(){
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
            viewModel.insertInventoryLocal(tempInsert)
            showToast("Berhasil menambahkan")
            findNavController().navigate(InvetoryInsertFragmentDirections.actionInvetoryInsertFragmentToInventoryFragment())
        }catch (e : Exception){
            showToast(e.message.toString())
        }
    }

    private fun deleteInventory(){
        try {
            viewModel.deleteInventoryLocal(detailId)
            showToast("Item telah dihapus")
            findNavController().navigate(InvetoryInsertFragmentDirections.actionInvetoryInsertFragmentToInventoryFragment())
        }catch (e : Exception){
            showToast(e.message.toString())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDetail(data : Inventory){
        val amount = data.amount.toString()
        binding.tvinventarisInsertName.setText(data.name)
        binding.tvinventarisInsertAmount.setText(amount)
        binding.tvinventarisInsertNote.setText(data.noted)
        binding.tvInventoryInsertdetail.text = "Detail data"
    }

    private fun showToast(title : String){
        Toast.makeText(requireContext(),title, Toast.LENGTH_SHORT).show()
    }

    companion object{
        const val camera_key_inventory = "inventory"
        const val camera_key = "back"
    }


}