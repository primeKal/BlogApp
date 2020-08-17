package com.kalu.blogapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kalu.blogapp.Fragments.HomeFragment;
import com.kalu.blogapp.Fragments.ProfileFragment;
import com.kalu.blogapp.Fragments.SettingsFragment;
import com.kalu.blogapp.Models.Post;
import com.kalu.blogapp.R;

import static com.kalu.blogapp.Activities.RegisterActivity.FReqCode;
import static com.kalu.blogapp.Activities.RegisterActivity.REQUESTCODE;

public class HomeActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
    NavigationView navigationView;
    private DrawerLayout drawer;


    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    Toolbar toolbar;

    Dialog popupAddPost=null;
    FloatingActionButton fab;
    ImageView popupuserImage,popupAddImage,popupPostimg;
    EditText popupTitle,popupDescription;
    ProgressBar popupCLickprogress;
    private Uri pickedImgUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
         toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(HomeActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_home);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container,new HomeFragment()).commit();


            updateNavHeader();

        }
        fab=findViewById(R.id.floatingActionButtonpopup);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inipopup();

            }
        });

    }

    private void signCLickImage() {
        popupPostimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>22)
                    checkAndRequestPermission();
                else {openGallary();}


            }
        });

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                toolbar.setTitle("Home");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container,new HomeFragment()).commit();
                break;
            case R.id.nav_profile:
                toolbar.setTitle("Profile");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container,new ProfileFragment()).commit();
                break;
            case R.id.nav_settings:

               toolbar.setTitle("Settings");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container,new SettingsFragment()).commit();
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent lognActivity=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(lognActivity);
                finish();
                break;
            default:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
    public void updateNavHeader(){
        navigationView = findViewById(R.id.nav_view);
        View headerview=navigationView.getHeaderView(0);
        TextView navName=headerview.findViewById(R.id.nav_username);
        TextView navMail=headerview.findViewById(R.id.emaile);
        ImageView navPhoto=headerview.findViewById(R.id.nav_userphoto);

        String name= currentUser.getDisplayName();
        String email= currentUser.getEmail();
        Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),email, Toast.LENGTH_LONG).show();

        navName.setText("Welcome, "+name);
       navMail.setText(name);
       Glide.with(this).load(currentUser.getPhotoUrl()).into(navPhoto);

    }

    private void inipopup() {
        popupAddPost=new Dialog(HomeActivity.this);
        popupAddPost.setContentView(R.layout.popuo_add_post);
        popupAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        popupAddPost.getWindow().getAttributes().gravity= Gravity.TOP;
       // Toast.makeText(getApplicationContext(),"successfull", Toast.LENGTH_LONG).show();
         popupAddPost.show();

         popupuserImage=popupAddPost.findViewById(R.id.pop_user_image);
         popupPostimg=popupAddPost.findViewById(R.id.pop_img);
         popupAddImage=popupAddPost.findViewById(R.id.pop_add);
         popupTitle=popupAddPost.findViewById(R.id.pop_title);
        popupDescription=popupAddPost.findViewById(R.id.pop_description);
        popupCLickprogress=popupAddPost.findViewById(R.id.pop_progressBar2);
        Glide.with(HomeActivity.this).load(currentUser.getPhotoUrl()).into(popupuserImage);
        popupAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupCLickprogress.setVisibility(View.VISIBLE);
                popupAddImage.setVisibility(View.INVISIBLE);
                if(!popupTitle.getText().toString().isEmpty() && (!popupDescription.getText().toString().isEmpty()) && pickedImgUrl != null){
                    StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Image_blog");
                    final StorageReference imgfilepath=storageReference.child(pickedImgUrl.getLastPathSegment());
                    imgfilepath.putFile(pickedImgUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imgfilepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imgdownloadlink=uri.toString();
                               Post post=new Post(popupTitle.getText().toString()
                                                ,popupDescription.getText().toString(),imgdownloadlink,currentUser.getUid(),currentUser.getPhotoUrl().toString());
                               addPost(post);


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    shortMessage(e.getMessage());
                                    popupCLickprogress.setVisibility(View.INVISIBLE);
                                    popupAddImage.setVisibility(View.VISIBLE);

                                }
                            });
                        }
                    });

                }else {
                    popupCLickprogress.setVisibility(View.VISIBLE);
                    popupAddImage.setVisibility(View.INVISIBLE);
                    shortMessage("Please Verify All Fields");
                }

            }
        });
        signCLickImage();


    }

    private void addPost(Post post) {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference("Post").push();

        String key=reference.getKey();
        post.setPostKey(key);

        reference.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                shortMessage("Post Added Successfully");
                popupCLickprogress.setVisibility(View.INVISIBLE);
                popupAddImage.setVisibility(View.VISIBLE);
                popupAddPost.dismiss();
            }
        });
    }

    private void shortMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    private void openGallary() {
        shortMessage("Opening ur gallary");
        Intent gallaryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(gallaryIntent,"choose ur image"),REQUESTCODE);
//        Intent gallaryIntent=new Intent(Intent.ACTION_GET_CONTENT);
//        gallaryIntent.setType("Image/*");
//        startActivityForResult(Intent.createChooser(gallaryIntent,"choose ur image"),REQUESTCODE);
//
    }

    private void checkAndRequestPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE))
                Toast.makeText(HomeActivity.this,"Please Accept permission",Toast.LENGTH_LONG).show();
            else ActivityCompat.requestPermissions(HomeActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},FReqCode);
        }
        else openGallary();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null){
            pickedImgUrl=data.getData();
            popupPostimg.setImageURI(pickedImgUrl);
            shortMessage(pickedImgUrl.getLastPathSegment()+"is selected");

        }
    }

}
