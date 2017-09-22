package lijianchnag.bledemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    Handler mHandler;

    private static final long SCAN_PERIOD = 10000;

    public final static  String BLE_CONNECTED = "BLE_CONNECTED";

    public final static String BLE_DISCONNECTED = "BLE_DISCONNECTED";

    public final static String BLE_SERVICES_DISCOVERED = "BLE_SERVICES_DISCOVERED";

    public final static String BLE_DATA_AVAILABLE ="BLE_DATA_AVAILABLE";

    public final static UUID UUID_NOTIFY =
            UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_SERVICE =
            UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");

    String intentAction;

    BluetoothGattCallback mGattCallBack = new BluetoothGattCallback() {
        /**
         * 连接状态发生变化的回调。
         * 1,如果是处于连接状态，发送连接上的广播给蓝牙控制界面。
         * @param gatt
         * @param status
         * @param newState
         */
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//            super.onConnectionStateChange(gatt, status, newState);
            Log.d(TAG,"oldStatus=" + status + " NewStates=" + newState);
            if(status == BluetoothGatt.GATT_SUCCESS){
                if(newState == BluetoothProfile.STATE_CONNECTED){
                    intentAction = BLE_CONNECTED;
                    broadcastUpdate(intentAction);
                }
            }
        }
    };

    private void broadcastUpdate(String intentAction) {
        Intent intent = new Intent(intentAction);
        mContext.sendBroadcast(intent);
    }


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
    private BleRecyclerViewAdapter adapter;

    private BleUtils(Context mContext){
        this.mContext = mContext;
        startService();
        mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        mHandler = new Handler();
        myLeScanCallBack = new MyLeScanCallBack();
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
//        if(mServiceConnection != null)
//        mContext.unbindService(mServiceConnection);
        if(mBleService != null){
            mBleService = null;
        }
    }

    public void openBle() {
        /**
         * 如果mBluetoothAdapter为空，则表示该设备不支持蓝牙。
         * 如果没打开蓝牙。申请设备打开蓝牙。
         */

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

    public boolean isBleOpen(){
        isBleOpen = mBluetoothAdapter.isEnabled();
        return  isBleOpen;
    }

    public void scanDevice(){
        isBleOpen = mBluetoothAdapter.isEnabled();

        if(!isBleOpen){
            openBle();
        }else{
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBluetoothAdapter.stopLeScan(myLeScanCallBack);
                }
            },SCAN_PERIOD);
            mBluetoothAdapter.startLeScan(myLeScanCallBack);
        }
    }

    public void setAdapter(BleRecyclerViewAdapter adapter) {
        this.adapter = adapter;
        adapter.setmList(mDeviceList);
    }

    /**
     * 盛放扫描到的蓝牙设备的容器
     */
    List<BluetoothDevice> mDeviceList = new ArrayList<>();

    public void connect(BluetoothDevice mBluetoothDevice) {

        mBluetoothDevice.connectGatt(mContext,false,mGattCallBack);
    }

    private class MyLeScanCallBack implements BluetoothAdapter.LeScanCallback{

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.d(TAG,"device.getName()"+device.getName()+"device.getAddress()"+device.getAddress());
//            mDeviceList.clear();
            if(!mDeviceList.contains(device)) {
                mDeviceList.add(device);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
