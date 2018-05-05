package parserPuppy;

public class Parser {
	private String[] allTokens;
	private String[][] strWithAllTokens;
	
	private StringBuilder removedCommentStr;
	private String[] inputStr;
	private String[][] strWithoutComment;
	
	private int cursor;
	
	private boolean errorTag;
	
	public Parser(String[] words){
		this.allTokens = words;
		this.removedCommentStr = new StringBuilder();
		this.strWithAllTokens = new String[words.length][2];
		this.cursor = 0;
		this.errorTag = false;
	}
	
	public void parsing(){
		/*
		for(int i=0; i<this.strWithoutComment.length; i++){
			System.out.println("TOKEN: "+this.strWithoutComment[i][0]+" -> "+this.strWithoutComment[i][1]);
		}
		*/
		
		System.out.println("//////////////////////////////////\n");
		System.out.println("parsing starts....\n");
		
		// program_start chk
		if(!(this.strWithoutComment[this.cursor][0].equals("program_start"))){
			System.out.println("\n	PARSE ERROR: "+this.strWithoutComment[this.cursor][0]+" -> program_start error");
			this.errorTag = true;
			System.out.println("\nParsing Failed!\n");
			System.out.println("//////////////////////////////////\n");
			System.exit(0);
		}else{
			//correct
			//System.out.println("\nStart-stmt�̴�!");
			//System.out.println("	PARSE: "+this.strWithoutComment[this.cursor][0]+" -> "+this.strWithoutComment[this.cursor][1]);
			this.cursor++;
			
			// stmt-sequence chk
			while(stmtCheck()){
				if(this.strWithoutComment[this.cursor][0].equals("program_end")){
					//System.out.println("Start-stmt�̴�!\n");
					//System.out.println("	PARSE: "+this.strWithoutComment[this.cursor][0]+" -> "+this.strWithoutComment[this.cursor][1]);
					break;
				}
			};
			System.out.println("Parsing successful!....\n");
			System.out.println("//////////////////////////////////\n");
			
		}
		
	}
	
