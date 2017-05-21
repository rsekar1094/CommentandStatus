package com.example.rajrajas.spritle.Activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rajrajas.spritle.Adapter.Status_adapte;
import com.example.rajrajas.spritle.Dummy;
import com.example.rajrajas.spritle.MyDbController;
import com.example.rajrajas.spritle.R;
import com.example.rajrajas.spritle.RecyclerTouchListener;
import com.example.rajrajas.spritle.databinding.StatusActivityBinding;
import com.example.rajrajas.spritle.model.Status_item;

import java.util.List;

/**
 * Created by rajrajas on 5/3/2017.
 */

public class Status_activity extends AppCompatActivity {
    StatusActivityBinding statusbinding;
    MyDbController controller;
    RecyclerView recyclerView;
    List<Status_item> data;
    Dialog exit_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusbinding = DataBindingUtil.setContentView(this, R.layout.status_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Status");

        controller = new MyDbController(Dummy.db_context);

        recyclerView = (RecyclerView) findViewById(R.id.item_list);

        initRecycler(recyclerView);

    }


    private void initRecycler(RecyclerView status_recycler)
    {
        data=controller.get_all_status_message_list();
        status_recycler.setHasFixedSize(true);
        status_recycler.setAdapter(new Status_adapte(getApplicationContext(), data,getSupportFragmentManager()));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_status_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_status)
        {
            Intent intent = new Intent(Status_activity.this, Add_status_activity.class);
            Status_activity.this.startActivity(intent);
            Status_activity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed()
    {

            exit_dialog_Show();

    }

    public void exit_dialog_Show()
    {
        try {
            exit_dialog = new Dialog(Status_activity.this);
            exit_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            exit_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

            exit_dialog.setContentView(R.layout.dialog_exit);
            Button exit_yes_bt = (Button) exit_dialog.findViewById(R.id.yes_bt);
            Button exit_no_bt = (Button) exit_dialog.findViewById(R.id.no_bt);

            exit_yes_bt.setOnClickListener(new Button.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                public void onClick(View v) {
                    finishAffinity();
                    exit_dialog.dismiss();
                }
            });

            exit_no_bt.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {

                    exit_dialog.dismiss();
                }
            });

            exit_dialog.show();
            exit_dialog.setCancelable(false);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Something wrong" + e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }





}
