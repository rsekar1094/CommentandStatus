package com.example.rajrajas.spritle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.rajrajas.spritle.model.Status_item;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by rajrajas on 5/2/2017.
 */

public class MyDbController extends SQLiteOpenHelper
{
    public MyDbController(Context applicationcontext)
    {
        super(applicationcontext, "spritle.db", null, 1);
    }

    //Creates Table
    @Override
    public void onCreate(SQLiteDatabase database) {

        String query;

        //Table Id: 1
        query = "CREATE TABLE Account (Individual_id INTEGER,Name TEXT,Email_id TEXT,Phone_number TEXT,Password Text,Drawable_text TEXT,IsActive INTEGER)";
        database.execSQL(query);
        insert_dummy_account_details(database);

        //Table Id: 2
        query = "CREATE TABLE Status_Message (Status_Message_id INTEGER,Status_Message_Text TEXT,Individual_id INTEGER,Insert_date Text)";
        database.execSQL(query);
        insert_defalult_status_message(database);

        //Table Id: 3
        query = "CREATE TABLE Status_Message_likes (Status_Message_id INTEGER,Individual_id INTEGER)";
        database.execSQL(query);
        insert_defalult_likes(database);

        //Table Id: 4
        query = "CREATE TABLE Status_Message_comments (Status_Message_id INTEGER,Status_Message_comments_id INTEGER,Individual_id INTEGER,Comments TEXT,Insert_date Text)";
        database.execSQL(query);
        insert_defalult_comments(database);

        //Table Id: 5
        query = "CREATE TABLE Status_Message_comments_likes (Status_Message_id INTEGER,Status_Message_comments_id INTEGER,Individual_id INTEGER)";
        database.execSQL(query);
        insert_defalult_comments_likes(database);

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;

        query = "DROP TABLE IF EXISTS Account";
        database.execSQL(query);

        query = "DROP TABLE IF EXISTS Status_Message";
        database.execSQL(query);

        query = "DROP TABLE IF EXISTS Status_Message_likes";
        database.execSQL(query);

        query = "DROP TABLE IF EXISTS Status_Message_comments";
        database.execSQL(query);

        query = "DROP TABLE IF EXISTS Status_Message_comments_likes";
        database.execSQL(query);


    }

    private void insert_dummy_account_details(SQLiteDatabase database) {
        String name[] = {"Raja sekar", "Gowtham Raj", "Manjula", "Amal"};
        String drawable_name[] = {"rajini", "jackie", "ramya", "saruk"};

        ContentValues values = new ContentValues();

        for (int i = 0; i < 4; i++) {
            int temp = i + 1;
            values.put("Individual_id", temp);
            values.put("Name", name[i]);
            values.put("Email_id", "xyz@gmail.com");
            values.put("Phone_number", "8844332211");
            values.put("Password", "dummy");
            values.put("Drawable_text", drawable_name[i]);
            values.put("IsActive", 0);
            database.insert("Account", null, values);
        }


    }

    public void insert_account_details(String[] res) {
        ContentValues values = new ContentValues();
        SQLiteDatabase database = this.getWritableDatabase();
        values.put("Individual_id", "5");
        values.put("Name", res[0]);
        values.put("Email_id", res[1]);
        values.put("Phone_number", res[2]);
        values.put("Password", res[3]);
        values.put("IsActive", 1);
        values.put("Drawable_text", "wolv");
        database.insert("Account", null, values);

    }

    public Boolean check_user_credetnails(String username, String password) {
        return !get_query_string("select count(*) from Account where Email_id=" + username + " and Password=" + password).equals("0");

    }

