import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

@ExtendWith(SeleniumJupiter.class)
class mainTest {


    @Test
    void testWithOneChrome(ChromeDriver driver) {
        driver.get("https://bonigarcia.github.io/selenium-jupiter/");
        assertThat(driver.getTitle(),
                containsString("JUnit 5 extension for Selenium"));
    }


    @Test
    void testHeadlessBrowser(HtmlUnitDriver driver) {
        driver.get("https://bonigarcia.github.io/selenium-jupiter/");
        assertThat(driver.getTitle(),
                containsString("JUnit 5 extension for Selenium"));
    }




    @DisplayName("Main Test")
    @Test
    void main(ChromeDriver browser) {
        browser.get("https://blazedemo.com");
        WebElement href = null;
        boolean staleElement = true;
        while(staleElement){
            try{
                href =browser.findElement(By.className("btn-primary"));
                staleElement = false;

            } catch(StaleElementReferenceException e){
                staleElement = true;
            }
        }

        href.click();
    }

    @DisplayName("Main Test")
    @Test
    void pick_flight(ChromeDriver browser) {
        browser.get("https://blazedemo.com/reserve.php");

        WebElement href = null;
        boolean staleElement = true;
        while(staleElement){
            try{
                href = browser.findElement(By.className("btn-small"));
                staleElement = false;

            } catch(StaleElementReferenceException e){
                staleElement = true;
            }
        }

        href.click();
    }

    @DisplayName("Main Test")
    @Test
    void purchase_flight(ChromeDriver browser) {
        browser.get("https://blazedemo.com/purchase.php");
        WebElement href = null;
        boolean staleElement = true;
        while(staleElement){
            try{
                href =  browser.findElement(By.className("btn-primary"));
                staleElement = false;

            } catch(StaleElementReferenceException e){
                staleElement = true;
            }
        }
        href.click();
    }
}