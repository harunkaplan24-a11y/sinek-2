package com.example.sinek;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.core.app.NotificationCompat;
import java.util.Random;

public class FlyService extends Service {

    private WindowManager windowManager;
    private RelativeLayout rootLayout;
    private ImageView flyImage;
    private Button closeButton;
    private WindowManager.LayoutParams params;
    private Random random = new Random();
    private Handler moveHandler = new Handler(Looper.getMainLooper());

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        startInForeground();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        rootLayout = new RelativeLayout(this);

        // Sinek Görseli (Yüklediğiniz sink.png dosyasına bağlandı)
        flyImage = new ImageView(this);
        flyImage.setImageResource(R.drawable.sink); 
        
        RelativeLayout.LayoutParams flyParams = new RelativeLayout.LayoutParams(150, 150);
        rootLayout.addView(flyImage, flyParams);

        // Kapatma (X) Butonu
        closeButton = new Button(this);
        closeButton.setText("X");
        closeButton.setTextColor(Color.WHITE);
        closeButton.setBackgroundColor(Color.RED);
        closeButton.setVisibility(View.GONE); // İlk başta gizli

        RelativeLayout.LayoutParams closeParams = new RelativeLayout.LayoutParams(120, 120);
        closeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        closeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        closeParams.setMargins(0, 50, 50, 0);
        rootLayout.addView(closeButton, closeParams);

        // Ekran Düzeni Parametreleri
        int layoutType = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                : WindowManager.LayoutParams.TYPE_PHONE;

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                layoutType,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        params.gravity = Gravity.TOP | Gravity.LEFT;
        windowManager.addView(rootLayout, params);

        // Sineğe Dokunma Olayı (Dokununca kaçar)
        flyImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    flyAway();
                }
                return true;
            }
        });

        // X Butonuna Basınca Kapanma
        closeButton.setOnClickListener(v -> stopSelf());

        // Rastgele Hareket Döngüsü
        startMoving();

        // 30 Saniye Zamanlayıcısı
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            closeButton.setVisibility(View.VISIBLE);
        }, 30000);
    }

    private void flyAway() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels - 200;
        int screenHeight = getResources().getDisplayMetrics().heightPixels - 200;

        float newX = random.nextInt(Math.max(screenWidth, 100));
        float newY = random.nextInt(Math.max(screenHeight, 100));

        flyImage.animate()
                .x(newX)
                .y(newY)
                .setDuration(200) // Hızlı kaçış
                .start();
    }

    private void startMoving() {
        moveHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                flyAway();
                moveHandler.postDelayed(this, 1000 + random.nextInt(2000));
            }
        }, 1000);
    }

    private void startInForeground() {
        String CHANNEL_ID = "sinek_service_channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Sinek Servisi",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Sinek Çalışıyor")
                .setContentText("Sinek ekranda dolaşıyor...")
                .setSmallIcon(R.drawable.sink)
                .build();

        startForeground(1, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (rootLayout != null) {
            windowManager.removeView(rootLayout);
        }
        moveHandler.removeCallbacksAndMessages(null);
    }
}
