package com.example.movie_scout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class MenuFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        // Find the TextView for Sign Out
        TextView textSignOut = view.findViewById(R.id.sign_out);

        // Set click listener for sign out
        textSignOut.setOnClickListener(v -> {
            // Clear user session (e.g., SharedPreferences)
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userSession", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();  // Remove all saved data
            editor.apply();

            Toast.makeText(getActivity(), "Sign out successfully", Toast.LENGTH_SHORT).show();
            // Navigate to LoginActivity
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);

            // Finish the current activity to prevent going back to it
            getActivity().finish();
        });

        return view;

    }
}