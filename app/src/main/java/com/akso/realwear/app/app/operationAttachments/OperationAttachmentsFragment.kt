package com.akso.realwear.app.app.operationAttachments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akso.realwear.R
import com.akso.realwear.app.app.attachments.AttachmentData
import com.akso.realwear.app.app.attachments.AttachmentItemAdapter
import com.akso.realwear.app.app.operationDetails.OperationDetailsActivity
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.ZEAMIWOOPERDOCUMENTSType
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.ZFIORI_EAM_APP_SRV_Entities
import com.sap.cloud.mobile.odata.ByteStream
import com.sap.cloud.mobile.odata.OnlineODataProvider
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.lang.RuntimeException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OperationAttachmentsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OperationAttachmentsFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var ACTIVITY: OperationDetailsActivity
    private var attachmentsList = ArrayList<AttachmentData>()
    private lateinit var dataService : ZFIORI_EAM_APP_SRV_Entities
    val provider = OnlineODataProvider("Test", "https://devapimgmnt-dev-test.cfapps.eu20.hana.ondemand.com/Test")
    lateinit var currentPhotoPath: String
    var maintOrder : String? = ""
    var operationNum : String? = ""
    var operationControlKey : String? = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ACTIVITY = context as OperationDetailsActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        val data = ACTIVITY.attachmentData
        Log.i("operationattach", data.toString())
        operationNum = ACTIVITY.operationNumber
        maintOrder = ACTIVITY.orderNumber
        operationControlKey = ACTIVITY.operationControlKey
        for (attachment in data) {
            attachmentsList.addAll(listOf(AttachmentData(attachment.title, attachment.createdBy, attachment.createdOn, attachment.url, attachment.maintenanceOrder)))

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataService = ZFIORI_EAM_APP_SRV_Entities(provider)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_operation_attachments, container, false)
        val recyclerView : RecyclerView = view.findViewById(R.id.operation_attachment_list)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = AttachmentItemAdapter(attachmentsList)

    return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnCamera : Button = view.findViewById(R.id.btn_open_cam_operation)

        btnCamera.setOnClickListener {
            dispatchTakePictureIntent()
        }

    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("ddMMyyyy").format(Date())
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "test", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private var photoFile: File? = null
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                // Create the File where the photo should go
                photoFile = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Log.d("ERROR", "An error occured")
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, 1337)
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            photoFile?.let {
                val imageBitmap: Bitmap = BitmapFactory.decodeFile(it.absolutePath)
                val stream = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                val imageBytes = stream.toByteArray()
                val content : ByteStream = ByteStream.fromBinary(imageBytes)
                val newAttachment = ZEAMIWOOPERDOCUMENTSType()
                val headers = provider.httpHeaders
                headers.set("SLUG", "${it.name};${maintOrder};${operationNum};${operationControlKey}")
                Log.i("headers", headers.toString())
                try {
                    dataService.createMedia(newAttachment, content, headers )
                } catch (err: RuntimeException) {
                    Log.d("createMedia_fail", err.toString())
                    Toast.makeText(view?.context, "Attachment upload failed.", Toast.LENGTH_SHORT).show()

                }
            }
        }

        Toast.makeText(view?.context, "Image saved.", Toast.LENGTH_SHORT).show()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OperationAttachmentsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OperationAttachmentsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}