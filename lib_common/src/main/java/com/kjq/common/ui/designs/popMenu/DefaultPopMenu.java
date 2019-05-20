package com.kjq.common.ui.designs.popMenu;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;


import com.kjq.common.R;

import java.util.ArrayList;

public class DefaultPopMenu<Model extends PopMenuModel> extends PopMenu<Model> {


    public DefaultPopMenu(Activity activity) {
        super(activity);
    }

    @Override
    protected View onCreateView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.common_menu_default,null);
    }

    @Override
    protected RecyclerView findById(View view) {
        return view.findViewById(R.id.common_mDefault_rV_content);
    }

    @Override
    protected BaseAdapter onCreateAdapter(Context context, ArrayList itemList) {
        return new DefaultAdapter(context,itemList,R.layout.designs_item_menu_default);
    }
}
