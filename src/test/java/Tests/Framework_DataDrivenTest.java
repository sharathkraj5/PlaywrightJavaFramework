package Tests;

import Pages.*;
import Utils.DataProviderUtil;
import com.microsoft.playwright.Locator;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;

public class Framework_DataDrivenTest extends BaseTest {


    @DataProvider(name = "eventBookingData")
    public Object[][] eventBookingData() throws IOException {
        return DataProviderUtil.getJsonData("src/test/resources/eventBookingData.json");

    }

    @Test(groups = {"framework"},dataProvider = "eventBookingData", description = "Login and create an Events and verify if its booed")
    public void DemoTest(HashMap<String, String> data) throws IOException {

        LoginPage loginPage = new LoginPage(page,baseURL);
        DashboardPage dashboardPage = loginPage.loginToApplication();
        dashboardPage.waitForEventsToLoad();

        AdminEventPage adminEventPage = new AdminEventPage(page);
        adminEventPage.goTo();
        adminEventPage.CreateEvent(
                data.get("title"),
                data.get("description"),
                data.get("category"),
                data.get("city"),
                data.get("venue"),
                data.get("eventDateAndTime"),
                data.get("price"),
                data.get("seats")
        );

        EventPage eventpage = new EventPage(page);
        eventpage.goTo();
        //eventpage.waitForEventsToLoad();
        Locator targetCard = eventpage.findEventcard(data.get("title"));
        int seatNumBeforebooking = eventpage.getSeatcount(targetCard);
        System.out.println("Before booking: " + seatNumBeforebooking);
        BookingPage bookingPage = eventpage.booking_event(targetCard);
        bookingPage.BookingTickets(
                data.get("Name"),
                data.get("email"),
                data.get("phone"));
        bookingPage.VerifytheBookingTickets();
        System.out.println(bookingPage.Ticket_count(data.get("title")));


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