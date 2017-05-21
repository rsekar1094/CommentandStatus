package com.example.rajrajas.spritle;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rajrajas.spritle.Adapter.Comments_adapter;
import com.example.rajrajas.spritle.model.Status_item;

import java.util.List;

/**
 * Created by rajrajas on 5/4/2017.
 */

public class Comments_bottomSheet extends BottomSheetDialogFragment implements View.OnClickListener
{

    private Dialog bottom_sheet_dialog;


    private EditText add_comments_edit;
    private ImageButton add_bt;
    private TextView tot_like;

    MyDbController controller;
    private String status_id;

    private List<Status_item> data;
    private  RecyclerView comments_recycler;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        status_id=getArguments().getString("status_id");
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback()
    {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState)
        {
            if (newState == BottomSheetBehavior.STATE_HIDDEN)
            {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset)
        {
        }
    };

    @Override
    public void setupDialog(Dialog dialog, int style)
    {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.comments_bottomsheet, null);
        dialog.setContentView(contentView);


        controller= new MyDbController(Dummy.db_context);

        bottom_sheet_dialog=dialog;

        add_comments_edit=(EditText) contentView.findViewById(R.id.comments_edit);
        tot_like=(TextView) contentView.findViewById(R.id.like_cnt_txt);

        add_bt=(ImageButton) contentView.findViewById(R.id.save_bt);
        add_bt.setOnClickListener(this);

        comments_recycler=(RecyclerView) contentView.findViewById(R.id.comment_list);

        data=(controller.get_all_comments(status_id));
        initRecycler(comments_recycler);

        insert_tot_like_text();

        add_comments_edit.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void afterTextChanged(Editable s)
            {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after)
            {
                if(!s.toString().isEmpty())
                {
                    add_bt.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count)
            {


            }
        });

        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();

        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        if (behavior != null && behavior instanceof BottomSheetBehavior)
        {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }

    private void initRecycler(final RecyclerView commentsrecycler)
    {
        commentsrecycler.setHasFixedSize(true);
        commentsrecycler.setAdapter(new Comments_adapter(getActivity(), data));
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.save_bt:
                save_comment();
                break;

        }

    }

    void save_comment()
    {
        if(!add_comments_edit.getText().toString().equals(""))
        {
            controller.insert_comment(status_id,add_comments_edit.getText().toString());
            data=(controller.get_all_comments(status_id));
            initRecycler(comments_recycler);
            add_comments_edit.setText("");
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

    }

    private void insert_tot_like_text()
    {
         String user_name,count;
        if(!controller.get_query_string("select count(*) from Status_Message_likes where Status_Message_id="+status_id).equals("0"))
        {
            if (controller.get_query_string("select count(*) from Status_Message_likes where Status_Message_id=" + status_id + " and Individual_id=5").equals("0")) {
                String ind_id = controller.get_query_string("select Individual_id from Status_Message_likes where Status_Message_id=" + status_id);
                user_name = controller.get_query_string("select Name from Account where Individual_id=" + ind_id);
                count = controller.get_query_string("select count(*) from Status_Message_likes where Status_Message_id=" + status_id + " and Individual_id!=" + ind_id);
                if (count.equals("0")) {
                    tot_like.setText(user_name);
                } else {
                    if (count.equals("1")) {
                        tot_like.setText(user_name + " and " + count + " other");
                    } else
                        tot_like.setText(user_name + " and " + count + " others");
                }
            } else {
                user_name = "you";
                count = controller.get_query_string("select count(*) from Status_Message_likes where Status_Message_id=" + status_id + " and Individual_id!=5");
                if (count.equals("0")) {
                    tot_like.setText(user_name);
                } else {
                    if (count.equals("1")) {
                        tot_like.setText(user_name + " and " + count + " other");
                    } else
                        tot_like.setText(user_name + " and " + count + " others");
                }
            }
        }
        else {
            tot_like.setText("0 likes");
        }
    }


}


