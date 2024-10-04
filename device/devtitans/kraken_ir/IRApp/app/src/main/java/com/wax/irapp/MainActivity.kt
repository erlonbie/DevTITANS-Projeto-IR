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
import com.wax.irapp.databinding.ActivityMainBinding
import com.wax.irapp.models.IRCommand

import android.os.ServiceManager;
import android.os.IBinder;
import android.os.RemoteException;

import devtitans.smartir.ISmartIR;

class MainActivity : AppCompatActivity(), IRCommandAdapter.IRCommandClickListener, PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: IRCommandDatabase
    private lateinit var IBinder binder;
    private lateinit var ISmartIR service;
    lateinit var viewModel: IRCommandViewModel
    lateinit var adapter: IRCommandAdapter
    lateinit var selectedIRCommand: IRCommand

    private val updateIRCommand = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
        if (result.resultCode == Activity.RESULT_OK){
            val irCommand = result.data?.getSerializableExtra("command") as? IRCommand
            if (irCommand!= null){

                viewModel.updateCommand(irCommand)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initialize the UI
        initUI()

        viewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(IRCommandViewModel::class.java)

        viewModel.allCommands.observe(this) { list ->
            list?.let {
                adapter.updateList(list)
            }
        }

        database = IRCommandDatabase.getDatabase(this)

        //viewModel.insertCommand(IRCommand(1, "Command 1", "Code 1", ""))
    }

    private fun initUI() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(3, LinearLayout.VERTICAL)
        adapter = IRCommandAdapter(this,this)
        binding.recyclerView.adapter = adapter

        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK){

                val irCommand = result.data?.getSerializableExtra("command") as? IRCommand
                if (irCommand != null){
                    viewModel.insertCommand(irCommand)
                }
            }
        }

        binding.fbAddNote.setOnClickListener {
            val intent = Intent(this, AddIRCommandActivity::class.java)
            getContent.launch(intent)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!= null){
                    adapter.filterList(newText)
                }
                return true
            }

        })
    }

    override fun onItemClicked(irCommand: IRCommand) {
        val intent = Intent(this@MainActivity, AddIRCommandActivity::class.java)
        intent.putExtra("current_command", irCommand)
        updateIRCommand.launch(intent)

    }

    override fun onLongItemClicked(irCommand: IRCommand, cardView: CardView) {
        selectedIRCommand = irCommand
        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView: CardView) {
        val popup = PopupMenu(this,cardView)
        popup.setOnMenuItemClickListener(this@MainActivity)
        popup.inflate(R.menu.pop_up_menu)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {

        if (item?.itemId == R.id.delete_ircommand){
            viewModel.deleteCommand(selectedIRCommand)
            return true
        }
        return false
    }
}
