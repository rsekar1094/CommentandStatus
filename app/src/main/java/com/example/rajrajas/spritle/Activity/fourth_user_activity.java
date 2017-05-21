package com.example.rajrajas.spritle.Activity;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rajrajas.spritle.Dummy;
import com.example.rajrajas.spritle.MyDbController;
import com.example.rajrajas.spritle.R;

/**
 * Created by rajrajas on 5/5/2017.
 */

public class fourth_user_activity extends AppCompatActivity
{

    MyDbController controller;
    private TextView username,userphone,useremail;
    private ImageView img;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User Page");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.cancel);

        controller = new MyDbController(Dummy.db_context);

        username=(TextView) findViewById(R.id.User_name_text);
        userphone=(TextView) findViewById(R.id.Phone_text);
        useremail=(TextView) findViewById(R.id.email_text);

        img=(ImageView) findViewById(R.id.profile_image);

        username.setText(controller.get_query_string("select Name from Account where Individual_id=4"));
        userphone.setText(controller.get_query_string("select Phone_number from Account where Individual_id=4"));
        useremail.setText(controller.get_query_string("select Email_id from Account where Individual_id=4"));



        Resources res = getResources();
        String mDrawableName =controller.get_query_string("select Drawable_text from Account where Individual_id=4");
        Drawable drawable=null;
        if(!mDrawableName.equals("dummy"))
        {
            int resID = res.getIdentifier(mDrawableName, "mipmap", getPackageName());
            drawable = res.getDrawable(resID);
            img.setImageDrawable(drawable);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return(super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
    }

}
