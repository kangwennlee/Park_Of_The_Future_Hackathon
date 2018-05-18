package hackathon.sg.potf;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPurchaseHistory extends Fragment {
    Button btnManage;
    TextView txtViewPurchaseId,txtViewPurchaseDate;
    ListView listViewPurchaseProduct;
    LinearLayout layoutProd;

    public FragmentPurchaseHistory() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_purchase_history, container, false);

        btnManage = v.findViewById(R.id.btnManage);
        txtViewPurchaseId = v.findViewById(R.id.txtViewPurchaseId);
        txtViewPurchaseDate = v.findViewById(R.id.txtViewPurchaseDate);
        listViewPurchaseProduct = v.findViewById(R.id.listViewPurchaseProduct);
        layoutProd = v.findViewById(R.id.layoutProd);
        return v;
    }
}
