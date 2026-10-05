package Pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class EventPage {

    Page page;
    private static final String event_url ="[data-testid='nav-events']";
    private static final String event_card ="event-card";
    private static final String seat ="seats";
    private static final String Button_clk ="[data-testid='book-now-btn']";

    public EventPage(Page page) {
        this.page = page;
    }

    public void goTo(){
        page.locator(event_url).click();
    }

    public Locator waitForEventsToLoad(){
        Locator eventCards = page.getByTestId(event_card);
        assertThat(eventCards.first()).isVisible();
        System.out.println(eventCards.count());
        return eventCards;
    }

    public Locator findEventcard(String titlecard){
        Locator eventCards = waitForEventsToLoad();
        Locator targetCard =eventCards.filter(new Locator.FilterOptions().setHasText(titlecard));
        assertThat(targetCard).isVisible();
        return targetCard;
    }

    public int getSeatcount(Locator targetCard){
        String seatsText = targetCard.getByText(seat).innerText();
        // System.out.println(seatsText);
        return Integer.parseInt(seatsText.split(" ")[0]);

    }

    public BookingPage booking_event(Locator targetCard){
        targetCard.locator(Button_clk).click();
        return new BookingPage(page);
    }


}