	public boolean stmtCheck(){
		String expect;
		
		// program_end check
		if(this.cursor >= this.strWithoutComment.length-1){
			System.out.println("\n	PARSE ERROR: "+this.strWithoutComment[this.cursor][0]+" -> Error: no 'program_end' tag.");
			this.errorTag = true;
			System.out.println("\nParsing Failed!\n");
			System.out.println("//////////////////////////////////\n");
			System.exit(0);
		}
		
		while(!this.errorTag){
			switch(choiceStmtType()){
			///////////////////////////////////////
			/////////////// IF STMT////////////////
			///////////////////////////////////////
			case "if-stmt":
				while(true){
					// if (exp) then (stmt-sequence) [else stmt-sequence] if_end
					switch(this.strWithoutComment[this.cursor][0]){
					case "if":
						// if (exp) then (stmt-sequence) if_end
						this.cursor++;
						//System.out.println("	PARSE: "+this.strWithoutComment[this.cursor][0]+" -> "+this.strWithoutComment[this.cursor][1]);
						if(this.strWithoutComment[this.cursor][0].equals("(")){
							//expression check
							expCheck();
							if(this.strWithoutComment[this.cursor][0].equals(")")){
								//System.out.println("	PARSE: "+this.strWithoutComment[this.cursor][0]+" -> "+this.strWithoutComment[this.cursor][1]);
								this.cursor++;
							}else{
								System.out.println("\n	PARSE ERROR: "+this.strWithoutComment[this.cursor][0]+" -> If-stmt Error: if ������ (exp)�� Ȯ�����ּ���.");
								this.errorTag=true;
								System.out.println("\nParsing Failed!\n");
								System.out.println("//////////////////////////////////\n");
								System.exit(0);
								return false;
							}
						}else{
							System.out.println("\n	PARSE ERROR: "+this.strWithoutComment[this.cursor][0]+" -> If-stmt Error: if ������ (exp)�� Ȯ�����ּ���.");
							this.errorTag=true;
							System.out.println("\nParsing Failed!\n");
							System.out.println("//////////////////////////////////\n");
							System.exit(0);
							return false;
						}
						
						// then Ȯ��
						if(this.strWithoutComment[this.cursor][0].equals("then")){
							//System.out.println("	PARSE: "+this.strWithoutComment[this.cursor][0]+" -> "+this.strWithoutComment[this.cursor][1]);
							this.cursor++;
						}else{
							System.out.println("\n	PARSE ERROR: "+this.strWithoutComment[this.cursor][0]+" -> If-stmt Error: then�� �����ϴ�.");
							this.errorTag=true;
							System.out.println("\nParsing Failed!\n");
							System.out.println("//////////////////////////////////\n");
							System.exit(0);
							return false;
						}
						
						// stmt-sequence chk
						//System.out.println("  if-stmt �ȿ��� stmt-sequence Ȯ�� ��.");
						while(stmtCheck()){
							if(this.strWithoutComment[this.cursor][0].equals("else")
									|| this.strWithoutComment[this.cursor][0].equals("if_end")){
								// ���ٰ� else�� if_end �߰�
								break;
							}else if(this.cursor >= this.strWithoutComment.length-1){
								// if_end �� ã�� ����
								System.out.println("\n	PARSE ERROR: "+this.strWithoutComment[this.cursor][0]+" -> If-stmt Error: no if_end");
								this.errorTag=true;
								System.out.println("\nParsing Failed!\n");
								System.out.println("//////////////////////////////////\n");
								System.exit(0);
								return false;
							}
						};
						//System.out.println("  if-stmt �ȿ��� stmt-sequence Ȯ�� ����!");
						//System.out.println("	PARSE: "+this.strWithoutComment[this.cursor][0]+" -> "+this.strWithoutComment[this.cursor][1]);
						
						// else or if_end chk
						if(this.strWithoutComment[this.cursor][0].equals("else")){
							// stmt-sequence chk, go to case 'else'
							continue;		
						}
						else if(this.strWithoutComment[this.cursor][0].equals("if_end")){
							this.cursor++;
							if(this.strWithoutComment[this.cursor][0].equals(";")){
								//System.out.println("If-stmt ���!");
								this.cursor++;
								return true;
							}else{
								System.out.println("\n	PARSE ERROR: "+this.strWithoutComment[this.cursor][0]+" -> If-stmt Error: if_end �ڿ� ;�� �ٿ��� �ؿ�.");
								this.errorTag=true;
								System.out.println("\nParsing Failed!\n");
								System.out.println("//////////////////////////////////\n");
								System.exit(0);
								return false;
							}
						}else{
							System.out.println("\n	PARSE ERROR: "+this.strWithoutComment[this.cursor][0]+" -> If-stmt Error: if_end error");
							this.errorTag=true;
							System.out.println("\nParsing Failed!\n");
							System.out.println("//////////////////////////////////\n");
							System.exit(0);
							return false;
						}
					case "else":
						// [else stmt-sequence] if_end
						this.cursor++;
						//System.out.println("  if-stmt-else �ȿ��� stmt-sequence Ȯ�� ��.");
						while(stmtCheck()){
							if(this.strWithoutComment[this.cursor][0].equals("if_end")){
								break;
							}
						};
						//System.out.println("  if-stmt-else �ȿ��� stmt-sequence Ȯ�� ����.");
						
						if(this.strWithoutComment[this.cursor][0].equals("if_end")){
							this.cursor++;
							if(this.strWithoutComment[this.cursor][0].equals(";")){
								//System.out.println("If-stmt ���!");
								this.cursor++;
								return true;
							}else{
								System.out.println("\n	PARSE ERROR: "+this.strWithoutComment[this.cursor][0]+" -> If-stmt Error: if_end �ڿ� ;�� �ٿ��� �ؿ�.");
								this.errorTag=true;
								System.out.println("\nParsing Failed!\n");
								System.out.println("//////////////////////////////////\n");
								System.exit(0);
								return false;
							}
						}else{
							System.out.println("\n	PARSE ERROR: "+this.strWithoutComment[this.cursor][0]+" -> If-stmt Error: if_end error");
							this.errorTag=true;
							System.out.println("\nParsing Failed!\n");
							System.out.println("//////////////////////////////////\n");
							System.exit(0);
							return false;
						}
					default:
						System.out.println("\n	PARSE ERROR: " +this.strWithoutComment[this.cursor][0]+" -> If-stmt Error. Unmatched keyword.");
						this.errorTag=true;
						System.out.println("\nParsing Failed!\n");
						System.out.println("//////////////////////////////////\n");
						System.exit(0);
						return false;
					}
				}
			///////////////////////////////////////
			/////////// REPEAT STMT////////////////
			///////////////////////////////////////
			case "repeat-stmt":
				// repeat (stmt-sequence) repaet_end
				this.cursor++;
				
				// stmt-sequence chk
				//System.out.println("  repeat-stmt �ȿ��� stmt-sequence Ȯ�� ��.");
				while(stmtCheck()){
					if(this.strWithoutComment[this.cursor][0].equals("repeat_end")){
						// ���ٰ� repeat_end �߰�
						break;
					}else if(this.cursor >= this.strWithoutComment.length-1){
						// repeat_end �� ã�� ����
						System.out.println("\n	PARSE ERROR: "+this.strWithoutComment[this.cursor][0]+" -> Repeat-stmt Error: repeat_end error");
						this.errorTag=true;
						System.out.println("\nParsing Failed!\n");
						System.out.println("//////////////////////////////////\n");
						System.exit(0);
						return false;
					}
				};
				//System.out.println("  repeat-stmt �ȿ��� stmt-sequence Ȯ�� ����!");
				//System.out.println("	PARSE: "+this.strWithoutComment[this.cursor][0]+" -> "+this.strWithoutComment[this.cursor][1]);
				
				// repeat_end chk
				if(this.strWithoutComment[this.cursor][0].equals("repeat_end")){
					this.cursor++;
					if(this.strWithoutComment[this.cursor][0].equals(";")){
						//System.out.println("Repeat-stmt ���!");
						this.cursor++;
						return true;
					}else{
						System.out.println("\n	PARSE ERROR: "+this.strWithoutComment[this.cursor][0]+" -> Repeat-stmt Error: repeat_end �ڿ� ;�� �ٿ��� �ؿ�.");
						this.errorTag=true;
						System.out.println("\nParsing Failed!\n");
						System.out.println("//////////////////////////////////\n");
						System.exit(0);
						return false;
					}
				}else{
					System.out.println("\n	PARSE ERROR: "+this.strWithoutComment[this.cursor][0]+" -> Repeat-stmt Error: repeat_end error");
					this.errorTag=true;
					System.out.println("\nParsing Failed!\n");
					System.out.println("//////////////////////////////////\n");
					System.exit(0);
					return false;
				}
				
			///////////////////////////////////////
			/////////// ASSIGN STMT////////////////
			///////////////////////////////////////
			case "assign-stmt":
				// user-defined identifier := exp
				// = number | id Ȯ��
				if(this.strWithoutComment[this.cursor][1].equals("assign")){
					//System.out.println("	PARSE: "+this.strWithoutComment[this.cursor][0]+" -> "+this.strWithoutComment[this.cursor][1]);
					this.cursor++;
					if(this.strWithoutComment[this.cursor][1].equals("User-defined id") || this.strWithoutComment[this.cursor][1].equals("Number")){
						//System.out.println("	PARSE: "+this.strWithoutComment[this.cursor][0]+" -> "+this.strWithoutComment[this.cursor][1]);
						this.cursor++;
					}else{
						System.out.println("\n	PARSE ERROR: "+this.strWithoutComment[this.cursor][0]+" -> Assign-stmt Error: "+this.strWithoutComment[this.cursor][1]);
						this.errorTag=true;
						System.out.println("\nParsing Failed!\n");
						System.out.println("//////////////////////////////////\n");
						System.exit(0);	
						return false;
					}
				}else{
					System.out.println("\n	PARSE ERROR: "+this.strWithoutComment[this.cursor][0]+" -> Assign-stmt Error: = �� Ȯ���ϼ���.");
					this.errorTag=true;
					System.out.println("\nParsing Failed!\n");
					System.out.println("//////////////////////////////////\n");
					System.exit(0);	
					return false;
				}
				// = number | id �ڿ� �� Ȯ��.
				expect = "Operator";
				while(true){
					if(this.strWithoutComment[this.cursor][1].equals("Keyword")){
						System.out.println("\n	PARSE ERROR: "+this.strWithoutComment[this.cursor][0]+" -> Assign-stmt Error: ; ����.");
						this.errorTag=true;
						System.out.println("\nParsing Failed!\n");
						System.out.println("//////////////////////////////////\n");
						System.exit(0);	
						return false;
					}
					else if(this.strWithoutComment[this.cursor][1].equals("semicolon")){
						//System.out.println("	PARSE: "+this.strWithoutComment[this.cursor][0]+" -> "+this.strWithoutComment[this.cursor][1]);
						this.cursor++;
						break;
					}
					else{
						// {(+|-|*|/) id | number}*;
						if((this.strWithoutComment[this.cursor][0].equals("+") || this.strWithoutComment[this.cursor][0].equals("-")
								|| this.strWithoutComment[this.cursor][0].equals("*")
								|| this.strWithoutComment[this.cursor][0].equals("/")) && expect.equals("Operator")){
							//System.out.println("	PARSE: "+this.strWithoutComment[this.cursor][0]+" -> "+this.strWithoutComment[this.cursor][1]);
							expect = "id or num";
							this.cursor++;
						}else if((this.strWithoutComment[this.cursor][1].equals("User-defined id") 
								|| this.strWithoutComment[this.cursor][1].equals("Number")) && expect.equals("id or num")){
							//System.out.println("	PARSE: "+this.strWithoutComment[this.cursor][0]+" -> "+this.strWithoutComment[this.cursor][1]);
							expect = "Operator";
							this.cursor++;
						}else{
							//error
							System.out.println("\n	PARSE ERROR: "+this.strWithoutComment[this.cursor][0]+" -> Assign-stmt Error. "+this.strWithoutComment[this.cursor][1]);
							this.errorTag=true;
							System.out.println("\nParsing Failed!\n");
							System.out.println("//////////////////////////////////\n");
							System.exit(0);	
							return false;
						}
					}
				}
				if(this.errorTag){
					//System.out.println("Assign-stmt ����!");
					return false;
				}else{
					//System.out.println("Assign-stmt ���!");
					return true;
				}
			///////////////////////////////////////
			/////////// PRINT STMT/////////////////
			///////////////////////////////////////	
			case "print-stmt":
				// print user-defined id {, user-defined id}*
				// user-defined id Ȯ��
				if(this.strWithoutComment[this.cursor][1].equals("User-defined id")){
					//System.out.println("	PARSE: "+this.strWithoutComment[this.cursor][0]+" -> "+this.strWithoutComment[this.cursor][1]);
					this.cursor++;
				}else{
					System.out.println("\n	PARSE ERROR: "+this.strWithoutComment[this.cursor][0]+" -> Print-stmt Error: print �������� id�� �;��մϴ�.");
					this.errorTag=true;
					System.out.println("\nParsing Failed!\n");
					System.out.println("//////////////////////////////////\n");
					System.exit(0);
					return false;
				}
				// {, user-defined id}* Ȯ��
				expect ="comma";
				while(true){
					if(this.strWithoutComment[this.cursor][1].equals("Keyword")){
						System.out.println("\n	PARSE ERROR: "+this.strWithoutComment[this.cursor][0]+" -> Print-stmt Error: ; ����.");
						this.errorTag=true;
						System.out.println("\nParsing Failed!\n");
						System.out.println("//////////////////////////////////\n");
						System.exit(0);	
						return false;
					}
					else if(this.strWithoutComment[this.cursor][1].equals("semicolon")){
						//System.out.println("	PARSE: "+this.strWithoutComment[this.cursor][0]+" -> "+this.strWithoutComment[this.cursor][1]);
						this.cursor++;
						break;
					}else{
						if(this.strWithoutComment[this.cursor][1].equals("comma") && expect.equals("comma")){
							//System.out.println("	PARSE1: "+this.strWithoutComment[this.cursor][0]+" -> "+this.strWithoutComment[this.cursor][1]);
							expect = "id";
							this.cursor++;
						}else if(this.strWithoutComment[this.cursor][1].equals("User-defined id") && expect.equals("id")){
							//System.out.println("	PARSE2: "+this.strWithoutComment[this.cursor][0]+" -> "+this.strWithoutComment[this.cursor][1]);
							expect = "comma";
							this.cursor++;
						}else{
							//error
							if(this.strWithoutComment[this.cursor][1].equals("comma") || this.strWithoutComment[this.cursor][1].equals("User-defined id")){
								System.out.println("\n	PARSE ERROR: "+this.strWithoutComment[this.cursor][0]+" -> Print-stmt Error");
								this.errorTag=true;
								System.out.println("\nParsing Failed!\n");
								System.out.println("//////////////////////////////////\n");
								System.exit(0);	
								return false;
							}
							else{
								System.out.println("\n	PARSE ERROR: "+this.strWithoutComment[this.cursor][0]+" -> Print-stmt Error: invalid word ���.");
								this.errorTag=true;
								System.out.println("\nParsing Failed!\n");
								System.out.println("//////////////////////////////////\n");
								System.exit(0);
								return false;
							}
						}
					}
				}
				if(this.errorTag){
					//System.out.println("Print-stmt ����!");
					return false;
				}else{
					//System.out.println("Print-stmt ���!");
					return true;
				}
			///////////////////////////////////////
			/////////// INPUT STMT/////////////////
			///////////////////////////////////////
			case "input-stmt":
				// input user-defined id {, user-defined id}*
				// user-defined id Ȯ��
				if(this.strWithoutComment[this.cursor][1].equals("User-defined id")){
					//System.out.println("	PARSE: "+this.strWithoutComment[this.cursor][0]+" -> "+this.strWithoutComment[this.cursor][1]);
					this.cursor++;
				}else{
					System.out.println("\n	PARSE ERROR: "+this.strWithoutComment[this.cursor][0]+" -> Input-stmt Error: input �������� id�� �;��մϴ�.");
					this.errorTag=true;
					System.out.println("\nParsing Failed!\n");
					System.out.println("//////////////////////////////////\n");
					System.exit(0);
					return false;
				}
				// {, user-defined id}* Ȯ��
				expect ="comma";
				while(true){
					if(this.strWithoutComment[this.cursor][1].equals("Keyword")){
						System.out.println("\n	PARSE ERROR: "+this.strWithoutComment[this.cursor][0]+" -> Input-stmt Error: ; ����.");
						this.errorTag=true;
						System.out.println("\nParsing Failed!\n");
						System.out.println("//////////////////////////////////\n");
						System.exit(0);	
						return false;
					}
					else if(this.strWithoutComment[this.cursor][1].equals("semicolon")){
						//System.out.println("	PARSE: "+this.strWithoutComment[this.cursor][0]+" -> "+this.strWithoutComment[this.cursor][1]);
						this.cursor++;
						break;
					}else{
						if(this.strWithoutComment[this.cursor][1].equals("comma") && expect.equals("comma")){
							//System.out.println("	PARSE: "+this.strWithoutComment[this.cursor][0]+" -> "+this.strWithoutComment[this.cursor][1]);
							expect = "id";
							this.cursor++;
						}else if(this.strWithoutComment[this.cursor][1].equals("User-defined id") && expect.equals("id")){
							//System.out.println("	PARSE: "+this.strWithoutComment[this.cursor][0]+" -> "+this.strWithoutComment[this.cursor][1]);
							expect = "comma";
							this.cursor++;
						}else{
							//error
							if(this.strWithoutComment[this.cursor][1].equals("comma") || this.strWithoutComment[this.cursor][1].equals("User-defined id")){
								System.out.println("\n	PARSE ERROR: "+this.strWithoutComment[this.cursor][0]+" -> Input-stmt Error");
								this.errorTag=true;
								System.out.println("\nParsing Failed!\n");
								System.out.println("//////////////////////////////////\n");
								System.exit(0);	
								return false;
							}
							else{
								System.out.println("\n	PARSE ERROR: "+this.strWithoutComment[this.cursor][0]+" -> Input-stmt Error: invalid word ���.");
								this.errorTag=true;
								System.out.println("\nParsing Failed!\n");
								System.out.println("//////////////////////////////////\n");
								System.exit(0);
								return false;
							}
						}
					}
				}
				if(this.errorTag){
					//System.out.println("Input-stmt ����!");
					return false;
				}else{
					//System.out.println("Input-stmt ���!");
					return true;
				}
			case "Error":
				System.out.println("\n	PARSE ERROR: Input-stat error");
				this.errorTag=true;
				System.out.println("\nParsing Failed!\n");
				System.out.println("//////////////////////////////////\n");
				System.exit(0);
				return false;
			default:
				return false;
			}
		}
		return false;
	}
	
