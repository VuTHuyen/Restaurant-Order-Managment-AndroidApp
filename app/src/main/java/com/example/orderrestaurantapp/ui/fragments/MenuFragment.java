package com.example.orderrestaurantapp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;

import com.example.orderrestaurantapp.MainActivity;
import com.example.orderrestaurantapp.R;
import com.example.orderrestaurantapp.menu.Database;

public class MenuFragment extends Fragment {


    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAppBarProperties();

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_menu, container, false);
    }
    private void setAppBarProperties() {
        Context context = this.getContext();
        com.google.android.material.appbar.MaterialToolbar topAppBar =
                getActivity().findViewById(R.id.topAppBar);
        topAppBar.setTitle("Orders");
        topAppBar.getMenu().getItem(0).setIcon(R.drawable.search___icon);
        topAppBar.getMenu().getItem(1).setIcon(R.drawable.ic_food_menu__icon);
        topAppBar.getMenu().getItem(0).setVisible(true);
        topAppBar.getMenu().getItem(1).setVisible(false);
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
    /**
     * This methode manages the action view of the search item on TopAppBar.
     * @param topAppBar a MaterialToolbar
     * @param context the current context
     */
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
                                                //((EventsAdapter) adapter).query_filter_all(query);
                                                return true;
                                            }

                                            @Override
                                            public boolean onQueryTextChange(String searchQuery) {
                                                //adapter is updated by changing String query
                                                //((EventsAdapter) adapter).query_filter_all(searchQuery);
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
