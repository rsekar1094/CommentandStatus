package com.example.rajrajas.spritle.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rajrajas.spritle.Dummy;
import com.example.rajrajas.spritle.MyDbController;
import com.example.rajrajas.spritle.R;
import com.example.rajrajas.spritle.databinding.CommentsListBinding;
import com.example.rajrajas.spritle.databinding.StatusRecyclerviewBinding;
import com.example.rajrajas.spritle.model.Status_item;

import java.util.Collections;
import java.util.List;

/**
 * Created by rajrajas on 5/4/2017.
 */

public class Comments_adapter extends RecyclerView.Adapter<ItemViewHolder>
{

    private List<Status_item> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    private  String[] like_change_array;
    MyDbController controller;
    private Status_item l;

    public Comments_adapter(Context context, List<Status_item> data)
    {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;


    }
    public void delete(int position)
    {
        data.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        final CommentsListBinding commentsListBinding = CommentsListBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);

        return new ItemViewHolder(context, commentsListBinding);

    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position)
    {
        controller = new MyDbController(Dummy.db_context);
        holder.bind(data.get(position));
        l = data.get(holder.getPosition());

        like_change_array=controller.get_query_stringArray("select Status_Message_comments_id from Status_Message_comments_likes  where Individual_id=5 and Status_Message_id="+l.getStatus_id());

        if(!controller.get_query_string("select count(*) from Status_Message_comments_likes  where Individual_id=5 and Status_Message_id="+l.getStatus_id()).equals("0"))
        {
            if(check_likes(l.getComment_id()))
                change_like_icon(holder.itemView);
        }

        holder.itemView.findViewById(R.id.like_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                l = data.get(holder.getPosition());
                like_change_array=controller.get_query_stringArray("select Status_Message_comments_id from Status_Message_comments_likes  where Individual_id=5 and Status_Message_id="+l.getStatus_id());

                if(check_likes(l.getComment_id()))
                {
                    change_default_like_icon(holder.itemView);
                    controller.delete_comments_like(l.getStatus_id(),l.getComment_id());
                    int like_cnt= Integer.parseInt(((TextView) holder.itemView.findViewById(R.id.like_cnt_txt)).getText().toString())-1;
                    ((TextView) holder.itemView.findViewById(R.id.like_cnt_txt)).setText(like_cnt+"");
                }
                else {
                    change_like_icon(holder.itemView);
                    controller.insert_comments_like(l.getStatus_id(),l.getComment_id());
                    int like_cnt= Integer.parseInt(((TextView) holder.itemView.findViewById(R.id.like_cnt_txt)).getText().toString())+1;
                    ((TextView) holder.itemView.findViewById(R.id.like_cnt_txt)).setText(like_cnt+"");
                }

            }
            });

    }
    @Override
    public int getItemCount()
    {
        return data.size();
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

    private void change_like_icon(View view)
    {
        ((Button) view.findViewById(R.id.like_bt)).setTextColor(context.getResources().getColor(R.color.textColor_dark));
        ((Button) view.findViewById(R.id.like_bt)).setText("Liked");
    }

    private void change_default_like_icon(View view)
    {
        ((Button) view.findViewById(R.id.like_bt)).setTextColor(context.getResources().getColor(R.color.colorPrimary));
        ((Button) view.findViewById(R.id.like_bt)).setText("Like");
    }
}

