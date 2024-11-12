package com.example.orderrestaurantapp.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.orderrestaurantapp.MainActivity;
import com.example.orderrestaurantapp.R;
import com.example.orderrestaurantapp.menu.Table;
import com.example.orderrestaurantapp.menu.Database;
import com.example.orderrestaurantapp.ui.adapters.MenuFoodAdapter;
import com.example.orderrestaurantapp.ui.adapters.TableManagerAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class TableManagerFragment extends Fragment {
    private TableManagerAdapter adapter;
    private FirebaseFirestore database;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database = FirebaseFirestore.getInstance();
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerViewTable);
        List<Table> tables = Database.getStartTable();
        for (Table table : tables) {
            String tableName = table.getNameOfTable();

            // Check if the collection exists in the database
            database.collection(tableName)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                // Collection exists, update the status of the table
                                table.setStatusOfTable(true);
                            }
                        } else {
                            // Handle any errors
                            Log.e("Firestore error", task.getException().getMessage());
                        }

                        // Update the RecyclerView adapter after checking each table
                        recyclerView.getAdapter().notifyDataSetChanged();
                    });
            adapter = new TableManagerAdapter(this.getContext(), tables);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        setAppBarProperties();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_table_recycleview, container, false);
    }
    private void setAppBarProperties() {
        Context context = this.getContext();
        com.google.android.material.appbar.MaterialToolbar topAppBar =
                getActivity().findViewById(R.id.topAppBar);
        topAppBar.setTitle("Alle Tische");
        topAppBar.getMenu().getItem(0).setIcon(R.drawable.search___icon);
        topAppBar.getMenu().getItem(0).setVisible(true);
        topAppBar.getMenu().getItem(1).setVisible(false);
        topAppBar.getMenu().getItem(2).setVisible(false);
        if (MainActivity.actionMode != null) {
            MainActivity.actionMode.finish();
        }
        topAppBar
                .getMenu()
                .getItem(0)
                .setOnMenuItemClickListener(
                        new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                createActionView(topAppBar, context);
                                ((SearchView) MainActivity.actionMode.getMenu().getItem(0).getActionView())
                                        .onActionViewExpanded();
                                return true;
                            }
                        });
    }


        private void createActionView(
                com.google.android.material.appbar.MaterialToolbar topAppBar, Context context) {
            MainActivity.actionMode =
                    topAppBar.startActionMode(
                            new ActionMode.Callback() {
                                @Override
                                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                                    MenuInflater menuInflater = new MenuInflater(context);
                                    menuInflater.inflate(R.menu.contextual_action_bar, menu);

                                    SearchView searchView = new SearchView(context);
                                    searchView.setQuery(null, true);
                                    searchView.setQueryHint(getResources().getString(R.string.search));

                                    searchView.setOnQueryTextListener(
                                            new SearchView.OnQueryTextListener() {
                                                @Override
                                                public boolean onQueryTextSubmit(String query) {
                                                    //adapter is updated by submitting String query
                                                    ((TableManagerAdapter) adapter).query_filter_all(query);
                                                    return true;
                                                }

                                                @Override
                                                public boolean onQueryTextChange(String searchQuery) {
                                                    //adapter is updated by changing String query
                                                    ((TableManagerAdapter) adapter).query_filter_all(searchQuery);
                                                    return true;
                                                }
                                            });
                                    menu.findItem(R.id.actionMode1).setActionView(searchView);
                                    return true;
                                }

                                @Override
                                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                                    return false;
                                }

                                @Override
                                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.actionMode1:
                                    }
                                    return true;
                                }

                                @Override
                                public void onDestroyActionMode(ActionMode mode) {}
                            });
            ((SearchView) MainActivity.actionMode.getMenu().getItem(0).getActionView())
                    .onActionViewExpanded();
        }

}
