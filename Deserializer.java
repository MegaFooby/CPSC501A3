import org.jdom2.*;
import org.jdom2.output.*;
import java.util.*;
import java.lang.reflect.*;

public class Deserializer {
	IdentityHashMap<Object, Integer> seen;
	Vector<Boolean> checked;
	Vector<Object> checked_ref;
	Document doc;
	
	public static Object deserializer(Document doc) {
		try {
			Element rootElement = doc.getRootElement();

			List children = rootElement.getChildren();
			for(Object child_ : children){
				Element child = (Element) child_;

				Class<?> objClass = Class.forName(child.getAttribute("class").getValue());
				Object obj = objClass.getConstructor().newInstance();
				List objChildren = child.getChildren();
				for(Object field_ : objChildren) {
					Element field = (Element) field_;
					Field pField = objClass.getDeclaredField(field.getAttribute("name").getValue());
					pField.setAccessible(true);
					Class fieldType = pField.getType();
					pField.set(obj,field.getValue());
				}
				return obj;
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return null;
	}
}
