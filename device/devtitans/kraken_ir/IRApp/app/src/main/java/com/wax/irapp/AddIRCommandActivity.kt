package com.wax.irapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.wax.irapp.databinding.ActivityAddIrCommandBinding
import com.wax.irapp.models.IRCommand
import java.text.SimpleDateFormat
import java.util.Date

class AddIRCommandActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddIrCommandBinding

    private lateinit var iRCommand: IRCommand
    private lateinit var oldIRCommand: IRCommand
    private var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddIrCommandBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loadingSpinner.visibility = android.view.View.VISIBLE
        binding.tvCommandRegistered.visibility = android.view.View.GONE

        try {
            oldIRCommand = intent.getSerializableExtra("current_command") as IRCommand
            binding.etTilte.setText(oldIRCommand.title)
            binding.loadingSpinner.visibility = android.view.View.GONE
            binding.tvCommandRegistered.visibility = android.view.View.VISIBLE
            isUpdate = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.imgCheck.setOnClickListener {
            val title = binding.etTilte.text.toString()

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

        binding.imgBackArrow.setOnClickListener {
            onBackPressed()
        }
    }
}