package tcss450.uw.edu.whisper;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import tcss450.uw.edu.whisper.signin.SignInActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

/**
 * instrumentation test for the login fragment
 * @author Jacob Tillett
 * @version 12/7/2016
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginFragmentTest {

    @Rule
    public ActivityTestRule<SignInActivity> mActivityRule = new ActivityTestRule<>(
            SignInActivity.class);


    @Test
    public void testLogin() {

        String userName = "user";
        String pwd = "password";

        //enter data
        onView(withId(R.id.userid_edit))
                .perform(typeText(userName));
        onView(withId(R.id.pwd_edit))
                .perform(typeText(pwd));

        //login
        onView(withId(R.id.login_button))
                .perform(click());

        //test toast
        onView(withText("Logged In"))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));

        //logout
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText("LOGOUT")).perform(click());


    }


    @Test
    public void testLoginInvalidUser() {

        String userName = "notauser";
        String pwd = "password";

        //enter data
        onView(withId(R.id.userid_edit))
                .perform(typeText(userName));
        onView(withId(R.id.pwd_edit))
                .perform(typeText(pwd));

        //login
        onView(withId(R.id.login_button))
                .perform(click());

        //test toast
        onView(withText("Incorrect User Name"))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));



    }

//



    @Test
    public void testLoginEmptyPwd() {

        String userName = "user";


        //enter data
        onView(withId(R.id.userid_edit))
                .perform(typeText(userName));


        //login
        onView(withId(R.id.login_button))
                .perform(click());

        //test toast
        onView(withText("Enter password"))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));

    }


}
