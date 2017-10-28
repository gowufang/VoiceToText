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
				//���ӷ�������ʱ
				return false;
			}
			ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(obj);
			ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
			VTMessage ms=(VTMessage)ois.readObject();
			if(ms.getType().equals(VTMessageType.SUCCESS)){
				//������Ϣ,�����������
				LoginActivity.setMyInfo( ms.getContent());
				//����һ�����˺źͷ������������ӵ��߳�
				ClientConServerThread ccst=new ClientConServerThread(context,s);
				//������ͨ���߳�
				ccst.start();
				//���뵽��������
				ManageClientConServer.addClientConServerThread(((User)obj).getAccount(), ccst);
				b=true;
				Log.d("lhm","��¼�ɹ�����ӭʹ��¼��ϵͳ");
			}else if(ms.getType().equals(VTMessageType.FAIL)){
				Log.d("lhm","��¼ʧ�ܣ��ܲ��ң�����������˺�");
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
				//���ӷ�������ʱ
				return false;
			}
			ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(obj);
			ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
			VTMessage ms=(VTMessage)ois.readObject();
			if(ms.getType().equals(VTMessageType.SUCCESS)){
				b=true;
				Log.d("lhm","ע��ɹ�����ӭ��¼...............");
			}else if(ms.getType().equals(VTMessageType.FAIL)){
				b=false;
				Log.d("lhm","ע��ʧ�ܣ��ܲ���...............");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return b;
	}
}
