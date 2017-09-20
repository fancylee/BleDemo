package lijianchnag.bledemo;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    private static Toast toast;

    public ToastUtil() {
    }

    public static void showShort(Context context, CharSequence message) {
        if(toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
//            toast.setGravity(17, 0, 0);
        } else {
            toast.setText(message);
        }

        toast.show();
    }

    public static void showShort(Context context, int message) {
        if(toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }

        toast.show();
    }

    public static void showLong(Context context, CharSequence message) {
        if(toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        } else {
            toast.setText(message);
        }

        toast.show();
    }

    public static void showLong(Context context, int message) {
        if(toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        } else {
            toast.setText(message);
        }

        toast.show();
    }

    public static void show(Context context, CharSequence message, int duration) {
        if(toast == null) {
            toast = Toast.makeText(context, message, duration);
        } else {
            toast.setText(message);
        }

        toast.show();
    }

    public static void show(Context context, int message, int duration) {
        if(toast == null) {
            toast = Toast.makeText(context, message, duration);
        } else {
            toast.setText(message);
        }

        toast.show();
    }

    public static void hideToast() {
        if(toast != null) {
            toast.cancel();
        }

    }
}