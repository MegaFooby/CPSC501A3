import java.util.*;
import java.net.*;
import java.io.*;
import org.jdom2.*;
import org.jdom2.output.*;
import java.lang.reflect.*;

public class Sender {
	public Vector<Object> objects = new Vector<Object>();
	
	public static void main(String args[]) {
		String hostname = "0.0.0.0";
		int port = 1234;
		
		if(args.length == 2) {
			hostname = args[0];
			port = Integer.parseInt(args[1]);
		} else {
			Scanner keyboard = new Scanner(System.in);
			System.out.println("Please enter the hostname");
			hostname = keyboard.next();
			System.out.println("Please enter the port number");
			port = keyboard.nextInt();
		}
		
		/*ClassA foo = new ClassA();
		ClassA bar = new ClassA(2, 'b', foo);
		foo.c[0][0] = bar;*/
		Object foo = new Sender().create_object();
		Document sending = Serializer.serialize(foo);
		
		try{
			//new XMLOutputter().output(sending, System.out);
			XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(sending, new FileWriter("file.xml"));
        }catch(Exception e){}
		
		/*byte data[] = "Hello".getBytes();
		send(hostname, port, data);*/
		
		send_object(hostname, port, sending);
	}
	
	public static Field find_field(Class c, String name) throws NoSuchFieldException {
		if(c == Object.class) throw new NoSuchFieldException();
		Field f = null;
		try {
			f = c.getDeclaredField(name);
		} catch(NoSuchFieldException e) {
			return find_field(c.getSuperclass(), name);
		}
		return f;
	}
	
