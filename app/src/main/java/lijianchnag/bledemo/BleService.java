package lijianchnag.bledemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by 13155 on 2017/9/15:11:53.
 * Des :
 */

public class BleService extends Service {

    public String TAG = BleService.class.getSimpleName();

    LocalBinder mBinder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void log() {
        Log.d(TAG,"~~~~~~~~~~~~~~~~~~~开启Service~~~~~~~~~~~~~~~~~~~");
    }


    public  class LocalBinder  extends Binder {
        BleService getService(){
            return BleService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
