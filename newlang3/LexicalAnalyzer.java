package newlang3;

import java.io.IOException;


public interface LexicalAnalyzer {
	public LexicalUnit check(int i) throws Exception;
    public LexicalUnit get() throws Exception;
    public boolean expect(LexicalType type) throws Exception;
//    public void unget(LexicalUnit token) throws Exception;    
	public int getLine();
}
