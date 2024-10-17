package com.devtitans.KrakenIRapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.devtitans.KrakenIRapp.adapter.IRCommandAdapter;
import com.devtitans.KrakenIRapp.database.IRCommandDatabase;
import com.devtitans.KrakenIRapp.models.IRCommand;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import devtitans.smartirmanager.SmartIRManager; // Biblioteca do Manager

public class MainActivity extends AppCompatActivity implements IRCommandAdapter.IRCommandClickListener,
        PopupMenu.OnMenuItemClickListener {

    private static final String TAG = "KrakenIR.MainActivity";

    private IRCommandDatabase database;
    private IRCommandViewModel viewModel;
    private IRCommandAdapter adapter;
    private IRCommand selectedIRCommand;
    private SmartIRManager manager; // Atributo para o manager

    private final ActivityResultLauncher<Intent> updateIRCommand = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                Log.d(TAG, "Result returned from another activity.");
                if (result.getResultCode() == RESULT_OK) {
                    IRCommand irCommand = (IRCommand) result.getData().getSerializableExtra("command");
                    if (irCommand != null) {
                        Log.d(TAG, "Updating command: " + irCommand.getTitle());
                        viewModel.updateCommand(irCommand);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called, initializing UI.");
        setContentView(R.layout.activity_main);

        // Initialize the UI
        initUI();
        viewModel = new ViewModelProvider(this).get(IRCommandViewModel.class);
        manager = SmartIRManager.getInstance();

        viewModel.getAllCommands().observe(this, list -> {
            Log.d(TAG, "Observed change in command list.");
            if (list != null) {
                adapter.updateList(list);
            }
        });

        database = IRCommandDatabase.getDatabase(this);
        Log.d(TAG, "Database initialized.");
    }

    private void initUI() {
        Log.d(TAG, "Initializing UI components.");
        RecyclerView recyclerViewVar = findViewById(R.id.recycler_view);

        recyclerViewVar.setHasFixedSize(true);
        recyclerViewVar.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        adapter = new IRCommandAdapter(this, this);
        recyclerViewVar.setAdapter(adapter);

        ActivityResultLauncher<Intent> getContent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        IRCommand irCommand = (IRCommand) result.getData().getSerializableExtra("command");
                        if (irCommand != null) {
                            Log.d(TAG, "Inserting new command: " + irCommand.getTitle());
                            viewModel.insertCommand(irCommand);
                        }
                    }
                }
        );

        FloatingActionButton fbAddNote = findViewById(R.id.fb_add_note);
        fbAddNote.setOnClickListener(v -> {
            Log.d(TAG, "Floating action button clicked, launching AddIRCommandActivity.");
            Intent intent = new Intent(MainActivity.this, AddIRCommandActivity.class);
            getContent.launch(intent);
        });

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "Search query submitted: " + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "Search query text changed: " + newText);
                if (newText != null) {
                    adapter.filterList(newText);
                }
                return true;
            }
        });
    }

    @Override
    public void onItemClicked(IRCommand irCommand) {
        Log.d(TAG, "Item clicked: " + irCommand.getTitle() + ", launching AddIRCommandActivity.");
        Intent intent = new Intent(MainActivity.this, AddIRCommandActivity.class);
        intent.putExtra("current_command", irCommand);
        updateIRCommand.launch(intent);
    }

    @Override
    public void onLongItemClicked(IRCommand irCommand, CardView cardView) {
        Log.d(TAG, "Long item click detected on: " + irCommand.getTitle() + ", displaying popup menu.");
        selectedIRCommand = irCommand;
        popUpDisplay(cardView);
    }

    @Override
    public void onLongItemClicked(IRCommand irCommand, View cardView) {
        Log.d(TAG, "Long item click detected on: " + irCommand.getTitle() + ", displaying popup menu.");
        selectedIRCommand = irCommand;
        popUpDisplay(cardView);
    }

    private void popUpDisplay(View cardView) {
        Log.d(TAG, "Displaying popup menu.");
        PopupMenu popup = new PopupMenu(this, cardView);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.pop_up_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.delete_ircommand) {
            Log.d(TAG, "Delete option selected for command: " + selectedIRCommand.getTitle());
            viewModel.deleteCommand(selectedIRCommand);
            return true;
        }
        return false;
    }
}
