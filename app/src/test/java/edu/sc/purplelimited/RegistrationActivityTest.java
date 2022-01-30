package edu.sc.purplelimited;

import static org.junit.Assert.*;

import org.junit.Test;

public class RegistrationActivityTest {

    @Test
    public void validate() {
        String name = "Borat";
        String password = "Isnice123";
        String password2 = "Isnice123";
        boolean output;
        RegistrationActivity registrationActivity = new RegistrationActivity();
        output = registrationActivity.validate(name, password, password2);

        assertEquals(true, output);
    }
}