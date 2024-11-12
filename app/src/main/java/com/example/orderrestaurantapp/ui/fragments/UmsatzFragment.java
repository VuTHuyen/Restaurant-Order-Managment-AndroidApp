package com.example.orderrestaurantapp.ui.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderrestaurantapp.MainActivity;
import com.example.orderrestaurantapp.R;
import com.example.orderrestaurantapp.menu.Database;
import com.example.orderrestaurantapp.menu.Order;
import com.example.orderrestaurantapp.menu.OrderInfo;
import com.example.orderrestaurantapp.menu.Value;
import com.example.orderrestaurantapp.ui.adapters.OrderAdapter;
import com.example.orderrestaurantapp.ui.adapters.UmsatzAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.bouncycastle.jcajce.provider.symmetric.TEA;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UmsatzFragment extends Fragment {
    UmsatzAdapter adapter;
    EditText calendarView;
    FirebaseFirestore database;
    DatePickerDialog datePickerDialog;
    TextView total_profit_a_day;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database = FirebaseFirestore.getInstance();

        calendarView = view.findViewById(R.id.calendar);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = sdf.format(new Date());
        calendarView.setText(currentDate);
        setAppBarProperties();
        String date = calendarView.getText().toString();
        getDatabase(view,date);
        /**
        calendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(view.getRootView().getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                String selectedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                System.out.println("------------selectedDate -------" + selectedDate);

                                calendarView.setText(selectedDate);
                                getDatabase(view, selectedDate);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
         */


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_turnover_date, container, false);
    }

    public void getDatabase(View view, String date) {
        database
                .collection(date)
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
                                        List<OrderInfo> orderList = getListOrderInfo(dataList);
                                        /**
                                        System.out.println("----------length of list order info ---------"+ orderList.size());
                                        for (OrderInfo orderInfo:orderList){
                                            System.out.println("---order Info---" + orderInfo.getTotal_price());
                                            System.out.println("---order Info---" + orderInfo.getTimestamp());

                                        }
                                         */
                                        setPrice(view, OrderInfo.getTotalPriceADay(orderList));
                                        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewTurnover);
                                        adapter = new UmsatzAdapter(view.getContext(), orderList);
                                        recyclerView.setAdapter(adapter);
                                        recyclerView.setLayoutManager(
                                                new androidx.recyclerview.widget.LinearLayoutManager(getContext()));


                                    }
                                }
                            }
                        });

    }
    public List<OrderInfo> getListOrderInfo(ArrayList<Value> map) {
        List<OrderInfo> orderList = new ArrayList<>();
        for (Value v : map) {

            String payment = (String) v.getMap().get("Payment");
            String totalPrice = (String) v.getMap().get("Price");
            String timestamp =
                    ((com.google.firebase.Timestamp) v.getMap().get("timestamp")).toDate().toString();

            OrderInfo order = new OrderInfo(timeFirebaseFormater(timestamp), totalPrice, payment);
            orderList.add(order);
        }
        return orderList;
    }
    public String timeFirebaseFormater(String time){
        System.out.println("time "+ time);
        StringBuilder date_time = new StringBuilder();
        String[] time_split = time.split(" ");

        date_time.append(time_split[0])
                .append(" ")
                .append(time_split[1])
                .append(" ")
                .append(time_split[2])
                .append(" ")
                .append(time_split[3]);

        System.out.println("----time----"+ date_time);
        return date_time.toString();
    }
    public void setPrice(View view, String totalprice){
        total_profit_a_day = view.findViewById(R.id.total_price_umsatz);

        total_profit_a_day.setText(totalprice);
    }
    private void setAppBarProperties() {
        com.google.android.material.appbar.MaterialToolbar topAppBar =
                getActivity().findViewById(R.id.topAppBar);
        topAppBar.setTitle("Umsatz");
        topAppBar.getMenu().getItem(0).setVisible(false);
        topAppBar.getMenu().getItem(1).setVisible(false);
        topAppBar.getMenu().getItem(2).setVisible(false);
        if (MainActivity.actionMode != null) {
            MainActivity.actionMode.finish();
        }

    }
}
