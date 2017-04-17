package edu.csulb.scarnedice;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import android.os.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {
    private int userOverallScore = 0;
    private int userTurnScore = 0;
    private int cpuOverallScore = 0;
    private int cpuTurnScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view){
        TextView userTurnText = (TextView) findViewById(R.id.your_turn_text);
        TextView userOverallText = (TextView) findViewById(R.id.your_score_text);
        switch(view.getId()){
            case R.id.roll_btn:
                int roll = rollDie();
                if(roll != 1){
                    userTurnScore += roll;
                    userTurnText.setText("Your Turn: " + userTurnScore);
                }
                else{
                    userTurnScore = 0;
                    userTurnText.setText("Your Turn: " + userTurnScore);
                    computerTurn();
                }
                break;

            case R.id.hold_btn:
                userOverallScore += userTurnScore;
                userTurnScore = 0;
                userOverallText.setText("Your score: "+userOverallScore);
				if(userOverallScore >= 100){
					userOverallText.setText("You win");
                    setButtons(false);
					return;
				}
                computerTurn();
                break;
            case R.id.reset_btn:
                setButtons(true);
                resetData();
                break;
        }

    }

    public int rollDie(){
        ImageView diceFace = (ImageView)findViewById(R.id.dice_image);
        Random rand = new Random();
        int roll = rand.nextInt(5)+1;

        ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.dice_animation);
        anim.setTarget(diceFace);
        anim.setDuration(500);
        anim.start();

        Map<Integer,Integer> faces = new HashMap<>();
        faces.put(1,R.drawable.dice1);
        faces.put(2,R.drawable.dice2);
        faces.put(3,R.drawable.dice3);
        faces.put(4,R.drawable.dice4);
        faces.put(5,R.drawable.dice5);
        faces.put(6,R.drawable.dice6);

        diceFace.setImageResource(faces.get(roll));
        return roll;
    }
	
	public void resetData(){
		TextView userTurnText = (TextView) findViewById(R.id.your_turn_text);
        TextView userOverallText = (TextView) findViewById(R.id.your_score_text);
		TextView cpuStatusText = (TextView) findViewById(R.id.cpu_status_text);
        TextView cpuOverallText = (TextView) findViewById(R.id.cpu_score_text);
		TextView cpuTurnText = (TextView) findViewById(R.id.cpu_turn_text);
		userOverallScore = 0;
        userTurnScore = 0;
        cpuOverallScore = 0;
        cpuTurnScore = 0;
		userTurnText.setText("Your Turn: 0");
		userOverallText.setText("Your Score: 0");
		cpuOverallText.setText("Computer Score: 0");
		cpuTurnText.setText("Computer Turn: 0");
		cpuStatusText.setText("");
	}

    public void computerTurn(){
        setButtons(false);
        final TextView cpuStatusText = (TextView) findViewById(R.id.cpu_status_text);
        final TextView cpuTurnText = (TextView) findViewById(R.id.cpu_turn_text);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(cpuTurnScore < 20) { // re-roll
                    int roll = rollDie();
                    cpuStatusText.setText("Computer rolled a "+roll);
                    if (roll != 1) {
                        cpuTurnScore += roll;
                        cpuTurnText.setText("Computer Turn: "+cpuTurnScore);
                        handler.postDelayed(this,1000);
                    }
                    else {
                        cpuTurnScore = 0;
                        cpuTurnText.setText("Computer Turn: "+cpuTurnScore);
                        setButtons(true);
                    }
                }
                else{
                    TextView cpuOverallText = (TextView) findViewById(R.id.cpu_score_text);
                    cpuStatusText.setText("Computer holds");
                    cpuOverallScore += cpuTurnScore;
                    cpuTurnScore = 0;
                    cpuOverallText.setText("Computer score: "+cpuOverallScore);
					if(cpuOverallScore >= 100){
						cpuStatusText.setText("Computer Wins");
					}
					else{
                        setButtons(true);
                    }
                }
            }
        }, 500);
    }

    public void setButtons(boolean value){
        Button holdButton = (Button) findViewById(R.id.hold_btn);
        Button rollButton = (Button) findViewById(R.id.roll_btn);
        holdButton.setEnabled(value);
        rollButton.setEnabled(value);
    }
}
