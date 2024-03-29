package com.akaf.nikoodriver.feature.auth.registering.fillInfo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.data.responses.serviceTypeResponse.Doc
import kotlinx.android.synthetic.main.dialog_service_types.*
import kotlinx.android.synthetic.main.dialog_service_types.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class ServiceTypesDialog() :DialogFragment(), ServiceTypeAdapter.ServiceTypeCallback {
    val viewModel: FillInfoViewModel by viewModel()
    val serviceTypeAdapter= ServiceTypeAdapter()
    var passData: PassData?=null
    var vehicleType:String?=""
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogStyle)
        vehicleType=arguments?.getString("vehicleType")

    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_service_types, container, false)



        vehicleType?.let { viewModel.getServiceTypes(it) }
        viewModel.serviceTypes.observe(viewLifecycleOwner){


            if (it.isSuccessful){
                view.rvServiceTypes.layoutManager= LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
                view.rvServiceTypes.adapter=serviceTypeAdapter
                serviceTypeAdapter.serviceTypeCallback=this
                serviceTypeAdapter.serviceTypes= it.body()?.data?.docs as ArrayList<Doc>
                dialogProgress.visibility=View.GONE
            }else if (!it.isSuccessful) {
                Toast.makeText(activity,"خطای اتصال به سرور",Toast.LENGTH_SHORT).show()
                dialogProgress.visibility=View.GONE
            }
        }





        return view
    }

    override fun onItemClicked(doc: Doc) {
        passData?.selectedItem(doc)
        dismiss()


    }

    interface PassData{
        fun selectedItem(doc: Doc)
    }

}