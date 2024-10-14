package com.example.movie_scout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavMenu extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout framelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bottom_nav);

        bottomNavigationView = findViewById(R.id.navigationMenu);
        framelayout = findViewById(R.id.frame_layout);

        // Load the HomeFragment initially
        loadFragment(new HomeFragment(), true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    selectedFragment = new HomeFragment();
                } else if (itemId == R.id.favorite) {
                    selectedFragment = new FavFragment();
                } else if (itemId == R.id.genre) {
                    selectedFragment = new GenreFragment();
                }else if (itemId == R.id.menu){
                    selectedFragment = new MenuFragment();
                }
                
                if (selectedFragment != null) {
                    loadFragment(selectedFragment, false);
                }
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment, boolean isAppInitialize) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isAppInitialize) {
            fragmentTransaction.add(R.id.frame_layout, fragment);
        } else {
            fragmentTransaction.replace(R.id.frame_layout, fragment);
        }

        fragmentTransaction.commit();
    }
}
