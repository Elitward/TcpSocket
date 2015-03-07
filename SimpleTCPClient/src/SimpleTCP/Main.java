package SimpleTCP;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
	final static String INIT_MSG = "ABCDEF\n";

	public static void main(String[] args) {
		String host = null;
		int port = 0;
		if(args.length==1){
			port = Integer.parseInt(args[0]);
		}else if(args.length==2){
			host = args[0];
			port = Integer.parseInt(args[1]);
		}else{
			return;
		}
		
		try {
			System.out.println("Client: connect to " + host + ":" + port);
			Socket sock = new Socket(host, port);
			
			OutputStream os = null;
			InputStream  is = null;
			OutputStreamWriter osw = null;
			InputStreamReader  isr = null;
			try {
				os = sock.getOutputStream();
				is = sock.getInputStream();
				osw = new OutputStreamWriter(os);
				isr = new InputStreamReader(is);
				
				osw.write(INIT_MSG);
				osw.flush();
				
				while(true){
					int c = isr.read();
					if(c==-1){
						break;
					}else{
						osw.write(c);
						osw.flush();
						System.out.print((char)c);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(os!=null)
						os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					if(is!=null)
						is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					if(osw!=null)
						osw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					if(isr!=null)
						isr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
