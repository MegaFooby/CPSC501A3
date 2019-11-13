import java.util.*;
import java.net.*;
import java.io.*;


public class Receiver {
	public static void main(String args[]) {
		int port = 1234;
		byte data[] = receive(port);
	}
	
	public static byte[] receive(int port) {
		ServerSocket server = null;
		DataInputStream in;
		try {
			server = new ServerSocket(port);
			System.out.println ("Server IP address: " + server.getInetAddress().getHostAddress() + ", port " + port);
			Socket sock = server.accept();
			//System.out.println("Client accepted");
			in = new DataInputStream(sock.getInputStream());
			int length = in.readInt();
			System.out.println(length);
			byte data[] = new byte[length];
			int red = 0;
			while(red < length) {
				red += in.read(data, red, length-red);
			}
			//System.out.println("Data received");
			sock.close();
			server.close();
			
			return data;
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return null;
	}
}
