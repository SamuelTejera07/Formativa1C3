package com.example.apiservices;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PlayerAdapter extends ArrayAdapter<Player> {

    public PlayerAdapter(Context context, ArrayList<Player> players) {
        super(context, 0, players);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Player player = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_player, parent, false);
        }

        TextView tvPlayerName = convertView.findViewById(R.id.tvPlayerName);
        TextView tvPlayerPosition = convertView.findViewById(R.id.tvPlayerPosition);
        TextView tvPlayerNumber = convertView.findViewById(R.id.tvPlayerNumber);

        if (player != null) {
            tvPlayerName.setText(player.getName());
            tvPlayerPosition.setText(player.getPosition());
            tvPlayerNumber.setText("Número: " + (player.getShirtNumber().equals("N/A") ? "N/A" : player.getShirtNumber()));
        }

        return convertView;
    }
} 