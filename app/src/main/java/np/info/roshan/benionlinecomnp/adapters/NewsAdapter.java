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

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import np.info.roshan.benionlinecomnp.R;
import np.info.roshan.benionlinecomnp.activities.NewsDetails;
import np.info.roshan.benionlinecomnp.helper.Singleton;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.CustomViewHolder> {

    private final Context context;

    private ArrayList<String> mTitles = new ArrayList<>(),
            mDates = new ArrayList<>(),
            mImages = new ArrayList<>(),
            mContents = new ArrayList<>(),
            mCategories = new ArrayList<>(),
            mWriters = new ArrayList<>();
    private ArrayList<Integer> mIds = new ArrayList<>();


    public NewsAdapter(Context context, ArrayList mIds, ArrayList mTitles, ArrayList mDates, ArrayList mImages, ArrayList mContents, ArrayList mCategories, ArrayList mWriters) {
        this.context = context;
        this.mTitles = mTitles;
        this.mDates = mDates;
        this.mContents = mContents;
        this.mImages = mImages;
        this.mCategories = mCategories;
        this.mWriters = mWriters;
        this.mIds = mIds;

    }


    @Override
    public NewsAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_post, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NewsAdapter.CustomViewHolder holder, final int position) {
        holder.title.setText(mTitles.get(position));
        holder.date.setText(Singleton.convertDate(mDates.get(position)));
        holder.category.setText(mCategories.get(position));

        if (!mImages.get(position).equals("")) {

            Picasso.with(context).load(mImages.get(position)).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.header).error(R.drawable.header).into(holder.featuredImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context).load(mImages.get(position)).placeholder(R.drawable.header).error(R.drawable.header).into(holder.featuredImage, new Callback() {

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
            Picasso.with(context).load(R.drawable.header).into(holder.featuredImage);
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
                    .putExtra("newsTitle", mTitles.get(getAdapterPosition()))
                    .putExtra("newsImage", mImages.get(getAdapterPosition()))
                    .putExtra("newsDate", mDates.get(getAdapterPosition()))
                    .putExtra("newsContent", mContents.get(getAdapterPosition()))
                    .putExtra("newsCategory", mCategories.get(getAdapterPosition()))
                    .putExtra("newsAuthor", mWriters.get(getAdapterPosition()))
                    .putExtra("newsId", mIds.get(getAdapterPosition()));
            context.startActivity(intent);

        }
    }
}
