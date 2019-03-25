
public class FunctionCalculatorRunner {
	public static void main(String[] args) {
        //"(1+(1+1))"
        //"(1+(1+(1+1)))"
        //"2*(1+(1+1))"
        //"4*(1+(2+3))"
        //"4*(1+0+(2+3))"
        //"2*(2+3)*2+2"
        //"((1+3*2^(1+1*(1+1-1))))/(1+1-1)" //=13
        //"sin(-1+(2+3*2)*x^(-1+1+1))"  // x=7 // -0.9997551733586199
        FunctionCalculator func = new FunctionCalculator("s In(     -1+(2+3*2)*X^(-1+1+1))",7.0);
        System.out.println("RESULT is "+func.Calculator());
        System.out.println("Braces check:"+func.CheckBraces());

	}
}