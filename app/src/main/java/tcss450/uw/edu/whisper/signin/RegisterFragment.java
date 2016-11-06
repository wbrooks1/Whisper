package tcss450.uw.edu.whisper.signin;


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

import tcss450.uw.edu.whisper.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private final static String USER_ADD_URL
            = "http://cssgate.insttech.washington.edu/~tillettj/Android/registerUser.php?";

    private EditText userIdText;
    private EditText pwdText;

    private RegisterFragment mListerner;


    public RegisterFragment() {
        // Required empty public constructor
    }


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
                if(TextUtils.isEmpty(confirmPwd)) {
                    Toast.makeText(v.getContext(), "Enter confirmation password"
                            , Toast.LENGTH_SHORT).show();
                    confirmationPwd.requestFocus();
                    return;
                }
                if(!pwd.contentEquals(confirmPwd)) {
                    Toast.makeText(v.getContext(), "Passwords to not match"
                            , Toast.LENGTH_SHORT).show();
                    pwdText.requestFocus();
                    confirmationPwd.setText("");
                    return;
                }
                if(pwd.contentEquals(confirmPwd)) {
                    Toast.makeText(v.getContext(), "User Registered"
                            , Toast.LENGTH_SHORT).show();


                    // TODO: 11/5/2016  add users into the database
                    String url = buildRegisterURL(v);



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

    public interface AddUserListener {
        public void addUser(String url);
    }

    private String buildRegisterURL(View v) {

        StringBuilder sb = new StringBuilder(USER_ADD_URL);

        try {
            String userName = userIdText.getText().toString();
            sb.append("user_name=");
            sb.append(userName);

            String password = pwdText.getText().toString();
            sb.append("password");
            sb.append(password);

            Log.i("RegisterFragment", sb.toString());
        } catch (Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

        return sb.toString();
    }

}
