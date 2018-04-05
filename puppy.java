//package scannerPuppy;

import java.io.*;

public class puppy {

	public static void main(String[] args) {
		File srcFile;
		Scanner scanner;
		
		if(args.length == 0){
			System.out.println("You should input a file.");
			return;
		}else if(args.length == 1){
			for (int i = 0; i < args.length; i++) {
				if(args[i].substring(args[i].length() - 5).equals("puppy")){
					System.out.println("src file: " + args[i]);
					srcFile = new File(args[i]);
					scanner = new Scanner(srcFile);
					
					try {
						scanner.run();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else{
					System.out.println("You should input a file extention of '.puppy'");
					return;
				}
	        }
		}else{
			System.out.println("You cannot input several files, but only one.");
			return;
		}
	}

}