	public String choiceStmtType(){
		// program_end tag ��� �Ѿ.
		if(this.cursor >= this.strWithoutComment.length-1){
			System.out.println("\n	PARSE ERROR: "+this.strWithoutComment[this.cursor][0]+" -> Error: no 'program_end' tag.");
			this.errorTag = true;
			System.exit(0);
			return "Error";	
		}else{
			switch(this.strWithoutComment[this.cursor][0]){
			case "if":
			case "else":
			case "if_end":
				//System.out.println("If-stmt��!");
				//System.out.println("	PARSE: "+this.strWithoutComment[this.cursor][0]+" -> "+this.strWithoutComment[this.cursor][1]);
				return "if-stmt";
			case "repeat":
				//System.out.println("Repeat-stmt��!");
				//System.out.println("	PARSE: "+this.strWithoutComment[this.cursor][0]+" -> "+this.strWithoutComment[this.cursor][1]);
				return "repeat-stmt";
			case "print":
				//System.out.println("Print-stmt��!");
				//System.out.println("	PARSE: "+this.strWithoutComment[this.cursor][0]+" -> "+this.strWithoutComment[this.cursor][1]);
				this.cursor++;
				return "print-stmt";
			case "input":
				//System.out.println("Input-stmt��!");
				//System.out.println("	PARSE: "+this.strWithoutComment[this.cursor][0]+" -> "+this.strWithoutComment[this.cursor][1]);
				this.cursor++;
				return "input-stmt";
			default:
				// assign 
				if(this.strWithoutComment[this.cursor][1].equals("User-defined id") || this.strWithoutComment[this.cursor][1].equals("Number")){
					// assign
					//System.out.println("Assign-stmt��!");
					//System.out.println("	PARSE: "+this.strWithoutComment[this.cursor][0]+" -> "+this.strWithoutComment[this.cursor][1]);
					this.cursor++;
					return "assign-stmt";
				}else{
					// invalid word
					System.out.println("\n	PARSE ERROR:"+this.strWithoutComment[this.cursor][0]+" -> "+this.strWithoutComment[this.cursor][1]);
					this.errorTag = true;
					System.out.println("\nParsing Failed!\n");
					System.out.println("//////////////////////////////////\n");
					System.exit(0);
					return "Error";
				}
			}	
		}
	}	
	
