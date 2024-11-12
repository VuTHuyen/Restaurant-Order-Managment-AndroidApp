package com.example.orderrestaurantapp.ui.fragments;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderrestaurantapp.MainActivity;
import com.example.orderrestaurantapp.R;
import com.example.orderrestaurantapp.menu.Database;
import com.example.orderrestaurantapp.menu.DrinkThreeFive;
import com.example.orderrestaurantapp.menu.MenuReader;
import com.example.orderrestaurantapp.menu.Order;
import com.example.orderrestaurantapp.ui.adapters.MenuDrinkThreeFiveAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MenuDrinkThreeFiveFragment extends Fragment {
    private MenuDrinkThreeFiveAdapter adapter;
    private Bundle bundle;
    public static MenuDrinkThreeFiveFragment newInstance() {
        MenuDrinkThreeFiveFragment fragment = new MenuDrinkThreeFiveFragment();
        return fragment;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerViewMenu);
        AssetManager assetManager = this.getContext().getAssets();
        bundle = this.getArguments();

        String refId = "";
        if (bundle.getString("TableID") != null) {
            refId = bundle.getString("TableID");
        }
        List<DrinkThreeFive> drinks = MenuReader.getDrinkThreeFive(assetManager, "Drinks35.csv");
        adapter = new MenuDrinkThreeFiveAdapter(this.getContext(), drinks, refId, getActivity(),getResources());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        bundle = this.getArguments();
        setAppBarProperties(refId);


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycleview_menu, container, false);
    }

    private void setAppBarProperties(String refId) {
        Context context = this.getContext();
        com.google.android.material.appbar.MaterialToolbar topAppBar =
                getActivity().findViewById(R.id.topAppBar);
        topAppBar.setTitle("Menu");
        topAppBar.getMenu().getItem(0).setIcon(R.drawable.search___icon);
        topAppBar.getMenu().getItem(0).setVisible(true);
        topAppBar.getMenu().getItem(1).setIcon(R.drawable.ic_order_food__icon);
        topAppBar.getMenu().getItem(1).setVisible(true);
        if (MainActivity.actionMode != null) {
            MainActivity.actionMode.finish();
        }
        String finalRefId = refId;
        topAppBar
                .getMenu()
                .getItem(1)
                .setOnMenuItemClickListener(
                        new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                OrderFragment fragment = OrderFragment.newInstance();
                                Bundle bundle = new Bundle();
                                bundle.putString("TableID", finalRefId);
                                fragment.setArguments(bundle);
                                ((AppCompatActivity) context)
                                        .getSupportFragmentManager()
                                        .beginTransaction()
                                        .setReorderingAllowed(true)
                                        .replace(R.id.containerView, fragment, null)
                                        .addToBackStack(null)
                                        .commit();
                                return true;
                            }
                        });

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
                                                ((MenuDrinkThreeFiveAdapter) adapter).query_filter_all(query);
                                                return true;
                                            }

                                            @Override
                                            public boolean onQueryTextChange(String searchQuery) {
                                                //adapter is updated by changing String query
                                                ((MenuDrinkThreeFiveAdapter) adapter).query_filter_all(searchQuery);
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

