//package demo3;
//
//public class Demo4 {
//	private static int count=0;
//	public static synchronized void incount() //locks the variable and can be accessed by a thread only
//	{
//		count++;
//	}
//	public static void main(String[] args) {
//		Thread t1 = new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				 for (int i = 0; i < 10000; i++) {
//				//	count++;
//					 incount();
//				}
//			}
//		});
//		
//Thread t2 = new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				 for (int i = 0; i < 10000; i++) {
//					//count++;
//					 incount();
//				}
//			}
//		});
//
//t1.start(); //thread 1 starts
//t2.start(); // thread 2 starts
//try { // Java join method can be used to pause the current thread execution until unless the specified thread is dead
//	t1.join();
//	t2.join();
//} catch (InterruptedException e) {
//	// TODO Auto-generated catch block
//	e.printStackTrace();
//}
//System.out.println("Count is "+count); //doesnot wait for both threads to finish before executing this
//
//	}
//
//}
