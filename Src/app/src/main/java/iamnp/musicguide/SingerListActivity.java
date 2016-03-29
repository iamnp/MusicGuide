package iamnp.musicguide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

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

    private List<Singer> singers = new ArrayList<Singer>();
    private SimpleItemRecyclerViewAdapter adapter = new SimpleItemRecyclerViewAdapter(singers);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singer_list);

        singersDb = new SingersDb(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.singer_detail_container) != null) {
            twoPane = true;
        }

        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadDataIntoDb();
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.singer_list);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);

        ShowDataFromDb(null);
        LoadDataIntoDb();
    }

    private void ShowDataFromDb(List<Singer> s) {
        singers.clear();
        singers.addAll(s == null ? singersDb.getAllSingers() : s);
        adapter.notifyDataSetChanged();
    }

    private void LoadDataIntoDb() {
        api.getSingers().enqueue(new Callback<List<Singer>>() {
            @Override
            public void onResponse(Call<List<Singer>> call, final Response<List<Singer>> response) {
                new Thread() {
                    public void run() {
                        singersDb.deleteAllSingers();
                        for (Singer s : response.body()) {
                            singersDb.addSinger(s);
                        }
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
                swipeView.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.singer_list_menu, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextChange(String query) {
        // Here is where we are going to implement our filter logic
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Singer> mValues;

        public SimpleItemRecyclerViewAdapter(List<Singer> items) {
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
            return mValues.get(position).id;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mNameView.setText(mValues.get(position).name);
            holder.mGenresView.setText(mValues.get(position).GenresAsString());
            holder.mStatsView.setText(mValues.get(position).StatsAsString(SingerListActivity.this.getApplicationContext()));
            Picasso.with(SingerListActivity.this.getApplicationContext()).load(mValues.get(position).cover.small).into(holder.mImageView);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (twoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putLong(SingerDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        SingerDetailFragment fragment = new SingerDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.singer_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, SingerDetailActivity.class);
                        intent.putExtra(SingerDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
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
}
