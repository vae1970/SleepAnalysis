package top.vae1970.sleepAnalysis;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final UsageStatsManager usageStatsManager = (UsageStatsManager) getApplicationContext().getSystemService(Context.USAGE_STATS_SERVICE);// Context.USAGE_STATS_SERVICE);
        String TAG = "MainActivity";
        SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy HH:mm:ss");

        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_WEEK, -1);
        long startTime = calendar.getTimeInMillis();

        Log.d(TAG, "Range start:" + dateFormat.format(startTime));
        Log.d(TAG, "Range end:" + dateFormat.format(endTime));

        assert usageStatsManager != null;
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_WEEKLY, startTime, endTime);
        System.out.println(usageStatsList);
        //  if usageStatsList is empty, start setting activity
        if (usageStatsList.isEmpty()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        } else {
            for (UsageStats usageStats : usageStatsList) {
                long time = usageStats.getTotalTimeInForeground() / 1000 / 60;
                String packageName = usageStats.getPackageName();
                if (time > 0) {
                    System.out.println(getAppName(getApplicationContext(), packageName) + ": " + time);
                }
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public String getAppName(Context context, String packName) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(packName, 0);
            return info.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getBitmap(Context context, String packageName) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext()
                    .getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        //xxx根据自己的情况获取drawable
        Drawable d = packageManager.getApplicationIcon(applicationInfo);
        BitmapDrawable bd = (BitmapDrawable) d;
        return bd.getBitmap();
    }

}
