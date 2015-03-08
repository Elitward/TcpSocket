package SimpleTCP;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
		
		InputStream  getIn, fwdIn;
		OutputStream getOt, fwdOt;
		
		getIn = sockIn.getInputStream();
		getOt = sockIn.getOutputStream();
		
		fwdIn = sockOt.getInputStream();
		fwdOt = sockOt.getOutputStream();
		
		BufferedReader getInRd = new BufferedReader(new InputStreamReader (getIn));
		BufferedWriter getOtWt = new BufferedWriter(new OutputStreamWriter(getOt));
		
		BufferedReader fwdInRd = new BufferedReader(new InputStreamReader (fwdIn));
		BufferedWriter fwdOtWt = new BufferedWriter(new OutputStreamWriter(fwdOt));
		
		Thread get2fwd = new Thread(){

			@Override
			public void run() {
				super.run();
				while(true){
					try {
						int c = getInRd.read();
						fwdOtWt.write(c);
						fwdOtWt.flush();
					} catch (IOException e) {
						e.printStackTrace();
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
						int c = fwdInRd.read();
						getOtWt.write(c);
						getOtWt.flush();
					} catch (IOException e) {
						e.printStackTrace();
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
