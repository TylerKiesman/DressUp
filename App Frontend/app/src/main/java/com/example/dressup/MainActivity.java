package com.example.dressup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.text.Layout;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Console;
import java.util.LinkedList;
import java.util.Queue;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener{

    private Animation fadeAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupFade();

        SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean hasSetup = preferences.getBoolean("didSetup", false);

        //setContentView(R.layout.activity_main);
        if(!hasSetup){
            setContentView(populateStartupTasks());
        } else {
            Intent myIntent = new Intent(this, HomeActivity.class);
            startActivity(myIntent);
        }
    }

    private void setupFade(){
        this.fadeAnimation = new AlphaAnimation(0.0f, 1.0f);
        this.fadeAnimation.setRepeatCount(1);
        this.fadeAnimation.setRepeatMode(Animation.REVERSE);
        this.fadeAnimation.setDuration(3000);
    }

    private View populateStartupTasks(){
        LinkedList<View> startupTasks = new LinkedList<>();

        TextView welcome = new TextView(this);
        welcome.setText("Welcome to DressUp!");
        welcome.setTextColor(Color.WHITE);
        welcome.setTextSize(36);
        welcome.setGravity(Gravity.CENTER);

        TextView helpMsg = new TextView(this);
        helpMsg.setText("We're eager to help you look your best!");
        helpMsg.setTextColor(Color.WHITE);
        helpMsg.setTextSize(36);
        helpMsg.setGravity(Gravity.CENTER);
        helpMsg.setVisibility(View.INVISIBLE);

        startupTasks.add(helpMsg);

        ConstraintLayout.LayoutParams setupParams =
                new ConstraintLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);

        ConstraintLayout myLayout = new ConstraintLayout(this);
        myLayout.setBackgroundColor(Color.parseColor("#f76252"));
        myLayout.setLayoutParams(setupParams);

        View nameLayout = buildNameInput();

        startupTasks.add(nameLayout);

        myLayout.addView(welcome, setupParams);
        myLayout.addView(helpMsg, setupParams);
        myLayout.addView(nameLayout);


        Animation.AnimationListener animationListener = new fadeListener(startupTasks, welcome);

        welcome.startAnimation(this.fadeAnimation);

        this.fadeAnimation.setAnimationListener(animationListener);

        return myLayout;
    }

    private View buildNameInput(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        LinearLayout.LayoutParams setupParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height / 7);
        setupParams.gravity = Gravity.CENTER;

        TextView question = new TextView(this);
        question.setLayoutParams(setupParams);
        question.setGravity(Gravity.BOTTOM | Gravity.CENTER);
        question.setPadding(0, 0, 0, 15);
        question.setText("What's your name?");
        question.setTextColor(Color.WHITE);
        question.setTextSize(36);

        layout.setGravity(Gravity.CENTER);

        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        input.setTextColor(Color.WHITE);
        input.setTextSize(24);
        LinearLayout.LayoutParams lay = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lay.setMargins(50, 0, 50, 0);
        input.setLayoutParams(lay);
        input.setEms(10);
        input.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
        input.setOnKeyListener(this);

        layout.addView(question);
        layout.addView(input);
        layout.setVisibility(View.INVISIBLE);

        return layout;
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
            EditText input = (EditText) view;
            input.getText();
            SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("didSetup", true);
            editor.putString("name", input.getText().toString());
            editor.apply();
            Intent myIntent = new Intent(this, HomeActivity.class);
            startActivity(myIntent);
            return true;
        }
        return false;
    }
}
