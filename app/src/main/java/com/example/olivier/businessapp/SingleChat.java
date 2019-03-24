package com.example.olivier.businessapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.olivier.businessapp.Objects.Activities.Base;

public class SingleChat extends Base {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_chat);
    }
}
