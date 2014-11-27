package utils;

import java.io.IOException;



public class Progress {

	public Progress(){

	}

	boolean flag=false;





	Thread th = new Thread() {
		@Override
		public void run() {
			try {
				String c= "|";				
				//System.out.write("\r|".getBytes());
				while(loading) {
					//System.out.write("-".getBytes());					
					System.out.write(c.getBytes());					
					Thread.sleep(1000);
				}
				System.out.write("| Done \r\n".getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};

	private static boolean loading = true;
	public synchronized void start(String msg) throws IOException, InterruptedException {
		System.out.println(msg);		
		th.start();
	}

	public void stop(){
		try {
			th.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	//	public static void main(String[] argv) throws Exception{
	//		String anim= "|/-\\";
	//		for (int x =0 ; x < 100 ; x++){
	//			String data = "\r" + anim.charAt(x % anim.length())  + " " + x ;
	//			System.out.write(data.getBytes());
	//			Thread.sleep(100);
	//		}
	//	}


	public static void main(String[] args){
		Progress run= new Progress();
		try {
			run.start("Calculating...");
			Thread.sleep(10000);
			run.stop();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
