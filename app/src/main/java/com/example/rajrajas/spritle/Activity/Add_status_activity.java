package com.example.rajrajas.spritle.Activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rajrajas.spritle.Dummy;
import com.example.rajrajas.spritle.MyDbController;
import com.example.rajrajas.spritle.R;

import java.util.ArrayList;
import java.util.HashMap;

import jp.wasabeef.richeditor.RichEditor;

/**
 * Created by rajrajas on 5/4/2017.
 */

public class Add_status_activity extends AppCompatActivity {

    private RichEditor mEditor;
    private Button at_btn;
    Button upload_bt;
    private String status_text="";
    MyDbController controller;
    private Boolean bold_bool=false,italic_bool=false,underline_bool=false, strike_bool=false,heading1_bool=false,heading2_bool=false, txtcolor_bool=false,txtbackground_bool=false,bullet_bool=false,blockquote_bool=false,number_bool=false;

    private Dialog user_dialog;
    private int Individual_id=0;
    int prev_txt_length=1;
    private ArrayList<String> anchor_strings;
    private ArrayList<Integer> anchor_position;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_status_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.cancel);

        getSupportActionBar().setTitle("Add Status");

        controller = new MyDbController(Dummy.db_context);

        anchor_strings= new ArrayList<String>();
        anchor_position= new ArrayList<Integer>();



        at_btn=(Button) findViewById(R.id.at_btn);

        mEditor = (RichEditor) findViewById(R.id.editor);
        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(22);
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setPlaceholder("Insert your status here...(@ functionality will work without other text properties or use the @ button)");

        upload_bt = (Button) findViewById(R.id.upload_bt);

        upload_bt.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v)
            {
                if(!status_text.equals(""))
                {
                    controller.insert_status(status_text);
                    Intent intent = new Intent(Add_status_activity.this, Status_activity.class);
                    Add_status_activity.this.startActivity(intent);
                    Add_status_activity.this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }

            }
        });

        at_btn.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v)
                                      {
                                          user_dialog_Show();
                                      }
                                  });



        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener()
        {
            @Override
            public void onTextChange(String text)
            {

                if(text.length()!=0)
                {
                    if (text.charAt(text.length()-1) == '@')
                    {
                        status_text=text;
                        user_dialog_Show();
                        prev_txt_length=text.length();
                    } else
                        {
                            status_text=text;
                        }
                }


            }
        });

        findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.undo();

            }
        });

        findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.redo();

            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v)
            {
                mEditor.setBold();
                bold_bool=setting_default_background(findViewById(R.id.action_bold),bold_bool);

            }
        });

        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setItalic();
                italic_bool=setting_default_background(findViewById(R.id.action_italic),italic_bool);

            }
        });


        findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v)
            {
                mEditor.setStrikeThrough();
                strike_bool=setting_default_background(findViewById(R.id.action_strikethrough),strike_bool);

            }
        });

        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setUnderline();
                underline_bool=setting_default_background(findViewById(R.id.action_underline),underline_bool);

            }
        });

        findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(1);
                heading1_bool=setting_default_background(findViewById(R.id.action_heading1),heading1_bool);

            }
        });

        findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(2);
                heading2_bool=setting_default_background(findViewById(R.id.action_heading2),heading2_bool);

            }
        });

        findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override public void onClick(View v)
            {
                mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;
                txtcolor_bool=setting_default_background(findViewById(R.id.action_txt_color),txtcolor_bool);

            }
        });

        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override public void onClick(View v) {
                mEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
                isChanged = !isChanged;
                txtbackground_bool=setting_default_background(findViewById(R.id.action_bg_color),txtbackground_bool);

            }
        });


        findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setBlockquote();
                blockquote_bool=setting_default_background(findViewById(R.id.action_blockquote),blockquote_bool);

            }
        });

        findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setBullets();
                bullet_bool=setting_default_background(findViewById(R.id.action_insert_bullets),bullet_bool);

            }
        });

        findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setNumbers();
                number_bool=setting_default_background(findViewById(R.id.action_insert_numbers),number_bool);

            }
        });

}


