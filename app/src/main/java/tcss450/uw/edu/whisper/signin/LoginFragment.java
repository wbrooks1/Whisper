package tcss450.uw.edu.whisper.signin;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import tcss450.uw.edu.whisper.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        final EditText userIdText = (EditText) v.findViewById(R.id.userid_edit);
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
                ft.commit();

            }
        });

        //check if a registered user
        return v;
    }


    public interface LoginInteractionListener {
        public void login(String userId, String pwd);
    }


}
