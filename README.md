# A Manual Scanner for the Language, Puppy #

This repository is the first homework for 2018-1 Compiler Theory class. 

It consists of:
1. java files
 : It includes puppy.java and Scanner.java which are java files for executing the program.
2. sample files
 : It includes Sample1.puppy ~ Sample4.puppy which are sample files for testing.

---------------------
## How to compile ##
<br>*Before start compiling, be aware that it works with Linux*

1. To compile java files, open your terminal and type :<br>
<code> $ javac puppy.java Scanner.java </code>


## How to use ##

* USER COMMANDS
  * NAME <br>
	mdparser : parses .md file into html file.

  * SYNOPSIS <br>
	<code>mdparser FILE.md [FILE.html]</code>

 * DESCRIPTION
	This manual page documents the mdparser command line functionality. It receives a .md file and
	converts it into a html file. Usage of mdparser are given below. 
	
		
	   -  mdparser source.md destination.html 
	      (converts source.md into a file named destination.html in stylish format)

	   -  mdparser source.md 		     
	      (assigns the same name with the source .md file if output file name is not given. In this case, returns source.html)


	**Note that at least one .md file must be specified*
