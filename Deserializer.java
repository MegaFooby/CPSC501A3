import org.jdom2.*;
import org.jdom2.output.*;
import java.util.*;
import java.lang.reflect.*;

public class Deserializer {
	
	public static Object deserializer(Document doc) {
		Vector<id_ref> unassigned = new Vector<id_ref>();
		Vector<arr_ref> unassigned_array = new Vector<arr_ref>();
		try {
			Element rootElement = doc.getRootElement();

			List children = rootElement.getChildren();
			Object obj[] = new Object[children.size()];
			for(int j = 0; j < children.size(); j++){
				Object child_ = children.get(j);
				Element child = (Element) child_;

				Class<?> objClass = Class.forName(child.getAttribute("class").getValue());
				int i = Integer.parseInt(child.getAttribute("id").getValue());
				if(objClass.isArray()) {
					obj[i] = Array.newInstance(objClass.getComponentType(), Integer.parseInt(child.getAttribute("length").getValue()));
					System.out.println(child.getValue());
					List these_children = child.getChildren();
					for(int count = 0; count < these_children.size(); count++) {
						Element this_child = (Element)these_children.get(count);
						Class fieldType = objClass.getComponentType();
						if(fieldType == Character.TYPE) {
							Array.set(obj[i], count, this_child.getValue().charAt(0));
						} else if(fieldType == Integer.TYPE) {
							Array.set(obj[i], count, Integer.parseInt(this_child.getValue()));
						} else if(fieldType == Float.TYPE) {
							Array.set(obj[i], count, Float.parseFloat(this_child.getValue()));
						} else if(fieldType == Byte.TYPE) {
							Array.set(obj[i], count, Byte.parseByte(this_child.getValue()));
						} else if(fieldType == Short.TYPE) {
							Array.set(obj[i], count, Short.parseShort(this_child.getValue()));
						} else if(fieldType == Long.TYPE) {
							Array.set(obj[i], count, Long.parseLong(this_child.getValue()));
						} else if(fieldType == Double.TYPE) {
							Array.set(obj[i], count, Double.parseDouble(this_child.getValue()));
						} else if(fieldType == Boolean.TYPE) {
							Array.set(obj[i], count, Boolean.parseBoolean(this_child.getValue()));
						} else {
							//object not created yet
							if(this_child.getValue().equals("null")) continue;
							unassigned_array.add(new arr_ref(Integer.parseInt(this_child.getValue()), count, obj[i]));
						}
					}
					continue;
				} else {
					obj[i] = objClass.getConstructor().newInstance();
				}
				List objChildren = child.getChildren();
				for(Object field_ : objChildren) {
					Element field = (Element) field_;
					Field pField = objClass.getDeclaredField(field.getAttribute("name").getValue());
					pField.setAccessible(true);
					Class fieldType = pField.getType();
					if(fieldType == Character.TYPE) {
						pField.set(obj[i], field.getValue().charAt(0));
					} else if(fieldType == Integer.TYPE) {
						pField.set(obj[i], Integer.parseInt(field.getValue()));
					} else if(fieldType == Float.TYPE) {
						pField.set(obj[i], Float.parseFloat(field.getValue()));
					} else if(fieldType == Byte.TYPE) {
						pField.set(obj[i], Byte.parseByte(field.getValue()));
					} else if(fieldType == Short.TYPE) {
						pField.set(obj[i], Short.parseShort(field.getValue()));
					} else if(fieldType == Long.TYPE) {
						pField.set(obj[i], Long.parseLong(field.getValue()));
					} else if(fieldType == Double.TYPE) {
						pField.set(obj[i], Double.parseDouble(field.getValue()));
					} else if(fieldType == Boolean.TYPE) {
						pField.set(obj[i], Boolean.parseBoolean(field.getValue()));
					} else {
						//object not created yet
						//System.out.println(child.getAttribute("id").getValue() + " " + pField.getName() + " " + field.getValue());
						if(field.getValue().equals("null")) continue;
						unassigned.add(new id_ref(Integer.parseInt(field.getValue()), pField, obj[i]));
					}
				}
			}
			for(int i = 0; i < unassigned.size(); i++) {
				unassigned.get(i).f.set(unassigned.get(i).obj, obj[unassigned.get(i).id]);
			}
			for(int i = 0; i < unassigned_array.size(); i++) {
				Array.set(unassigned_array.get(i).array, unassigned_array.get(i).place, obj[unassigned_array.get(i).id]);
			}
			return obj[0];
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return null;
	}
}

class id_ref {
	public int id;
	public Field f;
	public Object obj;
	public id_ref(int i, Field f, Object obj) {
		id = i;
		this.f = f;
		this.obj = obj;
	}
}

class arr_ref {
	public int id;
	public int place;
	public Object array;
	public arr_ref(int i, int place, Object arr) {
		id = i;
		this.place = place;
		array = arr;
	}
}
