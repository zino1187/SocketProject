package com.ios;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

//각 접속자마다 소켓과 스트림들이 유지되어 하므로, 
//이 클래스의 인스턴스마다 각 접속자에 관련된 객체들을 보관해놓자
public class ChatThread extends Thread{
	Socket client; 
	BufferedReader buffr; //듣기 
	BufferedWriter buffw; //말하기
	ChatServer chatServer;//서버에 접근할 일이 있으므로...
	
	//이 생성자는 접속자가 들어올때마다 호출되면 해당 접속자의 소켓과 스트림	
	//이 유지될 수 있다
	public ChatThread(Socket client, ChatServer chatServer) {
		this.client = client;
		this.chatServer = chatServer;
		
		try {
			buffr = new BufferedReader(new InputStreamReader(client.getInputStream()));
			buffw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//듣기 
	public void listen() {
		String msg = null;
		
		//이 객체는 살아있는 동안 끝없이 클라이언트의 메시지를 청취해야 한다!!
		//따라서 무한루프로 처리한다
		try {
			while(true) {
				msg = buffr.readLine(); //들은 메시지를 다시 클라이언트에게 
				//메아리로 보내야 한다
				//서버의 정보창에 메시지 출력하기 
				chatServer.area.append(msg+"\n");
				
				send(msg); //말하기 
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//말하고
	public void send(String msg) {
		//현재 채팅에 참여한 모든 사람들의 메시지를 보내자!!
		//Unicating : 대상이 혼자인 경우의  메시지 전송 
		//Multicasting : 다수에게 메시지 전송 
		for(int i=0; i<chatServer.list.size();i++) {
			ChatThread ct = chatServer.list.get(i); //한 사람씩 추출!!
			try {
				ct.buffw.write(msg+"\n");
				ct.buffw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	//run 메서드 재정의 !! 
	//run안에는 뭘 작성하는 것인가?? 
	//쓰레드로 실행시킬 코드를 run안에 작성해 놓으면 JVM이 알아서 호출해준다
	@Override
	public void run() {
		listen();
	}
	
}







