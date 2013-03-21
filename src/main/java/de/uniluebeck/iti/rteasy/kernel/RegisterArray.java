package de.uniluebeck.iti.rteasy.kernel;
import java.io.PrintWriter;
import java.util.BitSet;

import de.uniluebeck.iti.rteasy.RTSimGlobals;
import de.uniluebeck.iti.rteasy.frontend.ASTRegArrayDecl;
import de.uniluebeck.iti.rteasy.frontend.ASTRegBusDecl;
import de.uniluebeck.iti.rteasy.frontend.ParseException;

public class RegisterArray extends RegBus{

	private Register[] content;
	private String name;
	private Register ref;
	private int width;
	private int length;
	private BitRange br;
	private int registerNumber;
	//Gibt die Position des Zeigers an
	
	public RegisterArray(ASTRegArrayDecl radecl){
		super(radecl.getName(), radecl.getPositionRange(), radecl.getWidth(),
				radecl.getOffset(), radecl.getDirection());
		this.br = radecl.getBitRange();
		this.name = radecl.getName();
		this.width = br.getWidth();
		this.length = radecl.getNumberOfRegisters();
		content = new Register[length];
		for (int i = 0; i<length; i++) {
			ASTRegBusDecl decl = new ASTRegBusDecl(0);
			decl.setWidth(width);
			try {
				decl.setBitRange(br);
			} catch (ParseException e) {
			}
			content[i] = new Register(decl);
		}
	}
	
	public void setRegisterNumber(int r)
	{
		registerNumber = r;
	}
	
	public int getRegisterNumber()
	{
		return registerNumber;
	}
	
	public int getLength() {
		return content.length;
	}
	
	public BitRange getBitRange() {
		return br;
	}
	
	public int getWidth() {
		return width;
	}
	
	public String getPrettyDecl() {
		return name;
	}
	
	public String getFullDecl() {
		if (width>1){
		return (name + "("+br.begin+":"+br.end+")"+"["
				+ref.getIdStr()+"]");
		} else {
			return (name + "["+ref.getIdStr()+"]");
		}
	}
	
	public Register getRegister() {
		return content[registerNumber];
	}
	
	public Register getRegister(int pos) {
		return content[pos];
	}
	
	public BitVector getContent() {
		if (getPosition(ref) < this.getWidth()) {
		return content[getPosition(ref)].getContent();
		}
		else return BitVector.BV_FALSE;
	}
	
	public void setContent(BitVector bv) {
		content[registerNumber].setContent(bv);
	}
	
	public void setContent(BitVector bv, int pos) {
		content[pos].setContent(bv);
	}
	
	public void editContent(BitVector bv) {
		content[registerNumber].editContent(bv);
	}
	
	public void editContent(BitVector bv, int pos) {
		content[pos].editContent(bv);
	}
	
//	public Register getReference() {
//		if (ref!= null)	return ref;
//		else return null;
//	}

	public int getPosition(Register r) {
		String s = RTSimGlobals.boolArray2String(r.getBoolArrayOld(),
				RTSimGlobals.BASE_DEC);
		int pos =(Integer.parseInt(s));
		return pos;
	}
	
	public void update(Register r) {
		this.ref = r;
	}
	
	public boolean checkBitRange(BitRange br){
		return true;
//		if(ref.direction)
//		      return br.begin >= br.end && br.begin <= (ref.offset+width-1)
//		          && br.end >= ref.offset;
//		    else
//		      return br.begin <= br.end && br.begin >= ref.offset
//		         && br.end <= (ref.offset+width-1);
	}
	
	public boolean set(int i, boolean b) {
		boolean bool = content[registerNumber].set(i, b);
		content[registerNumber].commit();
		return bool;
	}
	
	public boolean get(int edgeType, int i) {
		return content[registerNumber].get(edgeType, i);
	}
	
	public String getVHDLName() {
		return "regArr_"+getIdStr();
	}

	public BitSet getContentBitSet() {
		return ref.getContentBitSet();
	}

	public void setContent(BitSet bs) {
		ref.setContent(bs);
	}

	public void editContent(BitSet bs) {
		ref.editContent(bs);
	}

	public boolean get(int i) {
		return ref.get(i);
	}

	public void clear() {
		for(int i=0;i<length;i++){
			content[i].clear();
		}
	}

	public boolean written() {
		return ref.written();
	}

	public String getContentStr(int base) {
		return ref.getContentStr(base);
	}

	public void emitVHDLSignalDeclarations(String string, PrintWriter out) {
			content[1].emitVHDLSignalDeclarationsArray(string, out, name);
	}
	
	public void emitVHDLInstantiation(String indent, PrintWriter out) {
	    out.println(indent+"-- component instantiation for register "+getIdStr());
	    out.println(indent+getVHDLName()+": dff_reg");
	    out.println(indent+"  GENERIC MAP(triggering_edge => '1', width => "+getWidth()+")");
	    out.println(indent+"  PORT MAP(CLK => CLK_SIG, RESET => RESET_SIG,");
	    out.println(indent+"           INPUT => "+getVHDLName()+"_in,");
	    out.println(indent+"           OUTPUT => "+getVHDLName()+"_out);");
	  }

	public String getSingleRegDecl() {
		String out = "";
		String dec = "declare register ";
		for(int i=0; i<length;i++){
			out=out+dec;
			out=out+name+"_"+i;
			out=out+content[i].getWidthDecl();
			out=out+"\n";
		}
		return out;
	}
}
