public class Calculator {
    Double value = 0.0;
    public Calculator(){}
    public void calculate(Object a, Object b, String op){
        switch (op) {
            case "+" -> value = (Double) a + (Double) b;
            case "-" -> value = (Double) a - (Double) b;
            case "*" -> value = (Double) a * (Double) b;
            case "/" -> value = (Double) a / (Double) b;
        }
    }
    public Double getResult(){
        return value;
    }
}
