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
import com.example.finalprojectapplication.data.model.Movie;
import com.example.finalprojectapplication.ui.activity.DetailMovieActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.finalprojectapplication.Contract.BASE_URL_POSTER;
import static com.example.finalprojectapplication.Contract.MOVIE_DATA;
import static com.example.finalprojectapplication.Contract.SIZE;

public class ListMovieAdapter extends RecyclerView.Adapter<ListMovieAdapter.ViewHolder> {
    private ArrayList<Movie> movies;
    private Context context;

    public ListMovieAdapter(Context context) {
        this.context = context;
        movies = new ArrayList<>();
    }

    private ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
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
    public ListMovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ListMovieAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Movie movie = movies.get(position);

        Glide.with(holder.itemView.getContext())
                .load(BASE_URL_POSTER + SIZE + movie.getPoster())
                .apply(new RequestOptions().override(96, 144))
                .into(holder.poster);
        holder.title.setText(movie.getTitle());
        holder.releaseDate.setText(movie.getReleaseDate());
        holder.voteCount.setRating((float) movie.getVoteCount()/100);
        holder.voteAverage.setText(String.valueOf(movie.getVoteAverage()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(this.getContext(), DetailMovieActivity.class);
            intent.putExtra(MOVIE_DATA, ListMovieAdapter.this.getMovies().get(position));
            ListMovieAdapter.this.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView title;
        TextView releaseDate;
        TextView voteAverage;
        RatingBar voteCount;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.poster);
            title = itemView.findViewById(R.id.title);
            releaseDate = itemView.findViewById(R.id.release_date);
            voteAverage = itemView.findViewById(R.id.vote_average);
            voteCount = itemView.findViewById(R.id.vote_count);
        }
    }
}
