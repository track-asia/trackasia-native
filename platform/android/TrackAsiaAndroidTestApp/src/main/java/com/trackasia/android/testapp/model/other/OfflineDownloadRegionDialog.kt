package com.trackasia.android.testapp.model.other

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.trackasia.android.testapp.R
import timber.log.Timber

class OfflineDownloadRegionDialog : DialogFragment() {
    interface DownloadRegionDialogListener {
        fun onDownloadRegionDialogPositiveClick(regionName: String?)
    }

    var listener: DownloadRegionDialogListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? DownloadRegionDialogListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder =
            AlertDialog.Builder(
                requireActivity(),
            )

        // Let the user choose a name for the region
        val regionNameEdit = EditText(activity)
        builder
            .setTitle("Choose a name for the region")
            .setIcon(R.drawable.ic_airplanemode_active_black)
            .setView(regionNameEdit)
            .setPositiveButton("Start") { dialog: DialogInterface?, which: Int ->
                val regionName = regionNameEdit.text.toString()
                listener?.onDownloadRegionDialogPositiveClick(regionName)
            }.setNegativeButton("Cancel") { dialog: DialogInterface?, which: Int -> Timber.d("Download cancelled.") }
        return builder.create()
    }
}
