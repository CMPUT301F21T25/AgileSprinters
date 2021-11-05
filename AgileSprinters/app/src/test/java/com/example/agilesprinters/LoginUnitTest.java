package com.example.agilesprinters;


//import org.junit.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class LoginUnitTest {


    @Rule
    //public ActivityTestRule<Login> mActivityTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);

    public Login LoginActivity;

    @Before
    public void setUp() throws Exception {
        //LoginActivity = mActivityTestRule.getActivity();
    }

    @Test
    public void testLogin(){
       /** getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                EditText email = LoginActivity.findViewById(R.id.email);
                EditText pass = LoginActivity.findViewById(R.id.password);
                email.setText("email");
                pass.setText("pass");
                Button LoginBtn = LoginActivity.findViewById(R.id.loginBtn);
                LoginBtn.performClick();
                assertTrue(LoginActivity.isCurUserLoggedIn());
            }
        })**/
    }

    @After
    public void tearDown() throws Exception {
        LoginActivity = null;
    }

}
