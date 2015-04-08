package SimpleTCP;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
		String listenPara = args[0];
		String forwardPara= args[1];
		
		Socket sockOt = new Socket( getHostFromPara(forwardPara), getPortFromPara(forwardPara) );
		System.out.println("Forward to " + forwardPara);

		ServerSocket listen = new ServerSocket( getPortFromPara(listenPara) );
		System.out.println("Listen at " + listenPara);

		Socket sockIn  = listen.accept();
		
		BufferedInputStream  getIn, fwdIn;
		BufferedOutputStream getOt, fwdOt;
		
		getIn = new BufferedInputStream(  sockIn.getInputStream() );
		getOt = new BufferedOutputStream( sockIn.getOutputStream());
		
		fwdIn = new BufferedInputStream( sockOt.getInputStream() );
		fwdOt = new BufferedOutputStream(sockOt.getOutputStream());
		
		Thread get2fwd = new Thread(){

			@Override
			public void run() {
				super.run();
				while(true){
					try {
						int c = getIn.read();
						fwdOt.write(c);
						fwdOt.flush();
					} catch (IOException e) {
						e.printStackTrace();
						break;
					}
				}
			}
			
		};
		Thread fwd2get = new Thread(){

			@Override
			public void run() {
				super.run();
				while(true){
					try {
						int c = fwdIn.read();
						getOt.write(c);
						getOt.flush();
					} catch (IOException e) {
						e.printStackTrace();
						break;
					}
				}
			}
			
		};
		
		get2fwd.start();
		fwd2get.start();
		
		get2fwd.join();
		fwd2get.join();

		sockIn.close();
		listen.close();
		sockOt.close();
	}

	private static String getHostFromPara(String para){
		int colon = para.indexOf(":");
		if(colon==-1){
			return null;
		}else{
			return para.substring(0, colon);
		}
	}

	private static int getPortFromPara(String para){
		int colon = para.indexOf(":");
		if(colon==-1){
			return Integer.parseInt(para);
		}else{
			return Integer.parseInt(para.substring(colon+1));
		}
	}
}
