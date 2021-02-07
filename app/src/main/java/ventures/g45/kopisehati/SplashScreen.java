package ventures.g45.kopisehati;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class SplashScreen extends Activity {

    Intent intent;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sharedPreferences = getSharedPreferences("kopisehati", 0);
        editor = sharedPreferences.edit();
        sharedPreferences.edit().remove("id_outlet").apply();

        editor.putInt("selected", 0);
        editor.apply();

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(1000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    /*if (no_hp.isEmpty()) {
                        Intent intent = new Intent(SplashScreen.this, SignIn.class);
                        startActivity(intent);
                    } else {
                        //editor.putString("id_alamat", "");
                        //editor.apply();*/
                    intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    //}
                }
            }
        };
        timerThread.start();
    }
}
