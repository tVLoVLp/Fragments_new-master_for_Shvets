package com.example.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.fragments.m_Realm.RealmHelper;
import com.example.fragments.m_Realm.Spacecraft;
import com.example.fragments.m_UI.MathAdapter;
import com.example.fragments.m_UI.MyAdapter;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Main3mth extends AppCompatActivity {
    Realm realm;
    ArrayList<String> spacecrafts;
    MathAdapter adapter;
    RecyclerView rv;
    EditText nameEditTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3mth);
        FloatingActionButton fab=findViewById(R.id.button_add_formula);

        rv=findViewById(R.id.recycler_view_math);
        rv.setLayoutManager(new LinearLayoutManager(this));
        //String position=getIntent().getStringExtra("id");
        //String subject =getIntent().getStringExtra("subject_name");
        Realm.init(getApplicationContext());
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("Math")
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        realm=Realm.getInstance(realmConfiguration);

        RealmHelper helper=new RealmHelper(realm);
        spacecrafts=helper.retrieve();

        adapter=new MathAdapter(this,spacecrafts);
        rv.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayInputDialog();
            }
        });
    }
    private void displayInputDialog(){
        Dialog d=new Dialog(this);
        d.setTitle("Save to realm");
        d.setContentView(R.layout.input_dialog);

        nameEditTxt=d.findViewById(R.id.nameEditText);
        Button saveBtn=d.findViewById(R.id.create);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spacecraft s=new Spacecraft();
                s.setName(nameEditTxt.getText().toString());

                RealmHelper helper=new RealmHelper(realm);
                helper.save(s);
                nameEditTxt.setText("");

                spacecrafts=helper.retrieve();
                adapter=new MathAdapter(Main3mth.this,spacecrafts);
                rv.setAdapter(adapter);

                Intent userscreen=new Intent(Main3mth.this, Main3mth.class);
                startActivity(userscreen);
            }
        });
        d.show();
    }
}