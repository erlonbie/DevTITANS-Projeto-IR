package com.wax.irapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.wax.irapp.adapter.IRCommandAdapter
import com.wax.irapp.database.IRCommandDatabase
//import com.wax.irapp.databinding.ActivityMainBinding
import com.wax.irapp.models.IRCommand

import android.os.ServiceManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

import devtitans.smartir.ISmartIR;

class MainActivity : AppCompatActivity(), IRCommandAdapter.IRCommandClickListener,
    PopupMenu.OnMenuItemClickListener {

    companion object {
        private const val TAG = "KrakenIR.MainActivity"
    }

    //private lateinit var binding: ActivityMainBinding
    private lateinit var database: IRCommandDatabase
    private lateinit var binder: IBinder;
    private lateinit var service: ISmartIR;
    lateinit var viewModel: IRCommandViewModel
    lateinit var adapter: IRCommandAdapter
    lateinit var selectedIRCommand: IRCommand

    private val updateIRCommand =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d(TAG, "Result returned from another activity.")
            if (result.resultCode == Activity.RESULT_OK) {
                val irCommand = result.data?.getSerializableExtra("command") as? IRCommand
                if (irCommand != null) {
                    Log.d(TAG, "Updating command: ${irCommand.title}")
                    viewModel.updateCommand(irCommand)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called, initializing UI.")
        setContentView(R.layout.activity_main)

        // Initialize the UI
        initUI()

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )
            .get(IRCommandViewModel::class.java)

        viewModel.allCommands.observe(this) { list ->
            Log.d(TAG, "Observed change in command list.")
            list?.let {
                adapter.updateList(list)
            }
        }

        database = IRCommandDatabase.getDatabase(this)
        Log.d(TAG, "Database initialized.")
    }

    private fun initUI() {
        Log.d(TAG, "Initializing UI components.")
        val recyclerViewVar: RecyclerView = findViewById(R.id.recycler_view)

        recyclerViewVar.setHasFixedSize(true)
        recyclerViewVar.layoutManager = StaggeredGridLayoutManager(3, LinearLayout.VERTICAL)
        adapter = IRCommandAdapter(this, this)
        recyclerViewVar.adapter = adapter

        val getContent =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {

                    val irCommand = result.data?.getSerializableExtra("command") as? IRCommand
                    if (irCommand != null) {
                        Log.d(TAG, "Inserting new command: ${irCommand.title}")
                        viewModel.insertCommand(irCommand)
                    }
                }
            }
        val fbAddNote: FloatingActionButton = findViewById(R.id.fb_add_note)
        fbAddNote.setOnClickListener {
            Log.d(TAG, "Floating action button clicked, launching AddIRCommandActivity.")
            val intent = Intent(this, AddIRCommandActivity::class.java)
            getContent.launch(intent)
        }

        val searchView: SearchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                Log.d(TAG, "Search query submitted: $p0")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG, "Search query text changed: $newText")
                if (newText != null) {
                    adapter.filterList(newText)
                }
                return true
            }

        })
    }

    override fun onItemClicked(irCommand: IRCommand) {
        Log.d(TAG, "Item clicked: ${irCommand.title}, launching AddIRCommandActivity.")
        val intent = Intent(this@MainActivity, AddIRCommandActivity::class.java)
        intent.putExtra("current_command", irCommand)
        updateIRCommand.launch(intent)

    }

    override fun onLongItemClicked(irCommand: IRCommand, cardView: CardView) {
        Log.d(TAG, "Long item click detected on: ${irCommand.title}, displaying popup menu.")
        selectedIRCommand = irCommand
        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView: CardView) {
        Log.d(TAG, "Displaying popup menu.")
        val popup = PopupMenu(this, cardView)
        popup.setOnMenuItemClickListener(this@MainActivity)
        popup.inflate(R.menu.pop_up_menu)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {

        if (item?.itemId == R.id.delete_ircommand) {
            Log.d(TAG, "Delete option selected for command: ${selectedIRCommand.title}")
            viewModel.deleteCommand(selectedIRCommand)
            return true
        }
        return false
    }
}
