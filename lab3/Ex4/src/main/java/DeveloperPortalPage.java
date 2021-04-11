
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

public class DeveloperPortalPage {
    private WebDriver driver;

    @FindBy(xpath = "/html/head/title")
    private WebElement heading;

    @FindBy(className = "freelancer_link")
    private WebElement joinToptalButton;

    //Constructor
    public DeveloperPortalPage (WebDriver driver){
        this.driver=driver;

        //Initialise Elements
        PageFactory.initElements(driver, this);
    }

    //We will use this boolean for assertion. To check if page is opened
    public boolean isPageOpened(){
        return heading.getText().toString().contains("Join as a Client | Toptal");
    }

    public void clikOnJoin(){
        joinToptalButton.click();
    }
}