package com.devtitans.KrakenIRapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.devtitans.KrakenIRapp.R;
import com.devtitans.KrakenIRapp.models.IRCommand;

import devtitans.smartirmanager.SmartIRManager;

import java.util.ArrayList;
import java.util.List;

public class IRCommandAdapter extends RecyclerView.Adapter<IRCommandAdapter.IRCommandViewHolder> {

    private static final String TAG = "KrakenIR.IRCommandAdapter";

    private final Context context;
    private final IRCommandClickListener listener;
    private final ArrayList<IRCommand> irCommandList = new ArrayList<>();
    private final ArrayList<IRCommand> fullList = new ArrayList<>();

    private SmartIRManager manager;

    public IRCommandAdapter(Context context, IRCommandClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public IRCommandViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "Creating view holder.");
        manager = SmartIRManager.getInstance();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new IRCommandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IRCommandViewHolder holder, int position) {
        IRCommand currentIRCommand = irCommandList.get(position);

        Log.d(TAG, "Binding view holder for command: " + currentIRCommand.getTitle());

        holder.title.setText(currentIRCommand.getTitle());
        holder.title.setSelected(true);

        holder.date.setText(currentIRCommand.getDate());
        holder.date.setSelected(true);

        holder.irCommandLayout.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.grey, null));

        holder.irCommandLayout.setOnClickListener(v -> listener.onItemClicked(irCommandList.get(holder.getAdapterPosition())));

        holder.irCommandLayout.setOnLongClickListener(v -> {
            listener.onLongItemClicked(irCommandList.get(holder.getAdapterPosition()), holder.irCommandLayout);
            return true;
        });

        holder.button.setOnClickListener(v -> {
            manager.set_transmit(currentIRCommand.getCode());
            Toast.makeText(context, currentIRCommand.getTitle() + " Enviado com Sucesso", Toast.LENGTH_SHORT).show();
        });
    }

    public void updateList(List<IRCommand> newList) {
        Log.d(TAG, "Updating command list with " + newList.size() + " items.");

        fullList.clear();
        fullList.addAll(newList);

        irCommandList.clear();
        irCommandList.addAll(fullList);

        notifyDataSetChanged();
    }

    public void filterList(String search) {
        irCommandList.clear();

        for (IRCommand item : fullList) {
            if (item.getTitle() != null && item.getTitle().toLowerCase().contains(search.toLowerCase())) {
                irCommandList.add(item);
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return irCommandList.size();
    }

    public static class IRCommandViewHolder extends RecyclerView.ViewHolder {
        CardView irCommandLayout;
        TextView title;
        ImageButton button;
        TextView date;

        public IRCommandViewHolder(View itemView) {
            super(itemView);
            irCommandLayout = itemView.findViewById(R.id.card_layout);
            title = itemView.findViewById(R.id.tv_title);
            button = itemView.findViewById(R.id.btn_send_command);
            date = itemView.findViewById(R.id.tv_date);
        }
    }

    public interface IRCommandClickListener {
        void onItemClicked(IRCommand irCommand);

        void onLongItemClicked(IRCommand irCommand, CardView cardView);

        void onLongItemClicked(IRCommand irCommand, View cardView);
    }
}
