package com.example.finalprojectapplication.data.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.finalprojectapplication.R;
import com.example.finalprojectapplication.data.model.TvShow;
import com.example.finalprojectapplication.ui.activity.DetailTvShowActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.finalprojectapplication.Contract.BASE_URL_POSTER;
import static com.example.finalprojectapplication.Contract.SIZE;
import static com.example.finalprojectapplication.Contract.TV_SHOW_DATA;

public class ListTvShowAdapter extends RecyclerView.Adapter<ListTvShowAdapter.ViewHolder> {
    private ArrayList<TvShow> tvShows;
    private Context context;

    public ListTvShowAdapter(Context context) {
        this.context = context;
        tvShows = new ArrayList<>();
    }

    private ArrayList<TvShow> getTvShows() {
        return tvShows;
    }

    public void setTvShows(ArrayList<TvShow> tvShows) {
        this.tvShows = tvShows;
        notifyDataSetChanged();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ListTvShowAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TvShow tvShow = tvShows.get(position);

        Glide.with(holder.itemView.getContext())
                .load(BASE_URL_POSTER + SIZE + tvShow.getPoster())
                .apply(new RequestOptions().override(96, 144))
                .into(holder.poster);
        holder.name.setText(tvShow.getName());
        holder.firstAirDate.setText(tvShow.getFirstAirDate());
        holder.voteCount.setRating((float) tvShow.getVoteCount());
        holder.voteAverage.setText(String.valueOf(tvShow.getVoteAverage()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(this.getContext(), DetailTvShowActivity.class);
            intent.putExtra(TV_SHOW_DATA, ListTvShowAdapter.this.getTvShows().get(position));
            ListTvShowAdapter.this.getContext().startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView name;
        TextView firstAirDate;
        TextView voteAverage;
        RatingBar voteCount;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.poster);
            name = itemView.findViewById(R.id.title);
            firstAirDate = itemView.findViewById(R.id.release_date);
            voteAverage = itemView.findViewById(R.id.vote_average);
            voteCount = itemView.findViewById(R.id.vote_count);
        }
    }
}
