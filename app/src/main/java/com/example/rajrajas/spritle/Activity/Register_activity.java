package com.example.rajrajas.spritle.Activity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;


import com.example.rajrajas.spritle.Dummy;
import com.example.rajrajas.spritle.MyDbController;
import com.example.rajrajas.spritle.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.id.input;


/**
 * Created by rajrajas on 2/19/2017.
 */

public class Register_activity  extends AppCompatActivity implements View.OnClickListener
{
    private MyDbController controller;

    private EditText name_txt,email_txt,phone_txt,password_txt,user_txt,reg_password_txt,re_password_txt;

    private ViewSwitcher switcher;
    private View register_layout,login_layout;

    private Button register_bt,sign_bt;
    private  TextView already_regisetered_bt,not_registered_bt,forgor_password_bt,welcome_txt;

    private Pattern pattern;
    private Matcher matcher;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.register_activity);

        Dummy.db_context = this;
        controller = new MyDbController(Dummy.db_context);
        if (!controller.get_query_string("select count(*) from Account where IsActive=1").equals("0"))
        {
            Intent intent = new Intent(Register_activity.this, Status_activity.class);
            Register_activity.this.startActivity(intent);
        }

        switcher=(ViewSwitcher) findViewById(R.id.switcher);

        login_layout=findViewById(R.id.login_layout);
        register_layout=findViewById(R.id.register_layout);

        welcome_txt=(TextView) findViewById(R.id.welcome_txt) ;


        name_txt=(EditText) register_layout.findViewById(R.id.name_txt);
        email_txt=(EditText) register_layout.findViewById(R.id.email_txt);
        phone_txt=(EditText) register_layout.findViewById(R.id.phone_txt);
        password_txt=(EditText) register_layout.findViewById(R.id.password_txt);
        re_password_txt=(EditText) register_layout.findViewById(R.id.re_password_txt);

        user_txt=(EditText) login_layout.findViewById(R.id.user_txt);
        reg_password_txt=(EditText) login_layout.findViewById(R.id.reg_password_txt);


        register_bt=(Button) register_layout.findViewById(R.id.register_bt);
        sign_bt=(Button) login_layout.findViewById(R.id.login_bt);

        forgor_password_bt=(TextView) login_layout.findViewById(R.id.forgot_password);
        already_regisetered_bt=(TextView) register_layout.findViewById(R.id.registered_bt);
        not_registered_bt=(TextView) login_layout.findViewById(R.id.not_registered_bt);

        register_bt.setOnClickListener(this);
        sign_bt.setOnClickListener(this);
        forgor_password_bt.setOnClickListener(this);
        not_registered_bt.setOnClickListener(this);
        already_regisetered_bt.setOnClickListener(this);



    }

    public void register_user()
    {
        String name=name_txt.getText().toString().equals("")?return_error_msg(name_txt):name_txt.getText().toString(),
                email=email_txt.getText().toString().equals("")?return_error_msg(email_txt):email_txt.getText().toString(),
                phone=phone_txt.getText().toString().equals("")?return_error_msg(phone_txt):phone_txt.getText().toString(),
                password=password_txt.getText().toString().equals("")?return_error_msg(password_txt):password_txt.getText().toString(),
                re_password=re_password_txt.getText().toString().equals("")?return_error_msg(re_password_txt):re_password_txt.getText().toString();


        if(!name.equals("NO VALUE")&&!email.equals("NO VALUE")&&!phone.equals("NO VALUE")&&!password.equals("NO VALUE")&&!re_password.equals("NO VALUE"))
        {
            if(!password.equals(re_password))
            {
                Snackbar.make(findViewById(R.id.main_container),"Passwords are not matching",Snackbar.LENGTH_SHORT).show();
            }
            else
                {
                    if(is_valid_email(email))
                    {
                        if(phone.length()==10)
                        {
                            String[] res = {name, email, phone, password};
                            controller.insert_account_details(res);
                            Intent intent;
                            intent = new Intent(Register_activity.this, Status_activity.class);
                            Register_activity.this.startActivity(intent);
                            Register_activity.this.overridePendingTransition(R.anim.from_right, R.anim.to_left);
                        }
                        else
                        {
                            Snackbar.make(findViewById(R.id.main_container),"Invalid Phone number(Should contain 10 digits)",Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Snackbar.make(findViewById(R.id.main_container),"Invalid Email Address",Snackbar.LENGTH_SHORT).show();
                    }
}
        }
        else
        {
            Snackbar.make(findViewById(R.id.main_container),"Please fill all the mandatory fields",Snackbar.LENGTH_SHORT).show();
        }
    }

    public void sign_in()
    {
        String name=user_txt.getText().toString().equals("")?return_error_msg(user_txt):user_txt.getText().toString(),
                password=reg_password_txt.getText().toString().equals("")?return_error_msg(reg_password_txt):reg_password_txt.getText().toString();

        if(!name.equals("NO VALUE")&&!password.equals("NO VALUE"))
        {

            if(controller.check_user_credetnails(name,password))
            {


            }
            else
            {
                Snackbar.make(findViewById(R.id.main_container),"Username and Password are not matching",Snackbar.LENGTH_SHORT).show();
            }
        }
        else
        {
            Snackbar.make(findViewById(R.id.main_container),"Please fill all the mandatory fields",Snackbar.LENGTH_SHORT).show();
        }

    }

    @NonNull
    private String return_error_msg(TextView temp_txt)
    {
        temp_txt.setError("Mandatory fields cannot be NULL");
        return "NO VALUE";
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.register_bt:
                register_user();
                break;
            case R.id.login_bt:
                sign_in();
                break;
            case R.id.registered_bt:
                {
                    welcome_txt.setText("Login to GEN1.11");
                switcher.setDisplayedChild(0);
                    switcher.setInAnimation(Register_activity.this, R.anim.slide_in_left);
                    switcher.setOutAnimation(Register_activity.this, R.anim.slide_out_right);
                 }
                break;
            case R.id.not_registered_bt: {
                welcome_txt.setText("Register to GEN1.11");
                switcher.setDisplayedChild(1);
                switcher.setInAnimation(Register_activity.this, R.anim.from_right);
                switcher.setOutAnimation(Register_activity.this, R.anim.to_left);

            }
                break;

        }

    }

    private Boolean is_valid_email(String email_addr)
    {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email_addr);
        return matcher.matches();
    }


    @Override
    public void onBackPressed()
    {

    }

    @Override
    public void onRestart(){
        super.onRestart();

    }

}
