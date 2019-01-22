package game.android.com.catstacks.engine.core;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import game.android.com.catstacks.R;
import game.android.com.catstacks.engine.pool.ArrayPool;

public class Game extends Activity
{
    private RelativeLayout layout;
    private MainSurfaceView surfaceView;
    private AdView adView;
    private InterstitialAd interstitialAd;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        initGL();
        initBannerAd();
        initInterstitialAd();
        initLayout();

        setContentView(layout);

        //Gets audio sample rate (files are 48k, fine on newer phones but some older ones expect 44.1k
        //AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //String sample = am.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE);
        //Log.println(Log.ERROR, "sample rate", sample);

        //Gets display refresh rate (60.000004 on most devices)
        //Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        //Log.println(Log.ERROR, "refresh", display.getRefreshRate() + "");
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        surfaceView.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        surfaceView.resume();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        surfaceView.destroy();
    }

    private void initGL()
    {
        MyHandler handler = new MyHandler(this);
        surfaceView = new MainSurfaceView(this.getApplicationContext(), handler);
    }

    final static class MyHandler extends Handler
    {
        private Game parent;

        public MyHandler(Game parent)
        {
            this.parent = parent;
        }

        @Override
        public void handleMessage(Message message)
        {
            if(message.what == 0)
            {
                parent.showBannerAd();
            }
            else if(message.what == 1)
            {
                parent.hideBannerAd();
            }
            else if(message.what == 2)
            {
                parent.showInterstitialAd();
            }
            super.handleMessage(message);
        }
    }

    private void initBannerAd()
    {
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.game_over_ad));
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setVisibility(View.GONE);

        String androidId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        adView.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice(androidId).build());
    }

    private void initInterstitialAd()
    {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_fullscreen_ad));

        String androidId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        interstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice(androidId).build());
        interstitialAd.loadAd(new AdRequest.Builder().addTestDevice("85CD4020078952C6F27C165582DD8661").build());
    }

    private void initLayout()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams paramsgl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.addView(surfaceView, paramsgl);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layout.addView(adView, params);
    }

    public void showBannerAd()
    {
        if(adView.getVisibility() != View.VISIBLE)
        {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            //layout.addView(adView, params);
            //String androidId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            //adView.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice(androidId).build());
            adView.setVisibility(View.VISIBLE);
        }
    }

    public void hideBannerAd()
    {
        if(adView.getVisibility() != View.GONE)
        {
            adView.setVisibility(View.GONE);
            //layout.removeView(adView);
        }
    }

    public void showInterstitialAd()
    {
        //String androidId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        //interstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice(androidId).build());
        //interstitialAd.loadAd(new AdRequest.Builder().addTestDevice("85CD4020078952C6F27C165582DD8661").build());

        if(interstitialAd.isLoaded())
        {
            interstitialAd.show();
        }
        else
        {
            interstitialAd.setAdListener(new AdListener()
            {
                @Override
                public void onAdLoaded()
                {
                    interstitialAd.show();
                }
            });
        }
    }
}
