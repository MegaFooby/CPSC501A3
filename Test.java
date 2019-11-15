import org.junit.*;
import static org.junit.Assert.*;
import java.lang.reflect.*;
import java.util.*;

public class Test {
	Object test_obj[];
	@Before
	public void init() {
		test_obj = new Object[1];
		test_obj[0] = new T1();
	}
	
	@org.junit.Test
	public void test1() {
		for(int i = 0; i < test_obj.length;i++) {
			Object obj = Deserializer.deserialize(Serializer.serialize(test_obj[i]));
			System.out.println(test_obj[0].toString() + " " + obj.toString());
			assertTrue(test_obj[i].equals(obj));
		}
	}
	
	@After
	public void destroy() {
		test_obj = null;
	}
}

class T1 {
	public String s;
	public T1() {
		s = "Hello world";
	}
	@Override
	public boolean equals(Object obj) {
		try {
			T1 tmp = (T1) obj;
			if(this.s.equals(tmp.s)) return true;
		} catch(Exception e) {}
		return false;
	}
}
