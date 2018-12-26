package newlang3;

import java.io.IOException;


public interface LexicalAnalyzer {
	public boolean expect(LexicalType type) throws IOException,SyntaxException;
	public LexicalUnit peek(int i) throws IOException,SyntaxException;
	public LexicalUnit get() throws IOException,SyntaxException;
	public int getLine();
}
