package hackathon.sg.potf;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCartProduct extends Fragment {
    TextView txtViewProductName,txtViewProductQuantity,txtViewProductPrice;
    ImageView imgViewProduct;

    public FragmentCartProduct() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cart_product, container, false);
        txtViewProductName = v.findViewById(R.id.txtViewProductName);
        txtViewProductQuantity = v.findViewById(R.id.txtViewProductQuantity);
        txtViewProductPrice = v.findViewById(R.id.txtViewProductPrice);
        imgViewProduct = v.findViewById(R.id.imgViewProduct);
        return v;
    }

}
