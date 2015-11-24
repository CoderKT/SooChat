package com.kekexun.soochat.activity.install;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kekexun.soochat.activity.BaseActivity;
import com.kekexun.soochat.activity.R;

public class InstallOnlineActivity extends BaseActivity {  
    private TextView mTextView;  
    private EditText mEditText;  
    private Button mButton;  
    private String currentFilePath = "";  
    private String currentTempFilePath = "";  
    private String strURL = "";  
    private String fileEx = "";  
    private String fileName = "";  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_install_online);
        
        mTextView = (TextView) findViewById(R.id.tvUpdateMessage);  
        mEditText = (EditText) findViewById(R.id.etPath);  
        mButton = (Button) findViewById(R.id.btnUpdate);  
        mButton.setOnClickListener(new OnClickListener() {  
  
            @Override  
            public void onClick(View v) {  
                // ���ļ����ص�����  
                mTextView.setText("������...");  
                strURL = mEditText.getText().toString();  
                // ��ȡ�ļ���׺  
                fileEx = strURL.substring(strURL.lastIndexOf('.') + 1, strURL.length()).toLowerCase();
                // ��ȡ�ļ���  
                fileName = strURL.substring(strURL.lastIndexOf('/') + 1, strURL.lastIndexOf('.'));  
                getFile(strURL);
            }  
  
        });  
    }
    
    @Override  
    protected void onResume() {  
        //ɾ����ʱ�ļ�  
        delFile(currentTempFilePath);  
        super.onResume();  
    }   
    
    @Override  
    protected void onPause() {  
        mTextView = (TextView) findViewById(R.id.tvUpdateMessage);  
        mTextView.setText("���سɹ�");  
        super.onPause();  
    }
  
    private void getFile(final String strPath) {  
        if (currentFilePath.equals(strPath)) {  
            getDataSource(strPath);  
        }  
        currentFilePath = strPath;  
        Runnable r = new Runnable() {  
  
            @Override  
            public void run() {  
                getDataSource(strPath);  
            }  
        };  
        new Thread(r).start();  
    }  
  
    private void getDataSource(String url) {  
        if (!URLUtil.isNetworkUrl(url)) {  
            mTextView.setText("����д��ȷ��URL");  
        } else {  
            try {  
                URL myUrl = new URL(url);  
                // ȡ������  
                URLConnection conn = myUrl.openConnection();  
                // ����  
                conn.connect();  
                // ���������  
                InputStream is = conn.getInputStream();  
                if (is == null) {  
                    throw new RuntimeException("stream is null");  
                }  
                // ������ʱ�ļ�  
                File myTempFile = File.createTempFile(fileName, "." + fileEx);  
                // ȡ����ʱ�ļ����·��  
                currentTempFilePath = myTempFile.getAbsolutePath();  
                FileOutputStream fos = new FileOutputStream(myTempFile);  
                byte[] buf = new byte[128];  
                do {  
                    // �������������������Ĵ�С  
                    int numread = is.read(buf);  
                    if (numread <= 0) {  
                        break;  
                    }  
                    fos.write(buf, 0, numread);  
                } while (true);  
                // ���ļ����а�װ  
                openFile(myTempFile);  
                is.close();  
            } catch (MalformedURLException e) {  
                e.printStackTrace();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
  
    private void openFile(File file) {  
        Intent intent = new Intent();  
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        intent.setAction(android.content.Intent.ACTION_VIEW);  
        String type = getMimeType(file);  
        intent.setDataAndType(Uri.fromFile(file), type);  
        startActivity(intent);  
    }  
  
    private String getMimeType(File file) {  
        String type = "";  
        String fname = file.getName();  
        // �����չ��  
        String end = fname  
                .substring(fname.lastIndexOf('.') + 1, fname.length())  
                .toLowerCase();  
        // ����չ�������;���MimeType  
        if ("m4a".equals(end) || "mp3".equals(end) || "mid".equals(end)  
                || "xmf".equals(end) || "ogg".equals(end) || "wav".equals(end)) {  
            type = "audio";  
        } else if ("3gp".equals(end) || "mp4".equals(end)) {  
            type = "video";  
        } else if ("jpg".equals(end) || "gif".equals(end) || "png".equals(end)  
                || "jpeg".equals(end) || "bmp".equals(end)) {  
            type = "image";  
        } else if ("apk".equals(end)) {  
            type = "application/vnd.android.package-archive";  
        } else {  
            type = "*";  
        }  
        if ("apk".equals(end)) {  
  
        } else {  
            type += "/*";  
        }  
        return type;  
    }
    
    private void delFile(String fileName){  
        File file = new File(fileName);  
        if(file.exists()){  
            file.delete();  
        }  
    }

}
