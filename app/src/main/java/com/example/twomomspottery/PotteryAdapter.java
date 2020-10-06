package com.example.twomomspottery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PotteryAdapter extends RecyclerView.Adapter<PotteryAdapter.PotteryViewHolder> {
    public static class PotteryViewHolder extends RecyclerView.ViewHolder {
        LinearLayout containerView;
        ImageView imageView;

        PotteryViewHolder(final View view) {
            super(view);
            containerView = view.findViewById(R.id.pottery_piece);
            imageView = view.findViewById(R.id.pottery_piece_pic);

            containerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Piece current = (Piece) containerView.getTag();
                    AlertDialog.Builder dialogBuilder;
                    final AlertDialog dialog;
                    dialogBuilder = new AlertDialog.Builder(v.getContext());
                    final View viewpopup = LayoutInflater.from(v.getContext()).inflate(R.layout.viewpopup, null);

                    TextView idTextView = (TextView) viewpopup.findViewById(R.id.pottery_id);
                    TextView categoryTextView = (TextView) viewpopup.findViewById(R.id.pottery_category);
                    TextView ownerTextView = (TextView) viewpopup.findViewById(R.id.pottery_maker);
                    TextView dateTextView = (TextView) viewpopup.findViewById(R.id.pottery_date);
                    TextView priceTextView = (TextView) viewpopup.findViewById(R.id.price);
                    Button deleteButton = (Button) viewpopup.findViewById(R.id.delete_pottery);
                    ImageView displayImage = (ImageView) viewpopup.findViewById(R.id.pottery_display);

                    String id = "ID Number: " + Integer.toString(current.id);
                    idTextView.setText(id);
                    String category = "Type: " + current.type;
                    categoryTextView.setText(category);
                    String made = "Made by: " + current.owner;
                    ownerTextView.setText(made);
                    String date = "Made on: " + current.date;
                    dateTextView.setText(date);
                    String price = "Price: " + current.cost;
                    priceTextView.setText(price);
                    String absolute = Environment.getExternalStorageDirectory() + "/Pictures/" + current.path;
                    displayImage.setImageDrawable(Drawable.createFromPath(absolute));




                    dialogBuilder.setView(viewpopup);
                    dialog = dialogBuilder.create();
                    dialog.show();

                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            File filepath = Environment.getExternalStorageDirectory();
                            File dir = new File(filepath.getAbsolutePath()+"/Pictures/");
                            String path = current.path;
                            File file = new File(dir, path);
                            boolean check = file.delete();
                            if (!check) {
                                Log.d("check", "not deleted");
                            }
                            else {
                                activity_browse.database.pieceDao().delete(current.id);
                            }

                            // Restart the activity to reload after deleting a piece
                            Intent intent = new Intent(v.getContext(), activity_browse.class);
                            v.getContext().startActivity(intent);
                        }
                    });




                }
            });

        }
    }

    private List<Piece> pottery = new ArrayList<>();

    @NonNull
    @Override
    public PotteryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pottery_piece, parent, false);

        return new PotteryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PotteryViewHolder holder, int position) {
        Piece current = pottery.get(position);
        holder.containerView.setTag(current);
        String test = Environment.getExternalStorageDirectory() + "/Pictures/" + current.path;
        holder.imageView.setImageDrawable(Drawable.createFromPath(test));

    }


    @Override
    public int getItemCount() {
        return pottery.size();
    }

    public void reload() {
        pottery = activity_browse.database.pieceDao().getAllPieces();
        notifyDataSetChanged();
    }

}