	public void expCheck(){
		// (number|id) (comparison-op) (number|id)
		//System.out.println("Expression checking...");
		this.cursor++;
		if(this.strWithoutComment[this.cursor][1].equals("Number")
				|| this.strWithoutComment[this.cursor][1].equals("User-defined id")){
			//System.out.println("	PARSE: "+this.strWithoutComment[this.cursor][0]+" -> "+this.strWithoutComment[this.cursor][1]);
			this.cursor++;
			if(this.strWithoutComment[this.cursor][0].equals("<") || this.strWithoutComment[this.cursor][0].equals(">")
					||this.strWithoutComment[this.cursor][0].equals("<=")||this.strWithoutComment[this.cursor][0].equals(">=")
					||this.strWithoutComment[this.cursor][0].equals("==")){
				//System.out.println("	PARSE: "+this.strWithoutComment[this.cursor][0]+" -> "+this.strWithoutComment[this.cursor][1]);
				this.cursor++;
				if(this.strWithoutComment[this.cursor][1].equals("Number") 
						||this.strWithoutComment[this.cursor][1].equals("User-defined id")){
					//System.out.println("	PARSE: "+this.strWithoutComment[this.cursor][0]+" -> "+this.strWithoutComment[this.cursor][1]);
					//System.out.println("Expression ok...");
					this.cursor++;
				}else{
					System.out.println("\n	PARSE ERROR: "+this.strWithoutComment[this.cursor][0] +"-> Error: expression error. Invalid word.");
					this.errorTag = true;
					System.out.println("\nParsing Failed!\n");
					System.out.println("//////////////////////////////////\n");
					System.exit(0);
				}
			}else{
				System.out.println("\n	PARSE ERROR "+this.strWithoutComment[this.cursor][0] +"-> Error: expression error. invalid op.");
				this.errorTag = true;
				System.out.println("\nParsing Failed!\n");
				System.out.println("//////////////////////////////////\n");
				System.exit(0);
			}
		}else{
			System.out.println("\n	PARSE ERROR "+this.strWithoutComment[this.cursor][0] +"-> Error: expression error. Invalid word.");
			this.errorTag = true;
			System.out.println("\nParsing Failed!\n");
			System.out.println("//////////////////////////////////\n");
			System.exit(0);
		}
		
	}
	
