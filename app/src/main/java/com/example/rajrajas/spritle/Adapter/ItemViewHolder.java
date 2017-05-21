package com.example.rajrajas.spritle.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.rajrajas.spritle.R;
import com.example.rajrajas.spritle.databinding.CommentsListBinding;
import com.example.rajrajas.spritle.databinding.StatusRecyclerviewBinding;
import com.example.rajrajas.spritle.model.Status_item;

/**
 * Created by rajrajas on 5/3/2017.
 */

public class ItemViewHolder extends RecyclerView.ViewHolder
{
    private StatusRecyclerviewBinding Item_binding=null;
    private Context context;
    private CommentsListBinding commentsListBinding=null;

    ItemViewHolder(Context context, StatusRecyclerviewBinding binding) {
        super(binding.getRoot());
        this.Item_binding = binding;
        this.context = context;
    }

    ItemViewHolder(Context context, CommentsListBinding binding) {
        super(binding.getRoot());
        this.commentsListBinding = binding;
        this.context = context;
    }

    void bind(Status_item item_data)
    {
        Resources res = context.getResources();
        String mDrawableName = item_data.getDrawable_name();
        Drawable drawable=null;
        if(!mDrawableName.equals("dummy"))
        {
            int resID = res.getIdentifier(mDrawableName, "mipmap", context.getPackageName());
            drawable = res.getDrawable(resID);
        }



        if(Item_binding!=null)
        {
            Item_binding.setItem(item_data);
            if (!mDrawableName.equals("dummy")) {
                Item_binding.profileImage.setImageDrawable(drawable);
            }
            Item_binding.updateTxt.setMovementMethod(LinkMovementMethod.getInstance());
            Item_binding.updateTxt.setText(Html.fromHtml(item_data.getStatustext()));


        }
        else if(commentsListBinding!=null)
        {
            commentsListBinding.setItem(item_data);
            if (!mDrawableName.equals("dummy")) {
                commentsListBinding.profileImage.setImageDrawable(drawable);
            }
            commentsListBinding.updateTxt.setText(Html.fromHtml(item_data.getStatustext()));
        }

    }

    public void showToast(String areaName)
    {
        Toast.makeText(context, "You clicked on "+areaName, Toast.LENGTH_SHORT).show();
    }

}
