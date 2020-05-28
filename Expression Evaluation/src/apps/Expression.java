package apps;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	/**
	 * Expression to be evaluated
	 */
	String expr;

	/**
	 * Scalar symbols in the expression
	 */
	ArrayList<ScalarSymbol> scalars;

	/**
	 * Array symbols in the expression
	 */
	ArrayList<ArraySymbol> arrays;

	/**
	 * String containing all delimiters (characters other than variables and
	 * constants), to be used with StringTokenizer
	 */
	public static final String delims = " \t*+-/()[]";

	/**
	 * Initializes this Expression object with an input expression. Sets all
	 * other fields to null.
	 * 
	 * @param expr
	 *            Expression
	 */
	public Expression(String expr) {
		this.expr = expr;
	}

	/**
	 * Populates the scalars and arrays lists with symbols for scalar and array
	 * variables in the expression. For every variable, a SINGLE symbol is
	 * created and stored, even if it appears more than once in the expression.
	 * At this time, values for all variables are set to zero - they will be
	 * loaded from a file in the loadSymbolValues method.
	 */
	public void buildSymbols() {
		
		scalars = new ArrayList<ScalarSymbol>();
		arrays = new ArrayList<ArraySymbol>();
	for(int i=0; i<expr.length(); i++){
    if(Character.isLetter(expr.charAt(i))){
    	 String currentStr="";
    	 boolean isArray=false;
	 while(i<expr.length() && Character.isLetter(expr.charAt(i))){
		currentStr=currentStr+expr.charAt(i); 
		i++;
	 }
	 if (i<expr.length() && expr.charAt(i)=='['){
		 isArray=true;
	 }
	 
	 if(isArray){
	ArraySymbol ArraySym=new ArraySymbol(currentStr);
//    ArraySym.values=null;  
    arrays.add(ArraySym); 
	}
	 else {
	ScalarSymbol scalarSym=new ScalarSymbol(currentStr);
//	scalarSym.value=0;
	scalars.add(scalarSym);	 
	}
	 }

	}
		
		
	}
	/**
	 * Loads values for symbols in the expression look up a file to load value
	 * 
	 * @param sc Scanner for values input
	 * @throws IOException if there is a problem with the input
	 */
	public void loadSymbolValues(Scanner sc) throws IOException {
		while (sc.hasNextLine()) {
			StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
			int numTokens = st.countTokens();
			// number of token
			String sym = st.nextToken();
			// read values from scalars and arrays into the following objects
			ScalarSymbol ssymbol = new ScalarSymbol(sym);
			ArraySymbol asymbol = new ArraySymbol(sym);
			int ssi = scalars.indexOf(ssymbol);
			int asi = arrays.indexOf(asymbol);
			if (ssi == -1 && asi == -1) {
				continue;
			}
			int num = Integer.parseInt(st.nextToken());
			if (numTokens == 2) { // scalar symbol
				scalars.get(ssi).value = num;
			} else { // array symbol
				asymbol = arrays.get(asi);
				asymbol.values = new int[num];
				// following are (index,val) pairs
				while (st.hasMoreTokens()) {
					String tok = st.nextToken();
					StringTokenizer stt = new StringTokenizer(tok, " (,)");
					int index = Integer.parseInt(stt.nextToken());
					int val = Integer.parseInt(stt.nextToken());
					asymbol.values[index] = val;
				}
			}
		}
	}

	/**
	 * Evaluates the expression, using RECURSION to evaluate subexpressions and
	 * to evaluate array subscript(index) expressions. May need a separate
	 * private recursive method to recurse on a subexpression.
	 * 
	 * @return Result of evaluation
	 */
	public float evaluate() {
		// Get rid of White space and Tab if occur.

	
	return evaluate(expr, expr.length()-1);
		
	}

	private float evaluate(String expression, int endIndex) {
    float rvalue=0;
	Stack<String> operator=new Stack<String>(); 
	Stack<Float>values=new Stack<Float>();
	StringTokenizer st=new StringTokenizer(expression,delims,true);
	String symbolName="";
	               System.out.println("evaluate method recursed" + "Expression " + expression);
	              
	               
	while(st.hasMoreTokens()){  
	String token=st.nextToken();  System.out.println("token :" + token);
	int startIndex=0; 
	
	if(token.equals("(") || token.equals("[")){
	if(token.equals("(")) {
	 startIndex=expression.indexOf("(")+1;            
	 endIndex=findEndIndex(expression,startIndex,'(',')');  
	}
	 
	else if(token.equals("[")){ System.out.print("original startIndex :" + expression.indexOf("["));
	  startIndex=expression.indexOf("[")+1;
	  endIndex=findEndIndex(expression,startIndex,'[',']'); 
	  System.out.println("startIndex, endIndex : " + startIndex+" " +endIndex);
	 }
	 
	 rvalue=evaluate(expression.substring(startIndex,endIndex),endIndex-1);//call to recursion
	
	
	System.out.println("token is " + token + " rvalue is "+ (int)rvalue);
	if(token.equals("(")){
	values.push(rvalue);	
	}
	else if (token.equals("[")){
	ArraySymbol symbol=new ArraySymbol(symbolName);
	symbol= arrays.get(arrays.indexOf(symbol));
	values.push((float)symbol.values[(int)rvalue]);
	}  
//	if (endIndex <= expression.length()) {
//		try {
//			expression = expression.substring(endIndex + 1); // NextToken does not count token in recursion we already did.
//			st = new StringTokenizer(expression, delims, true);
//		} catch (Exception e) {
//			// System.out.println(expression);
//			// e.printStackTrace(); 
//		} 
//	}
	 if(endIndex<expr.length()){  System.out.print("final values is " + values.peek());
		if(endIndex+1<expr.length()){
	expression=expression.substring(endIndex+1); System.out.print("new expression is "+expression+"token is " +token);
	 st=new StringTokenizer(expression,delims,true);
	}
	else {      System.out.print("no more expression "+expression+"token " + token);
	break;
	}
	 }	
	}
	
	
	if((token.charAt(0)>='a' && token.charAt(0)<='z')||(token.charAt(0)>='A' && token.charAt(0)<='Z')){ 
		symbolName=token; System.out.print("token is " + token);
		ScalarSymbol ssymbol = new ScalarSymbol(symbolName); 
	 if(scalars.indexOf(ssymbol)>=0){  
	  int ssi = scalars.indexOf(ssymbol);
	  float scalarValue=(float)(scalars.get(ssi).value);
	  values.push(scalarValue);  
	}
	 
	}
	
	
	else if(token.equals("*") || token.equals("/") || token.equals("-") || token.equals("+")){//operator variable
	 if (!operator.isEmpty()){
	  if((operator.peek()=="*" || operator.peek()=="/") && (token.equals("+") || token.equals("-"))){
		values.push(calculate(operator.pop(),values.pop(),values.pop()));
	  }
	 }
	  operator.push(token); 
	}
	
	else if(token.charAt(0)>='0' && token.charAt(0)<='9'){//scalar variable  
		System.out.print("token is integer "+token);
		values.push(Float.parseFloat(token));
	}
	
	} 
	
	
	while(!operator.isEmpty()){     
     values.push(calculate(operator.pop(),values.pop(),values.pop()));
	} 
	return values.pop();
	
	
	
	
	
		
	}

	/**
	 * Return Last index which is ')', ']'. Pass it for recursion.
	 * 
	 * @param expression
	 * @param start
	 * @param open : '(' or '['         
	 * @param close : ')' or ']'
	 * @return last index for subexpression
	 */
	private int findEndIndex(String expression, int start, char open, char close) {
	int count=0;
	int lastIndex=0;
	for(int i=start; i<expression.length();i++){
	if (expression.charAt(i)==open){
		count++;
	}
	else if (expression.charAt(i)==close && count==0){
		lastIndex=i;
		break;
	}
	else if (expression.charAt(i)==close && count>0){
		count--;
	}
	
	}  
	return lastIndex;
	
	}
		
		


	/**
	 * Addiction, Multiplication, Subdivision, Subtraction
	 * 
	 * @param operation
	 *            such as (+,-,*,/)
	 * @param b,a
	 * @return
	 */
	private float calculate(String oper, float b, float a) {

		float ret = 0;

		switch (oper) { 
		case "/":
			ret = a / b;
			break;
		case "*":
			ret = a * b;
			break;
		case "+":
			ret = a + b;
			break;
		case "-":
			ret = a - b;
			break;
		}

		return ret;
	}

	/**
	 * Utility method, prints the symbols in the scalars list
	 */
	public void printScalars() {
		for (ScalarSymbol ss : scalars) {
			System.out.println(ss);
		}
	}

	/**
	 * Utility method, prints the symbols in the arrays list
	 */
	public void printArrays() {
		for (ArraySymbol as : arrays) {
			System.out.println(as);
		}
	}

}
