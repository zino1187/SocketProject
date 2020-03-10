package com.ios;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

//콘솔 기반의 채팅 서버를 구축한다!!
//소켓이란? 네트워크의 각종 기능을 추상시켜놓은 객체를 의미한다
//따라서 개발자는 네트워크 상세 기술을 모르더라도, 메서드 호출만으로 제어할수
//있다..( 추상화의 목적)
public class ConsoleServer {
	//접속자를 감지해야, 대화도 나눌 수 있으므로, 먼저 접속자 감지 소켓부터
	//준비해야 한다 
	ServerSocket server;
	int port = 9999;
	public ConsoleServer() {
		try {
			server = new ServerSocket(port);
			//서버 가동 
			Socket client = server.accept(); //클라이언트가 접속할때까지 대기한다!!
			//들어오면?? 그 클라이언트와 대화를 나눌 소켓을 반환한다!!
			System.out.println("접속자 발견!!");
			//socket을 이용하면,접속한 클라이언트와 서버가 통신을 할수있게 해준다
			InputStream is = client.getInputStream(); //byte기반 스트림 얻기!!
			
			//데이터를 읽어들여 보자!!
			//int data = is.read();
			//System.out.print(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		ConsoleServer consoleServer = new ConsoleServer();
	}
}







