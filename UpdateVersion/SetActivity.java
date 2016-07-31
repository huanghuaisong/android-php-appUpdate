package com.example.administrator.dongzhiwuapp.UpdateVersion;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.administrator.dongzhiwuapp.R;

public class SetActivity extends Activity {

	private UpdateInfo info;
	private ProgressDialog progressDialog;//下载进度条窗口
	UpdateInfoService updateInfoService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update);
        Button button=(Button)findViewById(R.id.button1);
	    button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				checkUpdate();
			}
		});
	}



   private void checkUpdate(){
		new Thread() {
			public void run() {
				try {
					updateInfoService = new UpdateInfoService(SetActivity.this);
					info = updateInfoService.getUpDateInfo();
					handler1.sendEmptyMessage(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
   }
   
	@SuppressLint("HandlerLeak")
	private Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			if (updateInfoService.isNeedUpdate()) {
				showUpdateDialog();
			}
		}
	};

	private void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("请升级APP版本至" + info.getVersion());
		builder.setMessage(info.getDescription());
		builder.setCancelable(false);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					downFile(info.getUrl());
				} else {
					Toast.makeText(SetActivity.this, "SD卡不可用，请插入SD卡",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.create().show();
	}


	//进入下载
	void downFile(final String url) { 
		progressDialog = new ProgressDialog(SetActivity.this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setTitle("正在下载");
		progressDialog.setMessage("请稍后...");
		progressDialog.setProgress(0);
		progressDialog.show();
		updateInfoService.downLoadFile(url, progressDialog,handler1);
	}
	
}