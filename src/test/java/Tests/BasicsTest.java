package Tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BasicsTest {

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
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Sign In")).click();


        assertThat(page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();

        // create an event from admin page
        page.navigate("https://eventhub.rahulshettyacademy.com/admin/events");
        page.getByLabel("Title").fill("QA meetup- 2027");
        page.locator("#admin-event-form textarea").fill("Qa Engineer meetup freshers and experienced can meet");
        page.getByLabel("Category").selectOption("Workshop");
        page.getByLabel("City").fill("Madurai");
        page.getByLabel("Venue").fill("Raja muthaiah Mandram, madurai");
        page.getByLabel("Event Date & Time").fill("2027-01-29T18:00");
//        page.waitForTimeout(3000);
        page.getByLabel("Price").fill("399");
        page.getByLabel("Total Seats").fill("500");
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("+ Add Event")).click();
//        page.waitForTimeout(3000);
        // Event created
        assertThat(page.getByText("Event created!")).isVisible();

        // Step - 2 -find the newly created event in the event page

        page.locator("[data-testid='nav-events']").click();
        Locator eventCards = page.getByTestId("event-card");
        System.out.println(eventCards.count());
        // visibility of the card which we have added
        Locator targetCard = eventCards.filter(new Locator.FilterOptions().setHasText("QA meetup- 2027"));
        assertThat(targetCard).isVisible();
        String seatsText = targetCard.getByText("seats").innerText();
        // System.out.println(seatsText);
        int seatNumBeforebooking = Integer.parseInt(seatsText.split(" ")[0]);
        System.out.println("Beforebooking :"+ seatNumBeforebooking);


        // book the ticket from event page
        targetCard.locator("[data-testid='book-now-btn']").click();
       //page.locator("[type='button']").click();

        page.getByLabel("Full Name").fill("Poornima devi");
        page.getByLabel("Email").fill("Poornima@temp.com");
        page.getByLabel("Phone Number").fill("3245543654");
        page.locator("[id='confirm-booking']").click();

        // Confirm booking
        assertThat(page.getByText("Your tickets are reserved.")).isVisible();
        String bookingRef = page.locator(".booking-ref").innerText();
        //System.out.println(bookingRef);
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("View My Bookings")).click();

        // verify the booking card
        Locator bookingCards = page.locator("#booking-card");
        //System.out.println(bookingCards.count());
        Locator targetbookingcard = bookingCards.filter(new Locator.FilterOptions().setHasText(bookingRef));
        assertThat(targetbookingcard).isVisible();


        //  verify the Seat count decrease
        page.locator("[data-testid='nav-events']").click();
        page.waitForTimeout(1000);
        Locator eventCardsAfterbooking = page.getByTestId("event-card");
        // visibility of the card which we have added
        Locator targetCardAfterbooking = eventCardsAfterbooking.filter(new Locator.FilterOptions().setHasText("QA meetup- 2027"));
        assertThat(targetCardAfterbooking).isVisible();
        String seatsTextAfterbooking = targetCardAfterbooking.getByText("seats").innerText();
        System.out.println("Afterbooking :"+ seatsTextAfterbooking);

        int seatNumAfterbooking = Integer.parseInt(seatsTextAfterbooking.split(" ")[0]);
          // compare the beforebooking and afterbooking
        Assert.assertTrue(seatNumBeforebooking> seatNumAfterbooking);
    }




    @AfterMethod
    public void TearDown() {
           System.out.print("Tear down");

        if (browser != null) {
            browser.close();
        }

        if (playwright != null) {
            playwright.close();
        }

    }

}