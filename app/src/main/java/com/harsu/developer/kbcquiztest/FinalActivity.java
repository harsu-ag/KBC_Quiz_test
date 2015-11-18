package com.harsu.developer.kbcquiztest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import helper.TableManager;

public class FinalActivity extends AppCompatActivity {

    TableManager tableManager;
int correct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        correct=0;

        tableManager = new TableManager(this);

        displaySolution();

        TextView score=(TextView) findViewById(R.id.score);
        score.setText(correct+" answered correctly");

    }

    private void displaySolution() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.container);

        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 1; tableManager.alreadySelected(i); i++) {
            View view = inflater.inflate(R.layout.custom_row, layout, false);
            TextView question = (TextView) view.findViewById(R.id.question);
            RadioButton option1 = (RadioButton) view.findViewById(R.id.option1);
            RadioButton option2 = (RadioButton) view.findViewById(R.id.option2);
            RadioButton option3 = (RadioButton) view.findViewById(R.id.option3);
            RadioButton option4 = (RadioButton) view.findViewById(R.id.option4);

            QuestionSet questionSet = tableManager.getQuestionAt(i);
            if (questionSet != null) {
                question.setText(questionSet.getQuestion());
                option1.setText(questionSet.getOption1());
                option2.setText(questionSet.getOption2());
                option3.setText(questionSet.getOption3());
                option4.setText(questionSet.getOption4());
                int answer,answered;
                answer=tableManager.getAnswer(i);
                answered=tableManager.getAnswered(i);
                switch (answer){
                    case 1:
                        option1.setTextColor(Color.GREEN);
                        break;
                    case 2:
                        option2.setTextColor(Color.GREEN);
                        break;
                    case 3:
                        option3.setTextColor(Color.GREEN);
                        break;
                    case 4:
                        option4.setTextColor(Color.GREEN);
                        break;
                }
                if(answer==answered){
                    correct++;

                }
                else{
                    switch (answered){
                        case 1:
                            option1.setTextColor(Color.RED);
                            break;
                        case 2:
                            option2.setTextColor(Color.RED);
                            break;
                        case 3:
                            option3.setTextColor(Color.RED);
                            break;
                        case 4:
                            option4.setTextColor(Color.RED);
                            break;
                    }
                }

            }
            layout.addView(view);
        }

    }

}
