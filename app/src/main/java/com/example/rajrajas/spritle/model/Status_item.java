package com.example.rajrajas.spritle.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rajrajas on 5/3/2017.
 */

public class Status_item
{
    private String username,statustime,statustext,totallikes,totalcomments,drawable_name,status_id,comment_id;

    public Status_item(String username,String statustime,String statustext,String totallikes,String totalcomments,String drawable_name,int status_id)
    {
        this.username=username;
        this.statustime=statustime;
        this.statustext=statustext;
        this.totallikes=totallikes;
        this.totalcomments=totalcomments+" comments";
        this.drawable_name=drawable_name;
        this.status_id=status_id+"";
    }


    public Status_item(String username,String statustime,String statustext,String totallikes,String drawable_name,String status_id,String comment_id)
    {
        this.username=username;
        this.statustime=statustime;
        this.statustext=statustext;
        this.totallikes=totallikes;
        this.status_id=status_id;
        this.comment_id=comment_id;
        this.drawable_name=drawable_name;
    }

    public String getUsername() {
        return username;
    }
    public String getStatustime()
    {
        return calc_time_diff(statustime);
    }
    public String getStatustext()
    {
        return statustext;
    }
    public String getTotallikes()
    {
        return totallikes;
    }
    public String getTotalcomments()
    {
        return totalcomments;
    }

    public String getDrawable_name()
    {
        return drawable_name;
    }

    public String getStatus_id()
    {
        return status_id;
    }
    public  String getComment_id()
    {
        return comment_id;
    }

    private String calc_time_diff(String time)
    {
        Date status_date,current_date;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            status_date = formatter.parse(time);
        }catch (Exception e)
        {
            status_date=new Date();
        }
        current_date=new Date();

        long diff = current_date.getTime() - status_date.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);
        int diffInDays = (int) ((current_date.getTime() - status_date.getTime()) / (1000 * 60 * 60 * 24));

        if(diffInDays>0)
        {
            return status_date.getDate()+" "+month_name(status_date.getMonth())+" at "+status_date.getHours()+":"+status_date.getMinutes();
        }
        else
            if (diffHours>1)
            {
                return diffHours+" hrs";
            }
            else if (diffMinutes>1)
            {
                return diffMinutes+" mins";
            }
            else
            {
                return "Just Now";
            }

    }

    private String month_name(int i)
    {
        switch (i)
        {
            case 1:
                return "jan";
            case 2:
                return "feb";
            case 3:
                return "mar";
            case 4:
                return "apr";
            case 5:
                return "may";
            case 6:
                return "jun";
            case 7:
                return "jul";
            case 8:
                return "aug";
            case 9:
                return "sep";
            case 10:
                return "oct";
            case 11:
                return "nov";
            case 12:
                return "dec";
        }
        return "null";
    }

}
