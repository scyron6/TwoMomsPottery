package com.example.twomomspottery;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Blob;

public class activity_browse extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PotteryAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    public static PotteryDatabase database;
    private Button saveNew;
    private EditText potteryOwner;
    private EditText potteryType;
    private EditText potteryPrice;
    private EditText potteryDate;
    private Button addImageButton;
    private ImageView imageView;
    private Bitmap image;
    private OutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);


        database = Room.databaseBuilder(getApplicationContext(), PotteryDatabase.class, "pottery")
                .allowMainThreadQueries().build();

        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new GridLayoutManager(this,2);
        adapter = new PotteryAdapter();

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        FloatingActionButton button = findViewById(R.id.add_piece);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewPieceDialog();
            }
        });



        adapter.reload();
    }


    @Override
    protected void onResume() {
        super.onResume();
        adapter.reload();
    }

    public void createNewPieceDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View piecePopupView = getLayoutInflater().inflate(R.layout.piecepopup, null);

        saveNew = (Button) piecePopupView.findViewById(R.id.save_piece);
        imageView = (ImageView) piecePopupView.findViewById(R.id.chosenPicture);
        potteryPrice = (EditText) piecePopupView.findViewById(R.id.pottery_price);
        potteryType = (EditText) piecePopupView.findViewById(R.id.pottery_type);
        addImageButton = (Button) piecePopupView.findViewById(R.id.insertImage);
        potteryOwner = (EditText) piecePopupView.findViewById(R.id.creator);
        potteryDate = (EditText) piecePopupView.findViewById(R.id.pottery_date);

        dialogBuilder.setView(piecePopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        saveNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String price = potteryPrice.getText().toString();
                String type = potteryType.getText().toString();
                String owner = potteryOwner.getText().toString();
                String date = potteryDate.getText().toString();
                Bitmap toSave = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                File filepath = Environment.getExternalStorageDirectory();
                File dir = new File(filepath.getAbsolutePath()+"/Pictures/");
                String path = System.currentTimeMillis() + ".jpg";
                File file = new File(dir, path);

                try {
                    outputStream = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                toSave.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);


                try {
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                database.pieceDao().create(type, price, date, owner, path);
                dialog.cancel();
                adapter.reload();
            }
        });

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            try {

                Uri uri = data.getData();
                ParcelFileDescriptor parcelFileDescriptor =
                        getContentResolver().openFileDescriptor(uri, "r");
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                parcelFileDescriptor.close();
                imageView.setImageBitmap(image);
            }
            catch (IOException e) {
                Log.e("error", "Image not found", e);
            }
        }
    }


}