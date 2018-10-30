package newlang3;

public class ValueImpl {

	int iValue=0;
	double dValue=0.0;
	String SValue="";
	boolean bValue=false;
	ValueType vType;


//  実装すべきコンストラクタ
	public ValueImpl(String s){
		vType=ValueType.STRING;
		SValue=s;
		try {
			iValue=Integer.parseInt(s);
		} catch (Exception e){
			iValue=0;
		}
	}
//	public Value(int i);
//	public Value(double d);
//	public Value(boolean b);
//	public String get_sValue();

	public String getSValue(){
		return SValue;
	}
	// ストリング型で値を取り出す。必要があれば、型変換を行う。
    public int getIValue(){
		return 0;
	}
   	// 整数型で値を取り出す。必要があれば、型変換を行う。
    public double getDValue(){
		return 0.00;
	}
    // 小数点型で値を取り出す。必要があれば、型変換を行う。
    public boolean getBValue(){
		return false;
	}
    // 論理型で値を取り出す。必要があれば、型変換を行う。
    public ValueType getType(){
		return vType;
	}
}



//valueOfを使う
