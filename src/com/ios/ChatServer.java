package com.ios;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

//관리의 편의상 채팅서버를 GUI기반으로 구축한다!!
public class ChatServer extends JFrame{
	JPanel p_north; 
	JTextField t_port;
	JButton bt;
	JTextArea area; 
	JScrollPane scroll;
	ServerSocket server;
	Thread runThread; //메인쓰레드는 이벤트처리, UI처리에 사용되어야 하므로
	//절대 대기상태나 루프에 빠지게 해서는 안된다!! 
	//서버 가동을 개발자 정의 쓰레드에게 맡기자!!
	boolean flag=true;//서버 가동 여부를 결정하는 변수!!
	//모든 접속자마다 1:1 대응하는 객체인 채팅쓰레드 인스턴스의 명단을 
	//컬렉션 유형으로 보관해 놓자!! (순서있는것 List , 순서없는 것 Set , Map)
	Vector<ChatThread> list = new Vector<ChatThread>(); 
	
	public ChatServer() {
		super("서버");
		p_north = new JPanel();		
		t_port = new JTextField("9999",10);
		bt = new JButton("서버가동");
		area = new JTextArea();
		scroll = new JScrollPane(area);
		area.setBackground(Color.YELLOW);
		
		p_north.add(t_port);
		p_north.add(bt);
		add(p_north, BorderLayout.NORTH);
		add(scroll);
		
		setBounds(500,100,300,400); //x, y, width, height 
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//버튼에 리스너 연결!!!
		bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startServer();
			}
		});		
	}
	
	//ServerSocket을 생성하고 accept()  호출하자!!
	public void startServer() {

		runThread = new Thread(){
			public void run() {
				try {
					server = new ServerSocket(Integer.parseInt(t_port.getText()));
					area.append("서버 가동\n");
					
					//쓰레드가 죽으면 안되므로, 무한루프로 처리하자!!
					while(flag) {
						Socket client = server.accept(); //클라이언트의 접속이 발견되기 전까지 대기상태에 빠진
						String ip = client.getInetAddress().getHostAddress();
						area.append(ip+"님 접속\n");
						//소켓을 보관하며 대화를 나눌 객체의 인스턴스 생성하면서 
						//소켓을 전달하자, 그래야 스트림으로 대화를 나눌것이므로..
						ChatThread ct = new ChatThread(client , ChatServer.this );
						ct.start();//이렇게해야 jVM의 Runnable영역으로 들어감
						
						//지금 서버의 채팅에 참여자로 등록하자!!
						list.add(ct);
						
						area.append("현재 접속자는 "+list.size()+"\n");
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		//쓰레드는 생성 후 Runnable 로 밀어넣어야 일함
		runThread.start();
	}
	
	public static void main(String[] args) {
		new ChatServer();
	}
}


