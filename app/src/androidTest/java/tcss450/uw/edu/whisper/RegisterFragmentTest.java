package tcss450.uw.edu.whisper;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import tcss450.uw.edu.whisper.signin.RegisterFragment;
import tcss450.uw.edu.whisper.signin.SignInActivity;

import static android.support.test.espresso.Espresso.onView;
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
 * @author Jacob Tillett
 * @version 12/6/2016
 *
 * A instrumentation test for the register fragment
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegisterFragmentTest {

    @Rule
    public ActivityTestRule<SignInActivity> mActivityRule = new ActivityTestRule<>(
            SignInActivity.class);


    @Test
    public void testRegister() {

        Random rand = new Random();

        //generate username
        String userName = "test" + (rand.nextInt(10) + 1 ) + (rand.nextInt(10) + 1 )
                + (rand.nextInt(10) + 1 ) + (rand.nextInt(10) + 1 );

        String password = "";
        for (int i = 0; i < rand.nextInt(5) + 5; i++) {
            password += rand.nextInt(10);
        }


        //get to registration page
        onView(withId(R.id.new_user)).perform(click());

        //enter data
        onView(withId(R.id.user_name)).perform(typeText(userName));
        onView(withId(R.id.password)).perform(typeText(password));
        onView(withId(R.id.reenter_password)).perform(typeText(password));

        //click register button
        onView(withId(R.id.register_button)).perform(click());

        //test the toast
        onView(withText("User successfully registered!"))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView())))).check(matches(isDisplayed()));




    }



    @Test
    public void testRegistrationInvalidPassword() {

        Random rand = new Random();

        //generate username
        String userName = "test" + (rand.nextInt(10) + 1 ) + (rand.nextInt(10) + 1 )
                + (rand.nextInt(10) + 1 ) + (rand.nextInt(10) + 1 );

        String password = "";
        for (int i = 0; i < rand.nextInt(4); i++) {
            password += rand.nextInt(10);
        }


        //get to registration page
        onView(withId(R.id.new_user)).perform(click());

        //enter data
        onView(withId(R.id.user_name)).perform(typeText(userName));
        onView(withId(R.id.password)).perform(typeText(password));
        onView(withId(R.id.reenter_password)).perform(typeText(password));

        //click register button
        onView(withId(R.id.register_button)).perform(click());


        //test the toast
        onView(withText("Password must be atleast 4 characters"))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView())))).check(matches(isDisplayed()));


    }





    @Test
    public void testRegistrationPasswordsDontMatch() {

        Random rand = new Random();

        //generate username
        String userName = "test" + (rand.nextInt(10) + 1 ) + (rand.nextInt(10) + 1 )
                + (rand.nextInt(10) + 1 ) + (rand.nextInt(10) + 1 );

        String password = "";
        for (int i = 0; i < rand.nextInt(5) + 5; i++) {
            password += rand.nextInt(10);
        }


        //get to registration page
        onView(withId(R.id.new_user)).perform(click());

        //enter data
        onView(withId(R.id.user_name)).perform(typeText(userName));
        onView(withId(R.id.password)).perform(typeText(password));
        onView(withId(R.id.reenter_password)).perform(typeText("test"));

        //click register button
        onView(withId(R.id.register_button)).perform(click());


        //test the toast
        onView(withText("Passwords do not match"))
                .inRoot(withDecorView(not(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))).check(matches(isDisplayed()));

    }









}
