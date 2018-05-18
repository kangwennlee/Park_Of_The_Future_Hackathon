package hackathon.sg.potf;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import hackathon.sg.potf.FirebaseClass.*;

public class ProductDetail extends AppCompatActivity {


    TextView txtViewProductName,txtViewProductCategory, txtViewProductPrice;
    ImageView imgViewProduct;
    Button btnProceed,btnCancel;
    //String[] prodDetail;
    Product product;
    private int dialogTheme;
    private String twizoAPI = "twizo:7cf2E7HI2zjSQnRME2Oss0hvBJV1XGtC0bYRlojZPOV6IIsT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        getSupportActionBar().setTitle("Product Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txtViewProductName = findViewById(R.id.txtViewProductName);
        txtViewProductCategory = findViewById(R.id.txtViewProductCategory);
        txtViewProductPrice = findViewById(R.id.txtViewProductPrice);
        imgViewProduct = findViewById(R.id.imgViewProduct);
        btnProceed = findViewById(R.id.btnProceed);
        btnCancel = findViewById(R.id.btnCancel);
        //prodDetail = new String[4];
        product = new Product();

        Intent intent = getIntent();
        String id = intent.getStringExtra("product");

        retrieveProductDetail(id);
        Fabric.with(this, new Answers(), new Crashlytics());

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addCart();
                requestOTP();
            }
        });
    }

    private void requestOTP(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View v = inflater.inflate(R.layout.dialog_otp, null);
        builder.setView(v)
                // Add action buttons
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        TextView textView = v.findViewById(R.id.username);
                        verifyTotp(textView.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //LoginDialogFragment.this.getDialog().cancel();
                        dialog.dismiss();
                    }
                });
        builder.show();
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

    private void verifyTotp(String token){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://api-asia-01.twizo.com/totp/hackathon2?token="+token;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_SHORT).show();
                addCart();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if(error.networkResponse.statusCode==422){
                    Toast.makeText(getApplicationContext(),"Invalid OTP Code",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),String.valueOf(error.networkResponse.statusCode),Toast.LENGTH_SHORT).show();
                }
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



    private void addCart() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Cart cart = dataSnapshot.getValue(Cart.class);
                ArrayList<Product> products;
                if (cart != null) {
                    products = cart.getCarts();
                    products.add(product);
                } else {
                    products = new ArrayList<>();
                    products.add(product);
                }
                Double total = 0.0;
                for (Product product : products) {
                    total += product.getProductPrice();
                }
                Cart newCart = new Cart(products, total);
                databaseReference.setValue(newCart);
                Toast.makeText(getApplicationContext(), "Added to cart!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(),CartList.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void retrieveProductDetail(final String id){
        DatabaseReference databaseProduct = FirebaseDatabase.getInstance().getReference("Product").child(id);
        databaseProduct.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                product = dataSnapshot.getValue(Product.class);
                txtViewProductName.setText(product.getProductName());
                txtViewProductCategory.setText(product.getProductCategory());
                txtViewProductPrice.setText("SGD " + product.getProductPrice());
                FirebaseStorage.getInstance().getReference().child("Product").child(dataSnapshot.child("productId").getValue().toString()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        BitmapDownloaderTask task = new BitmapDownloaderTask(imgViewProduct);
                        task.execute(uri.toString());
                    }
                });
                Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName("Product Detail")
                        .putCustomAttribute("Product Name", product.getProductName())
                );
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
