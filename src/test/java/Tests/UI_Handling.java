package Tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

public class UI_Handling {


    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeMethod(alwaysRun = true)
    public void Setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        // browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        // browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
//        page.setDefaultTimeout(7000);
        page.navigate("https://rahulshettyacademy.com/AutomationPractice/");
    }


    @Test(groups = "smoke")
    public void Radio_button(){

        Locator radio_button = page.locator("input[value='radio2']");
        radio_button.check();
        Assert.assertTrue(radio_button.isChecked());

    }

    @Test
    public void Drop_down(){

      page.locator("select").selectOption("option3");

    }

    @Test
    public void Checkbox_btn(){

        page.locator("input[value='option1']").check();

    }


    @Test
    public void Popup_validation(){
        page.onDialog(dialog -> dialog.accept());
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Alert")).click();
        page.waitForTimeout(5000);
    }

    @Test
    public void Element_displayed(){

        page.locator("input[value='Hide']").click();
        page.locator("input[value='Show']").click();
        page.getByPlaceholder("Hide/Show Example").fill("Hi sharath");

    }

    @Test
    public void Mouse_hover(){
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Mouse Hover")).hover();

        page.getByRole(AriaRole.LINK,new Page.GetByRoleOptions().setName("Top")).hover();

    }

    @Test
    public void iframes_example(){
        FrameLocator framePage = page.frameLocator("courses-iframe");
        framePage.getByRole(AriaRole.LINK,new FrameLocator.GetByRoleOptions().setName("Learning paths")).click();
        String textcheck = framePage.locator(".inner-box h1").textContent();
        System.out.println(textcheck);


    }


    @Test
    public void ScreenshotTest(){

        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshot.png")));
        Locator displayedEditbox = page.getByPlaceholder("Hide/Show Example");
        displayedEditbox.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get("screenshot1.png")));

    }



}
