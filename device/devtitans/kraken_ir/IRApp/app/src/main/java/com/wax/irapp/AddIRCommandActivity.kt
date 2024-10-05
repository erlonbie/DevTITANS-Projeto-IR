package com.wax.irapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
//import com.wax.irapp.databinding.ActivityAddIrCommandBinding
import com.wax.irapp.models.IRCommand
import java.text.SimpleDateFormat
import java.util.Date

class AddIRCommandActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "KrakenIR.AddIRCommandActivity"
    }

    //private lateinit var binding: ActivityAddIrCommandBinding

    private lateinit var iRCommand: IRCommand
    private lateinit var oldIRCommand: IRCommand
    private var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called, initializing UI.")
        setContentView(R.layout.activity_add_ir_command)

        val loadingSpinner: ProgressBar = findViewById(R.id.loading_spinner)
        loadingSpinner.visibility = android.view.View.VISIBLE
        Log.d(TAG, "Loading spinner set to visible.")

        val tvCommandRegistered: TextView = findViewById(R.id.tv_command_registered)
        tvCommandRegistered.visibility = android.view.View.GONE
        Log.d(TAG, "Text view for command registration hidden.")

        val etTitle : TextView = findViewById(R.id.et_tilte)
        try {
            oldIRCommand = intent.getSerializableExtra("current_command") as IRCommand
            etTitle.setText(oldIRCommand.title)
            Log.d(TAG, "Updating existing command: ${oldIRCommand.title}")
            loadingSpinner.visibility = android.view.View.GONE
            tvCommandRegistered.visibility = android.view.View.VISIBLE
            isUpdate = true
        } catch (e: Exception) {
            Log.e(TAG, "No existing command found. Exception: ${e.message}")
            e.printStackTrace()
        }

        val imgCheck: ImageView = findViewById(R.id.img_check)
        imgCheck.setOnClickListener {
            val title = etTitle.text.toString()
            Log.d(TAG, "Check button clicked, title: $title")

            if (title.isNotEmpty()) {
                val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm a")
                val formattedDate = formatter.format(Date())

                if (isUpdate) {
                    iRCommand = IRCommand(
                        oldIRCommand.id, title, code = "123", formattedDate
                    )
                    Log.d(TAG, "Command updated: $title, date: $formattedDate")
                } else {
                    iRCommand = IRCommand(
                        null, title, code = "123", formattedDate
                    )
                    Log.d(TAG, "New command created: $title, date: $formattedDate")
                }

                val intent = Intent()
                intent.putExtra("command", iRCommand)
                setResult(Activity.RESULT_OK, intent)
                Log.d(TAG, "Command set in result intent, finishing activity.")
                finish()
            } else {
                Log.d(TAG, "Title is empty, showing Toast message.")
                Toast.makeText(
                    this@AddIRCommandActivity,
                    "Please enter some data",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
        }

        val imgBackArrow : ImageView = findViewById(R.id.img_back_arrow)
        imgBackArrow.setOnClickListener {
            Log.d(TAG, "Back arrow clicked, calling onBackPressed.")
            onBackPressed()
        }
    }
}