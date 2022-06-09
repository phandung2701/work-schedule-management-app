package com.example.btl_nhom4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;


import com.example.btl_nhom4.adapter.ViewPager2AdapterWorkspace;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WorkspaceActivityAdmin extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    ViewPager2 viewPager2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace);

        bottomNavigation = findViewById(R.id.bottom_nav_workspace);
        viewPager2 = findViewById(R.id.view_pager2_workspace);
        // set adapter cho viewpager2
        ViewPager2AdapterWorkspace adapter = new ViewPager2AdapterWorkspace(this);
        viewPager2.setAdapter(adapter);

        //khi vuốt qua lại giữa các fragment thì các tab bottom navigation thay đổi tương ứng
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigation.getMenu().findItem(R.id.bottom_nav_workspace_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigation.getMenu().findItem(R.id.bottom_nav_workspace_project).setChecked(true);
                        break;
                    case 2:
                        bottomNavigation.getMenu().findItem(R.id.bottom_nav_workspace_workplace).setChecked(true);
                        break;
                    case 3:
                        bottomNavigation.getMenu().findItem(R.id.bottom_nav_workspace_add).setChecked(true);
                        break;
                }
            }
        });

        // khi click vào item tab bottom navigation thì call fragment tương ứng
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_nav_workspace_home:
                        viewPager2.setCurrentItem(0);
                        break;
                    case R.id.bottom_nav_workspace_project:
                        viewPager2.setCurrentItem(1);
                        break;
                    case R.id.bottom_nav_workspace_workplace:
                        viewPager2.setCurrentItem(2);
                        break;
                    case R.id.bottom_nav_workspace_add:
                        viewPager2.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });

    }
}