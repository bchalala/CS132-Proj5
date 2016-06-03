package myvisitor;

import cs132.vapor.ast.*;
import java.io.*;
import utils.FuncBK;

public class MInstrVisitor extends VInstr.Visitor<Throwable>{

	public static FuncBK fbk;

	public MInstrVisitor(FuncBK funcbk) {
		fbk = funcbk;
	}

	public void visit(VAssign a) throws Throwable {
		String dest = a.dest.toString();
		String src = a.source.toString();
		// Case where src is a register
		if (src.charAt(0) == '$')
			fbk.addLine("move " + dest + " " + src);
		// Case where src is a label
		else if (src.charAt(0) == ':')
			fbk.addLine("la " + dest + " " + src.substring(1));
		// Case where src is an immediate
		else
			fbk.addLine("li " + dest + " " + src);
	} 

	public void visit(VBranch b) throws Throwable {
		// Build the string for the if line. Only thing the target can be is a code label.
		if (b.positive) 
			fbk.addLine("bnez " + b.value.toString() + " " + b.target.toString().substring(1));
		else 
			fbk.addLine("beqz " + b.value.toString() + " " + b.target.toString().substring(1));
	}

	public void visit(VBuiltIn c) throws Throwable {
		String builtintype = c.op.name;
		boolean v0;
		boolean v1;
		String arg0;
		String arg1;

		switch (builtintype) {
			case "Add":
				// v0 and v1 are true if they are used to store immediates.
				v0 = false;
				v1 = false;
				arg0 = c.args[0].toString();
				arg1 = c.args[1].toString();

				if (arg0.charAt(0) != '$') {
					if (arg0.charAt(0) == ':') {
						fbk.addLine("la $v0 " + arg0.substring(1));
						arg0 = arg0.substring(1);
					}
					else 
						fbk.addLine("li $v0 " + arg0);
					v0 = true;
				}
				if (arg1.charAt(0) != '$') {
					if (arg1.charAt(0) == ':') {
						fbk.addLine("la $v1 " + arg1.substring(1));
						arg1 = arg1.substring(1);
					}
					else 
						fbk.addLine("li $v1 " + arg1);
					v1 = true;
				}

				arg0 = v0 ? "$v0" : arg0;
				arg1 = v1 ? "$v1" : arg1;
				fbk.addLine("addu " + c.dest.toString() + " " + arg0 + " " + arg1);
			break;

			case "Sub":
				// v0 and v1 are true if they are used to store immediates.
				v0 = false;
				v1 = false;
				arg0 = c.args[0].toString();
				arg1 = c.args[1].toString();

				if (arg0.charAt(0) != '$') {
					if (arg0.charAt(0) == ':') {
						fbk.addLine("la $v0 " + arg0.substring(1));
						arg0 = arg0.substring(1);
					}
					else 
						fbk.addLine("li $v0 " + arg0);
					v0 = true;
				}
				if (arg1.charAt(0) != '$') {
					if (arg1.charAt(0) == ':') {
						fbk.addLine("la $v1 " + arg1.substring(1));
						arg1 = arg1.substring(1);
					}
					else 
						fbk.addLine("li $v1 " + arg1);
					v1 = true;
				}

				arg0 = v0 ? "$v0" : arg0;
				arg1 = v1 ? "$v1" : arg1;
				fbk.addLine("subu " + c.dest.toString() + " " + arg0 + " " + arg1);

			break;

			case "MulS":
				// v0 and v1 are true if they are used to store immediates.
				v0 = false;
				v1 = false;
				arg0 = c.args[0].toString();
				arg1 = c.args[1].toString();

				if (arg0.charAt(0) != '$') {
					if (arg0.charAt(0) == ':') {
						fbk.addLine("la $v0 " + arg0.substring(1));
						arg0 = arg0.substring(1);
					}
					else 
						fbk.addLine("li $v0 " + arg0);
					v0 = true;
				}
				if (arg1.charAt(0) != '$') {
					if (arg1.charAt(0) == ':') {
						fbk.addLine("la $v1 " + arg1.substring(1));
						arg1 = arg1.substring(1);
					}
					else 
						fbk.addLine("li $v1 " + arg1);
					v1 = true;
				}

				arg0 = v0 ? "$v0" : arg0;
				arg1 = v1 ? "$v1" : arg1;
				fbk.addLine("mul " + c.dest.toString() + " " + arg0 + " " + arg1);

			break;

			case "Eq":
				// v0 and v1 are true if they are used to store immediates.
				v0 = false;
				v1 = false;
				arg0 = c.args[0].toString();
				arg1 = c.args[1].toString();

				if (arg0.charAt(0) != '$') {
					if (arg0.charAt(0) == ':') {
						fbk.addLine("la $v0 " + arg0.substring(1));
						arg0 = arg0.substring(1);
					}
					else 
						fbk.addLine("li $v0 " + arg0);
					v0 = true;
				}
				if (arg1.charAt(0) != '$') {
					if (arg1.charAt(0) == ':') {
						fbk.addLine("la $v1 " + arg1.substring(1));
						arg1 = arg1.substring(1);
					}
					else 
						fbk.addLine("li $v1 " + arg1);
					v1 = true;
				}

				arg0 = v0 ? "$v0" : arg0;
				arg1 = v1 ? "$v1" : arg1;
				fbk.addLine("seq " + c.dest.toString() + " " + arg0 + " " + arg1);

			break;

			case "Lt": 
				// v0 and v1 are true if they are used to store immediates.
				v0 = false;
				v1 = false;
				arg0 = c.args[0].toString();
				arg1 = c.args[1].toString();

				if (arg0.charAt(0) != '$') {
					if (arg0.charAt(0) == ':') {
						fbk.addLine("la $v0 " + arg0.substring(1));
						arg0 = arg0.substring(1);
					}
					else 
						fbk.addLine("li $v0 " + arg0);
					v0 = true;
				}
				if (arg1.charAt(0) != '$') {
					if (arg1.charAt(0) == ':') {
						fbk.addLine("la $v1 " + arg1.substring(1));
						arg1 = arg1.substring(1);
					}
					else 
						fbk.addLine("li $v1 " + arg1);
					v1 = true;
				}

				arg0 = v0 ? "$v0" : arg0;
				arg1 = v1 ? "$v1" : arg1;
				fbk.addLine("sltu " + c.dest.toString() + " " + arg0 + " " + arg1);

			break;

			case "LtS": 
				// v0 and v1 are true if they are used to store immediates.
				v0 = false;
				v1 = false;
				arg0 = c.args[0].toString();
				arg1 = c.args[1].toString();

				if (arg0.charAt(0) != '$') {
					if (arg0.charAt(0) == ':') {
						fbk.addLine("la $v0 " + arg0.substring(1));
						arg0 = arg0.substring(1);
					}
					else 
						fbk.addLine("li $v0 " + arg0);
					v0 = true;
				}
				if (arg1.charAt(0) != '$') {
					if (arg1.charAt(0) == ':') {
						fbk.addLine("la $v1 " + arg1.substring(1));
						arg1 = arg1.substring(1);
					}
					else 
						fbk.addLine("li $v1 " + arg1);
					v1 = true;
				}

				arg0 = v0 ? "$v0" : arg0;
				arg1 = v1 ? "$v1" : arg1;
				fbk.addLine("slt " + c.dest.toString() + " " + arg0 + " " + arg1);

			break;

			case "PrintIntS":
				arg0 = c.args[0].toString();
				if (arg0.charAt(0) == '$')
					fbk.addLine("move $a0 " + arg0);
				else if (arg0.charAt(0) == ':')
					fbk.addLine("la $a0 " + arg0.substring(1));
				else
					fbk.addLine("li $a0 " + arg0);
				fbk.addLine("jal _print");
			break;

			case "HeapAllocZ":
				arg0 = c.args[0].toString();
				if (arg0.charAt(0) == '$')
					fbk.addLine("move $a0 " + arg0);
				else
					fbk.addLine("li $a0 " + arg0);
				fbk.addLine("jal _heapAlloc");
				fbk.addLine("move " + c.dest.toString() + " $v0");
			break;

			case "Error":
				fbk.addLine("la $a0 _str0");
				fbk.addLine("j _error");
			break;

			default: 
				System.out.println("UNUSED BUILTIN FUNCTION: " + builtintype);
		}
	}

