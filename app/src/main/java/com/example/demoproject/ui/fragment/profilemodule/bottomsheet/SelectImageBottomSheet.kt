package logi.retail.ui.setupstore.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.demoproject.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectImageBottomSheet(var title: String, var imageNavigator: ImageNavigator) :
    BottomSheetDialogFragment() {
    private lateinit var txtTitle: TextView
    var itemView: View? = null
    var layoutCaptureImage: ConstraintLayout? = null
    var layoutGallery: ConstraintLayout? = null

    var width = 0
    override fun getTheme(): Int {
        super.getTheme()
        return R.style.BottomSheetDialogTheme
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        itemView = inflater.inflate(R.layout.layout_store_bottom_sheet, container, false)
        initView()
        return itemView
    }

    private fun initView() {
        txtTitle = itemView!!.findViewById(R.id.txtTitle)
        layoutCaptureImage = itemView!!.findViewById(R.id.layoutCaptureImage)
        layoutGallery = itemView!!.findViewById(R.id.layoutGallery)

        txtTitle.setText(title)

        layoutCaptureImage?.setOnClickListener { v: View? ->
            imageNavigator.clickOnCamera()
            dismiss()
        }
        layoutGallery?.setOnClickListener { v: View? ->
            imageNavigator.clickOnGallery()
            dismiss()
        }
    }


}