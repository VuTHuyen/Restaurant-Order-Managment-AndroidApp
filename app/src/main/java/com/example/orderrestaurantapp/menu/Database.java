package com.example.orderrestaurantapp.menu;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderrestaurantapp.MainActivity;
import com.example.orderrestaurantapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {

    /**
     * This method create all tables in restaurant
     * @return a list of table of restaurant
     */
    public static List<Table> getStartTable(){
        List<Table>  startTable = new ArrayList<Table>();
        startTable.add(new Table ("Tisch 1", false, false));
        startTable.add(new Table ("Tisch 2", false, false));
        startTable.add(new Table ("Tisch 3", false, false));
        startTable.add(new Table ("Tisch 4", false, false));
        startTable.add(new Table ("Tisch 5", false, false));
        startTable.add(new Table ("Tisch 6", false, false));
        startTable.add(new Table ("Tisch 7", false, false));
        startTable.add(new Table ("Tisch 8", false, false));
        startTable.add(new Table ("Tisch 9", false, false));
        startTable.add(new Table ("Tisch 10", false, false));

        startTable.add(new Table ("Tisch 11", false, false));
        startTable.add(new Table ("Tisch 12", false, false));
        startTable.add(new Table ("Tisch 13", false, false));
        startTable.add(new Table ("Tisch 14", false, false));
        startTable.add(new Table ("Tisch 15", false, false));
        startTable.add(new Table ("Tisch 16", false, false));
        startTable.add(new Table ("Tisch 17", false, false));
        startTable.add(new Table ("Tisch 18", false, false));
        startTable.add(new Table ("Tisch 19", false, false));
        startTable.add(new Table ("Tisch 20", false, false));
        startTable.add(new Table ("Tisch 21", false, false));
        startTable.add(new Table ("Tisch 22", false, false));
        startTable.add(new Table ("Tisch 23", false, false));
        startTable.add(new Table ("Tisch 24", false, false));
        startTable.add(new Table ("Tisch 25", false, false));

        startTable.add(new Table ("Tisch 70", false, false));
        startTable.add(new Table ("Tisch 71", false, false));
        startTable.add(new Table ("Tisch 72", false, false));
        startTable.add(new Table ("Tisch 73", false, false));
        startTable.add(new Table ("Tisch 74", false, false));
        startTable.add(new Table ("Tisch 75", false, false));
        startTable.add(new Table ("Tisch 76", false, false));
        startTable.add(new Table ("Tisch 77", false, false));
        startTable.add(new Table ("Tisch 78", false, false));
        startTable.add(new Table ("Tisch 79", false, false));
        startTable.add(new Table ("Tisch 80", false, false));

        return startTable;

    }

    /**
     * This method put the orders of a table in the database using FirebaseFirestore
     * @param database is FirebaseFirestore
     * @param order is a Order of the table with type Order
     * @param refId is a String, a table ID
     * @param activity FragmentActivity
     * @param resources Resources
     */
    public static void putDataInFirebase (FirebaseFirestore database, Order order, String refId, FragmentActivity activity, android.content.res.Resources resources){
        Map<String, Object> data = new HashMap<>();
        data.put("Ordername", order.getOrdername());
        data.put("NumberOfOrder", order.getNumber());
        data.put("PriceOfOrder", order.getPrice());
        data.put("TotalPrice", order.getTotalPrice());

        data.put("OrderWish", order.getwishfood_typdrink());
        data.put("Removeable", order.getRemovable());
        data.put("timestamp", com.google.firebase.Timestamp.now());
        data.put("Printed", order.getPrinted());
        database
                .collection(refId)
                .add(data)
                .addOnSuccessListener(
                        new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                //getDatabase(refId, view);
                                setSnackbar(activity.findViewById(R.id.container), resources.getString(R.string.data_add_success));

                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                setSnackbar(activity.findViewById(R.id.container), resources.getString(R.string.data_add_failed));

                            }
                        });
    }

    /**
     * This method put the paid table on a day in to database
     * @param database
     * @param finalPrice
     * @param payment
     * @param date
     */

    public static void putFinanceInFirebase (FirebaseFirestore database, String finalPrice, String payment, String date){
        Map<String, Object> data = new HashMap<>();
        data.put("Price", finalPrice);
        data.put("Payment", payment);
        data.put("timestamp", com.google.firebase.Timestamp.now());
        database
                .collection(date)
                .add(data)
                .addOnSuccessListener(
                        new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                //getDatabase(refId, view);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
    }

    /**
     * This method delete a collection from the database
     * @param database
     * @param collectionID
     * @param activity
     * @param resources
     */
    public static void deleteCourse(FirebaseFirestore database, String collectionID, FragmentActivity activity, android.content.res.Resources resources) {
        database.collection(collectionID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        WriteBatch batch = database.batch();
                        for (DocumentSnapshot document : task.getResult()) {
                            batch.delete(document.getReference());
                        }
                        batch.commit()
                                .addOnSuccessListener(result -> {
                                    database.collection(collectionID)
                                            .document()
                                            .delete()
                                            .addOnSuccessListener(v -> {
                                                setSnackbar(activity.findViewById(R.id.container), resources.getString(R.string.data_delete_success));
                                            })
                                            .addOnFailureListener(e -> {
                                                setSnackbar(activity.findViewById(R.id.container), resources.getString(R.string.data_delete_failed));
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    setSnackbar(activity.findViewById(R.id.container), resources.getString(R.string.data_delete_failed));
                                });
                    }
                });
    }

    public static void setSnackbar(View view, String message ){
        Snackbar snackbar;
        snackbar =
                Snackbar.make(
                        view,
                        message,
                        3300);
        View mView = snackbar.getView();
        // get textview inside snackbar view
        TextView mTextView =
                (TextView) mView.findViewById(com.google.android.material.R.id.snackbar_text);
        // set text to center
        mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        snackbar.show();
    }
    /**
     * this method returns a list of Orders.
     *
     * @param map ArrayList<Value>
     * @return List<Order>
     */

    public static List<Order> getListOrder(ArrayList<Value> map) {
        List<Order> orderList = new ArrayList<>();
        for (Value v : map) {
            String documentId = v.getId();

            String nameofOrder = (String) v.getMap().get("Ordername");
            Long numberOfOrder = (Long) v.getMap().get("NumberOfOrder");
            String priceOfOrder = (String) v.getMap().get("PriceOfOrder");
            String totalPrice = (String) v.getMap().get("TotalPrice");
            String wishofOrder = (String) v.getMap().get("OrderWish");
            boolean removeable = (Boolean) v.getMap().get("Removeable");
            boolean printed = (Boolean) v.getMap().get("Printed");

            Order order = new Order(nameofOrder, numberOfOrder, priceOfOrder, totalPrice, wishofOrder, removeable, documentId, printed);
            orderList.add(order);
        }
        return orderList;
    }


    /**
     * This method updates a value with the key Printed for a document in a collection
     * @param database
     * @param refId
     * @param docId
     * @param newPrintedValue
     */
    public static void updatePrintedInFirebase(FirebaseFirestore database, String refId, String docId, boolean newPrintedValue) {
        DocumentReference docRef = database.collection(refId).document(docId);

        docRef
                .update("Printed", newPrintedValue)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }


}
