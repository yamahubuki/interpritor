package newlang4;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import newlang3.*;

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

	private ExprNode(Node l,Node r,LexicalType o){
		left=l;
		right=r;
		operator=o;
	}

	public static Node getHandrar(Environment in){
		return new ExprNode(in);
	}

	public void parse() throws Exception {
		List<Node> result=new ArrayList<>();
		List<LexicalType> operators = new ArrayList<>();

		add: while(true){
			switch(env.getInput().peek(1).getType()){
				case LP:
					env.getInput().get();
					Node h=ExprNode.getHandrar(env);
					h.parse();
					result.add(h);
					if (env.getInput().get().getType()!=LexicalType.RP){
						throw new SyntaxException("計算式の構成が不正です。)が見つかりません。"+env.getInput().getLine()+"行目");
					}
					break;
				case INTVAL:
				case DOUBLEVAL:
				case LITERAL:
					result.add(ConstNode.getHandrar(env,env.getInput().get().getValue()));
					break;
				case NAME:
					if (env.getInput().peek(2).getType()==LexicalType.LP){
						Node tmpNode=CallNode.getHandrar(env);
						tmpNode.parse();
						result.add(tmpNode);
					} else {
						result.add(VariableNode.getHandrar(env,env.getInput().get().getValue()));
					}
					break;
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
