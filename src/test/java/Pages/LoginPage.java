package Pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginPage {

       Page page;
       String baseURL;
       private static final String email_Label ="Email";
       private static final String password_Label ="Password";



    public LoginPage(Page page, String baseURL) {
        this.page = page;
        this.baseURL = baseURL;
    }

    public DashboardPage loginToApplication() {

        page.navigate(baseURL);
        String ti = page.title();
        System.out.print(ti);
        assertThat(page).hasTitle("EventHub — Discover & Book Events");

        // Log IN
        page.getByLabel(email_Label).fill("sharathkraj5@gmail.com");
        page.getByLabel(password_Label).fill("Sharath@1710");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

        DashboardPage dashboardPage = new DashboardPage(page);
        return dashboardPage;

    }


}
