package com.ios;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

//나 아닌 다른 사람이 서버에 메시지를 보내면, 내가 엔터를 치지 않더라도
//실시간으로 메시지를 받아야 하므로, 누군가에 의해 무한루프가 동작해야 한다
//하지만, 현재 클라이언트의 메인 쓰레드로 무한 루프를 걸면, UI와 이벤트 처리
//를 담당하는 메인쓰레드가 멈추게되므로,곧 어플리케이션이 중단된다...
//해결책?  별도의 쓰레드를 생성하여 처리 한다!!
public class ClientListener extends Thread{
	//대화와 관련된 객체들을 이 쓰레드가 보유하고 있으면 된다.. 
	//거의 서버측의 ChatThread와 거의 동일하다!!
	Socket client;
	BufferedReader buffr;
	BufferedWriter buffw; 
	ChatClient chatClient;
	
	public ClientListener(Socket client, ChatClient chatClient){
		this.client  =  client;
		this.chatClient = chatClient;
		
		try {
			buffr = new BufferedReader(new InputStreamReader(client.getInputStream()));
			buffw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	//끝없이 청취 
	public void run() {
		while(true) {
			listen();
		}
	}
	public void listen() {
		String msg= null;
		try {
			msg = buffr.readLine();
			//textarea에 반영 
			chatClient.area.append(msg+"\n");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(String msg) {
		try {
			buffw.write(msg+"\n");
			buffw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}