	public void removeComments(){
		int i;
		
		for(i=1; i<(this.allTokens.length); i++){
			switch(this.allTokens[i]){
			case "{":
				while(true){
					if(this.allTokens[i].equals("}")){
						break;
					}else{ i++; }
				}
				continue;
			default:
				this.removedCommentStr.append(this.allTokens[i]+" ");				
			}
		}
		//System.out.println(this.removedCommentStr);
		this.inputStr = removedCommentStr.toString().split("\\s+");
		this.strWithoutComment = new String[this.inputStr.length][2];
		
		//strWithToken�̶� ���ؼ� �ּ� ���� �������� strWithoutComment�� �ٽ� �ִ´�.
		for(int j=1, k=0; j<this.allTokens.length; j++){
			if(this.strWithAllTokens[j][1].equals("�ּ�")){
				//skip
			}else{
				this.strWithoutComment[k][0] = this.strWithAllTokens[j][0];
				this.strWithoutComment[k][1] = this.strWithAllTokens[j][1];
				k++;
			}
		}
		
	}
	
	public void setStrWithAllTokens(int idx, String str, String token){
		this.strWithAllTokens[idx][0]=str;
		this.strWithAllTokens[idx][1]=token;
	}
	
	public StringBuilder getRemovedCommentStr(){
		return this.removedCommentStr;
	}
}
