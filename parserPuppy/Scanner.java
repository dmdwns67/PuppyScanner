package parserPuppy;

import java.io.*;

public class Scanner {
	private Parser parser;
	private FileInputStream fis;
	private char curChar;
	private StringBuilder revisedStr;
	private StringBuilder rltStr;
	private StringBuilder errorPart;
	private StringBuilder errorMessage;
	private boolean error;
	private boolean addSpaceTag;
	private boolean chkNextChar;
	private File file;
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[33m";
	public static final String ANSI_RED_BACKGROUND ="\u001B[31m";
	public static final String ANSI_RESET = "\u001B[37m";
	
	public Scanner(File srcfile){
		this.file = srcfile;
		this.curChar = ' ';
		this.revisedStr = new StringBuilder();
		this.rltStr = new StringBuilder();
		this.errorPart = new StringBuilder();
		this.errorMessage = new StringBuilder();
		this.error = false;
		this.addSpaceTag = false;
		this.chkNextChar = false;
	}
	
	public void run() throws IOException{
		System.out.println("Scanner 클래스의 run 함수가 실행됨.");
		this.fis = new FileInputStream(this.file);
		int i;
		
		// read a puppy file character by character and remove comments
		while((i = this.fis.read()) != -1 ){
			this.curChar = (char) i;
			//System.out.println("curChar: " + this.curChar);
			
			// add a white space between tokens for preceding process.
			addSpaceB2nToken(this.curChar);
			
			// rewrite String to revisedStr
			this.revisedStr.append(this.curChar);
			if(this.addSpaceTag){
				this.revisedStr.append(" ");
				this.addSpaceTag = false;
			}
		}
		
		// split with whitespace (tab, whitespace, enter) and check the validity of the words
		System.out.println("\nscanning starts...\n");
		String[] words = this.revisedStr.toString().split("\\s+");
		
		// Parsing
		this.parser = new Parser(words);
		
		// classify Token and put them into 2D array 'strWithToken' in Parser
		this.errorPart.append(classifyToken(words));
		
		System.out.println("scanning ok...\n");
		
		// remove Comment from entire input string
		this.parser.removeComments();
		this.parser.parsing();
		
		// print result and error messages
		System.out.println("\n-------------result-------------");
		if(this.error == false){
			System.out.println("syntax OK\n...\n");
			System.out.println(this.rltStr);
		}else{
			String[] errorParts = this.errorPart.toString().split("\\s+");
			String[] errorMessages = this.errorMessage.toString().split("\n");
			for(int l=0; l<errorParts.length; l++){
				System.out.println(errorMessages[l]);
				System.out.println("\t"+errorParts[l]);
			}
			System.out.println();
			System.out.println(this.rltStr);
		}
		this.fis.close();
	}
	
	public void addSpaceB2nToken(char ch){
		try{
			/* add a white space between token and user-defined id. 
			 * this is a preceding process for token classification */
			if(ch == '<' || ch == '>'){
				this.chkNextChar = true;
				this.revisedStr.append(" ");
			}else if(ch == '='){
				if(this.chkNextChar){
					// <= || >=
					this.chkNextChar = false;
					this.addSpaceTag = true;
				}else{
					// just '='
					this.revisedStr.append(" ");
					this.addSpaceTag = true;
				}
			}
			else if(ch == '(' || ch == ')' || ch == ';' ||  ch == '+' || ch == '/'
					|| ch == '-' || ch == '*' || ch == '{' || ch == '}' || ch == ','){
				this.revisedStr.append(" ");
				this.addSpaceTag = true;
				if(this.chkNextChar){
					this.chkNextChar = false;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String classifyToken(String[] words){
		int i;
		
		for(i=1; i<(words.length); i++){
			switch(words[i]){
			// 연산자
			case "+":
			case "-":
			case "*":
			case ">":
			case "<":
			case "/":
			case "<=":
			case ">=":
			case "==":
				//System.out.println("Token: "+words[i]+" -> 연산자");
				this.rltStr.append(ANSI_YELLOW_BACKGROUND+words[i]+ANSI_RESET+" ");
				this.parser.setStrWithAllTokens(i, words[i], "연산자");
				continue;
			case "=":
				//System.out.println("Token: "+words[i]+" -> 연산자");
				this.rltStr.append(ANSI_YELLOW_BACKGROUND+words[i]+ANSI_RESET+" ");
				this.parser.setStrWithAllTokens(i, words[i], "assign");
				continue;
			//구분자
			case "{":
				// 주석 안의 내용인 경우는 token 분류 없이 skip
				//System.out.println("Token: "+words[i]+" -> 구분자");
				this.parser.setStrWithAllTokens(i, words[i], "주석");
				this.rltStr.append(ANSI_YELLOW_BACKGROUND+words[i]+ANSI_RESET);
				i++;
				while(!(words[i].equals("}"))){
					//System.out.println("주석입니다.");
					this.parser.setStrWithAllTokens(i, words[i], "주석");
					this.rltStr.append(words[i]+" ");
					i++;
				}
				if(words[i].equals("}")){
					//System.out.println("Token: "+words[i]+" -> 구분자");	
					this.parser.setStrWithAllTokens(i, words[i], "주석");
					this.rltStr.append(ANSI_YELLOW_BACKGROUND+words[i]+ANSI_RESET+"\n");
				}
				continue;
			case ";":
			case "(":
			case ")":
			case ",":
				if(words[i].equals(";")){
					this.rltStr.append(ANSI_YELLOW_BACKGROUND+words[i]+ANSI_RESET+"\n");
					this.parser.setStrWithAllTokens(i, words[i], "semicolon");
				}else if(words[i].equals(",")){
					this.rltStr.append(ANSI_YELLOW_BACKGROUND+words[i]+ANSI_RESET+" ");
					this.parser.setStrWithAllTokens(i, words[i], "comma");
				}else{
					this.rltStr.append(ANSI_YELLOW_BACKGROUND+words[i]+ANSI_RESET);
					this.parser.setStrWithAllTokens(i, words[i], "구분자");
				}
				//System.out.println("Token: "+words[i]+" -> 구분자");
				continue;
			// Keyword
			case "program_start":
			case "program_end":
			case "if_end":
			case "else" :
			case "then":
			case "repeat":
			case "repeat_end":
				if(words[i].equals("repeat_end")||words[i].equals("if_end")){
					this.rltStr.append(ANSI_YELLOW_BACKGROUND+words[i]+ANSI_RESET);
				}else{
					this.rltStr.append(ANSI_YELLOW_BACKGROUND+words[i]+ANSI_RESET+"\n");
				}
				//System.out.println("Token: "+words[i]+" -> Keyword");
				this.parser.setStrWithAllTokens(i, words[i], "Keyword");
				continue;
			case "if":
			case "input":
			case "print":
				this.rltStr.append(ANSI_YELLOW_BACKGROUND+words[i]+ANSI_RESET+" ");
				//System.out.println("Token: "+words[i]+" -> Keyword");
				this.parser.setStrWithAllTokens(i, words[i], "Keyword");
				continue;
			// user-defined identifier
			default:
				// Number check
				int cnt=0;
				for(int k=0; k < words[i].length(); k++){
					if(Character.isDigit(words[i].charAt(k))){
						cnt++;
					}
					if(words[i].charAt(k)=='.'){
						cnt++;
					}
				}
				if(words[i].length() > cnt){
					//this is User-defined id
					cnt = 0;
					// valid id if it is in [a-zA-Z][a-zA-Z0-9]*
					if((words[i].charAt(0) >= 'A' && words[i].charAt(0) <='Z') 
							|| (words[i].charAt(0)>='a') && (words[i].charAt(0) <='z')){
						cnt++;
						for(int k=1; k<words[i].length(); k++){
							if((words[i].charAt(k) >= 'A' && words[i].charAt(k) <='Z') 
									|| (words[i].charAt(k)>='a' && words[i].charAt(k) <='z')
									|| Character.isDigit(words[i].charAt(k))){
								cnt++;
							}
						}
						
						if(cnt == words[i].length()){
							// valid id
							this.rltStr.append(words[i]+" ");
							this.parser.setStrWithAllTokens(i, words[i], "User-defined id");
							//System.out.println("Token: "+words[i]+" -> User-defined id");
						}else{
							// invalid id: only first letter is valid.
							this.parser.setStrWithAllTokens(i, words[i], "Invalid word");
							this.rltStr.append(ANSI_RED_BACKGROUND+words[i]+ANSI_RESET+" ");
							//System.out.println("Token: "+words[i]+" -> Invalid word");
							this.errorPart.append(words[i]+" ");
							this.errorMessage.append("Error#1: Invalid word used.\n");
							this.error = true;
						}
					}else{
						// invalid id: first letter is invalid.
						this.parser.setStrWithAllTokens(i, words[i], "Invalid word");
						this.rltStr.append(ANSI_RED_BACKGROUND+words[i]+ANSI_RESET+" ");
						//System.out.println("Token: "+words[i]+" -> Invalid word");
						this.errorPart.append(words[i]+" ");
						this.errorMessage.append("Error#1: Invalid word used.\n");
						this.error = true;
					}
				}else{
					//this is number
					this.parser.setStrWithAllTokens(i, words[i], "Number");
					this.rltStr.append(words[i]+" ");
					//System.out.println("Token: "+words[i]+" -> Number");
				}
			}
		}
		
		return "";
	}
}
