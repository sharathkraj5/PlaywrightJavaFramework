package Pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AdminEventPage {
       Page page;
       String Event_url = "https://eventhub.rahulshettyacademy.com/admin/events";
       private static final String Title_Label ="Title";
       private static final String Description_Label ="Describe the event…";
       private static final String Category_Label ="Category";
       private static final String City_Label ="City";
       private static final String venue_Label ="Venue";
       private static final String Date_Label ="Event Date & Time";
       private static final String Price_Label ="Price";
       private static final String seats_Label ="Total Seats";
       private static final String success_message ="Event created!";


    public AdminEventPage(Page page) {

        this.page = page;
    }

    public void goTo(){

        page.navigate(Event_url);


    }

    public void CreateEvent(String title,String description, String category,String city,String venue,
                            String eventDateAndTime,String price,String seats){

        // create an event from admin page
        page.getByLabel(Title_Label).fill(title);
        page.getByPlaceholder(Description_Label).fill(description);
        page.getByLabel(Category_Label).selectOption(category);
        page.getByLabel(City_Label).fill(city);
        page.getByLabel(venue_Label).fill(venue);
        page.getByLabel(Date_Label).fill(eventDateAndTime);
//        page.waitForTimeout(3000);
        page.getByLabel(Price_Label).fill(price);
        page.getByLabel(seats_Label).fill(seats);
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("+ Add Event")).click();
//        page.waitForTimeout(3000);
        // Event created
        assertThat(page.getByText(success_message)).isVisible();
    }
}
