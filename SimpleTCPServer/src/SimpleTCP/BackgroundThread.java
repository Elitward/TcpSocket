package SimpleTCP;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class BackgroundThread extends Thread{
	private Socket sock;
	public Thread setSocket(Socket sock){
		this.sock = sock;
		return this;
	}

	@Override
	public void run() {
		super.run();
		if(sock==null){
			return;
		}
		
		OutputStream os = null;
		InputStream  is = null;
		OutputStreamWriter osw = null;
		InputStreamReader  isr = null;
		try {
			os = sock.getOutputStream();
			is = sock.getInputStream();
			osw = new OutputStreamWriter(os);
			isr = new InputStreamReader(is);
			
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
	}

}
