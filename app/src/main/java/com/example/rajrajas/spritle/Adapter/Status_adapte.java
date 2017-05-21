package com.example.rajrajas.spritle.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rajrajas.spritle.Activity.Status_activity;
import com.example.rajrajas.spritle.Comments_bottomSheet;
import com.example.rajrajas.spritle.Dummy;
import com.example.rajrajas.spritle.MyDbController;
import com.example.rajrajas.spritle.R;
import com.example.rajrajas.spritle.databinding.StatusRecyclerviewBinding;
import com.example.rajrajas.spritle.model.Status_item;

import java.util.Collections;
import java.util.List;

/**
 * Created by rajrajas on 5/3/2017.
 */

public class Status_adapte  extends RecyclerView.Adapter<ItemViewHolder>
{
    private List<Status_item> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    private FragmentManager fragmentManager;
    private Status_item l;
    MyDbController controller;
    private String[] like_change_array;

    public Status_adapte(Context context, List<Status_item> data, FragmentManager fragmentManager)
    {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.fragmentManager=fragmentManager;


    }
    public void delete(int position)
    {
        data.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

         final StatusRecyclerviewBinding statusRecyclerviewBinding = StatusRecyclerviewBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);

            return new ItemViewHolder(context, statusRecyclerviewBinding);

    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position)
    {
        controller = new MyDbController(Dummy.db_context);

        holder.bind(data.get(position));
        like_change_array=controller.get_query_stringArray("select Status_Message_id from Status_Message_likes  where Individual_id=5");

        if(!controller.get_query_string("select count(*) from Status_Message_likes  where Individual_id=5").equals("0"))
        {
            l = data.get(holder.getPosition());
            if(check_likes(l.getStatus_id()))
                change_like_icon(holder.itemView);
        }

        holder.itemView.findViewById(R.id.like_lay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                like_change_array=controller.get_query_stringArray("select Status_Message_id from Status_Message_likes  where Individual_id=5");
                l = data.get(holder.getPosition());
                if(check_likes(l.getStatus_id()))
                {
                    change_default_like_icon(holder.itemView);
                    controller.delete_Status_like(l.getStatus_id());
                    int like_cnt= Integer.parseInt(((TextView) holder.itemView.findViewById(R.id.like_cnt_txt)).getText().toString())-1;
                    ((TextView) holder.itemView.findViewById(R.id.like_cnt_txt)).setText(like_cnt+"");
                }
                else {
                    change_like_icon(holder.itemView);
                    controller.insert_Status_like(l.getStatus_id());
                    int like_cnt= Integer.parseInt(((TextView) holder.itemView.findViewById(R.id.like_cnt_txt)).getText().toString())+1;
                    ((TextView) holder.itemView.findViewById(R.id.like_cnt_txt)).setText(like_cnt+"");
                }

            }
        }
        );

        holder.itemView.findViewById(R.id.comment_lay).setOnClickListener(new View.OnClickListener()
                                                                          {
                                                                           @Override
                                                                           public void onClick(View view)
                                                                           {
                                                                               l = data.get(holder.getPosition());
                                                                               BottomSheetDialogFragment bottomSheetDialogFragment = new Comments_bottomSheet();
                                                                               Bundle args = new Bundle();
                                                                               args.putString("status_id",l.getStatus_id());
                                                                               bottomSheetDialogFragment.setArguments(args);
                                                                               bottomSheetDialogFragment.show(fragmentManager, bottomSheetDialogFragment.getTag());

                                                                           }
                                                                       }
        );

    }
    @Override
    public int getItemCount() {

        return data.size();
    }

    private void change_like_icon(View view)
    {
        ((ImageView)view.findViewById(R.id.heart_img)).setImageResource(R.mipmap.heart_primary);
        ((TextView) view.findViewById(R.id.like_text)).setTextColor(context.getResources().getColor(R.color.likeColor));
        ((TextView) view.findViewById(R.id.like_text)).setText("Liked");
    }

    private void change_default_like_icon(View view)
    {
        ((ImageView)view.findViewById(R.id.heart_img)).setImageResource(R.mipmap.heart_black);
        ((TextView) view.findViewById(R.id.like_text)).setTextColor(context.getResources().getColor(R.color.textColor));
        ((TextView) view.findViewById(R.id.like_text)).setText("Like");
    }
    private Boolean check_likes(String status_id)
    {
        for (String aLike_change_array : like_change_array)
        {
            if (status_id.equals(aLike_change_array))
            {
                return true;
            }
        }
        return false;
    }
}

