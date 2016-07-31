package com.example.administrator.dongzhiwuapp.UpdateVersion;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class UpdateInfoService {
	ProgressDialog progressDialog;
	Handler handler;
	Context context;
	UpdateInfo updateInfo;
	
	public UpdateInfoService(Context context){
		this.context=context;
	}


	/*
	* 获取更新信息
	* 版本信息存储在服务器的update.txt文件中
	* 更新信息包括:
	* 1、版本信息
	* 2、最新版本具体描述
	* 3、下载地址
	*
	* */
	public UpdateInfo getUpDateInfo() throws Exception {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://120.27.96.50/mes/index.php/API/Server/updateInfo");

		HttpResponse httpResponse = httpClient.execute(httpGet);
		UpdateInfo updateInfo = new UpdateInfo();
		if (httpResponse.getStatusLine().getStatusCode() == 200)//请求相应成功
		{
			HttpEntity entity = httpResponse.getEntity();
			String response = EntityUtils.toString(entity, "utf-8");
			JSONObject jsonObject=new JSONObject(response);

			//Log.d("dsdsada", "getUpDateInfo: "+response);
			updateInfo.setVersion(jsonObject.getString("version"));
			updateInfo.setDescription(jsonObject.getString("description"));
			updateInfo.setUrl(jsonObject.getString("url"));
			this.updateInfo = updateInfo;
			return updateInfo;
		}
		updateInfo.setVersion("");
		updateInfo.setDescription("");
		updateInfo.setUrl("");
		this.updateInfo = updateInfo;

		return updateInfo;
	}

	/*
	 *判断是否需要更新
	 */
	public boolean isNeedUpdate(){
			String new_version = updateInfo.getVersion();
			String now_version="";
			try {
				PackageManager packageManager = context.getPackageManager();
				PackageInfo packageInfo = packageManager.getPackageInfo(
						context.getPackageName(), 0);
				now_version= packageInfo.versionName;
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			if (new_version.equals(now_version)) {
				return false;
			} else {
				return true;
			}
	}

	/*
	*
	* 下载文件
	* */
	public void downLoadFile(final String url,final ProgressDialog pDialog,Handler h){
		progressDialog=pDialog;
		handler=h;
		new Thread() {
			public void run() {        
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					int length = (int) entity.getContentLength();
                                        progressDialog.setMax(length);
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File file = new File(
								Environment.getExternalStorageDirectory(),
								"Test.apk");
						fileOutputStream = new FileOutputStream(file);
                        byte[] buf = new byte[10];   
						int ch = -1;
						int process = 0;
						while ((ch = is.read(buf)) != -1) {       
							fileOutputStream.write(buf, 0, ch);
							process += ch;
							progressDialog.setProgress(process);
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}.start();
	}


	//下载完毕，退出下载，进入安装APP步骤
	void down() {
		handler.post(new Runnable() {
			public void run() {
				progressDialog.cancel();
				update();
			}
		});
	}


	//安装最新版本APP
	void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "Test.apk")),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	
}
