package wci.io;

import java.io.BufferedReader;
import java.io.PrintStream;

import wci.test.Pascal;

public privileged aspect IOFormatter {
	
	public static String PROJECT_DESP = "wci\n===\n\nWriting Compilers and Interpreters: " +
			"A Software Engineering Approach\n\n";
	public static Integer CHAPTER = 5;
	private static String chapterHeader = "OUTPUT FOR CHAPTER "+CHAPTER;
	
	public pointcut program(): execution(public static void Pascal.main(..));
	
	private String padding = "\t";

	public pointcut ps(): call(public void PrintStream+.println(..));
	
	before():ps(){
		PrintStream ps = (PrintStream)thisJoinPoint.getTarget();
		ps.print(padding);
	}
	
	public pointcut sourceOpen(): call(public BufferedReader+.new(..));
	
	public pointcut sourceClose(): call(public * BufferedReader+.close());
	
	void around():program(){
		PrintStream ps = Pascal.ps;
		ps.print(PROJECT_DESP);
		ps.print("===== BEGIN "+chapterHeader+" =====\n\n");
		proceed();
		ps.print("\n\n===== END "+chapterHeader+" =====\n");
	}
	
	after():sourceOpen(){
		PrintStream ps = Pascal.ps;
		ps.print("===== BEGIN SOURCE =====\n\n");
	}
	
	after():sourceClose(){
		PrintStream ps = Pascal.ps;
		ps.print("\n===== END SOURCE =====\n");
	}
	
}
