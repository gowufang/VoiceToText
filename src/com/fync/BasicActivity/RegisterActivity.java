package com.fync.BasicActivity;

import org.yhn.yq.client.model.VTClient;
import org.yhn.yq.common.MyTime;
import org.yhn.yq.common.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.fync.v2t.system.R;

public class RegisterActivity extends Activity {
	String sex="male";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.register);
		
		RadioGroup group = (RadioGroup)findViewById(R.id.register_radiogroup);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup arg0,int id) {

				switch(id){
				case R.id.register_radio_nv:
					sex="female";
					//Toast.makeText(RegisterActivity.this, "111111",Toast.LENGTH_SHORT).show();
					break;
				case R.id.register_radio_nan:
					sex="male";
					//Toast.makeText(RegisterActivity.this, "22222",Toast.LENGTH_SHORT).show();
					break;
				default:
						break;
				}
								 
			}
		});
		findViewById(R.id.rigister_btn_register).setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				//¼òµ¥Ð´ÁËÏÂ£¬
				EditText accountEt=(EditText) findViewById(R.id.registeretUid);
				EditText passwordEt=(EditText) findViewById(R.id.registeretPwd);
				EditText nickEt=(EditText) findViewById(R.id.register_nick);
			
				if(accountEt.getText().toString().equals("") || passwordEt.getText().toString().equals("")||nickEt.getText().equals("")){
					Toast.makeText(RegisterActivity.this, "ÕËºÅ»òÃÜÂë¡¢êÇ³Æ²»ÄÜÎª¿Õ£¡", Toast.LENGTH_SHORT).show();
				}else {
					User user=new User();
					user.setAccount(Integer.parseInt(accountEt.getText().toString()));
					user.setPassword(passwordEt.getText().toString());
					user.setNick(nickEt.getText().toString());
				
					user.setSex(sex);
					user.setNick(nickEt.getText().toString().trim());	 
					user.setAvatar(4);
					
					user.setTime(MyTime.geTimeNoS());
					user.setTime(MyTime.geTimeNoS());
					user.setOperation("register");
					boolean b=new VTClient(RegisterActivity.this).sendRegisterInfo(user);
					if(b){
						//×¢²á³É¹¦Ìø×ªµ½µÇÂ½
						Toast.makeText(RegisterActivity.this, "¹§Ï²Äã£¬×¢²á³É¹¦ £¡", Toast.LENGTH_SHORT).show();
						startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
					}
					else{
						Toast.makeText(RegisterActivity.this, "×¢²áÊ§°Ü£¡", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
}
