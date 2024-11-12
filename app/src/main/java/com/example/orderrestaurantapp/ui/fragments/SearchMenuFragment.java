package com.example.orderrestaurantapp.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.orderrestaurantapp.R;
import com.example.orderrestaurantapp.ui.TopAppBar;

public class SearchMenuFragment extends Fragment {

    private AppCompatButton filterOnFood;
    private AppCompatButton filterOnNormalDrink;
    private AppCompatButton filterOnDrink24;
    private AppCompatButton filterOnDrink35;
    public static SearchMenuFragment newInstance() {
        SearchMenuFragment fragment = new SearchMenuFragment();
        return fragment;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();
        String refId = bundle.getString("TableID");
        filterOnFood = (AppCompatButton) view.findViewById(R.id.filter_on_food);
        filterOnNormalDrink = (AppCompatButton) view.findViewById(R.id.filter_on_normal_drink);
        filterOnDrink24 = (AppCompatButton) view.findViewById(R.id.filter_on_drink24);
        filterOnDrink35 = (AppCompatButton) view.findViewById(R.id.filter_on_drink35);

        Context context = this.getContext();
        com.google.android.material.appbar.MaterialToolbar topAppBar =
                getActivity().findViewById(R.id.topAppBar);
        TopAppBar.setAppBarProperties(refId, context, topAppBar);

        filterOnFood.setOnClickListener(
                v -> {
                    MenuFoodFragment fragment = MenuFoodFragment.newInstance();
                    Bundle newbundle = new Bundle();
                    newbundle.putString("TableID", refId);
                    fragment.setArguments(bundle);
                    changeBackgroundColor(filterOnFood);
                    changeBackgroundColorToOriginal(filterOnNormalDrink);
                    changeBackgroundColorToOriginal(filterOnDrink24);
                    changeBackgroundColorToOriginal(filterOnDrink35);
                    ((AppCompatActivity) v.getContext())
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.containerButtonSearchEventView, fragment, null)
                            .addToBackStack(null)
                            .commit();

                });

        filterOnNormalDrink.setOnClickListener(
                v -> {
                    MenuDrinkWithoutVarityFragment fragment = MenuDrinkWithoutVarityFragment.newInstance();
                    Bundle newbundle = new Bundle();
                    newbundle.putString("TableID", refId);
                    fragment.setArguments(bundle);
                    changeBackgroundColor(filterOnNormalDrink);
                    changeBackgroundColorToOriginal(filterOnFood);
                    changeBackgroundColorToOriginal(filterOnDrink24);
                    changeBackgroundColorToOriginal(filterOnDrink35);

                    ((AppCompatActivity) v.getContext())
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.containerButtonSearchEventView, fragment, null)
                            .addToBackStack(null)
                            .commit();
                });
        filterOnDrink24.setOnClickListener(
                v -> {
                    MenuDrinkTwoFourFragment fragment = MenuDrinkTwoFourFragment.newInstance();
                    Bundle newbundle = new Bundle();
                    newbundle.putString("TableID", refId);
                    fragment.setArguments(bundle);

                    changeBackgroundColor(filterOnDrink24);
                    changeBackgroundColorToOriginal(filterOnNormalDrink);
                    changeBackgroundColorToOriginal(filterOnFood);
                    changeBackgroundColorToOriginal(filterOnDrink35);
                    ((AppCompatActivity) v.getContext())
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.containerButtonSearchEventView, fragment, null)
                            .addToBackStack(null)
                            .commit();

                });

        filterOnDrink35.setOnClickListener(
                v -> {
                    MenuDrinkThreeFiveFragment fragment = MenuDrinkThreeFiveFragment.newInstance();
                    Bundle newbundle = new Bundle();
                    newbundle.putString("TableID", refId);
                    fragment.setArguments(bundle);

                    changeBackgroundColor(filterOnDrink35);
                    changeBackgroundColorToOriginal(filterOnFood);
                    changeBackgroundColorToOriginal(filterOnNormalDrink);
                    changeBackgroundColorToOriginal(filterOnDrink24);

                    ((AppCompatActivity) v.getContext())
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(
                                    R.id.containerButtonSearchEventView,
                                    fragment,
                                    null)
                            .addToBackStack(null)
                            .commit();
                });
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    /**
     * The methode changes the background color when man clicks a button
     *
     * @param AppCompatButton button to click
     */
    public void changeBackgroundColor(AppCompatButton button) {
        button.setBackgroundTintMode(PorterDuff.Mode.ADD);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    /**
     * The methode changes the background color when man clicks a button
     *
     * @param AppCompatButton button to click
     */
    public void changeBackgroundColorToOriginal(AppCompatButton button) {
        button.setBackgroundTintMode(PorterDuff.Mode.CLEAR);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_menu, container, false);
    }
}
