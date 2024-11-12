package com.example.orderrestaurantapp.ui.fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.orderrestaurantapp.R;
import com.example.orderrestaurantapp.menu.Database;
import com.example.orderrestaurantapp.menu.MenuReader;
import com.example.orderrestaurantapp.menu.Order;
import com.example.orderrestaurantapp.menu.Value;
import com.example.orderrestaurantapp.ui.TopAppBar;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import android.Manifest;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.Style;

public class PrintButtonFragment extends Fragment {

    private CardView printForHotMeal;
    private CardView printForSushi;
    private CardView printForBar;
    public static PrintButtonFragment newInstance() {
        PrintButtonFragment fragment = new PrintButtonFragment();
        return fragment;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final int PERMISSION_REQUEST_CODE_BLUETOOTH = 1;

    private BluetoothDevice printerDevice;
    private BluetoothSocket socket;
    private OutputStream out;
    private EscPos escpos;
    private List<Order> orders;
    private String refId;
    private String kitchen;
    @RequiresApi(api = Build.VERSION_CODES.S)
    public void settableId(String refId){
        this.refId = refId;
    }
    public void setOrdersPrint(List<Order> orders){
        this.orders = orders;
    }
    public void setKitchen(String kitchen){
        this.kitchen = kitchen;
    }
    public String getKitchen(){
        return this.kitchen;
    }
    public String getRefId(){
        return this.refId;
    }
    public List<Order> getOrdersPrint(){
        return this.orders;
    }
    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
      /**  BluetoothAdapter bluetoothAdapter;
        BluetoothSocket bluetoothSocket;
        BluetoothDevice bluetoothDevice;

        OutputStream outputStream;
        InputStream inputStream;
        Thread thread;

        byte[] readBuffer;
        int readBufferPosition;
        boolean stopWorker;
        */
        printForHotMeal = view.findViewById(R.id.print_hotmeal_orders);
        printForSushi = view.findViewById(R.id.print_sushi_orders);
        printForBar = view.findViewById(R.id.print_bar_order);
        AssetManager assetManager = this.getContext().getAssets();
        Bundle bundle = this.getArguments();
        String refId = bundle.getString("TableID");
        settableId(refId);
        Context context = this.getContext();
        com.google.android.material.appbar.MaterialToolbar topAppBar =
                getActivity().findViewById(R.id.topAppBar);
        TopAppBar.setAppBarProperties(refId, context, topAppBar);

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        List<String> allMenuHotMeal = MenuReader.getAllMenu(assetManager, "HotMeal.csv");
        List<String> allMenuSushi = MenuReader.getAllMenu(assetManager, "Sushi.csv");
        List<String> allMenuBar = MenuReader.getAllMenu(assetManager, "Bar.csv");

        List<Order> allOrdersOfTable = getOrdersForTable(database, refId);


        printForHotMeal.setOnClickListener(
                v -> {
                    setOrdersPrint(null);

                    List<Order> orders = getOrderForKitchen(allMenuHotMeal, allOrdersOfTable);
                    setKitchen("Warm");
                    setOrdersPrint(orders);

                    if (checkPermissions()) {
                        initBluetooth();
                    } else {
                        requestPermissions();
                    }
                    for (Order order: orders){
                        Database.updatePrintedInFirebase(database, refId, order.getDocumentOrderID(), true);

                    }

                });

        printForSushi.setOnClickListener(
                v -> {
                    setOrdersPrint(null);

                    List<Order> orders = getOrderForKitchen(allMenuSushi, allOrdersOfTable);
                    setKitchen("Sushi");
                    setOrdersPrint(orders);

                    if (checkPermissions()) {
                        initBluetooth();
                    } else {
                        requestPermissions();
                    }
                    for (Order order: orders){
                        Database.updatePrintedInFirebase(database, refId, order.getDocumentOrderID(), true);

                    }


                });
        printForBar.setOnClickListener(
                v -> {
                    setOrdersPrint(null);

                    List<Order> orders = getOrderForKitchen(allMenuBar, allOrdersOfTable);
                    setKitchen("Bar");
                    setOrdersPrint(orders);

                    if (checkPermissions()) {
                        initBluetooth();
                    } else {
                        requestPermissions();
                    }
                    for (Order order: orders){
                        Database.updatePrintedInFirebase(database, refId, order.getDocumentOrderID(), true);

                    }
                });

    }

    @RequiresApi(api = Build.VERSION_CODES.S)

