package hackathon.sg.potf;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPurchaseProduct extends Fragment {
    TextView txtViewProductName,txtViewProductQuantity;

    public FragmentPurchaseProduct() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_purchase_product, container, false);

        txtViewProductName = v.findViewById(R.id.txtViewProductName);
        txtViewProductQuantity = v.findViewById(R.id.txtViewProductQuantity);

        return v;
    }

}
