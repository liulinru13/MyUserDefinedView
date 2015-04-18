package mmrx.com.myuserdefinedview.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mmrx.com.myuserdefinedview.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment04 extends Fragment {

    private TextView mText;
    private View mView;
    public MyFragment04() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment04_my_layout, container, false);
        mText = (TextView)mView.findViewById(R.id.fragment04_text);
        mText.setText("04");
        return mView;
    }


}
