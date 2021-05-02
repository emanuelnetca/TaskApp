package com.taskapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    // Add task layout
    private EditText titleAddTaskLayout;
    private EditText noteAddTaskLayout;
    private Button saveButtonAddTaskLayout;
    private FloatingActionButton floatingActionButtonHomeLayout;

    // Firebase
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    // Recycler
    private RecyclerView recyclerViewHomeLayout;

    // Update input field
    private EditText titleUpdateDeleteLayout;
    private EditText noteUpdateDeleteLayout;
    private Button deleteButtonUpdateDeleteLayout;
    private Button updateButtonUpdateDeleteLayout;

    // Variables
    private String title;
    private String note;
    private String date;
    private String refKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_activity_layout);

        // Database
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String uId = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Task").child(uId);
        databaseReference.keepSynced(true);

        // Recycler
        recyclerViewHomeLayout = findViewById(R.id.recyclerViewHomeLayout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewHomeLayout.setHasFixedSize(true);
        recyclerViewHomeLayout.setLayoutManager(linearLayoutManager);

        // Floating button
        floatingActionButtonHomeLayout = findViewById(R.id.floatingActionButtonHomeLayout);
        floatingActionButtonHomeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
                LayoutInflater layoutInflater = LayoutInflater.from(HomeActivity.this);
                View view = layoutInflater.inflate(R.layout.add_task_layout, null);

                alertDialogBuilder.setView(view);
                AlertDialog alertDialog = alertDialogBuilder.create();

                titleAddTaskLayout = view.findViewById(R.id.titleAddTaskLayout);
                noteAddTaskLayout = view.findViewById(R.id.noteAddTaskLayout);
                saveButtonAddTaskLayout = view.findViewById(R.id.saveButtonAddTaskLayout);

                saveButtonAddTaskLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = titleAddTaskLayout.getText().toString().trim();
                        String note = noteAddTaskLayout.getText().toString().trim();

                        if (TextUtils.isEmpty(title)) {
                            titleAddTaskLayout.setError("Required Field");
                            return;
                        }

                        if (TextUtils.isEmpty(title)) {
                            noteAddTaskLayout.setError("Required Field");
                            return;
                        }

                        String id = databaseReference.push().getKey();
                        String date = DateFormat.getDateInstance().format(new Date());
                        Data data = new Data(title, note, date, id);

                        databaseReference.child(id).setValue(data);

                        Toast.makeText(getApplicationContext(), "Data inserted", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();

                    }
                });

                alertDialog.show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Data, ViewHolderExtended> adapter = new FirebaseRecyclerAdapter<Data, ViewHolderExtended>
                (Data.class, R.layout.task_layout, ViewHolderExtended.class, databaseReference) {
            @Override
            protected void populateViewHolder(ViewHolderExtended viewHolderExtended, Data data, int position) {
                viewHolderExtended.setTitle(data.getTitle());
                viewHolderExtended.setNote(data.getNote());
                viewHolderExtended.setDate(data.getDate());

                viewHolderExtended.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refKey = getRef(position).getKey();
                        title = data.getTitle();
                        note = data.getNote();
                        updateData();
                    }
                });
            }
        };

        recyclerViewHomeLayout.setAdapter(adapter);
    }

    public static class ViewHolderExtended extends RecyclerView.ViewHolder {
        private View view;

        public ViewHolderExtended(View view) {
            super(view);
            this.view = view;
        }

        public void setTitle(String title) {
            TextView titleTaskLayout = view.findViewById(R.id.titleTaskLayout);
            titleTaskLayout.setText(title);
        }

        public void setNote(String note) {
            TextView noteTaskLayout = view.findViewById(R.id.noteTaskLayout);
            noteTaskLayout.setText(note);
        }

        public void setDate(String date) {
            TextView dateTaskLayout = view.findViewById(R.id.dateTaskLayout);
            dateTaskLayout.setText(date);
        }
    }

    public void updateData() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
        LayoutInflater layoutInflater = LayoutInflater.from(HomeActivity.this);

        View myview = layoutInflater.inflate(R.layout.update_delete_layout, null);
        alertDialogBuilder.setView(myview);

        AlertDialog alertDialog = alertDialogBuilder.create();

        titleUpdateDeleteLayout = myview.findViewById(R.id.titleUpdateDeleteLayout);
        noteUpdateDeleteLayout = myview.findViewById(R.id.noteUpdateDeleteLayout);

        titleUpdateDeleteLayout.setText(title);
        titleUpdateDeleteLayout.setSelection(title.length());

        noteUpdateDeleteLayout.setText(note);
        noteUpdateDeleteLayout.setSelection(note.length());

        updateButtonUpdateDeleteLayout = myview.findViewById(R.id.updateButtonUpdateDeleteLayout);
        deleteButtonUpdateDeleteLayout = myview.findViewById(R.id.deleteButtonUpdateDeleteLayout);

        updateButtonUpdateDeleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                title = titleUpdateDeleteLayout.getText().toString().trim();
                note = noteUpdateDeleteLayout.getText().toString().trim();

                date = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(title, note, date, refKey);

                databaseReference.child(refKey).setValue(data);

                // After I click this button the dialog will dismiss the view
                alertDialog.dismiss();
            }
        });

        deleteButtonUpdateDeleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child(refKey).removeValue();

                // After I click this button the dialog will dismiss the view
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
}