import java.util.*;
import java.net.*;
import java.io.*;
import org.jdom2.*;
import org.jdom2.output.*;

public class Sender {
	
	public static void main(String args[]) {
		String hostname = "0.0.0.0";
		int port = 1234;
		
		ClassA foo = new ClassA();
		ClassA bar = new ClassA(2, 'b', foo);
		foo.c[0] = bar;
		Document sending = Serializer.serialize(foo);
		
		try{
			new XMLOutputter().output(sending, System.out);
			XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(sending, new FileWriter("file.xml"));
        }catch(Exception e){}
		
		/*byte data[] = "Hello".getBytes();
		send(hostname, port, data);*/
		
		send_object(hostname, port, sending);
	}
	
	public static void send_object(String ipaddress, int port, Object obj) {
		ObjectOutputStream out;
		Socket sock = null;
		try {
			sock = new Socket(InetAddress.getByName(ipaddress), port);
			out = new ObjectOutputStream(sock.getOutputStream());
			out.writeObject(obj);
			out.flush();
			out.close();
			sock.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public static void send(String ipaddress, int port, byte data[]) {
		DataOutputStream out;
		Socket sock = null;
		try {
			sock = new Socket(InetAddress.getByName(ipaddress), port);
			out = new DataOutputStream(sock.getOutputStream());
			out.writeInt(data.length);
			out.write(data, 0, data.length);
			out.flush();
			out.close();
			sock.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
