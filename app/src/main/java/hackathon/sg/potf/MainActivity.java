package hackathon.sg.potf;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageButton buttonQR;
    Activity activity;
    TextView textViewName, textViewBalance, textViewNavName, textViewNavEmail;
    private String twizoAPI = "twizo:7cf2E7HI2zjSQnRME2Oss0hvBJV1XGtC0bYRlojZPOV6IIsT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setOrientationLocked(true);
                integrator.initiateScan();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        buttonQR = findViewById(R.id.imageButtonQR);
        activity = this;
        buttonQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setOrientationLocked(true);
                integrator.initiateScan();
            }
        });

        textViewName = findViewById(R.id.textViewName);
        textViewBalance = findViewById(R.id.textViewBalance);
        getUserInfo();

        ImageButton imageButton = findViewById(R.id.imageButtonTopUp);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity,TopUpActivity.class);
                startActivity(i);
            }
        });
        ImageButton imageButton1 = findViewById(R.id.imageButtonHistory);
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, PurchaseHistory.class);
                startActivity(i);
            }
        });
        //uploadProduct();
    }

    private void getUserInfo(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference databaseUser = FirebaseDatabase.getInstance().getReference("User").child(uid);
        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textViewName.setText("Welcome back " + dataSnapshot.child("name").getValue().toString()+"!");
                textViewBalance.setText("Balance: SGD "+dataSnapshot.child("balance").getValue().toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        textViewNavName = findViewById(R.id.textViewNavName);
        textViewNavEmail = findViewById(R.id.textViewNavEmail);
        textViewNavName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        textViewNavEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent i = new Intent(activity,AddItem.class);
            startActivity(i);
        } else if (id == R.id.nav_gallery) {
            Intent i = new Intent(activity, ProductList.class);
            startActivity(i);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            Intent i = new Intent(activity, CartList.class);
            startActivity(i);

        } else if (id == R.id.nav_share) {
            createOTPQR();
        } else if (id == R.id.nav_send) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent i = new Intent(getApplicationContext(), LauncherActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                showToast(R.string.sign_out_failed);
                            }
                        }
                    });
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void createOTPQR() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String urlDelete = "https://api-asia-01.twizo.com/totp/"+FirebaseAuth.getInstance().getCurrentUser().getUid();
        JsonObjectRequest jsonObjectRequestDlt = new JsonObjectRequest(Request.Method.DELETE, urlDelete, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=utf8");
                headers.put("Authorization","Basic "+encodeKey(twizoAPI));
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequestDlt);
        String url = "https://api-asia-01.twizo.com/totp";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("identifier",FirebaseAuth.getInstance().getCurrentUser().getUid()); //Need to change the name
            jsonObject.put("issuer","ScanPod");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Need to implement QR to show the TOTP for user to scan
                //textView.setText(response.toString());
                try {
                    String uri = response.getString("uri");
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("OTP").child(FirebaseAuth.getInstance().getUid());
                    databaseReference.setValue(uri);
                    Intent intent = new Intent(getApplicationContext(), SuccessPaymentActivity.class);
                    intent.putExtra("OTP", uri);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //textView.setText(error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=utf8");
                headers.put("Authorization","Basic "+encodeKey(twizoAPI));
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    public String encodeKey(String text){
        // Sending side
        byte[] data = new byte[0];
        try {
            data = text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(data, Base64.DEFAULT).replace("\n","");
    }

    @MainThread
    private void showToast(@StringRes int errorMessageRes) {
        Toast.makeText(getApplicationContext(), errorMessageRes, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                //Scan cancelled
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //Scan successful
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), ProductDetail.class);
                intent.putExtra("product", result.getContents());
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}
