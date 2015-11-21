package com.byteshaft.auction;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.byteshaft.auction.fragments.CategoriesFragment;
import com.byteshaft.auction.fragments.UserSettingFragment;
import com.byteshaft.auction.fragments.buyer.Buyer;
import com.byteshaft.auction.fragments.seller.Seller;
import com.byteshaft.auction.login.LoginActivity;
import com.byteshaft.auction.login.RegisterActivity;
import com.byteshaft.auction.utils.Helpers;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static boolean isLastFragmentAvailable = false;
    public static Button loginButton;
    public static Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isLastFragmentAvailable = true;
        if (Helpers.isUserLoggedIn()) {
            isLastFragmentAvailable = true;
            if (!Helpers.getLastFragment().equals("")) {
                if (Helpers.getLastFragment().contains("Buyer")) {
                    loadFragment(new Buyer());
                } else {
                    loadFragment(new Seller());
                }
            }
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        loginButton = (Button) findViewById(R.id.login_button);
        registerButton = (Button) findViewById(R.id.register_button);
        if (isLastFragmentAvailable) {
            loginButton.setVisibility(View.INVISIBLE);
            registerButton.setVisibility(View.INVISIBLE);
            loginButton.setEnabled(false);
            registerButton.setEnabled(false);
        }
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLastFragmentAvailable) {
            loginButton.setVisibility(View.INVISIBLE);
            registerButton.setVisibility(View.INVISIBLE);
            loginButton.setEnabled(false);
            registerButton.setEnabled(false);
        }
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.container, fragment);
        tx.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.nav_buyer:
                fragmentClass = Buyer.class;
                break;
            case R.id.nav_seller:
                fragmentClass = Seller.class;
                break;
            case R.id.nav_categories:
                fragmentClass = CategoriesFragment.class;
                break;
            case R.id.nav_user:
                fragmentClass = UserSettingFragment.class;
                break;
            default:
                fragmentClass = Buyer.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        menuItem.setCheckable(true);
        setTitle(menuItem.getTitle());
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//         if (!Helpers.isUserLoggedIn()) {
//         drawer.closeDrawer(GravityCompat.START);
//         Toast.makeText(getApplicationContext(), "please login or register first",
//                 Toast.LENGTH_SHORT).show();
//         return false;
//         }
        int id = item.getItemId();
        selectDrawerItem(item);
        drawer.closeDrawer(GravityCompat.START);
        loginButton.setVisibility(View.INVISIBLE);
        registerButton.setVisibility(View.INVISIBLE);
        loginButton.setEnabled(false);
        registerButton.setEnabled(false);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));//
                break;
            case R.id.register_button:
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
//
                break;
        }
    }
}