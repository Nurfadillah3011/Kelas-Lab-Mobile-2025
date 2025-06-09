package com.example.t8mobile_h071231080;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class FormActivity extends AppCompatActivity {
    public static final String EXTRA_NOTES = "extra_notes";
    public static final int RESULT_ADD = 101;
    public static final int RESULT_UPDATE = 201;
    public static final int RESULT_DELETE = 301;
    public static final int REQUEST_UPDATE = 200;
    private NoteHelper noteHelper;
    private Note note;
    private EditText etJudul, etDeskripsi;
    private boolean isEdit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        etJudul = findViewById(R.id.et_judul);
        etDeskripsi = findViewById(R.id.et_deskripsi);
        Button btnSave = findViewById(R.id.btn_save);
        Button btnDelete = findViewById(R.id.btn_delete);

        noteHelper = NoteHelper.getInstance(getApplicationContext());
        noteHelper.open();

        note = getIntent().getParcelableExtra(EXTRA_NOTES);
        if (note != null) {
            isEdit = true;
        } else {
            note = new Note();
        }

        String actionBarTitle;
        String buttonTitle;

        if (isEdit) {
            actionBarTitle = "Edit Note";
            buttonTitle = "Update";

            if (note != null) {
                etJudul.setText(note.getJudul());
                etDeskripsi.setText(note.getDeskripsi());
            }
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            // Mode add
            actionBarTitle = "Add Note";
            buttonTitle = "Save";
        }

        btnSave.setText(buttonTitle);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(actionBarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable back button
        }

        btnSave.setOnClickListener(view -> showSaveConfirmationDialog());
        btnDelete.setOnClickListener(view -> showDeleteConfirmationDialog());
    }


    private void showSaveConfirmationDialog() {
        String title = isEdit ? "Update Note" : "Simpan Note";
        String message = isEdit ?
                "Apakah anda yakin ingin mengupdate item ini?" :
                "Apakah anda yakin ingin menyimpan item ini?";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveNote();
                    }
                })
                .setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hapus Note")
                .setMessage("Apakah anda yakin ingin menghapus item ini?")
                .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteNote();
                    }
                })
                .setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void saveNote() {
        // Ambil input dari form
        String judul = etJudul.getText().toString().trim();
        String deskripsi = etDeskripsi.getText().toString().trim();

        if (judul.isEmpty()) {
            etJudul.setError("Please fill this field");
            return;
        }
        if (deskripsi.isEmpty()) {
            etDeskripsi.setError("Please fill this field");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(new Date());

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.NotesColumns.JUDUL, judul);
        values.put(DatabaseContract.NotesColumns.DESKRIPSI, deskripsi);

        if (isEdit) {
            values.put(DatabaseContract.NotesColumns.UPDATED_AT, currentTime);

            long result = noteHelper.update(String.valueOf(note.getId()), values);

            if (result > 0) {
                note.setJudul(judul);
                note.setDeskripsi(deskripsi);
                note.setUpdatedAt(currentTime);

                Intent intent = new Intent();
                intent.putExtra(EXTRA_NOTES, note);
                setResult(RESULT_UPDATE, intent);
                Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update data", Toast.LENGTH_SHORT).show();
            }
        } else {
            values.put(DatabaseContract.NotesColumns.CREATED_AT, currentTime);
            values.put(DatabaseContract.NotesColumns.UPDATED_AT, currentTime);

            long result = noteHelper.insert(values);

            if (result > 0) {
                note = new Note((int) result, judul, deskripsi, currentTime, currentTime);

                Intent intent = new Intent();
                intent.putExtra(EXTRA_NOTES, note);
                setResult(RESULT_ADD, intent);
                Toast.makeText(this, "Data added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deleteNote() {
        if (note != null && note.getId() > 0) {
            long result = noteHelper.deleteById(String.valueOf(note.getId()));

            if (result > 0) {
                setResult(RESULT_DELETE);
                Toast.makeText(this, "Data deleted successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to delete data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (noteHelper != null) {
            noteHelper.close();
        }
    }
}