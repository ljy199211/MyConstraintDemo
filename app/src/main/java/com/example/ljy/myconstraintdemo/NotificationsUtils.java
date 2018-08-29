package com.example.ljy.myconstraintdemo;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class NotificationsUtils {
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";


    @SuppressLint("NewApi")
    public static boolean isNotificationEnabled(Context context) {

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.N){
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            return notificationManagerCompat.areNotificationsEnabled();
        }else {
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo applicationInfo = context.getApplicationInfo();

            String packageName = context.getApplicationContext().getPackageName();
            int uid = applicationInfo.uid;
            Class appOpsClass;
            try {
                appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method appOpsClassMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW,
                        Integer.TYPE, Integer.TYPE, String.class);
                Field appOpsClassDeclaredField = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

                int value = (int) appOpsClassDeclaredField.get(Integer.class);

                return ((Integer) appOpsClassMethod.invoke(appOpsManager, value, uid, packageName) == AppOpsManager.MODE_ALLOWED);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;

        }
    }

    public static boolean isOpenNotice(Context context) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        return notificationManagerCompat.areNotificationsEnabled();
    }

}


