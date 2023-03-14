package com.akso.realwear.app.app.attachments
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akso.realwear.R
import com.akso.realwear.app.app.attachments.AttachmentItemAdapter

class AttachmentsActivity: AppCompatActivity() {
    private lateinit var attachmentItemAdapter: AttachmentItemAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_attachments)

        val recyclerView : RecyclerView = findViewById(R.id.attachment_list)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = attachmentItemAdapter






    }
}