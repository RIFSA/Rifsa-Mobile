package com.example.rifsa_mobile.view.fragment.diseasewiki

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rifsa_mobile.databinding.FragmentDiseaseBookBinding
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseDetailEntity
import com.example.rifsa_mobile.view.fragment.diseasewiki.adapter.DiseaseBookRecyclerViewAdapter
import com.example.rifsa_mobile.viewmodel.remoteviewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class DiseaseBookFragment : Fragment() {
    private var _binding : FragmentDiseaseBookBinding? = null
    private val binding get() = _binding!!

    private val remoteViewModel : RemoteViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }

    private var dataList = ArrayList<DiseaseDetailEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiseaseBookBinding.inflate(layoutInflater)

        getDiseaseWiki()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDiseasebookBack.setOnClickListener {
            findNavController().navigate(
                DiseaseBookFragmentDirections.actionDiseaseBookFragmentToDisaseFragment()
            )
        }
    }

    private fun getDiseaseWiki(){
        remoteViewModel.getDiseaseWiki().addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               if(snapshot.exists()){
                   snapshot.children.forEach { child ->
                       val data = child.getValue(DiseaseDetailEntity::class.java)
                       if (data != null) {
                           binding.pgbarKamus.visibility = View.GONE
                           dataList.add(data)
                           showDiseaseList(dataList)
                       }
                   }
               }
            }

            override fun onCancelled(error: DatabaseError) {
                binding.pgbarKamus.visibility = View.GONE
                showError(error.message.toString())
            }

        })
    }


    private fun showDiseaseList(data : List<DiseaseDetailEntity>){
        val adapter = DiseaseBookRecyclerViewAdapter(data)
        val recyclerView = binding.listPenyakit
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter.onDetailCallback(object : DiseaseBookRecyclerViewAdapter.OnDetailCallback{
            override fun onDetailCallBack(data: DiseaseDetailEntity) {
//                findNavController().navigate(
//                    DiseaseBookFragmentDirections.actionDiseaseBookFragmentToDisaseDetailFragment(
//                        data
//                    )
//                )
            }
        })
    }

    private fun showError(text : String){
        binding.tvErrorView.visibility = View.VISIBLE
        binding.tvErrorView.text = text
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}