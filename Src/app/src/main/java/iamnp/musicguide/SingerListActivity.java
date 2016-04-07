package iamnp.musicguide;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Main activity with singers list
 */
public class SingerListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private boolean twoPane;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://cache-default06h.cdn.yandex.net/download.cdn.yandex.net/mobilization-2016/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private YandexSingersApi api = retrofit.create(YandexSingersApi.class);
    private SingersDb singersDb;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeView;
    private CoordinatorLayout coordinatorLayout;

    private String currentQuery;

    // Singers list bound to RecyclerView adapter (e.g. filtered singers list)
    private List<Singer> boundSingers = new ArrayList<Singer>();
    // Singer list with all singers
    private List<Singer> allSingers = new ArrayList<Singer>();
    private SimpleItemRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singer_list);

        singersDb = new SingersDb(this);

        ((FloatingActionButton) findViewById(R.id.fab)).hide();

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // if layout has singer_detail_container than we are in two pane mode
        if (findViewById(R.id.singer_detail_container) != null) {
            twoPane = true;
        }

        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);
        // Load singers data from web on swipe-to-refresh
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadDataIntoDb();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.singer_list);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));

        adapter = new SimpleItemRecyclerViewAdapter(boundSingers);
        recyclerView.setAdapter(adapter);

        ShowDataFromDb(null);
        LoadDataIntoDb();
    }

    /**
     * Filter and show singers in recycler view
     * @param s list of singers to populate or null to load it from db
     */
    private void ShowDataFromDb(List<Singer> s) {
        allSingers = s == null ? singersDb.getAllSingers() : s;
        ShowFilteredSingers();
    }

    /**
     * Filters singers and shows filtered list in recycler view
     */
    private void ShowFilteredSingers() {
        boundSingers.clear();
        String q = currentQuery == null ? null : currentQuery.toLowerCase();
        for (int i = 0; i < allSingers.size(); ++i) {
            if (q == null || allSingers.get(i).name.toLowerCase().contains(q) || allSingers.get(i).genresAsString().toLowerCase().contains(q)) {
                boundSingers.add(allSingers.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Loads singers data from web, saves it to db and shows it
     */
    private void LoadDataIntoDb() {
        // Use swipeView.post in case LoadDataIntoDb() is called from onCreate()
        if (boundSingers.size() == 0) {
            swipeView.post(new Runnable() {
                @Override
                public void run() {
                    swipeView.setRefreshing(true);
                }
            });
        }
        api.getSingers().enqueue(new Callback<List<Singer>>() {
            @Override
            public void onResponse(Call<List<Singer>> call, final Response<List<Singer>> response) {
                new Thread() {
                    public void run() {
                        // Add singers to db on background thread
                        singersDb.deleteAllSingers();
                        for (Singer s : response.body()) {
                            singersDb.addSinger(s);
                        }
                        // Show singers data on main thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                swipeView.setRefreshing(false);
                                ShowDataFromDb(response.body());
                            }
                        });
                    }
                }.start();
            }

            @Override
            public void onFailure(Call<List<Singer>> call, Throwable t) {
                // Show snackbar with "Retry" button if request failed
                Snackbar.make(coordinatorLayout, getResources().getString(R.string.network_error), Snackbar.LENGTH_LONG)
                        .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                LoadDataIntoDb();
                            }
                        }).show();
                swipeView.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflates menu (with search view)
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.singer_list_menu, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextChange(String query) {
        // Store current search query and re-filter items
        currentQuery = query;
        ShowFilteredSingers();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * RecyclerView adapter class
     */
    public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Singer> mValues;

        public SimpleItemRecyclerViewAdapter(List<Singer> items) {
            // Use stable ids for better performance
            setHasStableIds(true);
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.singer_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public long getItemId(int position) {
            // Return item's stable id
            return mValues.get(position).id;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mNameView.setText(mValues.get(position).name);
            holder.mGenresView.setText(mValues.get(position).genresAsString());
            holder.mStatsView.setText(mValues.get(position).statsAsString(SingerListActivity.this.getApplicationContext()));
            Picasso.with(SingerListActivity.this.getApplicationContext()).load(mValues.get(position).cover.small).into(holder.mImageView);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (twoPane) {
                        // Add fragment to the detail pane
                        Bundle arguments = new Bundle();
                        arguments.putLong(SingerDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        SingerDetailFragment fragment = new SingerDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.singer_detail_container, fragment)
                                .commit();
                    } else {
                        // Launch detail activity
                        Context context = v.getContext();
                        Intent intent = new Intent(context, SingerDetailActivity.class);
                        intent.putExtra(SingerDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        // Launch activity with cool animation on Lollipop and higher
                        ActivityCompat.startActivity(SingerListActivity.this, intent, ActivityOptionsCompat.makeSceneTransitionAnimation(SingerListActivity.this).toBundle());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mNameView;
            public final TextView mGenresView;
            public final TextView mStatsView;
            public final ImageView mImageView;
            public Singer mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mNameView = (TextView) view.findViewById(R.id.nameTextView);
                mGenresView = (TextView) view.findViewById(R.id.genresTextView);
                mStatsView = (TextView) view.findViewById(R.id.statsTextView);
                mImageView = (ImageView) view.findViewById(R.id.imageView);
            }
        }
    }

    /**
     * RecyclerView Divider class
     */
    public class DividerItemDecoration extends RecyclerView.ItemDecoration {

        private Drawable mDivider;

        public DividerItemDecoration(Context context) {
            final TypedArray styledAttributes = context.obtainStyledAttributes(new int[]{android.R.attr.listDivider});
            mDivider = styledAttributes.getDrawable(0);
            styledAttributes.recycle();
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
}
