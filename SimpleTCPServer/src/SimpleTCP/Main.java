package SimpleTCP;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Main {
	private static ServerSocket listen = null;

	public static void main(String[] args) {
		if(args.length!=1)
			return;
		
		int port = Integer.parseInt(args[0]);
		try {
			listen = new ServerSocket(port);
			System.out.println("Server: Listen at " + port);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		ArrayList<BackgroundThread> btLst = new ArrayList<BackgroundThread>(); 
		try {
			do{
				BackgroundThread bt = acceptClientInBackground();
				btLst.add(bt);
			}while(System.in.read()!='q');
			
			System.out.println("Exiting...");
			for(int i=0; i<btLst.size(); i++){
				try {
					btLst.get(i).join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				listen.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	static private BackgroundThread acceptClientInBackground(){
		Socket server = null;
		try {
			server = listen.accept();
			System.out.println("Accept at " + server.getLocalPort() + "<-" + server.getPort());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		BackgroundThread bt = new BackgroundThread();
		bt.setSocket(server).start();
		return bt;
	}
}
