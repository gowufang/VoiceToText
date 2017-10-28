package com.fync.VoicetoText;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fync.TextData.TextDbHelper;
import com.fync.TextData.TextList;
import com.fync.chatActivity.ChatActivity2;
import com.fync.v2t.system.R;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechListener;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;

public class VoiceToText extends Activity {

	private EditText mTitleText;
	private EditText mBodyText;
	
	
	
	private EditText mResultText = null;
	private Long mRowId;
	private TextDbHelper mDbHelper;	
	
	private SpeechRecognizer iatRecognizer;
	//set engine,iat means dictation
	private String engine="iat";
	//set frequency,8000 or 16000
	private String rate="16000";
	//识别结果显示
	TextView contentvalue;
	private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

	private String TAG = "shitou";
	// Tip
	private Toast mToast;	
	//str1接收录音的转成文字的字符串，str2接收str1的值
	String str1,str2="";
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mToast = Toast.makeText(this, "", Toast.LENGTH_LONG);
		//用户登录
		SpeechUtility.createUtility(this, "appid=5528b8ce");
		mDbHelper = new TextDbHelper(this);
		mDbHelper.open();
		setContentView(R.layout.createdairy);

		mTitleText = (EditText) findViewById(R.id.title);
		Button previewbtn = (Button) findViewById(R.id.preview);
		mResultText = (EditText) findViewById(R.id.luyinkuang);

		mBodyText = (EditText) findViewById(R.id.body);
        Button lookButton=(Button)findViewById(R.id.look);
		Button confirmButton = (Button) findViewById(R.id.confirm);
		Button startButton = (Button) findViewById(R.id.luyin);
		mRowId = null;
		
		
		
		
		
		// 每一个intent都会带一个Bundle型的extras数据。
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String title = extras.getString(TextDbHelper.KEY_TITLE);
			String body = extras.getString(TextDbHelper.KEY_BODY);
			mRowId = extras.getLong(TextDbHelper.KEY_ROWID);

			if (title != null) {
				mTitleText.setText(title);//输入标题
			}
			if (body != null) {
				mBodyText.setText(body);//输入内容
			}
		}
		
		
		
		
		
		
/**********************************************************************************************************/

		//开始录音按钮
		startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showIatinvisble();	
			}
		});

		//添加至录音内容按钮
		previewbtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					str2=mBodyText.getText().toString();
					str1=mResultText.getText().toString();
					str2=str2+str1;
					mBodyText.setText(str2);
					
				}
			});
		
		

        //查看录音文件
        lookButton.setOnClickListener(new View.OnClickListener() {
			
			//@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String title = mTitleText.getText().toString();
				String body = mBodyText.getText().toString();
				if ("".equals(title)||"".equals(body)) {
					Toast.makeText(VoiceToText.this, "^_^不要忘记录音主题和内容哦！^_^", 5).show();
					return;
				}else if (mRowId != null) {
					mDbHelper.updateDiary(mRowId, title, body);
				} else{
				mDbHelper.createDiary(title, body);
				Intent mIntent = new Intent();
				setResult(RESULT_OK, mIntent);
			}
				Toast.makeText(VoiceToText.this, "保存成功！",Toast.LENGTH_SHORT).show();

				Intent intent=new Intent();
				intent.setClass(VoiceToText.this, TextList.class);
				startActivity(intent);
			}
		});
        
        
        //保存录音文件
		confirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				String title = mTitleText.getText().toString();
				String body = mBodyText.getText().toString();
				if ("".equals(title)||"".equals(body)) {
					Toast.makeText(VoiceToText.this, "^_^不要忘记录音主题和内容哦！^_^", 5).show();
					return;
				}else if (mRowId != null) {
					mDbHelper.updateDiary(mRowId, title, body);
				} else{
				mDbHelper.createDiary(title, body);
				Intent mIntent = new Intent();
				setResult(RESULT_OK, mIntent);
				Toast.makeText(VoiceToText.this, "保存成功！",Toast.LENGTH_SHORT).show();

			}
			}
		});
	}
	
	
	///oncreate结束
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/************************************************************************************************/
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);
        	}
		
	};
	
	protected void showIatinvisble() {
		iatRecognizer=SpeechRecognizer.createRecognizer(this, mInitListener);
		//获取引擎参数
		iatRecognizer.setParameter(SpeechConstant.DOMAIN, engine);
		iatRecognizer.setParameter(SpeechConstant.SAMPLE_RATE, rate);
		//clear myResulttext
		mResultText.setText(null);
		int ret = iatRecognizer.startListening(recognizerListener);
		Log.d(TAG, "startListening ret:"+ret);
		//iatRecognizer.startListening(recognizerListener);
	}
	
	private RecognizerListener recognizerListener = new RecognizerListener() {

		//@Override
		public void onBeginOfSpeech() {
			showTip("开始说话");
		}

		//@Override
		public void onError(SpeechError error) {
			// 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
			showTip(error.getPlainDescription(true));
		}

		//@Override
		public void onEndOfSpeech() {
			showTip("结束说话");
		}

		//@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			Log.d(TAG, results.getResultString());
			printResult(results);

			if (isLast) {
				// TODO 最后的结果
			}
		}

		//@Override
		public void onVolumeChanged(int volume) {
			showTip("当前正在说话，音量大小：" + volume);
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
		}
	};
	
	private void printResult(RecognizerResult results) {
		String text = JsonParser.parseIatResult(results.getResultString());

		String sn = null;
		// 读取json结果中的sn字段
		try {
			JSONObject resultJson = new JSONObject(results.getResultString());
			sn = resultJson.optString("sn");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mIatResults.put(sn, text);

		StringBuffer resultBuffer = new StringBuffer();
		for (String key : mIatResults.keySet()) {
			resultBuffer.append(mIatResults.get(key));
		}

		mResultText.setText(resultBuffer.toString());
		mResultText.setSelection(mResultText.length());
	}
	
	
	
	
	
	
	
	
	 // 用户登录回调监听器.
	private SpeechListener listener = new SpeechListener(){
		@Override
		public void onBufferReceived(byte[] arg0) {
		}
		@Override
		public void onCompleted(SpeechError error) {
			if(error != null) {
				Toast.makeText(VoiceToText.this, new String("error")
						, Toast.LENGTH_SHORT).show();
			}		
		
		}
		@Override
		public void onEvent(int arg0, Bundle arg1) {
			
		}	
	};
	
	
	
	private void showTip(String str)
	{
		if(!TextUtils.isEmpty(str))
		{
			mToast.setText(str);
			mToast.show();
		}
	}
}