package demo1;
 class MyClass extends Thread {

	 
	public void run() {
		for (int i = 0; i < 10; i++) {
			System.out.println("Thread: " +Thread.currentThread().getId()+" value "+i);
		}
		
	}
}


 public class demo{
	public static void main(String[] args) {
		MyClass myClass = new MyClass();
		myClass.start();
		MyClass myClass1 = new MyClass();
		//myClass1.start();
	}
}