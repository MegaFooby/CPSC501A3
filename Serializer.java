import org.jdom2.*;
import org.jdom2.output.*;
import java.util.*;
import java.lang.reflect.*;

/**
 * Creates a serialized version of an object
 */
public class Serializer {
	IdentityHashMap<Object, Integer> seen;
	Vector<Boolean> checked;
	Vector<Object> checked_ref;
	Document doc;
	int count;
	
	private Serializer(Object obj) {
		seen = new IdentityHashMap<Object, Integer>();
		checked = new Vector<Boolean>();
		checked_ref = new Vector<Object>();
		Element cereal = new Element("serialized");
		doc = new Document(cereal);
		count = 0;
		
		seen.put(obj, Integer.valueOf(count));
		count++;
		checked.add(Boolean.valueOf(false));
		checked_ref.add(obj);
		
		for(int i = 0; i < checked.size(); i++) {
			if(!checked.get(i).booleanValue()) {
				this.sir(checked_ref.get(i));
			}
		}
	}
	
	private void sir(Object obj) {
		if(checked.get(seen.get(obj)).booleanValue()) {
			return;
		}
		checked.set(seen.get(obj), Boolean.valueOf(true));
		Element object = new Element("object");
		object.setAttribute("class", obj.getClass().getName());
		object.setAttribute("id", seen.get(obj).toString());
		
		if(obj.getClass().isArray()) {
			object.setAttribute("length", Integer.toString(Array.getLength(obj)));
			if(obj.getClass().getName().charAt(1) == 'L' || obj.getClass().getName().charAt(1) == '[') {
				for(int i = 0; i < Array.getLength(obj); i++) {
					if(Array.get(obj, i) == null) {
						object.addContent(new Element("reference").setText("null"));
						continue;
					} else if(seen.get(Array.get(obj, i)) == null) {
						seen.put(Array.get(obj, i), Integer.valueOf(count));
						checked_ref.add(Array.get(obj, i));
						checked.add(Boolean.valueOf(false));
						count++;
					}
					object.addContent(new Element("reference").setText(seen.get(Array.get(obj, i)).toString()));
				}
			} else {
				for(int i = 0; i < Array.getLength(obj); i++) {
					object.addContent(new Element("value").setText(Array.get(obj, i).toString()));
				}
			}
			doc.getRootElement().addContent(object);
			return;
		}
		Class clazz = obj.getClass();
		while(clazz != Object.class) {
			for(Field f : clazz.getDeclaredFields()) {
				if(Modifier.isFinal(f.getModifiers())) continue;
				f.setAccessible(true);
				Element field = new Element("field");
				field.setAttribute("name", f.getName());
				field.setAttribute("declaringclass", f.getDeclaringClass().getName());
				try {
					if(f.getType().isPrimitive() || f.getType() == String.class) {
						field.addContent(new Element("value").setText(f.get(obj).toString()));
					} else if(f.get(obj) == null) {
						field.addContent(new Element("reference").setText("null"));
					} else {
						if(seen.get(f.get(obj)) == null) {
							seen.put(f.get(obj), Integer.valueOf(count));
							checked_ref.add(f.get(obj));
							checked.add(Boolean.valueOf(false));
							count++;
						}
						field.addContent(new Element("reference").setText(seen.get(f.get(obj)).toString()));
					}
				} catch(Exception e) {
					e.printStackTrace();
					System.exit(0);
				}
				object.addContent(field);
			}
			clazz = clazz.getSuperclass();
		}
		doc.getRootElement().addContent(object);
	}
	
	/**
	 * Creates the serialized object
	 * @param obj	The object to serialize
	 * @return	The serialized document
	 */
	public static Document serialize(Object obj) {
		Serializer ser = new Serializer(obj);
		return ser.doc;
	}
}
