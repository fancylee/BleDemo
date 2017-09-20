package lijianchnag.bledemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.IBinder;
import android.renderscript.Script;
import android.util.Log;

/**
 * Created by 13155 on 2017/9/15:11:35.
 * Des :蓝牙操作类。
 * 里面的内容包括蓝牙的开启，连接，发送数据，发送大数据，断开。
 *
 */

public class BleUtils {

    public String TAG = getClass().getSimpleName();

    BleService mBleService;

    BluetoothManager mBluetoothManager;

    BluetoothAdapter mBluetoothAdapter;

    boolean isBleOpen;

    MyLeScanCallBack myLeScanCallBack;



    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
           mBleService = ((BleService.LocalBinder)service).getService();
            mBleService.log();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBleService = null;
        }
    };

    public static BleUtils instance;
    Context mContext;

    private BleUtils(Context mContext){
        this.mContext = mContext;
        startService();
        mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);

    }

    public static BleUtils getInstance(Context mContext){
        if(instance == null){
            instance = new BleUtils(mContext);
        }
        return  instance;
    }

    public void startService(){
        Intent intent = new Intent(mContext,BleService.class);
        mContext.bindService(intent,mServiceConnection,Context.BIND_AUTO_CREATE);
    }


    public void unBindService() {
        mContext.unbindService(mServiceConnection);
    }

    public void openBle() {
        /**
         * 如果mBluetoothAdapter为空，则表示该设备不支持蓝牙。
         * 如果没打开蓝牙。申请设备打开蓝牙。
         */
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if(mBluetoothAdapter == null){
            ToastUtil.showShort(mContext,R.string.blenotsupport);
        }else{
            isBleOpen = mBluetoothAdapter.isEnabled();
           if(!mBluetoothAdapter.isEnabled()){
               Intent enableBleIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
               enableBleIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               mContext.startActivity(enableBleIntent);
           }
        }
    }

    private class MyLeScanCallBack implements BluetoothAdapter.LeScanCallback{

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.d(TAG,"device.getName()"+device.getName()+"device.getAddress()"+device.getAddress());
        }
    }
}
