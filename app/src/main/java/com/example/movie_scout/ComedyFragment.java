package com.example.movie_scout;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ComedyFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comedy, container, false);

        // Find TextView for Drama
        TextView textAction = view.findViewById(R.id.btn_action);
        TextView textHorror = view.findViewById(R.id.btn_horror);
        TextView textDrama = view.findViewById(R.id.btn_drama);

        // Set click listener to replace with DramaFragment
        textAction.setOnClickListener(v -> replaceFragment(new ActionFragment()));
        textHorror.setOnClickListener(v -> replaceFragment(new HorrorFragment()));
        textDrama.setOnClickListener(v -> replaceFragment(new DramaFragment()));
        return view;
    }
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);  // Replace with the ID of your FrameLayout container
        transaction.addToBackStack(null);  // Add to back stack so you can navigate back
        transaction.commit();
    }

}

