package com.kalu.blogapp.Adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kalu.blogapp.Models.Comment;
import com.kalu.blogapp.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyCommentHolder> {
    Context mcontext;
    List<Comment> mData;

    public CommentAdapter(Context mcontext, List<Comment> mData) {
        this.mcontext = mcontext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyCommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.comment_row,parent,false);
        MyCommentHolder myViewHolder=new MyCommentHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyCommentHolder holder, int position) {
        holder.commentusername.setText(mData.get(position).getName().toString());
        holder.thecommentitself.setText(mData.get(position).getContent().toString());
        holder.date.setText(timeStamtoString((long)(mData.get(position).getTimestamp())));
        Glide.with(mcontext).load(mData.get(position).getUimg()).into(holder.commentuserimg);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyCommentHolder extends RecyclerView.ViewHolder {
        ImageView commentuserimg;
        TextView commentusername;
        TextView thecommentitself,date;
        public MyCommentHolder(@NonNull View itemView) {
            super(itemView);
            commentuserimg=itemView.findViewById(R.id.commentuserimg);
            commentusername=itemView.findViewById(R.id.commentuser);
            thecommentitself=itemView.findViewById(R.id.thecomment);
            date=itemView.findViewById(R.id.date);
        }
    }

    public String timeStamtoString(long t){
        Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(t);
        String date= DateFormat.format("dd-MM-yyyy",calendar).toString();
        return date;
    }

}
