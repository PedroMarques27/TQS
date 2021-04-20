import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorStepsDefinition {

    Calculator calculator;
    @Given("^Calculator Initialized")
    public void setup() {
        calculator = new Calculator();
    }


    @When("^Add (\\d+) (\\d+)$")
    public void add(int arg1, int arg2) {
        calculator.calculate(arg1,arg2,"+");
    }
    @When("^Sub (\\d+) (\\d+)$")
    public void sub(int arg1, int arg2) {
        calculator.calculate(arg1,arg2,"-");
    }
    @When("^Multi (\\d+) (\\d+)$")
    public void multiply(int arg1, int arg2) {
        calculator.calculate(arg1,arg2,"*");
    }
    @When("^Div (\\d+) (\\d+)$")
    public void divide(int arg1, int arg2) {
        calculator.calculate(arg1,arg2,"/");
    }

    @Then("^= (\\d+)$")
    public void the_result_is(double expected) {
        assertEquals(expected, calculator.getResult());
    }
}
