package com.isme.login.post;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 登陆 - post
 * 
 * @author Administrator
 * 
 */
public class MainActivity extends Activity implements OnClickListener {

	//		基本地址：服务器ip地址：端口号/Web项目逻辑地址+目标页面（Servlet）的url-pattern
	private String baseUrl = "http://172.30.159.1:8080/LoginService/servlet/WelcomeUserServlet";
	
	private static String SD_PATH = Environment.getExternalStorageDirectory()+"//cache//";

	List<NameValuePair> pairList;

	private EditText etUsername;
	private EditText etPassword;
	private String name;
	private String password;

	private Button btnSure;
	private TextView tvResponse;
	
	private WebView wvResponse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		etUsername = (EditText) findViewById(R.id.et_username_input);
		etPassword = (EditText) findViewById(R.id.et_password_input);
		btnSure = (Button) findViewById(R.id.btn_request);

		tvResponse = (TextView) findViewById(R.id.tv_response);
		//TextView 滚动监听
		tvResponse.setMovementMethod(new ScrollingMovementMethod());
		
		//WebView 
		wvResponse = (WebView) findViewById(R.id.wv_response);
		
		btnSure.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// 处理 - post 请求
		name = etUsername.getText().toString();
		password = etPassword.getText().toString();

		NameValuePair pair1 = new BasicNameValuePair("username", name);
		NameValuePair pair2 = new BasicNameValuePair("password", password);

		pairList = new ArrayList<NameValuePair>();
		pairList.add(pair1);
		pairList.add(pair2);

		PostLoginTask task = new PostLoginTask();
		task.execute();
	}

	/**
	 * 内部类 - 异步任务
	 * 
	 * @author tanyi_000
	 * @time 下午11:21:29
	 * @data 2015-1-8
	 */
	class PostLoginTask extends AsyncTask<Void, Void, String> {

		String result = null;
		
		@Override
		protected String doInBackground(Void... params) {

			try {

				// 使用 HttpClient 对象来 请求
				HttpClient httpClient = new DefaultHttpClient();
				HttpParams httpParams = httpClient.getParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
				
				HttpPost httpPost = new HttpPost(baseUrl);

				HttpEntity requestHttpEntity;
				Log.i("pair", pairList.toString());
				requestHttpEntity = new UrlEncodedFormEntity(pairList);

				// 将请求内容加入到 请求中
				httpPost.setEntity(requestHttpEntity);

				// 发送请求
				HttpResponse httpResponse = httpClient.execute(httpPost);

				// 自定义 处理响应
				result = showResponseResult(httpResponse);

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			tvResponse.setText(result);
			
//			保存的sd卡 生成 HTML文件，然后通过webview展示出来
			File downFile = null;
			FileOutputStream fos = null;
			File filePath = new File(SD_PATH);
			if( ! filePath.exists()){
				filePath.mkdir();
			}
			downFile = new File(SD_PATH + "response.html");
			try {
				fos = new FileOutputStream(downFile);
				fos.write(result.getBytes());
				fos.flush();
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//直接 使用 String 内容，不从文件中获取 html文件了
			wvResponse.loadDataWithBaseURL(null, result, "text/html", "utf-8", null);
//			wvResponse.loadUrl("file:///"+SD_PATH + "response.html");
			
			Log.i("response", result);
		}

	}

	/**
	 * 自定义方法 - 处理post请求的响应
	 * 
	 * @param httpResponse
	 * @throws FileNotFoundException 
	 */
	private String showResponseResult(HttpResponse httpResponse) throws FileNotFoundException {
		if (null == httpResponse) {
			return null;
		}
		String result = "";
		
		try {

			HttpEntity httpEntity = httpResponse.getEntity();

			InputStream is = httpEntity.getContent();
			
//			 保存的sd卡 生成 HTML文件，然后通过webview展示出来 - 直接将字符串保存起来
//			byte[] buf = new byte[1024];
//			int len = 0;
//			while((len = is.read(buf)) != -1){
//				fos.write(buf, 0, len);
//				fos.flush();
//			}
//			fos.close();
//			is.close();

			BufferedReader bufReader = new BufferedReader(
					new InputStreamReader(is));
			String line = "";
			while (null != (line = bufReader.readLine())) {
				result += line;
				Log.i("isme", "line");
			}

		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;

	}

}
