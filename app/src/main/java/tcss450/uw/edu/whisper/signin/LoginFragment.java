package tcss450.uw.edu.whisper.signin;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.List;

import tcss450.uw.edu.whisper.R;

/**
 * @author Jacob Tillett
 * A simple {@link Fragment} subclass.
 * a fragment for logging in the user
 */
public class LoginFragment extends Fragment {

    /** url to get user info */
    private final static String LOGIN_URL
            = "http://cssgate.insttech.washington.edu/~tillettj/Android/userInfo.php?";
    /** the widget that has the user id */
    public EditText userIdText;
    /** the listener for logging in */
    public loginListener mListener;

    /**
     * empty constructor
     */
    public LoginFragment() {
        // Required empty public constructor
    }


    /**
     * creates what is in the fragment
     * @param inflater makes the view visable
     * @param container the thig that is being inflated
     * @param savedInstanceState
     * @return the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        userIdText = (EditText) v.findViewById(R.id.userid_edit);
        final EditText pwdText = (EditText) v.findViewById(R.id.pwd_edit);
        TextView register = (TextView) v.findViewById(R.id.new_user);
        Button signInButton = (Button) v.findViewById(R.id.login_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = userIdText.getText().toString();
                String pwd = pwdText.getText().toString();
                if(TextUtils.isEmpty(userId)) {
                    Toast.makeText(v.getContext(), "Enter User ID"
                            , Toast.LENGTH_SHORT).show();
                    userIdText.requestFocus();
                    return;
                }
                if(!userId.contains("@")) {
                    Toast.makeText(v.getContext(), "Enter a valid email address"
                            , Toast.LENGTH_SHORT).show();
                    userIdText.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(pwd)) {
                    Toast.makeText(v.getContext(), "Enter password"
                            , Toast.LENGTH_SHORT).show();
                    pwdText.requestFocus();
                    return;
                }
                if(pwd.length() < 3) {
                    Toast.makeText(v.getContext(), "Password must be atleast 4 characters"
                            , Toast.LENGTH_SHORT).show();
                    pwdText.requestFocus();
                    return;
                }


                ((SignInActivity) getActivity()).login(userId, pwd);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f = new RegisterFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, f);
                ft.addToBackStack(null);
                ft.commit();

            }
        });


        return v;
    }


    /**
     * parses the user info Json
     * @param userJSON the json that has the user info
     * @param userInfo an arraylist that holds the user info
     * @return reason for error
     */
    public static String parseUserJSON(String userJSON, List<String> userInfo) {
        String reason = null;
        if (userJSON != null) {
            try {
                JSONArray arr = new JSONArray(userJSON);

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    userInfo.add(obj.getString("email"));
                    userInfo.add(obj.getString("pwd"));
                }
            } catch (JSONException e) {
                reason =  "Unable to parse data, Reason: " + e.getMessage();

            }

        }
        return reason;
    }


    /**
     * interface for the login method
     */
    public interface LoginInteractionListener {
        public void login(String userId, String pwd);
    }

    /**
     * iinterface for the lofing listener
     */
    public interface loginListener {
        public void loginListener(String url);

    }

    /**
     * instanciates mListener
     * @param context the current state of the app
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegisterFragment.AddUserListener) {
            mListener = (LoginFragment.loginListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AddUserListener");
        }
    }

    /**
     * builds the url to get user info
     * @param v the view
     * @return the url
     */
    private String buildRUserrURL(View v) {

        StringBuilder sb = new StringBuilder(LOGIN_URL);

        try {
            String userName = userIdText.getText().toString();
            sb.append("email=");

            sb.append(userName);

            sb.append("&cmd=email");



            Log.i("RegisterFragment", sb.toString());
        } catch (Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

        return sb.toString();
    }


}
