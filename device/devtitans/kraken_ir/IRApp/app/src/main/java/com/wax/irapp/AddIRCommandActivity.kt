package com.wax.irapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
    //private lateinit var binding: ActivityAddIrCommandBinding

    private lateinit var iRCommand: IRCommand
    private lateinit var oldIRCommand: IRCommand
    private var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ir_command)
        val loadingSpinner : ProgressBar = findViewById(R.id.loading_spinner)
        loadingSpinner.visibility = android.view.View.VISIBLE
        val tvCommandRegistered : TextView = findViewById(R.id.tv_command_registered)
        tvCommandRegistered.visibility = android.view.View.GONE

        val etTilte : TextView = findViewById(R.id.et_tilte)
        try {
            oldIRCommand = intent.getSerializableExtra("current_command") as IRCommand
            etTilte.setText(oldIRCommand.title)
            loadingSpinner.visibility = android.view.View.GONE
            tvCommandRegistered.visibility = android.view.View.VISIBLE
            isUpdate = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val imgCheck : ImageView = findViewById(R.id.img_check)
        imgCheck.setOnClickListener {
            val title = etTilte.text.toString()

            if (title.isNotEmpty()) {
                val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm a")

                if (isUpdate) {
                    iRCommand = IRCommand(
                        oldIRCommand.id, title, code = "123", formatter.format(Date())
                    )
                } else {
                    iRCommand = IRCommand(
                        null, title, code = "123", formatter.format(Date())
                    )
                }

                val intent = Intent()
                intent.putExtra("command", iRCommand)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
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
            onBackPressed()
        }
    }
}