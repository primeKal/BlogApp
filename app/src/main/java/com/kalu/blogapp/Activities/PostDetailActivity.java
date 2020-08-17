package com.kalu.blogapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.kalu.blogapp.Adapters.CommentAdapter;
import com.kalu.blogapp.Models.Comment;
import com.kalu.blogapp.Models.Post;
import com.kalu.blogapp.R;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PostDetailActivity extends AppCompatActivity {
    ImageView imgpost,imgcurrentuser,imguserpost;
    TextView txtpostDescri,txtpostTitle,txtpostDate;
    EditText editTextComment;
    Button btnaddComent;

    RecyclerView rv;
    List<Comment> list;
    CommentAdapter cmtadd;

    FirebaseDatabase firebaseDatabase;
    FirebaseUser currentUser;
    String postKey;
    Comment newcomment;
    DatabaseReference cm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getSupportActionBar().hide();

        rv=findViewById(R.id.rv_comment);
        imgpost=findViewById(R.id.post_detail_img);
        imgcurrentuser=findViewById(R.id.post_detail_currentuser_img);
        imguserpost=findViewById(R.id.post_detail_user_img);

        txtpostDate=findViewById(R.id.post_detail_date);
        txtpostDescri=findViewById(R.id.post_detail_descri);
        txtpostTitle=findViewById(R.id.post_detail_title);

        firebaseDatabase=FirebaseDatabase.getInstance();
        updatePostdetailPage();

        editTextComment=findViewById(R.id.post_detail_comment);

        btnaddComent=findViewById(R.id.post_detail_add_comment);
        inirv();
        btnaddComent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnaddComent.setVisibility(View.INVISIBLE);
                DatabaseReference myRef=firebaseDatabase.getReference("Comment").child(postKey).push();
                String uid=currentUser.getUid();
                String unamee=currentUser.getDisplayName();
                String uimg=currentUser.getPhotoUrl().toString();
                String comment_contnt=editTextComment.getText().toString();
                newcomment=new Comment(unamee,uid,uimg,comment_contnt);
                myRef.setValue(newcomment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        shortMessage("YOu commented");
                        editTextComment.setText(" ");
                        btnaddComent.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        shortMessage("Failed to add commentbecause"+e.getMessage());
                    }
                });
            }
        });
    }
public void inirv(){
        rv.setLayoutManager(new LinearLayoutManager(this));
        cm=FirebaseDatabase.getInstance().getReference("Comment").child(postKey);
        cm.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list=new ArrayList<>();
                for(DataSnapshot postsnap:dataSnapshot.getChildren()){
                    Comment post=postsnap.getValue(Comment.class);
                    list.add(post);
                    //    Toast.makeText(getContext(),postList.size(),Toast.LENGTH_LONG).show();
                }
                cmtadd=new CommentAdapter(getApplicationContext(),list);
                rv.setAdapter(cmtadd);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


}
    private void shortMessage(String yOu_commented) {
        Toast.makeText(getApplicationContext(),yOu_commented,Toast.LENGTH_LONG).show();
    }

    private void updatePostdetailPage() {

        Bundle extras = getIntent().getExtras();
          postKey=extras.getString("key");
         String title=extras.getString("title");
         String description=extras.getString("description");
         String pictures=extras.getString("postImage");
         String userId=extras.getString("userId");
         String userPhoto=extras.getString("postuser");
         long timestamp=extras.getLong("timeStamp");

        currentUser=FirebaseAuth.getInstance().getCurrentUser();
        Glide.with(getApplicationContext()).load(currentUser.getPhotoUrl()).into(imgcurrentuser);
        Glide.with(getApplicationContext()).load(pictures).into(imgpost);
        Glide.with(getApplicationContext()).load(userPhoto).into(imguserpost);
        txtpostTitle.setText(title);
        txtpostDescri.setText(description);
        txtpostDate.setText(timeStamtoString(timestamp));
        }
public String timeStamtoString(long t){
    Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
    calendar.setTimeInMillis(t);
    String date= DateFormat.format("dd-MM-yyyy",calendar).toString();
    return date;
}
}
