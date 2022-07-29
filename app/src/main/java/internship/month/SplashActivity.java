package internship.month;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    ImageView imageView;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        sp = getSharedPreferences(ConstantSp.PREFERENCE,MODE_PRIVATE);
        imageView = findViewById(R.id.splash_iv);

        AlphaAnimation animation = new AlphaAnimation(0,1);
        animation.setDuration(2500);
        //animation.setRepeatCount(2);
        imageView.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sp.getString(ConstantSp.EMAIL,"").equalsIgnoreCase("")){
                    new CommonMethod(SplashActivity.this,MainActivity.class);
                    finish();
                }
                else{
                    new CommonMethod(SplashActivity.this,HomeActivity.class);
                    finish();
                }
            }
        },3000);

    }
}