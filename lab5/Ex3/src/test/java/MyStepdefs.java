import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.AfterAll;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MyStepdefs {
    private ChromeDriver webDriver;


    @And("I press {string}")
    public void iPressFindFlight(String value) {
        webDriver.findElement(By.xpath("//input[@value='"+value+"']")).click();
    }

    @Given("I am at {string}")
    public void iAmAt(String url) {
        webDriver = new ChromeDriver();
        webDriver.get(url);
    }

    @And("Choose {string} to {string}")
    public void chooseParisToLondon(String from, String to) {
        webDriver.findElement(By.name("fromPort")).click();
        {
            WebElement dropdown = webDriver.findElement(By.name("fromPort"));
            dropdown.findElement(By.xpath("//option[. = '"+from+"']")).click();
        }
        webDriver.findElement(By.name("toPort")).click();
        {
            WebElement dropdown = webDriver.findElement(By.name("toPort"));
            dropdown.findElement(By.xpath("//option[. = '"+to+"']")).click();
        }
    }

    @And("I check {string}")
    public void iCheckRememberMe(String id) {
        webDriver.findElement(By.id(id)).click();
    }

    @And("I complete {string} {string}")
    public void iCompleteCreditCardMonth(String id, String value) {
        webDriver.findElement(By.id(id)).sendKeys(String.valueOf(value));
    }

    @And("I Select Option {int}")
    public void iSelectOption(int arg0) {
        webDriver.findElement(By.cssSelector("tr:nth-child("+arg0+") .btn")).click();
    }


    @Then("The Page Title Should Be {string}")
    public void thePageTitleShouldBeConfirmation(String value) {
        new WebDriverWait(webDriver,10L).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().contains(value.toLowerCase());
            }
        });
    }
    @AfterAll()
    public void closeBrowser() {
        webDriver.quit();
    }
}
