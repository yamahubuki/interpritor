package newlang3;

public class ValueImpl implements Value {

	int iValue=0;
	double dValue=0.0;
	String SValue="";
	boolean bValue=false;
	ValueType vType;

	public ValueImpl(String s){
		vType=ValueType.STRING;
		SValue=s;
		try {
			iValue=Integer.parseInt(s);
		} catch (Exception e){}
		try {
			dValue=Double.parseDouble(s);
		} catch (Exception e){}
		if (s!=""){
			bValue=true;
		}
	}

	public ValueImpl(int i){
		vType=ValueType.INTEGER;
		SValue=String.valueOf(i);
		iValue=i;
		dValue=(double)i;
		if (i!=0){
			bValue=true;
		}
	}

	public ValueImpl(double d){
		vType=ValueType.DOUBLE;
		SValue=String.valueOf(d);
		dValue=d;
		iValue=(int)d;
		if (d!=0.00){
			bValue=true;
		}
	}

	public ValueImpl(boolean b){
		vType=ValueType.BOOL;
		SValue=String.valueOf(b);
		if (b==true){
			bValue=true;
			iValue=1;
			dValue=1.00;
		}
	}

	public String getSValue(){
		return SValue;
	}

	public int getIValue(){
		return iValue;
	}

	public double getDValue(){
		return dValue;
	}

	public boolean getBValue(){
		return bValue;
	}

	public ValueType getType(){
		return vType;
	}

	public String toString(){
		return getSValue();
	}
}
