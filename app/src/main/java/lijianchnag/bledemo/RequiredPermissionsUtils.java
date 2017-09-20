package lijianchnag.bledemo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * 安卓6.0应用需要的权限设置
 * @author lijianchang
 *
 */
public class RequiredPermissionsUtils {


	public static int REQ_CODE = 102;

	private Context mContext;

	public RequiredPermissionsUtils(Context context) {
		super();
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	public void requiredPermission(String permission){
		//判断当前Activity是否已经获得了该权限
		if (ContextCompat.checkSelfPermission(mContext,
				permission)
				!= PackageManager.PERMISSION_GRANTED) {

			//如果App的权限申请曾经被用户拒绝过，就需要在这里跟用户做出解释
			if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext,
					permission)) {
				Toast.makeText(mContext,"please give me the permission", Toast.LENGTH_SHORT).show();
				ActivityCompat.requestPermissions((Activity) mContext,
						new String[]{permission},
						REQ_CODE);
			} else {
				//进行权限请求
				ActivityCompat.requestPermissions((Activity) mContext,
						new String[]{permission},
						REQ_CODE);
			}
		}
	}

	public void requiredPermission(String permission, int REQ_CODE){
		//判断当前Activity是否已经获得了该权限String
		if (ContextCompat.checkSelfPermission(mContext,
				permission)
				!= PackageManager.PERMISSION_GRANTED) {

				ActivityCompat.requestPermissions((Activity) mContext,
						new String[]{permission},
						REQ_CODE);
		}
	}
}
