/**
 * �ͻ��˺ͷ������˱���ͨ�ŵ��߳�
 * ���ϵض�ȡ����������������
 */
package org.yhn.yq.client.model;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.yhn.yq.common.VTMessage;
import org.yhn.yq.common.VTMessageType;


import com.fync.FriendsActivity.GoodFriendFileActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ClientConServerThread extends Thread {
	private Context context;
	private  Socket s;
	public Socket getS() {return s;}
	public ClientConServerThread(Context context,Socket s){
		this.context=context;
		this.s=s;
	}
 
	
	@Override
	public void run() {
		while(true){
			ObjectInputStream ois = null;
			ObjectOutputStream oos = null;
			VTMessage m;
			try {
				ois = new ObjectInputStream(s.getInputStream());
				m=(VTMessage) ois.readObject();
				if(m.getType().equals(VTMessageType.COM_MES)){//�������������
					//�Ѵӷ�������õ���Ϣͨ���㲥����
					Intent intent = new Intent("org.yhn.yq.mes");
					String[] message=new String[]{
						m.getSender()+"",
						m.getSenderNick(),
						m.getSenderAvatar()+"",
						m.getContent(),
						m.getSendTime()};
					
					Log.d("lhm", message[0]);
					intent.putExtra("message", message);
					context.sendBroadcast(intent);
				}else if(m.getType().equals(VTMessageType.RET_ONLINE_FRIENDS)){//����Ǻ����б�
					//�������ߺ���
					GoodFriendFileActivity.goodFriendStr=m.getContent();
					//System.out.println(m.getContent());
					Log.d("lhm",m.getContent());
				}
			} catch (Exception e) {
				//e.printStackTrace();
				try {
					if(s!=null){
						s.close();
					}
				} catch (IOException e1) {
					//e1.printStackTrace();
				}
			}
		}
	}
	
}
