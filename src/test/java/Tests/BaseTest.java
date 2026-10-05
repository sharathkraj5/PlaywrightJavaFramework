package Tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.BeforeMethod;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BaseTest {

    Playwright playwright;
    Browser browser;
    Page page;
    String baseURL;

    @BeforeMethod
    public void Setup() throws IOException {

        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
        prop.load(fis);
        String browser_name = System.getProperty("browser")!= null ? System.getProperty("browser") : prop.getProperty("browser");
        //String browser_name = prop.getProperty("browser");
        String env_Name = System.getProperty("env")!= null ? System.getProperty("env") : prop.getProperty("env");

        playwright = Playwright.create();

        if("Firefox".equals(browser_name)){
            browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        } else if ("Safari".equals(browser_name)) {
            browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
        }else {
           // browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            browser = playwright.chromium().launch();
        }

        page = browser.newPage();
        page.setDefaultTimeout(7000);
        baseURL = prop.getProperty(env_Name + ".baseurl");
        page.navigate(baseURL);
    }
}
