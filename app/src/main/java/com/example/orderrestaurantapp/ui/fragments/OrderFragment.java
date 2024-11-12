package com.example.orderrestaurantapp.ui.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.orderrestaurantapp.menu.Database;
import com.example.orderrestaurantapp.menu.Order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderrestaurantapp.MainActivity;
import com.example.orderrestaurantapp.R;
import com.example.orderrestaurantapp.menu.Value;
import com.example.orderrestaurantapp.ui.adapters.OrderAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class OrderFragment extends Fragment {
    private OrderAdapter adapter;
    private FirebaseFirestore database;
    TextView total_price_orders;
    TextView price_sale;
    TextView price_after_sale;
    String price1;
    String price2;
    String price3;
    boolean orderNotEmpty;
    public static OrderFragment newInstance() {
        OrderFragment fragment = new OrderFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database = FirebaseFirestore.getInstance();

        setAppBarProperties(view);

    }

    public void getDatabase(View view, String refId) {
        database
                .collection(refId)
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(
                        new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(
                                    @Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                    // Handle the error
                                    Log.e("Firestore error", error.getMessage());
                                    return;
                                }

                                if (value != null) {
                                    ArrayList<Value> dataList = new ArrayList<>();
                                    for (DocumentSnapshot document : value.getDocuments()) {
                                        dataList.add(new Value(document.getId(), document.getData()));
                                    }
                                    if (!dataList.isEmpty()) {
                                        List<Order> orderList = Database.getListOrder(dataList);
                                        setOrderNotEmpty(true);
                                        price1 = Order.getTotalPrice(orderList);
                                        System.out.println("-------------price total order------------"+price1);
                                        setPrice(view, price1);
                                        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewOrder);
                                        adapter = new OrderAdapter(view.getContext(), orderList, refId, getActivity(), getResources());
                                        recyclerView.setAdapter(adapter);
                                        recyclerView.setLayoutManager(
                                                new androidx.recyclerview.widget.LinearLayoutManager(getContext()));
                                    }
                                }
                            }
                        });

    }
    public void setPrice(View view, String price1){
        total_price_orders = view.findViewById(R.id.total_price_orders);
        price_sale = view.findViewById(R.id.sale);
        price_after_sale = view.findViewById(R.id.total_after_sale);
        System.out.println("------------onViewCreated--price1------" + price1);
        total_price_orders.setText(price1);
        price2 = Order.getRabatt(price1, 0);
        price3 = Order.getFinalPrice(price1, price2);
        price_sale.setText(price2);
        price_after_sale.setText(price3);
    }
    public void setOrderNotEmpty(boolean isNotEmpty){
        orderNotEmpty = isNotEmpty;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_table_order, container, false);
    }
    private void setAppBarProperties(View view) {
        Context context = this.getContext();
        String refId = "";
        Bundle bundle = this.getArguments();
        if (bundle.getString("TableID") != null) {
            refId = bundle.getString("TableID");
            getDatabase(view, refId);
        }
        com.google.android.material.appbar.MaterialToolbar topAppBar =
                getActivity().findViewById(R.id.topAppBar);
        topAppBar.setTitle("Orders für " + refId);
        topAppBar.getMenu().getItem(0).setIcon(R.drawable.ic_food_menu__icon);
        topAppBar.getMenu().getItem(1).setIcon(R.drawable.ic_printer_svgrepo_com);
        topAppBar.getMenu().getItem(2).setIcon(R.drawable.ic_money_euro_svgrepo_com);

        topAppBar.getMenu().getItem(0).setVisible(true);
        topAppBar.getMenu().getItem(1).setVisible(true);
        topAppBar.getMenu().getItem(2).setVisible(true);
        if (MainActivity.actionMode != null) {
            MainActivity.actionMode.finish();
        }
        String finalRefId = refId;
        topAppBar
                .getMenu()
                .getItem(0)
                .setOnMenuItemClickListener(
                        new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                SearchMenuFragment fragment = SearchMenuFragment.newInstance();
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
                .getItem(1)
                .setOnMenuItemClickListener(
                        new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                if (orderNotEmpty) {
                                    PrintButtonFragment fragment = PrintButtonFragment.newInstance();
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
                                else {
                                    Database.setSnackbar(view.getRootView().findViewById(R.id.containerView), getResources().getString(R.string.order_empty));
                                    return false;
                                }
                            }
                        });

            topAppBar
                    .getMenu()
                    .getItem(2)
                    .setOnMenuItemClickListener(
                            new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    if (orderNotEmpty) {
                                        PaymentFragment fragment = PaymentFragment.newInstance();
                                        Bundle bundle = new Bundle();
                                        @SuppressLint("SimpleDateFormat")
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                        String currentDate = sdf.format(new Date());
                                        System.out.println("current date------------" + currentDate);
                                        System.out.println("order final price------------" + price3);

                                        bundle.putString("TheCurrentDate", currentDate);
                                        bundle.putString("FinalPrice", price3);
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
                                    }else {
                                        Database.setSnackbar(view.getRootView().findViewById(R.id.containerView), getResources().getString(R.string.order_empty));
                                        return false;
                                    }
                                }
                            });
    }

}
