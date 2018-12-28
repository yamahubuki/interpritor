package newlang4;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import newlang3.*;
import newlang5.*;

public class ExprNode extends Node {

	Node left=null;
	Node right=null;
	LexicalType operator=null;

	//自分のfirstをセットでもっておく
	private final static Set<LexicalType> FIRST=new HashSet<LexicalType>(Arrays.asList(
		LexicalType.NAME,
		LexicalType.SUB,
		LexicalType.LP,
		LexicalType.INTVAL,
		LexicalType.DOUBLEVAL,
		LexicalType.LITERAL
	));

	//演算子とその優先順位
	private static final Map<LexicalType,Integer> OPERATORS=new HashMap<>();
	static {
		OPERATORS.put(LexicalType.DIV,1);
		OPERATORS.put(LexicalType.MUL,2);
		OPERATORS.put(LexicalType.SUB,3);
		OPERATORS.put(LexicalType.ADD,4);
	}

	public static boolean isMatch(LexicalType type){
		return FIRST.contains(type);
	}

	private ExprNode(Environment in){
		env=in;
		type=NodeType.EXPR;
	}

	public static Node getHandler(Environment in){
		return new ExprNode(in);
	}

	public ExprNode(Node l,Node r,LexicalType o){
		type=NodeType.EXPR;
		left=l;
		right=r;
		operator=o;
	}

	public void parse() throws Exception {
		List<Node> result=new ArrayList<>();
		List<LexicalType> operators = new ArrayList<>();

		while(true){
			switch(env.getInput().peek(1).getType()){
				case LP:
					env.getInput().get();
					Node h=ExprNode.getHandler(env);
					h.parse();
					result.add(h);
					if (env.getInput().expect(LexicalType.RP)){
						env.getInput().get();
					} else {
						throw new SyntaxException("計算式の構成が不正です。)が見つかりません。"+env.getInput().getLine()+"行目");
					}
					break;
				case INTVAL:
				case DOUBLEVAL:
				case LITERAL:
					result.add(ConstNode.getHandler(env,env.getInput().get().getValue()));
					break;
				case SUB:
					if ((env.getInput().peek(2).getType()==LexicalType.INTVAL) || 
					(env.getInput().peek(2).getType()==LexicalType.DOUBLEVAL) || 
					(env.getInput().peek(2).getType()==LexicalType.LP) ){
						env.getInput().get();
						result.add(ConstNode.getHandler(env,new ValueImpl(-1)));
						addOperator(result,operators,LexicalType.MUL);
						continue;
					} else {
						throw new SyntaxException("計算式中において不正な－記号が使われています。");
					}
				case NAME:
					if (env.getInput().peek(2).getType()==LexicalType.LP){
						Node tmpNode=CallNode.getHandler(env);
						tmpNode.parse();
						result.add(tmpNode);
					} else {
						result.add(env.getVariable(env.getInput().get().getValue().getSValue()));
					}
					break;
				default:
					throw new SyntaxException("計算式の構成が不正です。");
			}

			if (OPERATORS.containsKey(env.getInput().peek(1).getType())){
				addOperator(result,operators,env.getInput().get().getType());
			} else {
				break;
			}
		}
		for(int i=operators.size()-1;i>=0;i--){
			if (operators.size()==1){
				left=result.get(0);
				right=result.get(1);
				operator=operators.get(0);
				return;
			}
			result.add(new ExprNode(result.get(result.size()-2),result.get(result.size()-1),operators.get(i)));
			result.remove(result.size()-3);
			result.remove(result.size()-2);
		} 
		left=result.get(0);
	}

	private void addOperator(List<Node> rList,List<LexicalType> oList,LexicalType newOperator) throws Exception{
		boolean flg=false;
		for(int i=oList.size()-1;i>=0;i--){
			if (OPERATORS.get(oList.get(i))<OPERATORS.get(newOperator)){
				flg=true;
				rList.add(new ExprNode(rList.get(rList.size()-2),rList.get(rList.size()-1),oList.get(i)));
				rList.remove(rList.size()-3);
				rList.remove(rList.size()-2);
				oList.remove(i);
			} else if (flg=true && OPERATORS.get(oList.get(i))>=OPERATORS.get(newOperator)){
				break;
			}
		}
		oList.add(newOperator);
	}

	public Value getValue() throws Exception{
		if (operator==null){
			return left.getValue();
		}
		Value val1=left.getValue();
		Value val2=right.getValue();
		if (val1==null || val2==null){
			throw new CalcurateException("nullに対して演算を試みました。");
		} else if (val1.getType()==ValueType.STRING || val2.getType()==ValueType.STRING){
			if (operator==LexicalType.ADD){
				return new ValueImpl(val1.getSValue()+val2.getSValue());
			} else {
				throw new CalcurateException("文字列に対して減算・乗算・除算を行う事はできません。");
			}
		}
		double result;
		if (operator==LexicalType.ADD){
			result=val1.getDValue()+val2.getDValue();
		} else if (operator==LexicalType.SUB){
			result=val1.getDValue()-val2.getDValue();
		} else if (operator==LexicalType.MUL){
			result=val1.getDValue()*val2.getDValue();
		} else if (operator==LexicalType.DIV){
			if (val2.getDValue()!=0.00){
				result=val1.getDValue()/val2.getDValue();
			} else {
				throw new CalcurateException("0で除算しました。");
			}
		} else {
			throw new InternalError("不正な演算子が指定されています。");
		}
		if(val1.getType()==ValueType.DOUBLE || val2.getType()==ValueType.DOUBLE){
			return new ValueImpl(result);
		} else {
			return new ValueImpl((int)result);
		}
	}

	public String toString() {
		String tmp="["+left.toString();
		if (operator!=null){
			tmp+=" "+operator.toString()+" "+right.toString();
		}
		return tmp+"]";
	}
}
