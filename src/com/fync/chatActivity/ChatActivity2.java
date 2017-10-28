package com.fync.chatActivity;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.yhn.yq.client.model.ManageClientConServer;
import org.yhn.yq.common.MyTime;
import org.yhn.yq.common.VTMessage;
import org.yhn.yq.common.VTMessageType;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fync.TextData.TextDbHelper;
import com.fync.TextData.TextList;
import com.fync.VoicetoText.*;
import com.fync.BasicActivity.ManageActivity;
import com.fync.BasicActivity.MoreActivity;
import com.fync.VoicetoText.JsonParser;
import com.fync.v2t.system.R;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechListener;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;

public class ChatActivity2 extends Activity {
	private String chatContent;//消息内容
	ListView chatListView;
	private EditText mTitleText;
	private Long mRowId;
	private TextDbHelper mDbHelper;	


	public List<ChatEntity> chatEntityList=new ArrayList<ChatEntity>();//所有聊天内容
	private int myAccount;
	private int chatAccount;
	private String chatNick;
	public static int[] avatar=new int[]{R.drawable.avatar_default,R.drawable.h001,R.drawable.h002,R.drawable.h003,
			R.drawable.h004,R.drawable.h005,R.drawable.h006};
	MyBroadcastReceiver br;
	private SpeechRecognizer iatRecognizer;
	//set engine,iat means dictation
	private String engine="iat";
	//set frequency,8000 or 16000
	private String rate="16000";
	private EditText mResultText = null;

	//识别结果显示
	TextView contentvalue;
	private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

	private String TAG = "shitou";
	// Tip
	static public EditText mBodyText;

	private Toast mToast;
	String str1,str2="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_activity2);
		mToast = Toast.makeText(this, "", Toast.LENGTH_LONG);
		SpeechUtility.createUtility(this, "appid=5528b8ce");
		mDbHelper = new TextDbHelper(this);
		mDbHelper.open();
		Button confirmButton = (Button) findViewById(R.id.confirm2);
		Button startButton = (Button) findViewById(R.id.luyin2);
		 mBodyText=(EditText)findViewById(R.id.body2);
	        Button lookButton=(Button)findViewById(R.id.look2);

		 mTitleText = (EditText) findViewById(R.id.title2);
		
		//设置top面板信息
		int chatAvatar=getIntent().getIntExtra("avatar", 0);
			chatAccount=getIntent().getIntExtra("account", 0);
			chatNick=getIntent().getStringExtra("nick");
			ImageView avatar_iv=(ImageView) findViewById(R.id.chat_top_avatar);
			avatar_iv.setImageResource(avatar[chatAvatar]);
			TextView nick_tv=(TextView) findViewById(R.id.chat_top_nick);
			nick_tv.setText("正在与"+chatNick+"一起录音");
			mResultText=(EditText) findViewById(R.id.et_input);
			
			
			
			
			
			
			
			//开始录音按钮
			startButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showIatinvisble();	
				}
			});
			
			
			
/**************************************************************************************/

			findViewById(R.id.ib_send).setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					str2=mBodyText.getText().toString();
					str1=mResultText.getText().toString();
					str2=str2+str1;
					mBodyText.setText(str2);
					
					
					
					myAccount=MoreActivity.me.getAccount();
					ObjectOutputStream oos;
					try {
						oos = new ObjectOutputStream
						//通过account找到该线程，从而得到OutputStream
						(ManageClientConServer.getClientConServerThread(myAccount).getS().getOutputStream());
						//得到输入的数据，并清空EditText
						chatContent=mResultText.getText().toString();
						
						
						//mBodyText.setText(chatContent);///////
						
						
						
						
						mResultText.setText("");
						//发送消息
						VTMessage m=new VTMessage();
						m.setType(VTMessageType.COM_MES);
						m.setSender(myAccount);
						m.setSenderNick(MoreActivity.me.getNick());
						m.setSenderAvatar(MoreActivity.me.getAvatar());
						m.setReceiver(chatAccount);
						m.setContent(chatContent);
						m.setSendTime(MyTime.geTimeNoS());
						oos.writeObject(m);
						//更新聊天内容
						updateChatView(new ChatEntity(
								MoreActivity.me.getAvatar(),
								chatContent,
					    		MyTime.geTime(),
					    		false));
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			
			});
			
			
			 //注册广播
			IntentFilter myIntentFilter = new IntentFilter(); 
	        myIntentFilter.addAction("org.yhn.yq.mes");
	        br=new MyBroadcastReceiver();
	        registerReceiver(br, myIntentFilter); 
			ManageActivity.addActiviy("ChatActivity", this);
			
			
			
			//保存并查看录音文件
			 lookButton.setOnClickListener(new View.OnClickListener() {
					
					//@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String title = mTitleText.getText().toString();
						String body = mBodyText.getText().toString();
						if ("".equals(title)||"".equals(body)) {
							Toast.makeText(ChatActivity2.this, "^_^不要忘记录音主题和内容哦！^_^", 5).show();
							return;
						}else if (mRowId != null) {
							mDbHelper.updateDiary(mRowId, title, body);
						} else{
						mDbHelper.createDiary(title, body);
						Intent mIntent = new Intent();
						setResult(RESULT_OK, mIntent);
					}
						Toast.makeText(ChatActivity2.this, "保存成功！",Toast.LENGTH_SHORT).show();
						Intent intent=new Intent();
						intent.setClass(ChatActivity2.this, TextList.class);
						startActivity(intent);
					}
				});
			 //保存并查看录音文件
			confirmButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					String title = mTitleText.getText().toString();
					String body = mBodyText.getText().toString();
					if ("".equals(title)||"".equals(body)) {
						Toast.makeText(ChatActivity2.this, "^_^不要忘记录音主题和内容哦！^_^", 5).show();
						return;
					}else if (mRowId != null) {
						mDbHelper.updateDiary(mRowId, title, body);
					} else{
					mDbHelper.createDiary(title, body);
					Intent mIntent = new Intent();
					setResult(RESULT_OK, mIntent);
					}
					Toast.makeText(ChatActivity2.this, "保存成功！",Toast.LENGTH_SHORT).show();

				}
			});
	}
	
	
	
	
	
	
	
	

	
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
		int ret = iatRecognizer.startListening(recognizerListener);////////////////////////////
		//Log.d(TAG, "startListening ret:"+ret);
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
				Toast.makeText(ChatActivity2.this, new String("error")
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
	 
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public void finish() {
		 unregisterReceiver(br);
		super.finish();
	}
	
	//广播接收器
	public class MyBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String[] mes = intent.getStringArrayExtra("message");
		    //更新聊天内容
		    updateChatView(new ChatEntity(
		    		Integer.parseInt(mes[2]),
		    		mes[3],
		    		mes[4],
		    		true));
 /*************文本内容得到对方录音文字****************************/
		    str2=mBodyText.getText().toString();
		    String str3=mes[3].toString();
		    str2=str2+str3;
		    mBodyText.setText(str2);

		}
	}
	public void updateChatView(ChatEntity chatEntity){
		chatEntityList.add(chatEntity);
		chatListView=(ListView) findViewById(R.id.lv_chat);
		chatListView.setAdapter(new ChatAdapter(this,chatEntityList));
	}
/**************************************************************************************/
	
	
	

}
