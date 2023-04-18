package com.akso.realwear.app.app.attachments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Camera
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akso.realwear.R
import com.akso.realwear.app.app.callAPI.ApiInterface
import com.akso.realwear.app.app.callAPI.RestApiService
import com.akso.realwear.app.app.orderDetails.WorkOrderDetailsActivity
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.WorkOrderDetail
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.ZEAMIWOATTACHMENTSHOWType
import com.sap.cloud.android.odata.zfiori_eam_app_srv_entities.ZFIORI_EAM_APP_SRV_Entities
import com.sap.cloud.mobile.foundation.common.ClientProvider
import com.sap.cloud.mobile.foundation.networking.CsrfTokenInterceptor
import com.sap.cloud.mobile.odata.ByteStream
import com.sap.cloud.mobile.odata.OnlineODataProvider
import com.sap.cloud.mobile.odata.Property
import com.sap.cloud.mobile.odata.RequestOptions
import com.sap.cloud.mobile.odata.http.HttpHeaders
import com.sap.cloud.mobile.odata.http.OKHttpHandler
import okhttp3.*
import okhttp3.RequestBody.Companion.asRequestBody
import okio.ByteString.Companion.toByteString
import java.io.*
import java.lang.RuntimeException
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.seconds


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AttachmentsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AttachmentsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var ACTIVITY: WorkOrderDetailsActivity
    private var attachmentsList = ArrayList<AttachmentData>()
    private lateinit var dataService : ZFIORI_EAM_APP_SRV_Entities
    lateinit var currentPhotoPath: String
    val provider = OnlineODataProvider("Test", "https://devapimgmnt-dev-test.cfapps.eu20.hana.ondemand.com/Test")
    var maintOrder = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ACTIVITY = context as WorkOrderDetailsActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        
        val data = ACTIVITY.attachmentData

        if(data.size == 0) {
           val noAttachments = view?.findViewById<TextView>(R.id.no_work_order_attachments)
           noAttachments?.visibility = View.VISIBLE
        }
        
        for(attachment in data) {
            attachmentsList.add(attachment)
            maintOrder = attachment.maintenanceOrder
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//         Inflate the layout for this fragment


        provider.serviceOptions.checkVersion = false
        provider.serviceOptions.requiresType = false
        provider.serviceOptions.cacheMetadata = false
        provider.networkOptions.allowTunneling = true
        dataService = ZFIORI_EAM_APP_SRV_Entities(provider)

        val view = inflater.inflate(R.layout.fragment_attachments, container, false)
        val recyclerView : RecyclerView = view.findViewById(R.id.attachment_list)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = AttachmentItemAdapter(attachmentsList)

       return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnCamera : Button = view.findViewById(R.id.btn_open_cam)

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
                val newAttachment = ZEAMIWOATTACHMENTSHOWType()
                val headers = provider.httpHeaders
                headers.set("SLUG", "${it.name};${maintOrder}")

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
         * @return A new instance of fragment AttachmentsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AttachmentsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}