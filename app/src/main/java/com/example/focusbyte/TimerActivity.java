package com.example.focusbyte;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class TimerActivity extends AppCompatActivity {
    private TextView tvTimer, tvTaskStatus;
    private CountDownTimer countDownTimer;
    private long mTimeLeftInMillis = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        tvTimer = findViewById(R.id.tvTimer);
        tvTaskStatus = findViewById(R.id.tvTaskStatus);

        // Task receive karna Task Screen se
        String taskName = getIntent().getStringExtra("TASK_NAME");
        int minutes = getIntent().getIntExtra("MINUTES", 0);
        if (taskName != null) {
            tvTaskStatus.setText("Working on: " + taskName);
            mTimeLeftInMillis = minutes * 60000L;
            updateTimerText();
        }

        findViewById(R.id.btnStart).setOnClickListener(v -> startTimer());
        findViewById(R.id.btnPause).setOnClickListener(v -> { if(countDownTimer != null) countDownTimer.cancel(); });
        findViewById(R.id.btnReset).setOnClickListener(v -> resetTimer());
        findViewById(R.id.btnBreak).setOnClickListener(v -> { mTimeLeftInMillis = 300000; tvTaskStatus.setText("Working on: Break"); updateTimerText(); });

        // Task Screen par jane ke liye connection
        findViewById(R.id.btnGoTask).setOnClickListener(v -> startActivity(new Intent(this, TasksActivity.class)));

        findViewById(R.id.btnSetTime).setOnClickListener(v -> showSetTimeDialog());
    }

    private void showSetTimeDialog() {
        EditText et = new EditText(this);
        et.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        new AlertDialog.Builder(this).setTitle("Set Focus Time (Minutes)").setView(et)
                .setPositiveButton("Set", (d, w) -> {
                    String val = et.getText().toString();
                    if(!val.isEmpty()) { mTimeLeftInMillis = Integer.parseInt(val) * 60000L; updateTimerText(); }
                }).show();
    }

    private void startTimer() {
        if (mTimeLeftInMillis <= 0) return;
        if (countDownTimer != null) countDownTimer.cancel();
        countDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override public void onTick(long l) { mTimeLeftInMillis = l; updateTimerText(); }
            @Override public void onFinish() { tvTaskStatus.setText("Time Up!"); }
        }.start();
    }

    private void resetTimer() { if(countDownTimer != null) countDownTimer.cancel(); mTimeLeftInMillis = 0; updateTimerText(); tvTaskStatus.setText("Working on: None"); }

    private void updateTimerText() {
        int mins = (int) (mTimeLeftInMillis / 1000) / 60;
        int secs = (int) (mTimeLeftInMillis / 1000) % 60;
        tvTimer.setText(String.format(Locale.getDefault(), "%02d:%02d", mins, secs));
    }
}