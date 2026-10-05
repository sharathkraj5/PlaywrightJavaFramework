package Tests;

import Pages.*;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class Framework_buildTest extends BaseTest {


    @Test(groups = {"framework"}, description = "Login and create an Events and verify if its booed")
    public void DemoTest() {
         String eventTitle = "QA - Meetup - Oct 2027";
        LoginPage loginPage = new LoginPage(page,baseURL);
        DashboardPage dashboardPage = loginPage.loginToApplication();
        dashboardPage.waitForEventsToLoad();

        AdminEventPage adminEventPage = new AdminEventPage(page);
        adminEventPage.goTo();
        adminEventPage.CreateEvent(
                eventTitle,
                "Qa Engineer meetup freshers and experienced can meet",
                "Workshop",
                "Madurai",
                "Raja muthaiah Mandram, madurai",
                "2027-01-29T18:00",
                "399",
                "500"
        );

        EventPage eventpage = new EventPage(page);
        eventpage.goTo();
        //eventpage.waitForEventsToLoad();
        Locator targetCard = eventpage.findEventcard(eventTitle);
        int seatNumBeforebooking = eventpage.getSeatcount(targetCard);
        System.out.println("Before booking: " + seatNumBeforebooking);
        BookingPage bookingPage = eventpage.booking_event(targetCard);
        bookingPage.BookingTickets(
                "Poornima devi",
                "Poornima@temp.com",
                "3245543654");
        bookingPage.VerifytheBookingTickets();
        //System.out.println(bookingPage.Ticket_count());


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