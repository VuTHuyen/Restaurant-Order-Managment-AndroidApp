package com.example.orderrestaurantapp.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.orderrestaurantapp.MainActivity;
import com.example.orderrestaurantapp.R;
import com.example.orderrestaurantapp.menu.Database;
import com.example.orderrestaurantapp.ui.TopAppBar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PaymentFragment extends Fragment {
    private FirebaseFirestore database;

    private CardView payment_cash;
    private CardView payment_card;
    String date;
    String price;
    String refID;
    public static PaymentFragment newInstance() {
        PaymentFragment fragment = new PaymentFragment();
        return fragment;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        payment_cash = view.findViewById(R.id.payment_cash);
        payment_card = view.findViewById(R.id.payment_card);
        Bundle bundle = this.getArguments();
        database = FirebaseFirestore.getInstance();
        Context context = this.getContext();


        if (bundle.getString("TheCurrentDate") != null && bundle.getString("FinalPrice") != null && bundle.getString("TableID") != null) {
            date = bundle.getString("TheCurrentDate");
            price = bundle.getString("FinalPrice");
            refID = bundle.getString("TableID");
        }
        com.google.android.material.appbar.MaterialToolbar topAppBar =
                getActivity().findViewById(R.id.topAppBar);
        TopAppBar.setAppBarProperties(refID, context, topAppBar);
        payment_cash.setOnClickListener(
                v -> {
                    Database.putFinanceInFirebase(database,price,"Cash", date);
                    Database.deleteCourse(database, refID, getActivity(), getResources());
                });
        payment_card.setOnClickListener(
                v -> {
                    Database.putFinanceInFirebase(database,price,"Card", date);
                    Database.deleteCourse(database, refID, getActivity(), getResources());
                });
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment, container, false);
    }

}