    public void delete_user_details() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete("Account", null, null);

    }


    public List<Status_item> get_all_status_message_list() {
        List<Status_item> data = new ArrayList<>();
        String query = "Select a.Name,sm.Insert_date,sm.Status_Message_Text,a.Drawable_text,sm.Status_Message_id from Account a,Status_Message sm where sm.Individual_id=a.Individual_id ORDER BY sm.Insert_date DESC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String status_message_id = cursor.getString(cursor.getColumnIndex("Status_Message_id"));
                Status_item l = new Status_item(cursor.getString(cursor.getColumnIndex("Name")), cursor.getString(cursor.getColumnIndex("Insert_date")), cursor.getString(cursor.getColumnIndex("Status_Message_Text")), get_query_string("select count(*) from Status_Message_likes where Status_Message_id=" + status_message_id, database), get_query_string("select count(*) from Status_Message_comments where Status_Message_id=" + status_message_id, database), cursor.getString(cursor.getColumnIndex("Drawable_text")), Integer.parseInt(status_message_id));
                data.add(l);
                cursor.moveToNext();
            }
        }
        return data;
    }


    List<Status_item> get_all_comments(String status_id) {
        List<Status_item> data = new ArrayList<>();
        String query = "Select a.Name,sm.Insert_date,sm.Comments,a.Drawable_text,sm.Status_Message_comments_id,sm.Status_Message_id from Account a,Status_Message_comments sm where sm.Individual_id=a.Individual_id and sm.Status_Message_id=" + status_id + " ORDER BY sm.Insert_date DESC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String status_message_id = cursor.getString(cursor.getColumnIndex("Status_Message_id"));
                String comment_id = cursor.getString(cursor.getColumnIndex("Status_Message_comments_id"));
                Status_item l = new Status_item(cursor.getString(cursor.getColumnIndex("Name")), cursor.getString(cursor.getColumnIndex("Insert_date")), cursor.getString(cursor.getColumnIndex("Comments")), get_query_string("select count(*) from Status_Message_comments_likes where Status_Message_id=" + status_message_id + " and Status_Message_comments_id= " + comment_id, database), cursor.getString(cursor.getColumnIndex("Drawable_text")), status_message_id, comment_id);
                data.add(l);
                cursor.moveToNext();
            }
        }
        return data;

    }
   /* private void update_tot_likes(int Status_Message_id,SQLiteDatabase database )
    {
        ContentValues values = new ContentValues();
        values.put("Total_likes", get_query_string("select count(*) from Status_Message_likes where Status_Message_id="+ Status_Message_id,database));
        database.update("Status_Message", values, "Status_Message_id" + "=" + Status_Message_id, null);
    }

    private void update_tot_comments(int Status_Message_id,SQLiteDatabase database )
    {
        ContentValues values = new ContentValues();
        values.put("Total_comments", get_query_string("select count(*) from Status_Message_comments where Status_Message_id="+ Status_Message_id,database));
        database.update("Status_Message", values, "Status_Message_id" + "=" + Status_Message_id, null);
    }*/

    private void insert_defalult_status_message(SQLiteDatabase database) {
        String status[] = {"Go for someone who is not only <B><font color=\"#E91E63\">proud to have you</font></B> but will also take every risk just to be with you", "People who <b><i>tolerate</i></b> me on the daily basis! are real heroes in my eye",
                "World is small and life is short.. Spread <i><font color=\"#E91E63\">smiles</font></i> and share peace.",
                               "Hey <a href=\"activity_first_user://.Activity.first_user_activity\">" +
                "Raja sekar<a> how are you.How is your <b>wife</b> <a href=\"activity_third_user://.Activity.Third_user_activity\">Manjula<a>"};
                //"I speak for what i see, I stand for what I Beileve. I strive for what i want."};

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        String insert_time[]={dateFormat.format(new Date(System.currentTimeMillis() - 9000 * 13200)),dateFormat.format(new Date(System.currentTimeMillis() - 10000 * 1000)),dateFormat.format(new Date(System.currentTimeMillis() - 7200 * 1000)),dateFormat.format(new Date(System.currentTimeMillis() - 3600 * 1000))};

       /* SQLiteDatabase database= this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("Status_Message_id", 1);
            values.put("Status_Message_Text", "hello");
            values.put("Individual_id",3);
            values.put("Total_likes,", 0);
            values.put("Total_comments", 0);
            values.put("Insert_date", getDateTime());
            database.insert("Status_Message", null, values);*/

        for (int i = 0; i < 4; i++) {
            int temp = i + 1;
            String sql =
                    "INSERT  INTO Status_Message (Status_Message_id, Status_Message_Text, Individual_id,Insert_date) VALUES(" + temp + ",'"+status[i]+"'," + temp + ",'" + insert_time[i] + "')";
            database.execSQL(sql);

        }


    }

    private void insert_defalult_likes(SQLiteDatabase database) {
        ContentValues values = new ContentValues();


        for (int i = 0; i < 4; i++) {
            for (int j = i; j < 4; j++) {
                values.put("Status_Message_id", j + 1);
                values.put("Individual_id", i+1);
                database.insert("Status_Message_likes", null, values);
            }
        }

        /*for(int i=0;i<4;i++) {
            update_tot_likes(i,database);
        }*/
    }

    private void insert_defalult_comments_likes(SQLiteDatabase database) {
        ContentValues values = new ContentValues();


        for (int i = 1; i <= 4; i++) {
            for (int j = i; j < 4; j++) {
                values.put("Status_Message_id", j + 1);
                values.put("Individual_id", i);
                values.put("Status_Message_comments_id", j + 1);
                database.insert("Status_Message_comments_likes", null, values);
            }
        }


    }


    private void insert_defalult_comments(SQLiteDatabase database) {
        ContentValues values = new ContentValues();

        String def_comments[]={"super","HA HA HA","Dude well said","Semma","Achaaaaaa","HE HE HE"};

        for (int i = 0; i < 4; i++) {
            for (int j = i; j < 4; j++) {
                values.put("Status_Message_id", j + 1);
                values.put("Individual_id", i+1);
                values.put("Status_Message_comments_id", j + 1);
                values.put("Comments",def_comments[i] );
                values.put("Insert_date", getDateTime());
                database.insert("Status_Message_comments", null, values);

            }
        }


    }


    public void delete_Status_like(String id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete("Status_Message_likes", "Status_Message_id" + "=" + id + " and Individual_id=5", null);
        // database.close();
    }

    public void insert_Status_like(String id) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Status_Message_id", id);
        values.put("Individual_id", "5");
        database.insert("Status_Message_likes", null, values);

    }

     void insert_comment(String status_id,String Comment)
    {
        int status_comment_id=Integer.parseInt(get_query_string("select count(*) from Status_Message_comments where Status_Message_id="+ status_id+" and Individual_id='5'"))+1;
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

                values.put("Status_Message_id", status_id);
                values.put("Individual_id", "5");
                values.put("Status_Message_comments_id", status_comment_id+"");
                values.put("Comments", Comment);
                values.put("Insert_date", getDateTime());
                database.insert("Status_Message_comments", null, values);

    }

    public void insert_status(String status_text)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        int status_id=Integer.parseInt(get_query_string("select count(*) from Status_Message"))+1;
            String sql =
                    "INSERT  INTO Status_Message (Status_Message_id, Status_Message_Text, Individual_id,Insert_date) VALUES(" + status_id + ",'"+status_text+"','5','" + getDateTime() + "')";
            database.execSQL(sql);

    }

    public void insert_comments_like(String status_id,String comment_id)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

                values.put("Status_Message_id", status_id);
                values.put("Individual_id", "5");
                values.put("Status_Message_comments_id",comment_id);
                database.insert("Status_Message_comments_likes", null, values);

    }

    public void delete_comments_like(String status_id,String comment_id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete("Status_Message_comments_likes", "Status_Message_id" + "=" + status_id + " and Status_Message_comments_id= "+comment_id+" and Individual_id=5", null);
        // database.close();
    }


    public String get_query_string(String selectQuery) {
        String res;
        SQLiteDatabase database1 = this.getWritableDatabase();
        Cursor cursor = database1.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        res = "";
        while (!cursor.isAfterLast()) {
            res = cursor.getString(0);
            cursor.moveToNext();
        }
        cursor.close();
        //database1.close();
        return res;
    }

    private String get_query_string(String selectQuery, SQLiteDatabase database1) {
        String res;

        Cursor cursor = database1.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        res = "";
        while (!cursor.isAfterLast()) {
            res = cursor.getString(0);
            cursor.moveToNext();
        }
        cursor.close();
        //database1.close();
        return res;
    }

    public String[] get_query_stringArray(String selectQuery) {
        String[] res;
        SQLiteDatabase database1 = this.getWritableDatabase();
        Cursor cursor = database1.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        res = new String[cursor.getCount()];
        int i = 0;
        while (!cursor.isAfterLast()) {
            res[i] = cursor.getString(0);
            i++;
            cursor.moveToNext();
        }
        cursor.close();
        return res;

    }


    //get current date and time
    String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }


}
