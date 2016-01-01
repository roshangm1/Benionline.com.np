package np.info.roshan.benionlinecomnp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

import np.info.roshan.benionlinecomnp.R;
import np.info.roshan.benionlinecomnp.activities.NewsDetails;
import np.info.roshan.benionlinecomnp.helper.Singleton;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.CustomViewHolder> {

    private Context context;

    private ArrayList<String> mTitles = new ArrayList<>(),
            mDates = new ArrayList<>(),
            mImages = new ArrayList<>(),
            mContents = new ArrayList<>(),
            mCategories = new ArrayList<>(),
            mWriters = new ArrayList<>();

    public NewsAdapter(Context context, ArrayList mTitles, ArrayList mDates, ArrayList mImages, ArrayList mContents, ArrayList mCategories, ArrayList mWriters) {
        this.context = context;
        this.mTitles = mTitles;
        this.mDates = mDates;
        this.mContents = mContents;
        this.mImages = mImages;
        this.mCategories = mCategories;
        this.mWriters = mWriters;

    }


    @Override
    public NewsAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_post, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final NewsAdapter.CustomViewHolder holder, final int position) {
        holder.title.setText(mTitles.get(position));
        holder.date.setText(Singleton.convertDate(mDates.get(position)));
        holder.category.setText(mCategories.get(position));

        if (!mImages.get(position).equals("")) {

            Picasso.with(context).load(mImages.get(position)).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(holder.featuredImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context).load(mImages.get(position)).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(holder.featuredImage, new Callback() {

                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Log.v("errorPicasso","Error loading images");
                        }
                    });
                }

            });

        } else {
            Picasso.with(context).load(R.drawable.placeholder).into(holder.featuredImage);
        }
    }

    @Override
    public int getItemCount() {
        return mTitles.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, date,category;
        ImageView featuredImage;

        public CustomViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.eachTitle);
            date = (TextView) itemView.findViewById(R.id.eachDate);
            category = (TextView) itemView.findViewById(R.id.eachCategory);
            featuredImage = (ImageView) itemView.findViewById(R.id.featuredImage);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(context, NewsDetails.class)
                    .putExtra("id", getAdapterPosition())
                    .putStringArrayListExtra("newsTitle", mTitles)
                    .putStringArrayListExtra("newsDate", mDates)
                    .putStringArrayListExtra("newsImage", mImages)
                    .putStringArrayListExtra("newsContent", mContents)
                    .putStringArrayListExtra("newsCategory", mCategories)
                    .putStringArrayListExtra("newsAuthor",mWriters);


            context.startActivity(intent);

        }
    }
}
