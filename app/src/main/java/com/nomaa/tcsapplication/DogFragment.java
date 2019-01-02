package com.nomaa.tcsapplication;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.nomaa.tcsapplication.Utilities.DogJsonUtils;
import com.nomaa.tcsapplication.Utilities.NetworkUtils;
import com.nomaa.tcsapplication.data.DogContract;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;

import static com.nomaa.tcsapplication.MainActivity.projection;

public class DogFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String DATA_INSERT_FINISHED = "data_insert_finished";

    Context mViewContext;
    RecyclerView mRecyclerView;
    public DogRecyclerViewAdapter mAdapter;
    ConstraintLayout progressBar;

    public static final int DOG_LOADER_ID = 105;

    public DogFragment() {
    }

    public static DogFragment newInstance() {
        DogFragment fragment = new DogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity) getContext()).getSupportLoaderManager().initLoader(DOG_LOADER_ID, null, this);
    }

    void restartLoader() {
        ((MainActivity) getContext()).getSupportLoaderManager().restartLoader(DOG_LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dog_list, container, false);

        mViewContext = view.getContext();
        progressBar = view.findViewById(R.id.progress_layout);

        mRecyclerView = view.findViewById(R.id.list_dogs);
        mAdapter = new DogRecyclerViewAdapter(getContext());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mViewContext));
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new CursorLoader(this.getContext(), DogContract.DogEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

        mRecyclerView.scrollToPosition(0);

        if (cursor == null || cursor.getCount() == 0) {
            new DogsTask(new WeakReference<>(this.getContext())).execute();
        } else {
            mAdapter.swapCursor(cursor);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        mAdapter.swapCursor(null);
    }

    private static class DogsTask extends AsyncTask<Void, Void, String> {

        private WeakReference<Context> context;

        DogsTask(WeakReference<Context> context) {
            super();

            this.context = context;
        }

        @Override
        protected String doInBackground(Void... voids) {

            URL url = NetworkUtils.getDogUrl();

            if (url == null || "".equals(url.toString())) {
                return null;//if url is null, return
            }

            String operationResultString = "";

            try {
                operationResultString = NetworkUtils.getResponseFromHttpURL(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                ContentValues[] values = DogJsonUtils.getDogContentValuesFromJson(context.get(), operationResultString);

                ContentResolver contentResolver = context.get().getApplicationContext().getContentResolver();

                int numInserted = contentResolver.bulkInsert(DogContract.DogEntry.CONTENT_URI, values);

            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            return operationResultString;
        }

        @Override
        protected void onPostExecute(String jsonString) {
            super.onPostExecute(jsonString);

            try {
                Intent broadcast = new Intent();
                broadcast.setAction(DATA_INSERT_FINISHED);
                context.get().sendBroadcast(broadcast);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            restartLoader();
        }
    };

    public void onResume() {
        super.onResume();

        restartLoader();

        IntentFilter filter = new IntentFilter();
        filter.addAction(DATA_INSERT_FINISHED);
        getActivity().registerReceiver(receiver, filter);
    }

    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }
}
