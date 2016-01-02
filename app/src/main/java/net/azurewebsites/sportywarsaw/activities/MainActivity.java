package net.azurewebsites.sportywarsaw.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import net.azurewebsites.sportywarsaw.MyApplication;
import net.azurewebsites.sportywarsaw.R;
import net.azurewebsites.sportywarsaw.fragments.MeetingsFragment;
import net.azurewebsites.sportywarsaw.fragments.StartupFragment;

import javax.inject.Inject;

/**
 * Main activity of the application
 *
 * @author Marcin Chudy
 */
public class MainActivity extends AppCompatActivity{

    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private TextView usernameTextView;

    @Inject SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.drawer_view);

        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(drawerToggle);

        ((MyApplication) getApplication()).getServicesComponent().inject(this);

        usernameTextView = (TextView) navigationView.getHeaderView(0)
                .findViewById(R.id.drawer_username);
        usernameTextView.setText(preferences.getString("username", ""));

        setupDrawerNavigation();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_content, new StartupFragment())
                .disallowAddToBackStack()
                .commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerNavigation() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }

    private void selectDrawerItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_log_out:
                logOut();
                break;
            case R.id.nav_meetings:
                switchFragment(MeetingsFragment.newInstance(1));
                break;
            case R.id.nav_first_fragment:
                switchFragment(StartupFragment.newInstance());
                break;
        }

        item.setChecked(true);
        setTitle(item.getTitle());
        drawer.closeDrawers();
    }

    private void logOut() {
        preferences.edit()
            .remove(LoginActivity.USERNAME_KEY)
            .remove(LoginActivity.ACCESS_TOKEN_KEY)
            .apply();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void switchFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content, fragment)
                .commit();
    }
}
