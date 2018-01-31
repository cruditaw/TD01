package com.example.cdsm.td01;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RadioGroup radioStartDevise;
    private RadioGroup radioEndDevise;
    private RadioButton selectedStartDevise;
    private RadioButton selectedEndDevise;
    private EditText zoneMontant;
    private TextView zoneResultat;
    private TextView txtMessages;
    private TextView zoneTaux;
    private Button btnRaz;
    private Button btnConvert;


    NumberFormat formatter2dec = new DecimalFormat("#0.00");
    NumberFormat formatter5dec = new DecimalFormat("#0.00000");
    private final double USD = 1.18263;
    private final double CAD = 1.49901133;
    private final double GBP = 0.883918561;
    private final double YEN = 132.73064;
    private final double EURO = 1.0;

    private Locale currentLocale;

    private HashMap<String, Double> tauxMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initController();

        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleConvert();
            }
        });

        btnRaz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnRazOnClickListener();
            }
        });
    }

    private void initController() {
        radioStartDevise = ((RadioGroup) findViewById(R.id.radioStartDevise));
        radioEndDevise = ((RadioGroup) findViewById(R.id.radioEndDevise));
        zoneMontant = ((EditText) findViewById(R.id.zoneMontant));
        zoneResultat = ((TextView) findViewById(R.id.zoneResultat));
        zoneTaux = ((TextView) findViewById(R.id.zoneTaux));
        btnRaz = ((Button) findViewById(R.id.btnRaz));
        btnConvert = ((Button) findViewById(R.id.btnConvert));
        txtMessages = ((TextView) findViewById(R.id.txtMessages));

        tauxMap = new HashMap<>();
        tauxMap.put("USD", USD);
        tauxMap.put("CAD", CAD);
        tauxMap.put("GBP", GBP);
        tauxMap.put("YEN", YEN);
        tauxMap.put("EURO", EURO);

        currentLocale = Locale.getDefault();
    }

    private void btnRazOnClickListener() {
        resetErrorMessage();

        radioStartDevise.clearCheck();
        radioEndDevise.clearCheck();
        zoneMontant.setText("");
        zoneTaux.setText("");
        zoneResultat.setText("");
    }

    private void handleConvert() {
        if (radioStartDevise.getCheckedRadioButtonId() > 0) {
            resetErrorMessage();

            if (radioEndDevise.getCheckedRadioButtonId() > 0) {
                resetErrorMessage();

                if (!zoneMontant.getText().toString().isEmpty()) {
                    resetErrorMessage();

                    selectedStartDevise = ((RadioButton) findViewById(radioStartDevise.getCheckedRadioButtonId()));
                    selectedEndDevise = ((RadioButton) findViewById(radioEndDevise.getCheckedRadioButtonId()));
                    handleTaux();

                    double res = convert(Double.valueOf(zoneMontant.getText().toString()), tauxMap.get(selectedStartDevise.getText().toString()), tauxMap.get(selectedEndDevise.getText().toString()));
                    zoneResultat.setText(String.valueOf(formatter2dec.format(res)));

                } else {
                    showErrorMessage("Le montant est requis !");
                }

            } else {
                showErrorMessage("Une devise de conversion est requise !");
            }

        } else {
            showErrorMessage("Une devise de départ est requise !");
        }
    }

    private void handleTaux() {
        double ts = tauxMap.get(selectedEndDevise.getText().toString());
        double te = tauxMap.get(selectedStartDevise.getText().toString());

        StringBuilder sb = new StringBuilder();
        sb.append("1 ").append(selectedStartDevise.getText().toString()).append(" = ");
        sb.append(formatter5dec.format(ts / te)).append(" ").append(selectedEndDevise.getText().toString());

        zoneTaux.setText(sb.toString());
    }

    private double convert(double val, double tauxStart, double tauxEnd) {


        double result = val * tauxEnd / tauxStart;
        System.out.println("convert -> usdVal= " + result + " , val= " + val + " , tauxStart= " + tauxStart + " , tauxEnd= " + tauxEnd);

        return result;
    }

    private void resetErrorMessage() {
        txtMessages.setText("");
        txtMessages.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    private void showErrorMessage(String message) {
        txtMessages.setText(message);
        txtMessages.setBackgroundColor(getResources().getColor(R.color.colorRed));
    }

}
