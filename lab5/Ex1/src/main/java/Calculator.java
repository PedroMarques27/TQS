public class Calculator {
    int value =0;
    public Calculator(){}
    public void calculate(int a, int b, String op){
        if (op.equals("+")) {
            value = a + b;
        }
        else if (op.equals("*")) {
            value =  a *  b;
        }
        else if (op.equals("-")) {
            value =  a -  b;
        }else{
            value =  a /  b;
        }
    }
    public int getResult(){
        return value;
    }
}