	public void visit(VCall c) throws Throwable {
		String s = c.addr.toString();
		if (s.charAt(0) == ':')
			fbk.addLine("jal " + s.substring(1));
		else
			fbk.addLine("jalr " + s);
	}

	public void visit(VGoto g) throws Throwable {
		if (g.target.toString().charAt(0) == ':')
			fbk.addLine("j " + g.target.toString().substring(1));
		else if (g.target.toString().charAt(0) == '$')
			fbk.addLine("jr " + g.target.toString());
		else
			fbk.addLine("j " + g.target.toString());
	}

	public void visit(VMemRead r) throws Throwable {
		// Get the correct memory address and offset, then lw to the dest.
		String memaddress = genMemAddress(r.source);
		fbk.addLine("lw " + r.dest.toString() + " " + memaddress);
	}

	public void visit(VMemWrite w) throws Throwable {
		String memaddress = genMemAddress(w.dest);

		// If the source is a label, then we need to load the address into $t9.
		String swreg = w.source.toString(); 
		if (swreg.charAt(0) == ':') {
			fbk.addLine("la $t9 " + swreg.substring(1));
			swreg = "$t9";
		} 
		else if (isStringNum(swreg)) {
			fbk.addLine("li $t9 " + swreg);
			swreg = "$t9";
		}

		// Then we can store the word at the memory address.
		fbk.addLine("sw " + swreg + " " + memaddress);
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
			switch (VMemRef.Stack.Region.valueOf(dest.region.toString())) {
				case In:
					offset = 4*dest.index;
					fp = true;
					break;
				case Out:
					offset = 4*dest.index;
					fp = false;
					break;
				case Local:
					offset = -8 + -4*(dest.index + 1);
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

			// Strange case where the memory address is a VMT or data segment.
			if (dest.base.toString().charAt(0) != '$') {
				int substr = (dest.base.toString().charAt(0) == ':') ? 1 : 0;
				fbk.addLine("la $t9 " + dest.base.toString().substring(substr));
				return dest.byteOffset + "($t9)";
			}
			String retstr = dest.byteOffset + "(" + dest.base.toString() + ")";
			return retstr;
		}
	}

	public boolean isStringNum(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (!Character.isDigit(s.charAt(i)))
				return false;
		}

		return true;
	}

}