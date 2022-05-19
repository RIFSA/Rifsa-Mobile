package com.example.rifsa_mobile.view.fragment.inventory.insert

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentInvetoryInsertDetailBinding
import com.example.rifsa_mobile.model.entity.inventory.Inventory
import com.example.rifsa_mobile.utils.Utils
import com.example.rifsa_mobile.view.fragment.camera.CameraFragment
import com.example.rifsa_mobile.view.fragment.inventory.InventoryFragment.Companion.detail_inventory
import com.example.rifsa_mobile.viewmodel.LocalViewModel
import com.example.rifsa_mobile.viewmodel.utils.ObtainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch


class InvetoryInsertFragment : Fragment() {
    private lateinit var binding : FragmentInvetoryInsertDetailBinding
    private lateinit var viewModel: LocalViewModel


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
        bottomMenu.visibility = View.VISIBLE

        binding.imgInventory.setOnClickListener {
            setFragmentCamera()
        }

        try {
            val data = requireArguments().getParcelable<Inventory>(detail_inventory)
            if (data != null){
                val pic = Uri.parse(data.urlPhoto)
                showImage(pic)
                showDetail(data)
                isDetail = true
                detailId = data.id_inventories
                sortId = data.id_sort
                currentImage = pic
            }
            showCameraImage()
        }catch (e : Exception){

        }

        binding.btnHarvestSave.setOnClickListener {
            lifecycleScope.launch {
                insertInventoryLocal()
            }
        }

        return binding.root
    }

    private fun showCameraImage(){
        val arrayFile = requireArguments().getSerializable(invetory_camera_key) as ArrayList<*>
        val file = arrayFile[0] as Uri

        currentImage = file
        showImage(file)

    }

    private fun showImage(data : Uri){
        Glide.with(requireActivity())
            .asBitmap()
            .load(data)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(binding.imgInventory)

    }

    private fun setFragmentCamera(){
        val bundle = Bundle()
        val fragment = CameraFragment()
        bundle.putString(camera_key, camera_key_inventory)
        fragment.arguments = bundle
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainnav_framgent,fragment)
            .addToBackStack(null)
            .commit()
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
        }catch (e : Exception){
            showToast(e.message.toString())
        }
    }

    private fun showDetail(data : Inventory){
        val amount = data.amount.toString()
        binding.tvinventarisInsertName.setText(data.name)
        binding.tvinventarisInsertAmount.setText(amount)
        binding.tvinventarisInsertNote.setText(data.noted)
    }

    private fun showToast(title : String){
        Toast.makeText(requireContext(),title, Toast.LENGTH_SHORT).show()
    }

    companion object{
        const val invetory_camera_key = "camera_pic"
        const val camera_key_inventory = "inventory"
        const val camera_key = "camera"

    }


}