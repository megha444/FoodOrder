package com.meghaagarwal.foodorder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddFood extends AppCompatActivity {

    private ImageButton mIBAddFood;
    private EditText mETName, mETDesc, mETPrice;
    private Button mBAdd;
    private Uri uri = null;
    private StorageReference mStorageReference = null;
    private DatabaseReference mDatabaseReference;
    private static final int GALLREQ = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        mIBAddFood = (ImageButton) findViewById(R.id.ibAddFood);
        mETName = (EditText) findViewById(R.id.etName);
        mETDesc = (EditText) findViewById(R.id.etDesc);
        mETPrice = (EditText) findViewById(R.id.etPrice);
        mBAdd = (Button) findViewById(R.id.bAdd);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Item");

        mIBAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("Image/*");
                startActivityForResult(galleryIntent, GALLREQ);
            }
        });

        mBAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String name_text = mETName.getText().toString().trim();
                final String desc_text = mETDesc.getText().toString().trim();
                final String price_text = mETPrice.getText().toString().trim();

                if(!TextUtils.isEmpty(name_text) && !TextUtils.isEmpty(desc_text) && !TextUtils.isEmpty(price_text))
                {
                    StorageReference filepath = mStorageReference.child(uri.getLastPathSegment());
                    filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            final String downloadurl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                            Log.d("FOODAPP", downloadurl);
                            Toast.makeText(AddFood.this, "Item Added", Toast.LENGTH_LONG).show();
                            final DatabaseReference newPostRef = mDatabaseReference.push();
                            newPostRef.child("name").setValue(name_text);
                            newPostRef.child("desc").setValue(desc_text);
                            newPostRef.child("price").setValue(price_text);
                            newPostRef.child("image").setValue(downloadurl);

                        }
                    });
                }
                else
                {
                    Toast.makeText(AddFood.this, "Invalid input", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLREQ && resultCode == RESULT_OK)
        {
            uri = data.getData();
            mIBAddFood = (ImageButton) findViewById(R.id.ibAddFood);
            mIBAddFood.setImageURI(uri);
        }
    }
}