package thisismyrobot.android.mozzy;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class AnimatedMozzy extends View implements OnTouchListener
{
    private boolean captured = false;
    private boolean flying = false;
    private Handler timerHandler = new Handler();
    Random random = new Random();

    public AnimatedMozzy(Context context)
    {
        super(context);
        this.setOnTouchListener(this);
        this.setKeepScreenOn(true);
        startAnimation();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        this.captured = !(event.getAction() == MotionEvent.ACTION_UP);
        return true;
    }

    public void onDraw(Canvas c)
    {
        if(this.flying && !this.captured)
        {
            this.setBackgroundColor(Color.RED);
        }
        else
        {
            this.setBackgroundColor(Color.BLUE);
        }
    }

    private int getRandomDelay()
    {
        int min = 500;
        int max = 3000;
        return random.nextInt(max - min) + min;
    }

    public void startAnimation()
    {
        timerHandler.removeCallbacks(updateAnimationType);
        timerHandler.post(updateAnimationType);
    }

    private Runnable updateAnimationType = new Runnable()
    {
        public void run()
        {
            if(!captured)
            {
                flying = !flying;
            }
            else
            {
                flying = false;
            }
            timerHandler.postDelayed(this, getRandomDelay());
        }
     };
}
