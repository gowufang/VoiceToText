package org.yhn.yq.client.model;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.yhn.yq.common.User;
import org.yhn.yq.common.VTMessage;
import org.yhn.yq.common.VTMessageType;

import com.fync.BasicActivity.LoginActivity;
import com.fync.FriendsActivity.GoodFriendFileActivity;
import com.fync.TextData.TextList;
//import org.yhn.yq.client.view.LoginActivity;
//import org.yhn.yq.client.view.MainActivity;

import android.content.Context;
import android.util.Log;

public class VTClient {
	private Context context;
	public Socket s;
	public VTClient(Context context){
		this.context=context;
	}
	public boolean sendLoginInfo(Object obj){
		boolean b=false;
		try {
			s=new Socket();
			try{
				s.connect(new InetSocketAddress("192.168.1.121",  6738   ),2000);
			}catch(SocketTimeoutException e){
				//连接服务器超时
				return false;
			}
			ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(obj);
			ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
			VTMessage ms=(VTMessage)ois.readObject();
			if(ms.getType().equals(VTMessageType.SUCCESS)){
				//个人信息,存入好友那里
				LoginActivity.setMyInfo( ms.getContent());
				//创建一个该账号和服务器保持连接的线程
				ClientConServerThread ccst=new ClientConServerThread(context,s);
				//启动该通信线程
				ccst.start();
				//加入到管理类中
				ManageClientConServer.addClientConServerThread(((User)obj).getAccount(), ccst);
				b=true;
				Log.d("lhm","登录成功，欢迎使用录音系统");
			}else if(ms.getType().equals(VTMessageType.FAIL)){
				Log.d("lhm","登录失败，很不幸，请检查密码和账号");
				b=false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return b;
	}
	
	public boolean sendRegisterInfo(Object obj){
		boolean b=false;
		try {
			s=new Socket();
			try{
				s.connect(new InetSocketAddress("192.168.1.121",6738),2000);
			}catch(SocketTimeoutException e){
				//连接服务器超时
				return false;
			}
			ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(obj);
			ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
			VTMessage ms=(VTMessage)ois.readObject();
			if(ms.getType().equals(VTMessageType.SUCCESS)){
				b=true;
				Log.d("lhm","注册成功，欢迎登录...............");
			}else if(ms.getType().equals(VTMessageType.FAIL)){
				b=false;
				Log.d("lhm","注册失败，很不幸...............");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return b;
	}
}
