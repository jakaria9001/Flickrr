package com.jak.flickrr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    Context context;
    List<Item> postList;

    public PostAdapter(Context context, List<Item> postList){
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(context).inflate(R.layout.each_photo, parent, false);
        return new PostHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        Item item = postList.get(position);
        holder.setImageView(item.getImageUrl());
        holder.setTitle(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        View view;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;
        }
        public void setImageView(String url){
            imageView = (ImageView) view.findViewById(R.id.imageView);

            Glide.with(context).load(url).into(imageView);

        }
        public void setTitle(String title){
            textView = (TextView) view.findViewById((R.id.titleTextView));
            textView.setText(title);
        }
    }
}
