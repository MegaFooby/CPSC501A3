import java.util.*;
import java.net.*;
import java.io.*;

public class Sender {
	
	public static void main(String args[]) {
		String hostname = "0.0.0.0";
		int port = 1234;
		byte data[] = "Hello".getBytes();
		try {
			send(hostname, port, data);
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public static void send(String ipaddress, int port, byte data[]) throws Exception {
		DataOutputStream out;
		Socket sock = null;
		
		try {
			sock = new Socket(InetAddress.getByName(ipaddress), port);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		try {
			out = new DataOutputStream(sock.getOutputStream());
			out.writeInt(data.length);
			out.write(data, 0, data.length);
			out.flush();
			out.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
