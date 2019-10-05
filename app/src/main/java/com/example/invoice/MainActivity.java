package com.example.invoice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.text.NumberFormat;

// Imports the classes for the widgets
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

// Import listener
import android.widget.TextView.OnEditorActionListener;

// Import SharedPreferences class and its Editor class
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MainActivity extends AppCompatActivity
implements OnEditorActionListener {

    // Variables for Widgets
    private EditText invoiceAmount;
    private TextView discountPercent;
    private TextView discountAmount;
    private TextView totalDiscount;

    // SharedPreference Object
    private SharedPreferences savedValues;
    // String for saved subtotal
    private String invoiceSubtotal = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Getting references to the data displaying widgets
        invoiceAmount = (EditText) findViewById(R.id.editText);
        discountPercent = (TextView) findViewById(R.id.dP10);
        discountAmount = (TextView) findViewById(R.id.dA15);
        totalDiscount = (TextView) findViewById(R.id.t135);

        // Sets the listener
        invoiceAmount.setOnEditorActionListener(this);

        // Get SharedPreferences object
        savedValues= getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    @Override
    public void onPause() {
        // save the instance variables
        Editor editor = savedValues.edit();
        editor.putString("invoiceSubtotal", invoiceSubtotal);
        editor.commit();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        // get the instance variables
        invoiceSubtotal = savedValues.getString("invoiceSubtotal", "");

        // set the subtotal amount
        invoiceAmount.setText(invoiceSubtotal);

        // calls the calculateAndDisplay method
        calculateAndDisplay();
    }

    public void calculateAndDisplay()  {

            // Local variables
            String subTotalString = invoiceAmount.getText().toString();
            float subTotal, dPercent, dAmount, total;

            // Gets the invoice amount
            if (subTotalString.equals("")){
                subTotal = 0;
            }
            else {
                subTotal = Float.parseFloat(subTotalString);
            }

            /* Calculate discount percent: 20% on greater than or equal to $200
             * 10% on greater than or equal to $100
             * 0% on less than $100
             */
            if (subTotal >= 200.00){
                dPercent = .20f;
            }
            else if (subTotal >= 100.00 && subTotal < 200.00){
                dPercent = .10f;
            }
            else{
                dPercent = 0f;
            }

            // Calculate discount amount and total
            dAmount = subTotal * dPercent;
            total = subTotal - dAmount;

            // display the results
            NumberFormat currency = NumberFormat.getCurrencyInstance();
            discountAmount.setText(currency.format(dAmount));
            totalDiscount.setText(currency.format(total));

            NumberFormat percent = NumberFormat.getPercentInstance();
            discountPercent.setText(percent.format(dPercent));
    }

    @Override
    public boolean onEditorAction(TextView v, int actionID, KeyEvent event) {
        if (actionID == EditorInfo.IME_ACTION_DONE )
        {
            calculateAndDisplay();
        }
        return false;
    }

}
