package com.github.jpage4500.jnitest.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.jpage4500.jnitest.R;
import com.github.jpage4500.jnitest.models.Actor;

import java.util.ArrayList;
import java.util.List;

public class ActorAdapter extends RecyclerView.Adapter<ActorAdapter.ActorViewHolder> {

    private Context context;
    private List<Actor> actorList;

    public ActorAdapter(Context context) {
        this.context = context;
        actorList = new ArrayList<>();
    }

    public void setActorList(List<Actor> actorList) {
        this.actorList = actorList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return actorList.size();
    }

    @Override
    public ActorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_actor, parent, false);
        return new ActorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ActorViewHolder holder, int position) {
        final Actor actor = actorList.get(position);

        holder.actorName.setText(actor.getName());
        holder.actorAge.setText(Integer.toString(actor.getAge()));

        String imageUrl = actor.getImageUrl();
        if (imageUrl == null || imageUrl.isEmpty()) {
            // use default image/placeholder
            imageUrl = "https://t4.ftcdn.net/jpg/02/15/84/43/240_F_215844325_ttX9YiIIyeaR7Ne6EaLLjMAmy4GvPC69.jpg";
        }
        Glide.with(context).load(imageUrl).into(holder.actorImage);
    }

    class ActorViewHolder extends RecyclerView.ViewHolder {
        final TextView actorName;
        final TextView actorAge;
        final ImageView actorImage;

        ActorViewHolder(View view) {
            super(view);
            actorName = view.findViewById(R.id.actorName);
            actorAge = view.findViewById(R.id.actorAge);
            actorImage = view.findViewById(R.id.actorImage);
        }
    }
}