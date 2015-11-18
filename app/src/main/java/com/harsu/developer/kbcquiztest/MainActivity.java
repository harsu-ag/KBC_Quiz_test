package com.harsu.developer.kbcquiztest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import helper.TableManager;

public class MainActivity extends AppCompatActivity {

    TableManager tableManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if any answer has been marked.

                RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(MainActivity.this, "Please Select an answer", Toast.LENGTH_SHORT).show();

                } else {
                    //mark the answer and proceed to next question
                    tableManager.markAnswer(getMarkedAnswer(radioGroup.getCheckedRadioButtonId()),1);
                    startActivity(new Intent(MainActivity.this,SecondActivity.class));
                }


            }
        });

        tableManager = new TableManager(this);

        addQuestions();

        tableManager.markAllUnSelected();      //it marks all question to be unselected ie 0

        setQuestion();


    }

    private int getMarkedAnswer(int checkedRadioButtonId) {
        switch (checkedRadioButtonId)
        {
            case R.id.option1:
                return 1;
            case R.id.option2:
                return 2;
            case R.id.option3:
                return 3;
            case R.id.option4:
                return 4;
        }
        return -1;
    }


    private void setQuestion() {
        /*Random rand = new Random();
        int randomNum = rand.nextInt(6);
        while (tableManager.alreadySelected(randomNum)){
            randomNum=rand.nextInt(6);
        }*/


        TextView question = (TextView) findViewById(R.id.question);
        RadioButton option1 = (RadioButton) findViewById(R.id.option1);
        RadioButton option2 = (RadioButton) findViewById(R.id.option2);
        RadioButton option3 = (RadioButton) findViewById(R.id.option3);
        RadioButton option4 = (RadioButton) findViewById(R.id.option4);

        QuestionSet questionSet = tableManager.getQuestionAt(1);
        if (questionSet != null) {
            question.setText(questionSet.getQuestion());
            option1.setText(questionSet.getOption1());
            option2.setText(questionSet.getOption2());
            option3.setText(questionSet.getOption3());
            option4.setText(questionSet.getOption4());
        }

    }

    private void addQuestions() {

        tableManager.addEntry("Which among these is a Mammal?", "Lizard", "Snake", "Human", "Ant",3);
        tableManager.addEntry("What is the capital of India?","Delhi","Hyderabad","Mumbai","Kolkata",1);
        tableManager.addEntry("What is the national Bird of India?","Sparrow","Peacock","Crow","King Fisher",2);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
