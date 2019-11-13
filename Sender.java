import java.util.*;
import java.net.*;
import java.io.*;

public class Sender {
	
	public static void main(String args[]) {
		String hostname = "0.0.0.0";
		int port = 1234;
		byte data[] = new byte[1024*1024*10];
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
			out = new DataOutputStream(sock.getOutputStream());
			out.writeInt(data.length);
			out.write(data, 0, data.length);
			out.flush();
			System.out.println("Data sent");
			out.close();
			sock.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
