package com.example.orderrestaurantapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.orderrestaurantapp.menu.Order;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

public class InvoiceActivity extends AppCompatActivity {
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothSocket printerSocket;
    private OutputStream outputStream;
    private BluetoothDevice selectedPrinter; // Store the selected printer device here
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // ...

    public void connectToPrinter() {
        try {
            UUID printerUUID = MY_UUID; // Replace with your printer's UUID
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            printerSocket = selectedPrinter.createRfcommSocketToServiceRecord(printerUUID);
            printerSocket.connect();
            outputStream = printerSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printInvoice(String invoiceData) {
        try {
            // Convert the invoice data to bytes
            byte[] dataBytes = invoiceData.getBytes();

            // Send data to the printer
            outputStream.write(dataBytes);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String generateInvoiceData(List<Order> orderList) {
        StringBuilder invoiceData = new StringBuilder();
        // Format the order data
        for (Order order : orderList) {
            invoiceData.append(order.getNumber())
                    .append("x  ")
                    .append(order.getOrdername())
                    .append("       ")
                    .append(order.getTotalPrice())
                    .append("\n");
        }
        return invoiceData.toString();
    }

    // ...

    public void initializeBluetooth() {
        // Check if Bluetooth is enabled
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            // Bluetooth is not available or not enabled, show appropriate message or prompt user to enable Bluetooth
            Toast.makeText(this, "Bluetooth is not available or not enabled", Toast.LENGTH_SHORT).show();
        }
    }

    public void connectToSelectedPrinter() {
        if (selectedPrinter == null) {
            // No printer is selected, show appropriate message or prompt user to select a printer
            Toast.makeText(this, "No printer selected", Toast.LENGTH_SHORT).show();
        } else {
            connectToPrinter();
        }
    }

    public void printOrder(List<Order> orderList) {
        String invoiceData = generateInvoiceData(orderList);
        printInvoice(invoiceData);
    }

    // ...

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the connection and release resources
        try {
            if (printerSocket != null) {
                outputStream.close();
                printerSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}