package thisismyrobot.android.mozzy;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class Mozzy extends Activity
{
    private AnimatedMozzy mozzy;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.main);
        mozzy = new AnimatedMozzy(this);
        ((LinearLayout)findViewById(R.id.main)).addView(mozzy);
    }

    public void onPause()
    {
        super.onPause();
        mozzy.stopAnimations();
        finish();
    }
}