    private List<Order> getOrderForKitchen(List<String> allMenu, List<Order> allOrdersOfTable){
        List<Order> result = new ArrayList<>();
        for(Order order: allOrdersOfTable){
            assert allMenu != null;
            System.out.println("-----printed----"+ order.getPrinted());
            if (allMenu.contains(order.getOrdername())){
                if(!order.getPrinted())
                    result.add(order);
            }

        }
        return result;
    }
    public List<Order> getOrdersForTable(FirebaseFirestore db, String refId) {
        List<Order> orderList = new ArrayList<>();
        db.collection(refId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            ArrayList<Value> dataList = new ArrayList<>();

                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                dataList.add(new Value(document.getId(), document.getData()));
                            }
                            if (!dataList.isEmpty()) {
                                orderList.addAll(Database.getListOrder(dataList));
                            }
                        }
                    }
                });
        return orderList;
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_print_button, container, false);
    }


    @RequiresApi(api = Build.VERSION_CODES.S)
    private boolean checkPermissions() {
        int result2 = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH);
        int result3 = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_ADMIN);
        int result4 = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT);

        return result2 == PackageManager.PERMISSION_GRANTED &&
                result3 == PackageManager.PERMISSION_GRANTED &&
                result4 == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.BLUETOOTH_CONNECT
                },
                PERMISSION_REQUEST_CODE_BLUETOOTH);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE_BLUETOOTH:
                if (grantResults.length > 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    initBluetooth();
                } else {
                    Log.i("Permission", "Bluetooth and/or location permission is denied");
                }
                break;
        }
    }

    private void initBluetooth() {
        SharedPreferences preferences = getContext().getSharedPreferences("PrinterPreferences", Context.MODE_PRIVATE);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        List<BluetoothDevice> pairedPrinterDevices = new ArrayList<>();
        for (BluetoothDevice device : pairedDevices) {
            pairedPrinterDevices.add(device);
        }

        String[] deviceNames = new String[pairedPrinterDevices.size()];
        for (int i = 0; i < pairedPrinterDevices.size(); i++) {
            deviceNames[i] = pairedPrinterDevices.get(i).getName();
        }

        new AlertDialog.Builder(getContext())
                .setTitle("Select Printer")
                .setSingleChoiceItems(deviceNames, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        printerDevice = pairedPrinterDevices.get(which);
                        dialog.dismiss();

                        // Save printer information to SharedPreferences
                        connectToPrinter();
                        /**
                         try {
                         if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                         // TODO: Consider calling
                         //    ActivityCompat#requestPermissions
                         // here to request the missing permissions, and then overriding
                         //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                         //                                          int[] grantResults)
                         // to handle the case where the user grants the permission. See the documentation
                         // for ActivityCompat#requestPermissions for more details.
                         return;
                         }
                         socket = printerDevice.createRfcommSocketToServiceRecord(MY_UUID);
                         socket.connect();
                         out = socket.getOutputStream();
                         escpos = new EscPos(out);
                         String output = generateInvoiceData(getOrdersPrint());
                         printInvoice(output, getRefId(), getKitchen());
                         } catch (IOException e) {
                         Log.e("Bluetooth", "Error in creating connection", e);
                         }
                         */
                    }
                })
                .show();
    }

    private void connectToPrinter(){
        try {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            socket = printerDevice.createRfcommSocketToServiceRecord(MY_UUID);
            socket.connect();
            out = socket.getOutputStream();
            escpos = new EscPos(out);
            String output = generateInvoiceData(getOrdersPrint());
            printInvoice(output, getRefId(), getKitchen());
        } catch (IOException e) {
            Log.e("Bluetooth", "Error in creating connection", e);
        }
    }



    public void printInvoice(String invoiceData, String table, String kitchen) {
        try {
            // Create a Style object
            Style style1 = new Style();
            style1.setFontName(Style.FontName.Font_B);
            style1.setBold(true);
            style1.setFontSize(Style.FontSize._3, Style.FontSize._3);

            // Create a Style object for the second line
            Style style2 = new Style();
            style2.setFontName(Style.FontName.Font_B);
            //style2.setBold(true);
            style2.setFontSize(Style.FontSize._2, Style.FontSize._2);
            //style2.setUnderline(Style.Underline.OneDotThick);

            StringBuilder refIDStyle = new StringBuilder();
            refIDStyle.append(table)
                    .append("-"+kitchen)
                    .append("\n");
            refIDStyle.append("--------------").append("\n");
            // Write text with your preferred output style
            escpos.write(style1, refIDStyle.toString());

            escpos.write(style2, invoiceData);


            escpos.feed(5).cut(EscPos.CutMode.FULL);
            escpos.close();
        } catch (IOException e) {
            Log.e("EscPos", "Error in printing", e);
        }
    }

    public String generateInvoiceData(List<Order> orderList) {
        StringBuilder invoiceData = new StringBuilder();
        if (!orderList.isEmpty()) {
            // Format the order data
            for (Order order : orderList) {
                invoiceData.append(order.getNumber())
                        .append("x  ")
                        .append(order.getOrdername())
                        .append("\n");
                invoiceData.append(order.getwishfood_typdrink()).append("\n");
            }
            return invoiceData.toString();
        } else return invoiceData.append("Keine Bestellung in dieser Küche").toString();
    }

    // ...
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (escpos != null) {
                escpos.close();
            }

            if (out != null) {
                out.close();
            }

            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            Log.e("Bluetooth", "Error in closing resources", e);
        }
    }

}
