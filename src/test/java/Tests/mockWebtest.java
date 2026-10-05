package Tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class mockWebtest {

    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeMethod
    public void Setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        // browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        // browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
        page.setDefaultTimeout(7000);
        page.navigate("https://eventhub.rahulshettyacademy.com/login");


    }

    @Test(description = "Login and create an Events and verify if its booed")
    public void DemoTest() {

        String ti = page.title();
        System.out.print(ti);
        assertThat(page).hasTitle("EventHub — Discover & Book Events");

        // Log IN
        page.getByLabel("Email").fill("sharathkraj5@gmail.com");
        page.getByLabel("Password").fill("Sharath@1710");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();


        assertThat(page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();
        // create an event from admin page

        page.route("**/api/events**", route -> route.fulfill(
                new Route.FulfillOptions().setPath(Paths.get("src/test/resources/events_6.json"))));


        page.navigate("https://eventhub.rahulshettyacademy.com/events");

        Locator eventcards = page.getByTestId("event-card");
        assertThat(eventcards).isVisible();

        Assert.assertEquals(eventcards.count(),"6");
        assertThat(page.locator(".mx-1").first()).isVisible();



    }
}
