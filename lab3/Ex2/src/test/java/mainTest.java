import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.*;

class mainTest {
    WebDriver browser ;




    @BeforeEach
    void setUp() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\bin\\chromedriver.exe");
        browser = new ChromeDriver();
    }

    @AfterEach
    void tearDown() {
        browser.close();
    }

    @DisplayName("Main Test")
    @Test
    void main() {



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
    void pick_flight() {
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
    void purchase_flight() {
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