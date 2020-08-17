package com.kalu.blogapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kalu.blogapp.Activities.PostDetailActivity;
import com.kalu.blogapp.Models.Post;
import com.kalu.blogapp.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    Context mcontext;
    List<Post> mData;


    public PostAdapter(Context mcontext, List<Post> mData) {
        this.mcontext = mcontext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.row_post_iss,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.mTitle.setText(mData.get(position).getTitle().toString());
        Glide.with(mcontext).load(mData.get(position).getPictures()).into(holder.mPostimg);
        Glide.with(mcontext).load(mData.get(position).getUserPhoto()).into(holder.mUserProfile);

    }

    @Override
    public int getItemCount() {
        if(mData == null)return 0;
        else return mData.size();
    }


    public class  MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        ImageView mUserProfile;
        ImageView mPostimg;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle=itemView.findViewById(R.id.row_pos_title);
            mUserProfile=itemView.findViewById(R.id.row_profile_img);
            mPostimg=itemView.findViewById(R.id.row_post_Img);
                     itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    Toast.makeText(mcontext, mData.get(position).getUserId(), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(mcontext, PostDetailActivity.class);
                    intent.putExtra("title",mData.get(position).getTitle());
                    intent.putExtra("postImage",mData.get(position).getPictures());
                    intent.putExtra("postuser",mData.get(position).getUserPhoto());
                    intent.putExtra("description",mData.get(position).getDescription());
                    intent.putExtra("key",mData.get(position).getPostKey());
                    intent.putExtra("id",mData.get(position).getUserId());
                    long timestamp= (long) mData.get(position).getTimestamp();
                    intent.putExtra("timeStamp",timestamp);
                    mcontext.startActivity(intent);
                }
            });

        }
    }
}
