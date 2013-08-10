package com.comxtohr.training;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;

public class MainActivity extends Activity {
	
	int k = 0;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button button_getIP = (Button) findViewById(R.id.button_getIP);
        Button button_root = (Button) findViewById(R.id.button_root);
        Button button_unroot = (Button) findViewById(R.id.button_unroot);
        Button button_screenshot = (Button) findViewById(R.id.button_screenshot);
        
        final TextView textview_IP = (TextView) findViewById(R.id.textView_IP);
        final TextView textview_status = (TextView) findViewById(R.id.textView_status);
        
        button_getIP.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				textview_IP.setText(getIP());
			}
		});
        
        button_screenshot.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Process process = null;
				DataOutputStream os = null;
				try {
					process = Runtime.getRuntime().exec("su");
					os = new DataOutputStream(process.getOutputStream());
		            os.writeBytes(String.format("/system/bin/screencap -p /mnt/sdcard/screenshot%1$03d.png\n",k));
		            os.writeBytes("exit\n");
		            os.flush();
		            int exitValue = process.waitFor();
		            if (exitValue == 0) textview_status.setText(String.format("screenshot%1$03d.png",k));
		            k++;
		            k %= 40;
		            if (os != null) os.close();
	                process.destroy();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/*Bitmap screenshot = getScreenShotBitmap();
				try {
					textview_status.setText(String.format("screenshot%1$03d.png",k));
					FileOutputStream fout = new FileOutputStream(String.format("/mnt/sdcard/screenshot%1$03d.png",k));
					screenshot.compress(Bitmap.CompressFormat.PNG,100,fout);
					fout.flush();
					fout.close();
					k++;
					k %= 40;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			}
        });
        
        button_root.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Process process = null;
				DataOutputStream os = null;
				try {
					process = Runtime.getRuntime().exec("su");
					os = new DataOutputStream(process.getOutputStream());
		            os.writeBytes("chmod 777 /dev/graphics/fb0\n");
		            os.writeBytes("setprop service.adb.tcp.port 5555\n");
		            os.writeBytes("stop adbd\n");
		            os.writeBytes("start adbd\n");
		            os.writeBytes("exit\n");
		            os.flush();
		            int exitValue = process.waitFor();
		            if (exitValue == 0) textview_status.setText("Root Status:Rooted");
		            if (os != null) os.close();
	                process.destroy();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
        
        button_unroot.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Process process = null;
				DataOutputStream os = null;
				try {
					process = Runtime.getRuntime().exec("su");
					os = new DataOutputStream(process.getOutputStream());
		            os.writeBytes("chmod 660 /dev/graphics/fb0\n");
		            os.writeBytes("setprop service.adb.tcp.port -1\n");
		            os.writeBytes("stop adbd\n");
		            os.writeBytes("start adbd\n");
		            os.writeBytes("exit\n");
		            os.flush();
		            int exitValue = process.waitFor();
		            if (exitValue == 0) textview_status.setText("Root Status:unRoot");
		            if (os != null) os.close();
	                process.destroy();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
    
    }
    
    public String getIP(){
    	WifiManager wm = (WifiManager)getSystemService(Context.WIFI_SERVICE);
    	WifiInfo wi = wm.getConnectionInfo();
    	int ip = wi.getIpAddress();
    	if (ip!=0)
    		return 	"IP:" + (ip & 0xFF ) + "." +
							((ip >> 8 ) & 0xFF) + "." + 
							((ip >> 16 ) & 0xFF) + "." + 
							( ip >> 24 & 0xFF);
    	else
    		return "IP:fail to get IP";
    }
    
    @SuppressWarnings("deprecation")
	public Bitmap getScreenShotBitmap() {
		FileInputStream buf = null;
		try {
			DisplayMetrics dm = new DisplayMetrics();
			Display display = getWindowManager().getDefaultDisplay();
			display.getMetrics(dm);
			int screenWidth = dm.widthPixels + 16;
			int screenHeight = dm.heightPixels;
			int pixelformat = display.getPixelFormat();
			PixelFormat localPixelFormat = new PixelFormat();
			PixelFormat.getPixelFormatInfo(pixelformat, localPixelFormat);
			int deepth = localPixelFormat.bytesPerPixel;
			
			byte[] piex = new byte[screenHeight * screenWidth * deepth];
			
			buf = new FileInputStream(new File("/dev/graphics/fb0"));
			DataInputStream dStream = new DataInputStream(buf);
			dStream.readFully(piex);
			if (buf != null) buf.close();
			int[] colors = new int[screenHeight * screenWidth];
			
			for (int m = 0; m < piex.length; m+=deepth) {
				int r = (piex[m] & 0xFF);
				int g = (piex[m + 1] & 0xFF);
				int b = (piex[m + 2] & 0xFF);
				int a = (piex[m + 3] & 0xFF);
				colors[m/deepth] = (a << 24) + (r << 16) + (g << 8) + b;
			}
			
			return 	Bitmap.createBitmap(colors, screenWidth, screenHeight,
					Bitmap.Config.ARGB_8888);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
