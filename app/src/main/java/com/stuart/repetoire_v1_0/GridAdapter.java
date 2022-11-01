package com.stuart.repetoire_v1_0;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class GridAdapter extends ArrayAdapter<String> {
    public GridAdapter(@NonNull Context context, ArrayList<String> list) {
        super(context, 0, list);
    }
}
