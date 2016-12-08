package tcss450.uw.edu.whisper.signin;


import android.content.Context;
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
import android.widget.Toast;

import java.net.URLEncoder;

import tcss450.uw.edu.whisper.R;

/**
 * @author Jacob Tillett
 * @version 12/4/2016
 * A simple {@link Fragment} subclass.
 * fragment for registering the user
 */
public class RegisterFragment extends Fragment {

    /** the url to acces the registerUser php file */
    private final static String USER_ADD_URL
            = "http://cssgate.insttech.washington.edu/~tillettj/Android/registerUser.php?";

    /** the edit text widget for the user id */
    private EditText userIdText;
    /** the edit text widget for the password */
    private EditText pwdText;

    /** the add user listener */
    private AddUserListener mListener;


    /**
     * empty constructor
     */
    public RegisterFragment() {
        // Required empty public constructor
    }


    /**
     * creates the register fragment
     * @param inflater makes the container visable
     * @param container what holds the widgets
     * @param savedInstanceState
     * @return the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_register, container, false);
        userIdText = (EditText) v.findViewById(R.id.user_name);
        pwdText = (EditText) v.findViewById(R.id.password);
        final EditText confirmationPwd = (EditText) v.findViewById(R.id.reenter_password);
        Button regiserButton = (Button) v.findViewById(R.id.register_button);
        regiserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = userIdText.getText().toString();
                String pwd = pwdText.getText().toString();
                String confirmPwd = confirmationPwd.getText().toString();
                if(TextUtils.isEmpty(userId)) {
                    Toast.makeText(v.getContext(), "Enter User ID"
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
                if(pwd.length() < 4) {
                    Toast.makeText(v.getContext(), "Password must be atleast 4 characters"
                            , Toast.LENGTH_SHORT).show();
                    pwdText.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(confirmPwd)) {
                    Toast.makeText(v.getContext(), "Enter confirmation password"
                            , Toast.LENGTH_SHORT).show();
                    confirmationPwd.requestFocus();
                    return;
                }
                if(!pwd.contentEquals(confirmPwd)) {
                    Toast.makeText(v.getContext(), "Passwords do not match"
                            , Toast.LENGTH_SHORT).show();
                    pwdText.requestFocus();
                    confirmationPwd.setText("");
                    pwdText.setText("");
                    return;
                }
                if(pwd.contentEquals(confirmPwd)) {




                    String url = buildRegisterURL(v);
                    mListener.addUser(url);





                    Fragment f = new LoginFragment();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fragment_container, f);

                    ft.commit();

                    return;
                }


            }
        });






        return v;

    }

    /**
     * an interface for the adduser method
     */
    public interface AddUserListener {
        public void addUser(String url);

    }

    /**
     * instanciates mListener
     * @param context the current state of the app
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddUserListener) {
            mListener = (AddUserListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AddUserListener");
        }
    }


    /**
     * builds the url to register users
     * @param v the view
     * @return the url
     */
    private String buildRegisterURL(View v) {

        StringBuilder sb = new StringBuilder(USER_ADD_URL);

        try {
            String userName = userIdText.getText().toString();
            sb.append("email=");

            sb.append(userName);

            String password = pwdText.getText().toString();
            sb.append("&pwd=");
            sb.append(URLEncoder.encode(password, "UTF-8"));


            Log.i("RegisterFragment", sb.toString());
        } catch (Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

        return sb.toString();
    }

}
