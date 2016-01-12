package net.azurewebsites.sportywarsaw.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.azurewebsites.sportywarsaw.MyApplication;
import net.azurewebsites.sportywarsaw.R;
import net.azurewebsites.sportywarsaw.fragments.FriendsFragment;
import net.azurewebsites.sportywarsaw.fragments.MeetingsFragment;
import net.azurewebsites.sportywarsaw.fragments.SportsFacilitiesFragment;
import net.azurewebsites.sportywarsaw.infrastructure.CustomCallback;
import net.azurewebsites.sportywarsaw.models.UserPlusModel;
import net.azurewebsites.sportywarsaw.services.UserService;
import net.azurewebsites.sportywarsaw.utils.BitmapUtils;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Call;

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
    private ImageView profileImage;

    @Inject UserService userService;
    @Inject SharedPreferences preferences;
    private LinearLayout drawerHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.drawer_view);

        View headerView = navigationView.getHeaderView(0);
        drawerHeader = (LinearLayout) headerView.findViewById(R.id.drawer_header);

        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(drawerToggle);

        ((MyApplication) getApplication()).getServicesComponent().inject(this);

        usernameTextView = (TextView) navigationView.getHeaderView(0)
                .findViewById(R.id.drawer_username);
        usernameTextView.setText(preferences.getString("username", ""));

        profileImage = (ImageView) headerView.findViewById(R.id.profile_image);
        loadImage();

        setupDrawerNavigation();
        showInitialFragment();
    }

    private void loadImage() {
        Call<UserPlusModel> call = userService.getDetails(preferences.getString("username", ""));
        call.enqueue(new CustomCallback<UserPlusModel>(this) {
            @Override
            public void onSuccess(UserPlusModel model) {
                Bitmap bitmap = BitmapUtils.decodeBase64(model.getPicture());
                profileImage.setImageBitmap(bitmap);
            }
        });
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
        drawerHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                intent.putExtra("username", preferences.getString("username",""));
                startActivity(intent);
            }
        });
    }

    private void selectDrawerItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_log_out:
                logOut();
                break;
            case R.id.nav_meetings:
                switchFragment(MeetingsFragment.newInstance());
                break;
            case R.id.nav_sports_facilities:
                switchFragment(SportsFacilitiesFragment.newInstance());
                break;
            case R.id.nav_friends:
                switchFragment(FriendsFragment.newInstance());
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
                .addToBackStack(null)
                .commit();
    }

    private void showInitialFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_content, MeetingsFragment.newInstance())
                .disallowAddToBackStack()
                .commit();
    }
}
