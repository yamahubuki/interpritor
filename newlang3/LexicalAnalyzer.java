package newlang3;

import java.io.IOException;


public interface LexicalAnalyzer {
	public LexicalUnit check(int i) throws IOException,SyntaxException;
    public LexicalUnit get() throws Exception,SyntaxException;
	public int getLine();
}
