package com.ios;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

//서버와 메시지를 주고받을 소켓 클라이언트를 정의!!
public class ChatClient extends JFrame{
	//객체가 객체를가지고 있을때의 관계를 has a 관계라 하고 멤버변수에 선언
	//한다!!
	Choice ch; //ip 목록 보여주기 
	JButton bt; 
	JTextArea area; 
	JScrollPane scroll;
	JTextField t_input;
	JPanel p_north;//눈에 보여지진 않지만, 다른 컴포넌트들을 포함시킬수 있는
						//컨테이너 클래스~~
						//참고로 모든 컨테이너는 배치관리자가 지원된다. 
	//개발자가 명시하지 않으면 JFrame --> BorderLayout  
	//                              Panel --> FlowLayout (안:LinearLayout)
	//위의 배치관리자가 디폴트로 지원된다
	
	//난생 처음 보는클래스를 사용하는 방법 
	//1) 해당 클래스가 무엇에 쓰이는 지 파악
	
	
	//2) 사용하는 법 (메모리에 올리는 법을알아야 한다)
	//모든 객체는 단 3가지 유형밖에 없다 
	//1.일반클래스 - new 생성자로 올린다 
	//2.추상클래스 - 자식을 new하거나, 이미 존재하는 인스턴스를 사용하면
	//3.인터페이스 - 자식을 new하거나, 이미 존재하는 인스턴스를 사용하면
	
	Socket client; 
	
	
	//서버의스트링 메시지를 받을 문자기반의 버퍼 처리된 입력스트림 
	BufferedReader buffr;
	
	OutputStream os;//byte기반 스트림은 한글이 깨지므러
	//문자기반 스트림으로 업그레이드하자
	OutputStreamWriter ow; //한 문자씩 처리한다. 따라서 
	//문자열을 읽어들이기 위해 너무 많은 메서드 호출이 일어남.. 
	//해결책?  문자를 모아놓고, 입력의 끝을 표시할때마다 문자열단위
	//처리하는 버퍼기반의 스트림이 필요한다!!
	BufferedWriter buffw; 
	ClientListener clientListener;
	
	public ChatClient() {
		//has a 로 보유한 부품들을 메모리에 생성하자 ( 초기화 시킨다)
		ch = new Choice();
		for(int i=1;i<=10;i++) {
			ch.add("192.168.100."+i);
		}
		bt = new JButton("접속");
		area = new JTextArea();
		scroll = new JScrollPane(area);//스크롤을 적용할 컴포넌트 넣기
		t_input = new JTextField(20);
		p_north = new JPanel();
		
		//패널에 부착 
		p_north.add(ch);
		p_north.add(bt);
		
		//center에 textarea부착
		add(scroll);
		
		//south에 입력창 부착 
		add(t_input, BorderLayout.SOUTH);
		
		//패널을북쪽에 부착!!
		add(p_north, BorderLayout.NORTH);
		
		setSize(300,400);
		setVisible(true);
		//창을 닫기 버튼 누를때 프로세스도 종료
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//내부익명 클래스 v
		bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connectServer();
			}
		});
		
		//입력박스에 리스너 연결!!
		//보통 재정의할 메서드가 3개이상 되면 Adapter클래스가 지원된다고
		//보면 된다.. ex) WindowListener (8개이상) --> WindowAdapter
		t_input.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					clientListener.send(t_input.getText());					
					t_input.setText("");//초기화
				}
			}
		});		
	}
	
	
	
	//원격지에 떨어진 소켓 서버에 접속을 시도한다!! (전화를 건다)
	//전화기가 준비되어야 하고, 통신에 전화기는 소켓이다
	public void connectServer() {
		try {
			client = new Socket(ch.getSelectedItem(), 9999);
			area.append("서버에 접속함\n");
			
			clientListener = new ClientListener(client, this);
			clientListener.start(); //쓰레드 동작 시작 
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new ChatClient();
	}

}



