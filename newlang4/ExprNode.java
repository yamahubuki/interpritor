package newlang4;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
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

	public static boolean isMatch(LexicalType type){
		return FIRST.contains(type);
	}

	private ExprNode(Environment in){
		env=in;
		type=NodeType.EXPR;
	}

	public ExprNode(Node l,Node r,LexicalType o){
		left=l;
		right=r;
		operator=o;
	}

	public static Node getHandler(Environment in){
		return new ExprNode(in);
	}

	public void parse() throws Exception {
		List<Node> result=new ArrayList<>();
		List<LexicalType> operators = new ArrayList<>();

		add: while(true){
			switch(env.getInput().peek(1).getType()){
				case LP:
					env.getInput().get();
					Node h=ExprNode.getHandler(env);
					h.parse();
					result.add(h);
					if (env.getInput().get().getType()!=LexicalType.RP){
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
						env.getInput().unget(new LexicalUnit(LexicalType.MUL));
						break;
/*
						env.getInput().get();
						result.add(ConstNode.getHandler(env,
						new ExprNode(ConstNode.getHandler(env,env.getInput().get().getValue()),
						ConstNode.getHandler(env,new ValueImpl(-1)),LexicalType.MUL).getValue()));
*/
					} else {
						throw new SyntaxException("計算式中において不正な－記号が使われています。");
					}
				case NAME:
					if (env.getInput().peek(2).getType()==LexicalType.LP){
						Node tmpNode=CallNode.getHandler(env);
						tmpNode.parse();
						result.add(tmpNode);
					} else {
						result.add(VariableNode.getHandler(env,env.getInput().get().getValue()));
					}
					break;
				default:
					throw new SyntaxException("計算式の構成が不正です。");
			}

			switch(env.getInput().peek(1).getType()){
				case ADD:
					for(int i=operators.size()-1;i>=0;i--){
						boolean flg=false;
						if (operators.get(i)==LexicalType.MUL ||
							operators.get(i)==LexicalType.DIV ||
							operators.get(i)==LexicalType.SUB){
							flg=true;
							result.add(new ExprNode(result.get(result.size()-2),result.get(result.size()-1),operators.get(i)));
							result.remove(result.size()-3);
							result.remove(result.size()-2);
							operators.remove(i);
						} else if (flg=true && operators.get(i)==LexicalType.ADD){
							break;
						}
					}
					operators.add(env.getInput().get().getType());
					break;
				case SUB:
					for(int i=operators.size()-1;i>=0;i--){
						boolean flg=false;
						if (operators.get(i)==LexicalType.MUL ||
						operators.get(i)==LexicalType.DIV ){
							flg=true;
							result.add(new ExprNode(result.get(result.size()-2),result.get(result.size()-1),operators.get(i)));
							result.remove(result.size()-3);
							result.remove(result.size()-2);
							operators.remove(i);
						} else if (flg=true &&
						(operators.get(i)==LexicalType.ADD ||
						operators.get(i)==LexicalType.SUB)){
							break;
						}
					}
					operators.add(env.getInput().get().getType());
					break;
				case MUL:
					for(int i=operators.size()-1;i>=0;i--){
						boolean flg=false;
						if (operators.get(i)==LexicalType.DIV){
							flg=true;
							result.add(new ExprNode(result.get(result.size()-2),result.get(result.size()-1),LexicalType.DIV));
							result.remove(result.size()-3);
							result.remove(result.size()-2);
							operators.remove(i);
						} else if (flg=true &&
						(operators.get(i)==LexicalType.MUL ||
						operators.get(i)==LexicalType.ADD ||
						operators.get(i)==LexicalType.SUB)){
							break;
						}
					}
					operators.add(env.getInput().get().getType());
					break;
				case DIV:
					operators.add(env.getInput().get().getType());
					break;
				default:
					break add;
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

	public Value getValue() throws Exception{
		if (operator==null){
			return left.getValue();
		}
		Value val1=left.getValue();
		Value val2=right.getValue();
		if (val1==null || val2==null){
			throw new CalcurateException("nullに対して演算を試みました。");
		}
		if (val1.getType()==ValueType.STRING || val2.getType()==ValueType.STRING){
			if (operator==LexicalType.ADD){
				return new ValueImpl(val1.getSValue()+val2.getSValue());
			} else {
				throw new CalcurateException("文字列に対して減算・乗算・除算を行う事はできません。");
			}
		} else if(val1.getType()==ValueType.DOUBLE || val2.getType()==ValueType.DOUBLE){
			if (operator==LexicalType.ADD){
				return new ValueImpl(val1.getDValue()+val2.getDValue());
			} else if (operator==LexicalType.SUB){
				return new ValueImpl(val1.getDValue()-val2.getDValue());
			} else if (operator==LexicalType.MUL){
				return new ValueImpl(val1.getDValue()*val2.getDValue());
			} else if (operator==LexicalType.DIV){
				if (val2.getDValue()!=0.00){
					return new ValueImpl(val1.getDValue()/val2.getDValue());
				} else {
					throw new CalcurateException("0で除算しました。");
				}
			} else {
				throw new InternalError("不正な演算子が指定されています。");
			}
		} else {
			if (operator==LexicalType.ADD){
				return new ValueImpl(val1.getIValue()+val2.getIValue());
			} else if (operator==LexicalType.SUB){
				return new ValueImpl(val1.getIValue()-val2.getIValue());
			} else if (operator==LexicalType.MUL){
				return new ValueImpl(val1.getIValue()*val2.getIValue());
			} else if (operator==LexicalType.DIV){
				if (val2.getIValue()!=0){
					return new ValueImpl(val1.getIValue()/val2.getIValue());
				} else {
					throw new CalcurateException("0で除算しました。");
				}
			} else {
				throw new InternalError("不正な演算子が指定されています。");
			}
		}
	}

	public String toString() {
		String tmp="[";
		tmp+=left.toString();
		if (operator!=null){
			tmp+=" "+operator.toString()+" ";
			tmp+=right.toString();
		}
		return tmp+"]";
	}
}
