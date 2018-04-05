# A Manual Scanner for the Language, Puppy #

This is the first homework for 2018-1 Compiler Theory class. 

It consists of:
1. java files <br>
 : It includes puppy.java and Scanner.java which are java files for executing the program.
2. sample files <br>
 : It includes Sample1.puppy ~ Sample4.puppy which are sample files for testing.

---------------------
## The language, Puppy ##
1. User-defined identifier starts with an alphabet letter followed by alphabet or number.
2. The program is case-sensitive.
3. Comments are delimited by a pair of {, and }.
4. The assignment is '='.
5. The keywords (reserved words) are marked with yellow in sample programs.


## How to compile ##
<br>*Before start compiling, be aware that it works with Linux*

1. To compile java files, open your terminal and type :<br>
<code> $ javac puppy.java Scanner.java </code>


## How to use ##

* USER COMMANDS
  * SYNOPSIS <br>
	<code>java puppy FILE.puppy</code>

 * DESCRIPTION
	This manual page documents the manual scanner for Puppy command line functionality. It receives a .puppy file 
	from the user and examines the validity of the words used. The keywords (reserved words) are marked with yellow, 
	and words which are not valid are marked in red. The usage is given below. 
		
	   -  java puppy source.puppy 		     

	**Note that only one .puppy file must be specified*
