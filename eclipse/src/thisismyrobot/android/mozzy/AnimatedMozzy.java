package thisismyrobot.android.mozzy;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaPlayer;
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
    private Paint brush;
    private int x;
    private int y;
    private int gx;
    private int gy;
    private int sw;
    private int sh;
    private boolean firstdraw = true;
    private MediaPlayer mp;

    public AnimatedMozzy(Context context)
    {
        super(context);
        this.setOnTouchListener(this);
        this.setKeepScreenOn(true);
        this.brush = new Paint(Paint.ANTI_ALIAS_FLAG);
        mp = MediaPlayer.create(context, R.raw.loop);
        mp.setLooping(true);
        startAnimations();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        this.captured = !(event.getAction() == MotionEvent.ACTION_UP);
        if(this.captured)
        {
            this.flying = false;
            if(mp.isPlaying())
            {
                mp.pause();
            }
        }
        return true;
    }

    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if(this.firstdraw)
        {
            this.sw = canvas.getWidth();
            this.sh = canvas.getHeight();
            this.x = this.sw / 2;
            this.y = this.sh / 2;
            this.firstdraw = false;
        }

        //Draw mozzy
        brush.setColor(Color.BLACK);
        brush.setStyle(Paint.Style.FILL);
        brush.setStrokeWidth(0);
        canvas.drawOval(new RectF(x-1.5f, y-5, x+1.5f, y), brush);
        canvas.drawOval(new RectF(x-1, y, x+1, y+8), brush);

        //front legs
        canvas.drawLine(x, y, x+4, y-4, brush);
        canvas.drawLine(x, y, x-4, y-4, brush);
        canvas.drawLine(x+4, y-4, x+6, y-8, brush);
        canvas.drawLine(x-4, y-4, x-6, y-8, brush);

        //middle legs
        canvas.drawLine(x, y, x+6, y+2, brush);
        canvas.drawLine(x, y, x-6, y+2, brush);
        canvas.drawLine(x+6, y+2, x+7, y+6, brush);
        canvas.drawLine(x-6, y+2, x-7, y+6, brush);

        //back legs
        canvas.drawLine(x, y, x+3, y+4, brush);
        canvas.drawLine(x, y, x-3, y+4, brush);
        canvas.drawLine(x+3, y+4, x+4, y+12, brush);
        canvas.drawLine(x-3, y+4, x-4, y+12, brush);

        //wings
        brush.setColor(Color.rgb(128, 128, 0));
        brush.setAlpha(64);
        brush.setStyle(Paint.Style.FILL);
        canvas.drawOval(new RectF(x, y-1, x+14, y+3), brush);
        canvas.drawOval(new RectF(x, y-1, x-14, y+3), brush);
        brush.setColor(Color.BLACK);
        brush.setAlpha(64);
        brush.setStyle(Paint.Style.STROKE);
        canvas.drawOval(new RectF(x, y-1, x+14, y+3), brush);
        canvas.drawOval(new RectF(x, y-1, x-14, y+3), brush);
    }

    private int getRandomDelay(int min, int max)
    {
        return random.nextInt(max - min) + min;
    }

    public void startAnimations()
    {
        timerHandler.removeCallbacks(updateAnimationType);
        timerHandler.post(updateAnimationType);
        timerHandler.removeCallbacks(updatePosition);
        timerHandler.post(updatePosition);
        timerHandler.removeCallbacks(updateGravity);
        timerHandler.post(updateGravity);
    }

    public void stopAnimations()
    {
        timerHandler.removeCallbacks(updateAnimationType);
        timerHandler.removeCallbacks(updatePosition);
        timerHandler.removeCallbacks(updateGravity);
        if(mp.isPlaying())
        {
            mp.stop();
        }
        mp.release();
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

            if(flying)
            {
                if(!mp.isPlaying())
                {
                    mp.start();
                }
            }
            else
            {
                if(mp.isPlaying())
                {
                    mp.pause();
                }
            }
            timerHandler.postDelayed(this, getRandomDelay(500, 3000));
        }
     };

     private Runnable updatePosition = new Runnable()
     {
         public void run()
         {
             if(!firstdraw && flying)
             {
                 if(x < gx)
                 {
                     x += random.nextInt(10);
                 }
                 else
                 {
                     x -= random.nextInt(10);
                 }
                 if(y < gy)
                 {
                     y += random.nextInt(10);
                 }
                 else
                 {
                     y -= random.nextInt(10);
                 }
                 x += random.nextInt(7) - 3;
                 y += random.nextInt(7) - 3;
                 invalidate();
             }
             timerHandler.postDelayed(this, 20);
         }
     };

     private Runnable updateGravity = new Runnable()
     {
         public void run()
         {
             if(!firstdraw)
             {
                 gx = random.nextInt(sw);
                 gy = random.nextInt(sh);
             }
             timerHandler.postDelayed(this, getRandomDelay(1500, 5000));
         }
     };
}
