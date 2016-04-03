package iamnp.musicguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Activity to display a fragment with singer detail info
 */
public class SingerDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singer_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Set appbar height to 1/3 of the screen height.
        AppBarLayout appbar = (AppBarLayout) findViewById(R.id.app_bar);
        float heightDp = getResources().getDisplayMetrics().heightPixels / 3;
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) appbar.getLayoutParams();
        lp.height = (int) heightDp;

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // No need to recreate fragment on device rotation, etc.
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putLong(SingerDetailFragment.ARG_ITEM_ID,
                    getIntent().getLongExtra(SingerDetailFragment.ARG_ITEM_ID, 0));
            SingerDetailFragment fragment = new SingerDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.singer_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent h = NavUtils.getParentActivityIntent(this);
            h.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            NavUtils.navigateUpTo(this, h);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
