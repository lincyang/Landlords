package com.landlords;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.view.Window;
import android.view.WindowManager;

public class MyActivity extends Activity {
	/*
	 * QQ:361106306
	 * by:С��
	 * ת�ش˳����뱣����Ȩ,δ������������������ҵ��;!
	 * */
	MyView myView;
	String messString;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if(msg.what==0){
				messString=msg.getData().getString("data");
				showDialog();
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		// ���ر�����
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ����״̬��
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// ��������
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		myView = new MyView(this,handler);
		setContentView(myView);
	}
	public void showDialog(){
		new AlertDialog.Builder(this).setMessage(messString)
		.setPositiveButton("���¿�ʼ��Ϸ", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				reGame();
			}
		}).setTitle("By:С��,QQ:361106306").create().show();
	}
	//���¿�ʼ��Ϸ
	public void reGame(){
		myView = new MyView(this,handler);
		setContentView(myView);
	}

}
