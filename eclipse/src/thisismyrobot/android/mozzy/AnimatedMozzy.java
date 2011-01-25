package thisismyrobot.android.mozzy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class AnimatedMozzy extends View implements OnTouchListener
{
    private boolean captured = false;
    private boolean flying = false;
    private long mStartTime;
    private Handler timerHandler = new Handler();

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
        return 1000;
    }

    public void startAnimation()
    {
        if (this.mStartTime == 0L)
        {
            this.mStartTime = System.currentTimeMillis();
            timerHandler.removeCallbacks(updateAnimationType);
            timerHandler.post(updateAnimationType);
       }
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