	public Object create_object() {
		Scanner keyboard = new Scanner(System.in);
		
		System.out.println("Enter the name of the class to create");
		Class<?> clazz = null;
		Object obj = null;
		try {
			String classname = keyboard.next();
			clazz = Class.forName(classname);
			obj = clazz.getConstructor().newInstance();
		} catch(ClassNotFoundException e) {
			System.out.println("Could not find class");
			return create_object();
		} catch(Exception e) {
			System.out.println("Could not create object");
			//e.printStackTrace();
			return create_object();
		}
		objects.add(obj);
		String input = "";
		
		while(!input.equalsIgnoreCase("done")) {
			System.out.println("Enter the name of the field to modify, \"done\" to finish, or \"ls\" to list fields");
			input = keyboard.next();
			if(input.equalsIgnoreCase("done")) break;
			if(input.equalsIgnoreCase("ls")) {
				Class c = clazz;
				while(c != Object.class) {
					for(Field f : c.getDeclaredFields()) {
						System.out.println(Inspector.format_class_name(f.getType()) + " " + f.getName());
					}
					c = c.getSuperclass();
				}
				continue;
			}
			Field f = null;
			try {
				f = find_field(clazz, input);
				f.setAccessible(true);
			} catch(NoSuchFieldException e) {
				System.out.println("Cannot find field \"" + input + "\"");
				continue;
			} catch(Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
			if(f.getType().isPrimitive() || f.getType() == String.class) {
				System.out.println("Enter the value for " + input);
				String value = keyboard.next();
				try {
					if(f.getType() == Byte.TYPE) {
						f.setByte(obj, Byte.parseByte(value));
					} else if(f.getType() == Boolean.TYPE) {
						f.setBoolean(obj, Boolean.parseBoolean(value));
					} else if(f.getType() == Character.TYPE) {
						f.setChar(obj, value.charAt(0));
					} else if(f.getType() == Double.TYPE) {
						f.setDouble(obj, Double.parseDouble(value));
					} else if(f.getType() == Float.TYPE) {
						f.setFloat(obj, Float.parseFloat(value));
					} else if(f.getType() == Integer.TYPE) {
						f.setInt(obj, Integer.parseInt(value));
					} else if(f.getType() == Long.TYPE) {
						f.setLong(obj, Long.parseLong(value));
					} else if(f.getType() == Short.TYPE) {
						f.setShort(obj, Short.parseShort(value));
					} else {
						f.set(obj, value);
					}
				} catch(Exception e) {
					System.out.println("Cannot set field \"" + input + "\" to " + value);
					continue;
				}
			} else if(f.getType().isArray()) {
				System.out.println("Enter the access value value");
				int access = keyboard.nextInt();
				String value = "";
				try {
					if(f.getType().getComponentType() == Byte.TYPE) {
						System.out.println("Enter the value");
						value = keyboard.next();
						Array.setByte(f.get(obj), access, Byte.parseByte(value));
					} else if(f.getType().getComponentType() == Boolean.TYPE) {
						System.out.println("Enter the value");
						value = keyboard.next();
						Array.setBoolean(f.get(obj), access, Boolean.parseBoolean(value));
					} else if(f.getType().getComponentType() == Character.TYPE) {
						System.out.println("Enter the value");
						value = keyboard.next();
						Array.setChar(f.get(obj), access, value.charAt(0));
					} else if(f.getType().getComponentType() == Double.TYPE) {
						System.out.println("Enter the value");
						value = keyboard.next();
						Array.setDouble(f.get(obj), access, Double.parseDouble(value));
					} else if(f.getType().getComponentType() == Float.TYPE) {
						System.out.println("Enter the value");
						value = keyboard.next();
						Array.setFloat(f.get(obj), access, Float.parseFloat(value));
					} else if(f.getType().getComponentType() == Integer.TYPE) {
						System.out.println("Enter the value");
						value = keyboard.next();
						Array.setInt(f.get(obj), access, Integer.parseInt(value));
					} else if(f.getType().getComponentType() == Long.TYPE) {
						System.out.println("Enter the value");
						value = keyboard.next();
						Array.setLong(f.get(obj), access, Long.parseLong(value));
					} else if(f.getType().getComponentType() == Short.TYPE) {
						System.out.println("Enter the value");
						value = keyboard.next();
						Array.setShort(f.get(obj), access, Short.parseShort(value));
					} else if(f.getType().getComponentType() == String.class){
						System.out.println("Enter the value");
						value = keyboard.next();
						Array.set(f.get(obj), access, value);
					} else {
						System.out.println("Enter a reference value or type \"create\" to make a new object");
						input = keyboard.next();
						try {
							if(input.equalsIgnoreCase("create")) {
								Array.set(f.get(obj), access, create_object(f.getType().getComponentType(), keyboard));
							} else {
								Array.set(f.get(obj), access, objects.get(Integer.parseInt(input)));
							}
						} catch(Exception e) {
							System.out.println("Could not set object");
						}
					}
				} catch(Exception e) {
					System.out.println("Cannot set field " + input + "[" + access + "] to " + value);
					e.printStackTrace();
					continue;
				}
			} else {//is object
				System.out.println("Enter a reference value or type \"create\" to make a new object");
				input = keyboard.next();
				try {
					if(input.equalsIgnoreCase("create")) {
						f.set(obj, create_object(f.getType(), keyboard));
					} else {
						f.set(obj, objects.get(Integer.parseInt(input)));
					}
				} catch(Exception e) {
					System.out.println("Could not set object");
				}
			}
		}
		
		keyboard.close();
		return obj;
	}
	
	public Object create_object(Class<?> clazz, Scanner keyboard) {
		Object obj = null;
		try {
			obj = clazz.getConstructor().newInstance();
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		objects.add(obj);
		String input = "";
		
		while(!input.equalsIgnoreCase("done")) {
			System.out.println("Enter the name of the field to modify, \"done\" to finish, or \"ls\" to list fields");
			input = keyboard.next();
			if(input.equalsIgnoreCase("done")) break;
			if(input.equalsIgnoreCase("ls")) {
				Class c = clazz;
				while(c != Object.class) {
					for(Field f : c.getDeclaredFields()) {
						System.out.println(Inspector.format_class_name(f.getType()) + " " + f.getName());
					}
					c = c.getSuperclass();
				}
				continue;
			}
			Field f = null;
			try {
				f = find_field(clazz, input);
				f.setAccessible(true);
			} catch(NoSuchFieldException e) {
				System.out.println("Cannot find field \"" + input + "\"");
				continue;
			} catch(Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
			if(f.getType().isPrimitive() || f.getType() == String.class) {
				System.out.println("Enter the value for " + input);
				String value = keyboard.next();
				try {
					if(f.getType() == Byte.TYPE) {
						f.setByte(obj, Byte.parseByte(value));
					} else if(f.getType() == Boolean.TYPE) {
						f.setBoolean(obj, Boolean.parseBoolean(value));
					} else if(f.getType() == Character.TYPE) {
						f.setChar(obj, value.charAt(0));
					} else if(f.getType() == Double.TYPE) {
						f.setDouble(obj, Double.parseDouble(value));
					} else if(f.getType() == Float.TYPE) {
						f.setFloat(obj, Float.parseFloat(value));
					} else if(f.getType() == Integer.TYPE) {
						f.setInt(obj, Integer.parseInt(value));
					} else if(f.getType() == Long.TYPE) {
						f.setLong(obj, Long.parseLong(value));
					} else if(f.getType() == Short.TYPE) {
						f.setShort(obj, Short.parseShort(value));
					} else {
						f.set(obj, value);
					}
				} catch(Exception e) {
					System.out.println("Cannot set field \"" + input + "\" to " + value);
					continue;
				}
			} else if(f.getType().isArray()) {
				System.out.println("Enter the access value value");
				int access = keyboard.nextInt();
				String value = "";
				try {
					if(f.getType().getComponentType() == Byte.TYPE) {
						System.out.println("Enter the value");
						value = keyboard.next();
						Array.setByte(f.get(obj), access, Byte.parseByte(value));
					} else if(f.getType().getComponentType() == Boolean.TYPE) {
						System.out.println("Enter the value");
						value = keyboard.next();
						Array.setBoolean(f.get(obj), access, Boolean.parseBoolean(value));
					} else if(f.getType().getComponentType() == Character.TYPE) {
						System.out.println("Enter the value");
						value = keyboard.next();
						Array.setChar(f.get(obj), access, value.charAt(0));
					} else if(f.getType().getComponentType() == Double.TYPE) {
						System.out.println("Enter the value");
						value = keyboard.next();
						Array.setDouble(f.get(obj), access, Double.parseDouble(value));
					} else if(f.getType().getComponentType() == Float.TYPE) {
						System.out.println("Enter the value");
						value = keyboard.next();
						Array.setFloat(f.get(obj), access, Float.parseFloat(value));
					} else if(f.getType().getComponentType() == Integer.TYPE) {
						System.out.println("Enter the value");
						value = keyboard.next();
						Array.setInt(f.get(obj), access, Integer.parseInt(value));
					} else if(f.getType().getComponentType() == Long.TYPE) {
						System.out.println("Enter the value");
						value = keyboard.next();
						Array.setLong(f.get(obj), access, Long.parseLong(value));
					} else if(f.getType().getComponentType() == Short.TYPE) {
						System.out.println("Enter the value");
						value = keyboard.next();
						Array.setShort(f.get(obj), access, Short.parseShort(value));
					} else if(f.getType().getComponentType() == String.class){
						System.out.println("Enter the value");
						value = keyboard.next();
						Array.set(f.get(obj), access, value);
					} else {
						System.out.println("Enter a reference value or type \"create\" to make a new object");
						input = keyboard.next();
						try {
							if(input.equalsIgnoreCase("create")) {
								Array.set(f.get(obj), access, create_object(f.getType().getComponentType(), keyboard));
							} else {
								Array.set(f.get(obj), access, objects.get(Integer.parseInt(input)));
							}
						} catch(Exception e) {
							System.out.println("Could not set object");
						}
					}
				} catch(Exception e) {
					System.out.println("Cannot set field " + input + "[" + access + "] to " + value);
					e.printStackTrace();
					continue;
				}
			} else {//is object
				System.out.println("Enter a reference value or type \"create\" to make a new object");
				input = keyboard.next();
				try {
					if(input.equalsIgnoreCase("create")) {
						f.set(obj, create_object(f.getType(), keyboard));
					} else {
						f.set(obj, objects.get(Integer.parseInt(input)));
					}
				} catch(Exception e) {
					System.out.println("Could not set object");
				}
			}
		}
		
		return obj;
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
		catch(ConnectException e) {
			//e.printStackTrace();
			System.out.println("Failed to connet to server");
			System.exit(0);
		}
		catch(Exception e) {
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
