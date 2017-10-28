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
	//ʶ������ʾ
	TextView contentvalue;
	private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

	private String TAG = "shitou";
	// Tip
	private Toast mToast;	
	//str1����¼����ת�����ֵ��ַ�����str2����str1��ֵ
	String str1,str2="";
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mToast = Toast.makeText(this, "", Toast.LENGTH_LONG);
		//�û���¼
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
		
		
		
		
		
		// ÿһ��intent�����һ��Bundle�͵�extras���ݡ�
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String title = extras.getString(TextDbHelper.KEY_TITLE);
			String body = extras.getString(TextDbHelper.KEY_BODY);
			mRowId = extras.getLong(TextDbHelper.KEY_ROWID);

			if (title != null) {
				mTitleText.setText(title);//�������
			}
			if (body != null) {
				mBodyText.setText(body);//��������
			}
		}
		
		
		
		
		
		
/**********************************************************************************************************/

		//��ʼ¼����ť
		startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showIatinvisble();	
			}
		});

		//�����¼�����ݰ�ť
		previewbtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					str2=mBodyText.getText().toString();
					str1=mResultText.getText().toString();
					str2=str2+str1;
					mBodyText.setText(str2);
					
				}
			});
		
		

        //�鿴¼���ļ�
        lookButton.setOnClickListener(new View.OnClickListener() {
			
			//@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String title = mTitleText.getText().toString();
				String body = mBodyText.getText().toString();
				if ("".equals(title)||"".equals(body)) {
					Toast.makeText(VoiceToText.this, "^_^��Ҫ����¼�����������Ŷ��^_^", 5).show();
					return;
				}else if (mRowId != null) {
					mDbHelper.updateDiary(mRowId, title, body);
				} else{
				mDbHelper.createDiary(title, body);
				Intent mIntent = new Intent();
				setResult(RESULT_OK, mIntent);
			}
				Toast.makeText(VoiceToText.this, "����ɹ���",Toast.LENGTH_SHORT).show();

				Intent intent=new Intent();
				intent.setClass(VoiceToText.this, TextList.class);
				startActivity(intent);
			}
		});
        
        
        //����¼���ļ�
		confirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				String title = mTitleText.getText().toString();
				String body = mBodyText.getText().toString();
				if ("".equals(title)||"".equals(body)) {
					Toast.makeText(VoiceToText.this, "^_^��Ҫ����¼�����������Ŷ��^_^", 5).show();
					return;
				}else if (mRowId != null) {
					mDbHelper.updateDiary(mRowId, title, body);
				} else{
				mDbHelper.createDiary(title, body);
				Intent mIntent = new Intent();
				setResult(RESULT_OK, mIntent);
				Toast.makeText(VoiceToText.this, "����ɹ���",Toast.LENGTH_SHORT).show();

			}
			}
		});
	}
	
	
	///oncreate����
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/************************************************************************************************/
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);
        	}
		
	};
	
	protected void showIatinvisble() {
		iatRecognizer=SpeechRecognizer.createRecognizer(this, mInitListener);
		//��ȡ�������
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
			showTip("��ʼ˵��");
		}

		//@Override
		public void onError(SpeechError error) {
			// �����룺10118(��û��˵��)��������¼����Ȩ�ޱ�������Ҫ��ʾ�û���Ӧ�õ�¼��Ȩ�ޡ�
			showTip(error.getPlainDescription(true));
		}

		//@Override
		public void onEndOfSpeech() {
			showTip("����˵��");
		}

		//@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			Log.d(TAG, results.getResultString());
			printResult(results);

			if (isLast) {
				// TODO ���Ľ��
			}
		}

		//@Override
		public void onVolumeChanged(int volume) {
			showTip("��ǰ����˵����������С��" + volume);
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
		}
	};
	
	private void printResult(RecognizerResult results) {
		String text = JsonParser.parseIatResult(results.getResultString());

		String sn = null;
		// ��ȡjson����е�sn�ֶ�
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
	
	
	
	
	
	
	
	
	 // �û���¼�ص�������.
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