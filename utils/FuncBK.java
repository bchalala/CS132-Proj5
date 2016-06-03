package utils;

import java.util.Vector;
import java.util.HashMap;
import java.util.Map;

import cs132.vapor.ast.VFunction;


/* 	Class for doing the function bookkeeping. 
 	Keeps track of:
 		- Length of the out segment
 		- Length of the local segment
*/ 

public class FuncBK {

	private String body = "";
	private int in;
	private int out;
	private int local;

	public int stackspace;

	public FuncBK(VFunction vfunc) {
		// Write the label for the function
		addLineNoTab(vfunc.ident + ":");

		// Put the return address and frame pointer on the stack.
		addLine("sw $fp -8($sp)");
		addLine("sw $ra -4($sp)");
		addLine("move $fp $sp");

		in = vfunc.stack.in;
		out = vfunc.stack.out;
		local = vfunc.stack.local;

		// Make space for the ra, fp, local, and out.
		stackspace = (local + out + 2)*4;
		addLine("subu $sp $sp " + stackspace);
	}

	public void addLine(String line) {
		body = body + "  " + line + "\n";
	}

	public void addLineNoTab(String line) {
		body = body + line + "\n";
	}

	public void output() {
		System.out.println(body);
	}

}