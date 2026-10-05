package Pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookingPage {

    Page page;


    private static final String Name_label ="Full Name";
    private static final String Email_label ="Email";
    private static final String Phone_label ="Phone Number";
    private static final String Button_click ="[id='confirm-booking']";
    private static final String Booking_ref =".booking-ref";
    private static final String Booking_card ="#booking-card";
    private static final String event_url ="[data-testid='nav-events']";


    public BookingPage(Page page) {
        this.page = page;
    }

    public void BookingTickets(String Name,String email,String phone) {


        page.getByLabel(Name_label).fill(Name);
        page.getByLabel(Email_label).fill(email);
        page.getByLabel(Phone_label).fill(phone);
        page.locator(Button_click).click();
        assertThat(page.getByText("Your tickets are reserved.")).isVisible();
    }
    
    
    public void VerifytheBookingTickets() {


        String bookingRef = page.locator(Booking_ref).innerText();
        //System.out.println(bookingRef);
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("View My Bookings")).click();

        // verify the booking card
        Locator bookingCards = page.locator(Booking_card);
        //System.out.println(bookingCards.count());
        Locator targetbookingcard = bookingCards.filter(new Locator.FilterOptions().setHasText(bookingRef));
        assertThat(targetbookingcard).isVisible();
    }

    public String Ticket_count(String title){

        //  verify the Seat count decrease
        page.locator(event_url).click();
        page.waitForTimeout(1000);
        Locator eventCardsAfterbooking = page.getByTestId("event-card");
        // visibility of the card which we have added
        Locator targetCardAfterbooking = eventCardsAfterbooking.filter(new Locator.FilterOptions().setHasText(title));
        assertThat(targetCardAfterbooking).isVisible();
        String seatsTextAfterbooking = targetCardAfterbooking.getByText("seats").innerText();
        System.out.println("Afterbooking :"+ seatsTextAfterbooking);
        return seatsTextAfterbooking;
    }
}
