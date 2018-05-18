package hackathon.sg.potf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jesusm.kfingerprintmanager.KFingerprintManager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class TopUpActivity extends AppCompatActivity {
    private static final String KEY = "KEY";
    Button btnNext, btnCancel;
    EditText txtAmount;
    Spinner spinBankType;
    double balance = 0, newBalance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);

        getSupportActionBar().setTitle("Top Up Wallet");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txtAmount = findViewById(R.id.txtAmount);
        spinBankType = findViewById(R.id.spinBankType);
        final ArrayList<String> cardArrayList = new ArrayList<>();
        cardArrayList.add("--Select a card--");
        Query query = FirebaseDatabase.getInstance().getReference().child("Card").child(FirebaseAuth.getInstance().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String cardNumber = userSnapshot.getKey().toString();
                    if (!cardArrayList.contains(cardNumber)) {
                        cardArrayList.add(cardNumber);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        cardArrayList.add("Add New Card");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cardArrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinBankType.setAdapter(adapter);
        spinBankType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String str = spinBankType.getItemAtPosition(i).toString();
                if (Objects.equals(str, "Add New Card")) {
                    Intent intent = new Intent(getApplicationContext(), AddCard.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = spinBankType.getSelectedItem().toString();
                String amt = txtAmount.getText().toString();
                if(!Objects.equals(str,"--Select a card--")&&!amt.equals("")&&!Objects.equals(str,"Add New Card")){
                    fingerAuth();
                }else{
                    if(amt.equals("")){
                        Toast.makeText(getApplicationContext(), "Please enter top up amount", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Please select a card.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Top Up cancelled.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void btnTopUpClicked() {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getUid());
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    balance = Double.parseDouble(dataSnapshot.child("balance").getValue().toString());
                    double amt = Double.parseDouble(txtAmount.getText().toString());
                    newBalance = balance + amt;
                    FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getUid()).child("balance").setValue(newBalance);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String currentDateandTime = sdf.format(new Date());
                    FirebaseDatabase.getInstance().getReference().child("TopUp").child(FirebaseAuth.getInstance().getUid()).child(currentDateandTime).child("TopUpAmount").setValue(amt);
                    FirebaseDatabase.getInstance().getReference().child("TopUp").child(FirebaseAuth.getInstance().getUid()).child(currentDateandTime).child("AmountBefore").setValue(balance);
                    FirebaseDatabase.getInstance().getReference().child("TopUp").child(FirebaseAuth.getInstance().getUid()).child(currentDateandTime).child("AmountAfter").setValue(newBalance);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Toast.makeText(getApplicationContext(), "Top Up Successful", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void fingerAuth() {
        createFingerprintManagerInstance().authenticate(new KFingerprintManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationSuccess() {
                //messageText.setText("Successfully authenticated");
                btnTopUpClicked();
            }

            @Override
            public void onSuccessWithManualPassword(@NotNull String password) {
                //messageText.setText("Manual password: " + password);
            }

            @Override
            public void onFingerprintNotRecognized() {
                //messageText.setText("Fingerprint not recognized");
                Toast.makeText(getApplicationContext(),"Fingerprint not recognized",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailedWithHelp(@Nullable String help) {
                //messageText.setText(help);
            }

            @Override
            public void onFingerprintNotAvailable() {
                //messageText.setText("Fingerprint not available");
            }

            @Override
            public void onCancelled() {
                //messageText.setText("Operation cancelled by user");
                Toast.makeText(getApplicationContext(),"Operation cancelled by user",Toast.LENGTH_SHORT).show();
            }
        }, getSupportFragmentManager());

    }

    private KFingerprintManager createFingerprintManagerInstance() {
        KFingerprintManager fingerprintManager = new KFingerprintManager(this, KEY);
        //fingerprintManager.setAuthenticationDialogStyle(dialogTheme);
        return fingerprintManager;
    }

}
