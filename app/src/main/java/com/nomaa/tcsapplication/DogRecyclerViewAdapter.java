package com.nomaa.tcsapplication;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nomaa.tcsapplication.data.DogContract;

import static com.nomaa.tcsapplication.MainActivity.INDEX_DOG_ID;


public class DogRecyclerViewAdapter extends RecyclerView.Adapter<DogRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private Cursor mCursor;

    public DogRecyclerViewAdapter(Context context) {
        this.context = context;;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_dog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        mCursor.moveToPosition(position);

        String nameRaw = mCursor.getString(MainActivity.INDEX_DOG_NAME);
        String img = mCursor.getString(MainActivity.INDEX_DOG_IMG);
        int id = mCursor.getInt(MainActivity.INDEX_DOG_ID);

        String name = Character.toUpperCase(nameRaw.charAt(0)) + nameRaw.substring(1);

        holder.mName.setText(name);
        holder.mDogId.setText("" + id);
        Glide.with(context).load(img).into(holder.mImage);

        holder.mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent detailIntent = new Intent(holder.itemView.getContext(), DetailActivity.class);

                Uri uriWithID = ContentUris.withAppendedId(DogContract.DogEntry.CONTENT_URI, Integer.parseInt(holder.mDogId.getText().toString()));
                detailIntent.setData(uriWithID);

                holder.itemView.getContext().startActivity(detailIntent);
            }
        });
    }

    @Override
    public int getItemCount() {

        if (mCursor != null)
            return mCursor.getCount();
        else return 0;
    }

    public void swapCursor(Cursor cursor) {
        this.mCursor = cursor;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        public final TextView mName;
        public final ImageView mImage;
        public final TextView mDogId;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName = (TextView) view.findViewById(R.id.dog_name);
            mImage = (ImageView) view.findViewById(R.id.dog_image);
            mDogId = view.findViewById(R.id.dog_id);
        }
    }
}
