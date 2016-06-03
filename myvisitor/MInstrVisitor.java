package myvisitor;

import cs132.vapor.ast.*;
import utils.FuncBK;

public class MInstrVisitor extends VInstr.Visitor<Throwable>{

	public static FuncBK fbk;

	public MInstrVisitor(FuncBK funcbk) {
		fbk = funcbk;
	}

	public void visit(VAssign a) throws Throwable {
		String dest = a.dest.toString();
		String src = a.source.toString();
		if (src.charAt(0) == '$')
			fbk.addLine("move " + dest + " " + src);
		else
			fbk.addLine("li " + dest + " " + src);
	} 

	public void visit(VBranch b) throws Throwable {
		// Build the string for the if line.
		if (b.positive) 
			fbk.addLine("bnez " + b.value.toString() + " " + b.target.toString().substring(1));
		else 
			fbk.addLine("beqz " + b.value.toString() + " " + b.target.toString());
	}

	public void visit(VBuiltIn c) throws Throwable {


	}

	public void visit(VCall c) throws Throwable {
		fbk.addLine("jalr " + c.addr.toString());
	}

	public void visit(VGoto g) throws Throwable {
		fbk.addLine("j " + g.target.toString().substring(1));
	}

	public void visit(VMemRead r) throws Throwable {

	}

	public void visit(VMemWrite w) throws Throwable {
		String memaddress = genMemAddress(w.dest);
		fbk.addLine("sw " + w.source.toString() + " " + memaddress);
	}

	public void visit(VReturn r) throws Throwable {
		// Cleans up the stack, then outputs the code
		fbk.addLine("lw $ra -4($fp)");
		fbk.addLine("lw $fp -8($fp)");
		fbk.addLine("addu $sp $sp " + fbk.stackspace);
		fbk.addLine("jr $ra");
	}

	public String genMemAddress(VMemRef vmr) {
		if (vmr instanceof VMemRef.Stack) {
			VMemRef.Stack dest = (VMemRef.Stack) vmr;

			// Get the correct offset for a stack memory access
			int offset = 0;
			boolean fp = false;
			switch (VMemRef.Stack.Region.valueOf(dest.region)) {
				case VMemRef.Stack.Region.In:
					offset = 4*dest.index;
					fp = true;
					break;
				case VMemRef.Stack.Region.Out:
					offset = 4*dest.index;
					fp = false;
					break;
				case VMemRef.Stack.Region.Local:
					offset = -8 + -4*dest.index;
					fp = true;
					break;
			}

			String retstr = offset + "(";
			String reg = fp ? "$fp" : "$sp";
			retstr += reg + ")";
			return retstr;
		} 
		else {
			VMemRef.Global dest = (VMemRef.Global) vmr;
			String retstr = dest.byteOffset + "(" + dest.base.toString() + ")";
			return retstr;
		}
	}

}