package lijianchnag.bledemo;

import android.animation.ObjectAnimator;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static lijianchnag.bledemo.BleUtils.BLE_CONNECTED;
import static lijianchnag.bledemo.BleUtils.BLE_DISCONNECTED;

/**
 * Created by 13155 on 2017/9/21:15:09.
 * Des :
 */

public class BleControlActivity extends AppCompatActivity {

    @Bind(R.id.activity_blecontrol_tv_name)
    TextView mTvName;
    @Bind(R.id.activity_blecontrol_tv_address)
    TextView mTvAddress;
    @Bind(R.id.activity_blecontrol_scrollview)
    ScrollView scrollView;
    @Bind(R.id.activity_blecontrol_tv_send)
    TextView mTvSend;
    @Bind(R.id.activity_blecontrol_bt_send)
    Button mBtSend;
    @Bind(R.id.activity_blecontrol_tv_received)
    TextView mTvRecriver;
    View mIvBle;
    BleUtils bleUtils;
    public   final String TAG = BleControlActivity.this.getClass().getSimpleName();
    String[] strings = new String[]{
        "fffe012b1001000101c3",
        "fffe01201001000bb000000043000000010200cf",
        "fffe012011010047000000014d75547265657b3c23423130323d303030337b23423936303d30322c332c303030662c3323423030313d3033653823423936303d30322c322c303030662c337d3e7d58",
    };
    BluetoothDevice bluetoothDevice;
    ObjectAnimator objectAnimator;
    Context mContext;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case BLE_CONNECTED:
                    ToastUtil.showShort(mContext,R.string.connected);
                    break;
                case BLE_DISCONNECTED:
                    ToastUtil.showShort(mContext,R.string.disconnected);
                    break;

            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blecontrol);
        ButterKnife.bind(this);
        bluetoothDevice = (BluetoothDevice)(getIntent().getParcelableExtra(MainActivity.DEVICE));
        Log.d(TAG,"bluetoothDevice"+bluetoothDevice.getAddress());
        bleUtils = BleUtils.getInstance(this);
        bleUtils.connect(bluetoothDevice);
        registerReceiver(broadcastReceiver,makeGattUpdateIntentFilter());
        mIvBle = LayoutInflater.from(this).inflate(R.layout.imageview_ble,null,false);
        objectAnimator = ObjectAnimator.ofFloat(mIvBle,"alpha",0,360).setDuration(1000);
        mContext = this;
    }

    private IntentFilter makeGattUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BleUtils.BLE_CONNECTED);
        intentFilter.addAction(BleUtils.BLE_DISCONNECTED);
        intentFilter.addAction(BleUtils.BLE_DATA_AVAILABLE);
        intentFilter.addAction(BleUtils.BLE_SERVICES_DISCOVERED);
        return intentFilter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
//        ImageView mIv = new ImageView(this);
//        mIv.setImageResource(R.mipmap.rbc_bluetooth_2);
        menu.findItem(R.id.menu_refresh).setActionView(mIvBle);
        mIvBle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return true;
    }
}
