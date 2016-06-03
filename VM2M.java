import cs132.util.ProblemException;
import cs132.vapor.parser.VaporParser;
import cs132.vapor.ast.VBuiltIn.Op;
import cs132.vapor.ast.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;
import java.util.HashMap;
import java.util.Map;

import myvisitor.MInstrVisitor;
import utils.FuncBK;

public class VM2M {

	public static void main(String[] args) {
		try { 
			// Parse the vapor program
			VaporProgram program = parseVapor(System.in, System.err); 

			// Generate the VMT Segment
			printDataSeg(program);

			// Print the text for linking to main
			printInitText();

			// Parse the individual functions within the vapor-m program
			for (VFunction vf: program.functions) 
				translateFunction(vf);

			// Print the postamble to the program
			printPostamble();

		}
		catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}

	public static VaporProgram parseVapor(InputStream in, PrintStream err)
	throws IOException
	{
		Op[] ops = {
			Op.Add, Op.Sub, Op.MulS, Op.Eq, Op.Lt, Op.LtS,
			Op.PrintIntS, Op.HeapAllocZ, Op.Error,
		};

		boolean allowLocals = false;
		String[] registers = {
			"v0", "v1",
			"a0", "a1", "a2", "a3",
			"t0", "t1", "t2", "t3", "t4", "t5", "t6", "t7",
			"s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7",
			"t8",
		};
		boolean allowStack = true;

		VaporProgram program;
		try {
			program = VaporParser.run(new InputStreamReader(in), 1, 1,
				java.util.Arrays.asList(ops),
				allowLocals, registers, allowStack);
		}
		catch (ProblemException ex) {
			err.println(ex.getMessage());
			return null;
		}

		return program;
	}

	public static void translateFunction(VFunction func) {

		// Data Structure for keeping track of the function's stack
		FuncBK fbk = new FuncBK(func);

		// Visitor for translating instructions
		MInstrVisitor instrvis = new MInstrVisitor(fbk);

		// Variables for label bookkeeping
		int linenum = 1;
		int labelnum = 0;
		int maxlabelnum = func.labels.length;

		// Visit every instruction
		for (VInstr inst: func.body) {
			try {
				// Add labels to the proper place.
				while (labelnum < maxlabelnum 
					&& func.labels[labelnum].instrIndex < linenum) {
					fbk.addLineNoTab(func.labels[labelnum].ident + ":");
					labelnum++;
				}
				inst.accept(instrvis);
				linenum++;
			}
			catch (Throwable t) {
				System.out.println("Error, a throwable was thrown! Oh no! Not throwables!");
			}
		}

		// Write the output of the translation
		fbk = instrvis.fbk;
		fbk.output();
		return;
	}

	public static void printDataSeg(VaporProgram program) {
		if (program.dataSegments != null) {
			System.out.println(".data\n");
			for (VDataSegment ds: program.dataSegments) {
				System.out.println(ds.ident + ":");
				if (ds.values != null) {
					for (VOperand.Static v: ds.values) {
						System.out.println("  " + v.toString().substring(1));
					}
				}
				System.out.println("");
			}
		}
	}

	public static void printInitText() {
		String initSeg = ".text\n\n";
		initSeg += "  jal Main\n";
		initSeg += "  li $v0 10\n";
		initSeg += "  syscall\n";
		System.out.println(initSeg);
	}

	public static void printPostamble() {
		String pa = "_print:\n";
  		pa += "  li $v0 1   # syscall: print integer\n";
  		pa += "  syscall\n";
  		pa += "  la $a0 _newline\n";
  		pa += "  li $v0 4   # syscall: print string\n";
  		pa += "  syscall\n";
  		pa += "  jr $ra\n";

  		pa += "_error:\n";
  		pa += "  li $v0 4   # syscall: print string\n";
  		pa += "  syscall\n";
  		pa += "  li $v0 10  # syscall: exit\n";
  		pa += "  syscall\n";

  		pa += "_heapAlloc:\n";
  		pa += "  li $v0 9   # syscall: sbrk\n";
  		pa += "  syscall\n";
  		pa += "  jr $ra\n";

  		pa += ".data\n";
  		pa += ".align 0\n";
  		pa += "_newline: .asciiz \"\\n\"\n";
  		pa += "_str0: .asciiz \"null pointer\\n\"\n";
  		System.out.println(pa);
	}


}
