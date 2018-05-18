package hackathon.sg.potf;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import hackathon.sg.potf.FirebaseClass.*;

public class PurchaseHistory extends AppCompatActivity {
    ListView listViewPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);
        getSupportActionBar().setTitle("Purchase History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listViewPurchase = findViewById(R.id.listViewPurchase);

        retrievePurchaseHistory();
    }

    private void retrievePurchaseHistory(){
        final ArrayList<Purchase> purchases = new ArrayList<>();
        DatabaseReference databaseProduct = FirebaseDatabase.getInstance().getReference().child("Purchase").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseProduct.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    purchases.add(ds.getValue(Purchase.class));
                    ds.getKey();
                }
                if(purchases.size() == 0){
                    Toast.makeText(getApplicationContext(), "Purchases Empty ! Please Purchase An Item First Before Proceed.", Toast.LENGTH_SHORT).show();
                }else{
                    PurchaseAdapter arrayAdapter = new PurchaseAdapter(getApplicationContext(), purchases);
                    arrayAdapter.addAll(purchases);
                    listViewPurchase.setAdapter(arrayAdapter);
                }
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

    public class PurchaseAdapter extends ArrayAdapter<Purchase> {
        ArrayList<Purchase> purchases;
        PurchaseAdapter(Context context, ArrayList<Purchase> purchases){
            super(context, R.layout.activity_purchase_history, R.id.listViewPurchase);
            this.purchases = purchases;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_purchase_history, parent, false);
            }
            Button btnManage = convertView.findViewById(R.id.btnManage);
            TextView txtViewPurchaseId = convertView.findViewById(R.id.txtViewPurchaseId);
            TextView txtViewPurchaseDate = convertView.findViewById(R.id.txtViewPurchaseDate);
            ListView listViewPurchaseProduct = convertView.findViewById(R.id.listViewPurchaseProduct);

            Purchase purchase = getItem(position);
            txtViewPurchaseId.setText(purchase.getPurchaseId());

            try {
                // trim and display date
                SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd_HHmmss");
                Date dateObj = formater.parse(purchase.getPurchaseId());
                txtViewPurchaseDate.setText(DateFormat.format("dd/MM/yyyy HH:mm", dateObj));
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ArrayList<Product> products = purchase.getProductPurchased().getCarts();
            ProductAdapter productArrayAdapter = new ProductAdapter(getContext(), products);
            productArrayAdapter.addAll(products);
            listViewPurchaseProduct.setAdapter(productArrayAdapter);
            //Toast.makeText(getContext(), products + "", Toast.LENGTH_LONG).show();

            // Future Improvement to view Purchase
            btnManage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            return convertView;
        }
    }

    public class ProductAdapter extends ArrayAdapter<Product> {
        ArrayList<Product> products;
        ProductAdapter(Context context, ArrayList<Product> products){
            super(context, R.layout.fragment_purchase_history, R.id.listViewPurchaseProduct);
            this.products = products;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_purchase_product, parent, false);
            }
            TextView txtViewProductName = convertView.findViewById(R.id.txtViewProductName);
            TextView txtViewProductQuantity = convertView.findViewById(R.id.txtViewProductQuantity);

            Product product = getItem(position);
            txtViewProductName.setText(product.getProductName());
            txtViewProductQuantity.setText("1");

            return convertView;
        }
    }
    public class ProductAdapter2 extends ArrayAdapter<Product> {
        String[] products;
        ProductAdapter2(Context context, ArrayList<Product> products){
            super(context, R.layout.fragment_purchase_history, R.id.listViewPurchaseProduct);
            this.products = products.toArray(new String[0]);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_purchase_product, parent, false);
            }
            TextView txtViewProductName = convertView.findViewById(R.id.txtViewProductName);
            TextView txtViewProductQuantity = convertView.findViewById(R.id.txtViewProductQuantity);

            Product product = getItem(position);
            txtViewProductName.setText(product.getProductName());
            txtViewProductQuantity.setText("1");

            return convertView;
        }
    }
}
