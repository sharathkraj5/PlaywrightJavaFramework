package Tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

public class Window_handling {
    Playwright playwright;
    Browser browser;
    Page page;
    BrowserContext context;

    @BeforeMethod
    public void setUp(){

        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext();

        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
        page = context.newPage();
        page.setDefaultTimeout(5000);
        page.navigate("https://rahulshettyacademy.com/loginpagePractise/");
    }

    @Test
    public void childWindowhandling(){

        Locator blinkingTexts = page.locator(".blinkingText");
      //  Page newPage = context.waitForPage(() -> { blinkingTexts.first().click();});
        page.waitForTimeout(3000);
        Page newPage = context.waitForPage(()-> blinkingTexts.first().click());
        newPage.waitForLoadState();
        String child_text = newPage.locator(".red").textContent();
        String emilID = child_text.split("at")[1].split(" ")[0];
        System.out.println("mailid: " + emilID);

        page.getByLabel("Username:").fill(emilID);



    }

    @Test
    public void UI_controls(){
        // Radio button
        Locator user = page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("user"));
        user.click();
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Okay")).click();
        Assert.assertTrue(user.isChecked());

        //check box
        Locator checkboxTerms= page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("I Agree to the terms and conditions"));
        checkboxTerms.check();
        Assert.assertTrue(checkboxTerms.isChecked());

        // drop down
        page.getByRole(AriaRole.COMBOBOX).selectOption("Teacher");
        page.waitForTimeout(3000);


    }

    @AfterMethod
    void tear_down(){
        context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get("trace.zip")));
    }


}
