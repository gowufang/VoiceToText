package com.fync.BasicActivity;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.yhn.yq.client.model.ManageClientConServer;
import org.yhn.yq.client.model.VTClient;
import org.yhn.yq.common.User;
import org.yhn.yq.common.VTMessage;
import org.yhn.yq.common.VTMessageType;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fync.FriendsActivity.GoodFriendFileActivity;
import com.fync.TextData.ShareList;
import com.fync.TextData.TextList;
import com.fync.VoicetoText.VoiceToText;
import com.fync.v2t.system.R;

public class LoginActivity extends Activity {
	public static String userInfo;
	EditText accountEt,passwordEt;
	static String myInfo;
 
	public static String getMyInfo() {
		return myInfo;
	}

	public static void setMyInfo(String myInfo) {
		LoginActivity.myInfo = myInfo;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.login);
	    
	    accountEt=(EditText) findViewById(R.id.etUid);
	    passwordEt=(EditText) findViewById(R.id.etPwd);
	    Button btnLogin=(Button) findViewById(R.id.login);
	    btnLogin.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				if(accountEt.getText().toString().equals("") || passwordEt.getText().toString().equals("")){
					Toast.makeText(LoginActivity.this, "账号或密码不能为空！", Toast.LENGTH_SHORT).show();
				}else{
					login(Integer.parseInt(accountEt.getText().toString()), passwordEt.getText().toString());
//					getNick(nick);
				}
			}
	    });
	    findViewById(R.id.register).setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
			}
	    });
	    ManageActivity.addActiviy("LoginActivity", this);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// TODO Auto-generated method stub
		menu.add(0, 1, 0, "反馈");
		return super.onCreateOptionsMenu(menu);
	}
	
	
	
	
	void login(int a, String p){
		User user=new User();
		user.setAccount(a);
		user.setPassword(p);
		user.setOperation("login");
		boolean b=new VTClient(this).sendLoginInfo(user);
		//登陆成功
		if(b){
			/*try {
				//发送一个要求返回在线好友的请求的Message
				ObjectOutputStream oos = new ObjectOutputStream	(
						ManageClientConServer.getClientConServerThread(user.getAccount()).getS().getOutputStream());
				VTMessage m=new VTMessage();
				m.setType(VTMessageType.GET_ONLINE_FRIENDS);
				m.setSender(user.getAccount());
				oos.writeObject(m);
			} catch (IOException e) {
				e.printStackTrace();
			}*/
			//转到主界面
			TextList.setMyUser(user);
			startActivity(new Intent(this, TextList.class));
		}else {
			Toast.makeText(this, "登陆失败", Toast.LENGTH_SHORT).show();
		//	startActivity(new Intent(this, TextList.class));
		}
	}
	//ceshi:
	void login2(int account,String nick,String sex)
	{
		User user=new User();
		user.setAccount(account);
		user.setNick(nick);
		user.setPassword(sex);
		
	}
}
