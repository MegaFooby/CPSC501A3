
public class ClassA {
	public int a[] = new int[2];
	public char b;
	public ClassA c[] = new ClassA[2];
	
	public ClassA(int a, char b, ClassA c) {
		this.a[0] = a;
		this.a[1] = a+1;
		this.b = b;
		this.c[0] = c;
	}
	
	public ClassA() {
		a[0] = 1;
		a[1] = 2;
		b = 'a';
		c[0] = null;
	}
}
