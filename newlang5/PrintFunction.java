package newlang5;

import newlang3.*;
import newlang4.*;

public class PrintFunction extends Function{

    public PrintFunction() {
    }

    public Value invoke(ExprListNode arg) throws Exception {
		System.out.println(arg.get(0).getSValue());
		return null;        
    }
}
