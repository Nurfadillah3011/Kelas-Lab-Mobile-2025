package com.example.t8mobile_h071231080;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {
    private RecyclerView rvNotes;
    private NoteAdapter adapter;
    private NoteHelper noteHelper;
    private TextView noData;
    private EditText etSearch;
    private ImageView ivClearSearch;
    private ArrayList<Note> allNotes = new ArrayList<>();
    private final int REQUEST_ADD = 100;
    private final int REQUEST_UPDATE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecyclerView();

        noteHelper = NoteHelper.getInstance(getApplicationContext());
        setupSearchFeature();
    }

    private void initViews() {
        rvNotes = findViewById(R.id.rv_students);
        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        noData = findViewById(R.id.noData);
        etSearch = findViewById(R.id.et_search);
        ivClearSearch = findViewById(R.id.iv_clear_search);

        fabAdd.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, FormActivity.class);
            startActivityForResult(intent, REQUEST_ADD);
        });
    }

    private void setupRecyclerView() {
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NoteAdapter(this);
        rvNotes.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    private void setupSearchFeature() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterNotes(s.toString().trim());
                ivClearSearch.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ivClearSearch.setOnClickListener(v -> {
            etSearch.setText("");
            etSearch.clearFocus();
        });
    }

    private void filterNotes(String query) {
        ArrayList<Note> filteredList = new ArrayList<>();

        if (query.isEmpty()) {
            filteredList.addAll(allNotes);

        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Note note : allNotes) {
                if (note.getJudul() != null &&
                        note.getJudul().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(note);
                }
            }
        }

        updateAdapterWithResults(filteredList, query);
    }

    private void updateAdapterWithResults(ArrayList<Note> filteredList, String query) {
        if (filteredList.size() > 0) {
            adapter.setNotes(filteredList);
            noData.setVisibility(View.GONE);
            rvNotes.setVisibility(View.VISIBLE);
        } else {
            adapter.setNotes(new ArrayList<>());
            rvNotes.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);

            noData.setText(query.isEmpty() ? "No notes available" :
                    "No results found for \"" + query + "\"");
        }
    }

    private void loadNotes() {
        new LoadNotesAsync(this, notes -> {
            allNotes.clear();
            allNotes.addAll(notes);
            filterNotes(etSearch.getText().toString().trim());
        }).execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD && resultCode == FormActivity.RESULT_ADD) {
            showToast("Note added successfully");
            loadNotes();

        } else if (requestCode == REQUEST_UPDATE) {
            if (resultCode == FormActivity.RESULT_UPDATE) {
                showToast("Note updated successfully");
                loadNotes();

            } else if (resultCode == FormActivity.RESULT_DELETE) {
                showToast("Note deleted successfully");
                loadNotes();
            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (noteHelper != null) {
            noteHelper.close();
        }
    }

    private static class LoadNotesAsync {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadNotesCallback> weakCallback;

        private LoadNotesAsync(Context context, LoadNotesCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        void execute() {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            executor.execute(() -> {
                Context context = weakContext.get();
                if (context != null) {
                    NoteHelper noteHelper = NoteHelper.getInstance(context);
                    noteHelper.open();

                    try {
                        Cursor notesCursor = noteHelper.queryAll();
                        ArrayList<Note> notes = MappingHelper.mapCursorToArrayList(notesCursor);
                        notesCursor.close();

                        handler.post(() -> {
                            LoadNotesCallback callback = weakCallback.get();
                            if (callback != null) {
                                callback.postExecute(notes);
                            }
                        });
                    } catch (Exception e) {
                        handler.post(() -> {
                            LoadNotesCallback callback = weakCallback.get();
                            if (callback != null) {
                                callback.postExecute(new ArrayList<>());
                            }
                        });
                    }
                }
            });
        }
    }

    interface LoadNotesCallback {
        void postExecute(ArrayList<Note> notes);
    }
}