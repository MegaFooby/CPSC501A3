import java.util.*;
import java.net.*;
import java.io.*;


public class Receiver {
	public static void main(String args[]) {
		int port = 1234;
		ServerSocket server = null;
		DataInputStream in;
		try {
			server = new ServerSocket(port);
			Socket sock = server.accept();
			in = new DataInputStream(sock.getInputStream());
			int length = in.readInt();
			byte data[] = new byte[length];
			int red = 0;
			while(red < length) {
				red += in.read(data, red, length-red);
			}
			System.out.println(new String(data));
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
