package tcss450.uw.edu.whisper.signin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.nsd.NsdManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import tcss450.uw.edu.whisper.R;
import tcss450.uw.edu.whisper.WebServiceActivity;

/**
 * @author Jacob Tillett
 * @version 12/4/2016
 * Class controls the sign in to the application.
 */
public class SignInActivity extends AppCompatActivity implements LoginFragment.LoginInteractionListener,  RegisterFragment.AddUserListener, LoginFragment.loginListener {

    /** helps allow app to remember who was loggin in */
    private static SharedPreferences mSharedPreferences;

    private  ArrayList<String> mUserList;

    /**
     * creates a saved preference and if logged in goes to webserviceactivity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS)
                , Context.MODE_PRIVATE);
        if (!mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new LoginFragment())
                    .commit();
        } else {
            Intent i = new Intent(this, WebServiceActivity.class);
            startActivity(i);
            finish();
        }

    }

    /**
     * method used for loggin in
     * @param userId the email of the user
     * @param pwd the password of the user
     */
    @Override
    public void login(String userId, String pwd) {
        mSharedPreferences
                .edit()
                .putBoolean(getString(R.string.LOGGEDIN), true)
                .putString("UserName", userId)
                .commit();

        Intent i = new Intent(this, WebServiceActivity.class);
        startActivity(i);
        finish();

    }

    /**
     * for getting the username in another part of the app so we can use the username in the database
     * @return the username of the person logged in
     */
    public static String getUserName() {
        return mSharedPreferences.getString("UserName", null);
        //return LoginFragment.userId;
    }




    /**
     * adds the user to the database
     * @param url the url that contains the user info
     */
    @Override
    public void addUser(String url) {
        AddUserTask task = new AddUserTask();
        task.execute(new String[]{url.toString()});



    }

    @Override
    public void loginListener(String url) {
        LoginTask task = new LoginTask();
        task.execute(new String[]{url.toString()});

    }


    /**
     * async task for useing web services for registering
     */
    private class AddUserTask extends AsyncTask<String, Void, String> {


        /**
         * calls super
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * tries to add user
         * @param urls the url to add a user
         * @return the responce for trying to add user
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to add User, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }


        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
                    Toast.makeText(getApplicationContext(), "User successfully registered!"
                            , Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to register: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * async tast for loging the user
     */
    private class LoginTask extends AsyncTask<String, Void, String> {


        /**
         * calls super
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * tries to add user
         * @param urls the url to add a user
         * @return the responce for trying to add user
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to login, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }


        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.

            mUserList = new ArrayList<String>();
            result = LoginFragment.parseUserJSON(result, mUserList);

            System.out.print(mUserList.get(0));
            Log.v("user LIst", mUserList.get(0));


            String user = LoginFragment.userId;
            String pwd = LoginFragment.pwd;

            if(mUserList.contains(user))  {
                Log.v("USER", mUserList.get(mUserList.indexOf(user)));
                Log.v("PWD", mUserList.get(mUserList.indexOf(user)  +  1));
                if(pwd.matches(mUserList.get(mUserList.indexOf(user) + 1))) {
                    login(user, pwd);
                    Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
                }


            } else {
                Toast.makeText(getApplicationContext(), "Incorrect User Name", Toast.LENGTH_SHORT).show();
            }



        }
    }

}
