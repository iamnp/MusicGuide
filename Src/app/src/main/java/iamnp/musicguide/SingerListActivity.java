package iamnp.musicguide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
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
import java.util.HashMap;
import java.util.List;

/**
 * An activity representing a list of Singers. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link SingerDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class SingerListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://cache-default06h.cdn.yandex.net/download.cdn.yandex.net/mobilization-2016/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private YandexSingersApi api = retrofit.create(YandexSingersApi.class);
    private RecyclerView recyclerView;
    private SingersDb singersDb;
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

        recyclerView = (RecyclerView)findViewById(R.id.singer_list);

        if (findViewById(R.id.singer_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                LoadDataIntoDb();
            }
        });
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
        ShowDataFromDb();
        LoadDataIntoDb();
    }

    private void ShowDataFromDb() {
        singers.clear();
        singers.addAll(singersDb.getAllSingers(0, 10));
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
                                    ShowDataFromDb();
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
                    if (mTwoPane) {
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