private Boolean setting_default_background(View view,Boolean tbool)
{


    if(!tbool)
    {
        view.setBackgroundColor(getResources().getColor(R.color.lightPrimaryColor));
        return true;
    }
    else
    {
        view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        return false;
    }
    /*findViewById(R.id.action_insert_numbers).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    findViewById(R.id.action_insert_bullets).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    findViewById(R.id.action_blockquote).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    findViewById(R.id.action_bg_color).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    findViewById(R.id.action_txt_color).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    findViewById(R.id.action_heading2).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    findViewById(R.id.action_heading1).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    findViewById(R.id.action_underline).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    findViewById(R.id.action_strikethrough).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    findViewById(R.id.action_italic).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    findViewById(R.id.action_bold).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    findViewById(R.id.action_redo).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    findViewById(R.id.action_undo).setBackgroundColor(getResources().getColor(R.color.colorPrimary));

*/

}

String regenerate_status_text()
{
    for(int i=0;i<anchor_position.size();i++)
    {
        String first_part=status_text.substring(0,anchor_position.get(i));
        String second_part=status_text.substring(anchor_position.get(i),status_text.length());
        status_text=first_part+" "+anchor_strings.get(i)+" "+second_part;
    }
return status_text;
}


    public void user_dialog_Show()
    {
        try
        {
            user_dialog = new Dialog(Add_status_activity.this);
            user_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            user_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

            user_dialog.setContentView(R.layout.user_dialog);

            String user_name[]=controller.get_query_stringArray("select Name from Account ORDER BY Individual_id ASC");
            String user_drawable[]=controller.get_query_stringArray("select Drawable_text from Account ORDER BY Individual_id ASC");


            ListView simpleListView=(ListView) user_dialog.findViewById(R.id.simpleListView);

            ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();

            for (int i=0;i<user_name.length;i++)
            {
                HashMap<String,String> hashMap=new HashMap<>();//create a hashmap to store the data in key value pair
                hashMap.put("name",user_name[i]);

                Resources res = getResources();
                String mDrawableName = user_drawable[i];

                if(!mDrawableName.equals("dummy"))
                {
                    int resID = res.getIdentifier(mDrawableName, "mipmap", getPackageName());
                    hashMap.put("image",resID+"");
                }
                else
                {
                    hashMap.put("image","");
                }
                arrayList.add(hashMap);//add the hashmap into arrayList
            }
            String[] from={"name","image"};//string array
            int[] to={R.id.textView,R.id.imageView};//int array of views id's
            SimpleAdapter simpleAdapter=new SimpleAdapter(this,arrayList,R.layout.list_view_items,from,to);//Create object and set the parameters for simpleAdapter
            simpleListView.setAdapter(simpleAdapter);//sets the adapter for listView

            //perform listView item click event
            simpleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {

                    String url=select_url(i+1);
                    String user_name=controller.get_query_string("select Name from Account where Individual_id="+(i+1));
                    if(!url.equals("nothing"))
                    {
                        mEditor.insertLink(url,user_name);
                        //anchor_strings.add("<a href="+url+">"+user_name+"</a>");
                        //anchor_position.add(status_text.length());
                    }
                    user_dialog.cancel();
                }
            });

            user_dialog.show();
            user_dialog.setCancelable(false);
        } catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Something wrong" + e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    private String select_url(int i)
    {
        switch (i)
        {
            case 1:
                {
                return "activity_first_user://.Activity.first_user_activity";
                }
            case 2:
                return "activity_second_user://.Activity.second_user_activity";
            case 3:
                return "activity_third_user://.Activity.Third_user_activity";
            case 4:
                return "activity_fourth_user://.Activity.fourth_user_activity";
            case 5:
                return "activity_fifth_user://.Activity.fifth_user_activity";
            default:
                return "nothing";

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