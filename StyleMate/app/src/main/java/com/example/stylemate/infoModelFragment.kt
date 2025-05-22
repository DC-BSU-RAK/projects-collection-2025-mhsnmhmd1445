package com.example.stylemate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class InfoModelFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_info_model, container, false)

        // Get references to views
        val textInfoTips = view.findViewById<TextView>(R.id.text_info_tips)
        val dismissButton = view.findViewById<Button>(R.id.btn_got_it)

        // Populate tips text with all tip strings
        textInfoTips.text = getString(R.string.info_tip_1) + "\n" +
                getString(R.string.info_tip_2) + "\n" +
                getString(R.string.info_tip_3) + "\n" +
                getString(R.string.info_tip_4) + "\n" +
                getString(R.string.info_tip_5)

        // Set up dismiss button
        dismissButton.setOnClickListener {
            dismiss()
        }

        return view
    }

    companion object {
        const val TAG = "InfoModalFragment"

        fun newInstance(): InfoModelFragment {
            return InfoModelFragment()
        }
    }
}