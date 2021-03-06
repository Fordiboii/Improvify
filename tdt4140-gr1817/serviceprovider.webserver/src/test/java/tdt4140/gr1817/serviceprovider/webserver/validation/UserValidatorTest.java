package tdt4140.gr1817.serviceprovider.webserver.validation;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class UserValidatorTest {

    private UserValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new UserValidator(new Gson());
    }

    @Test
    public void shouldBeLegalUser() throws Exception {
        //Given
        String json = "{\"id\": 1, \"firstName\": \"Sam\", \"lastName\": \"Smith\", \"height\": 182.5, \"birthDate\": \"1999-09-09\", \"username\": \"yolo\", \"password\": \"secret\", \"email\": \"email@domain.no\"}";

        //When
        boolean outcome = validator.validate(json);

        //Then
        assertThat(outcome, is(true));
    }

    @Test
    public void shouldLackRequiredFields() throws Exception {
        //Given
        String json = "{\"id\": 1, \"firstName\": \"Sam\", \"lastName\": \"Smith\", \"birthDate\": \"1999-09-09\", \"username\": \"yolo\", \"password\": \"secret\", \"email\": \"email@domain.no\"}";

        //When
        boolean outcome = validator.validate(json);

        //Then
        assertThat(outcome, is(false));
    }

    @Test
    public void shouldBeMalformedJson() throws Exception {
        //Given
        String json = "(\"id\": 2)";

        //When
        boolean outcome = validator.validate(json);

        //Then
        assertThat(outcome, is(false));
    }

    @Test
    public void shouldIgnoreIllegalID() throws Exception {
        //Given
        String json = "{\"id\": -1, \"firstName\": \"Sam\", \"lastName\": \"Smith\", \"height\": 182.5, \"birthDate\": \"1999-09-09\", \"username\": \"yolo\", \"password\": \"secret\", \"email\": \"email@domain.no\"}";

        //When
        boolean outcome = validator.validate(json);

        //Then
        assertThat(outcome, is(true));
    }

    @Test
    public void shouldHaveIllegalBirthDate() throws Exception {
        //Given
        String json = "{\"id\": 1, \"firstName\": \"Sam\", \"lastName\": \"Smith\", \"height\": 182.5, \"birthDate\": \"3999-09-09\", \"username\": \"yolo\", \"password\": \"secret\", \"email\": \"email@domain.no\"}";

        //When
        boolean outcome = validator.validate(json);

        //Then
        assertThat(outcome, is(false));
    }

    @Test
    public void shouldHaveIllegalName() throws Exception {
        //Given
        String firstNameJson = "{\"id\": 1, \"firstName\": \"Sam3\", \"lastName\": \"Smith\", \"height\": 182.5, \"birthDate\": \"1999-09-09\", \"username\": \"yolo\", \"password\": \"secret\", \"email\": \"email@domain.no\"}";
        String lastNameJson = "{\"id\": 1, \"firstName\": \"Sam\", \"lastName\": \"5mith\", \"height\": 182.5, \"birthDate\": \"1999-09-09\", \"username\": \"yolo\", \"password\": \"secret\", \"email\": \"email@domain.no\"}";

        //When
        boolean outcomeFirstName = validator.validate(firstNameJson);
        boolean outcomeLastName = validator.validate(lastNameJson);

        //Then
        assertThat(outcomeFirstName, is(false));
        assertThat(outcomeLastName, is(false));
    }

    @Test
    public void shouldHaveIllegalEmail() throws Exception {
        //Given
        String json = "{\"id\": 1, \"firstName\": \"Sam\", \"lastName\": \"Smith\", \"height\": 182.5, \"birthDate\": \"1999-09-09\", \"username\": \"yolo\", \"password\": \"secret\", \"email\": \"wrong.mail\"}";

        //When
        boolean outcome = validator.validate(json);

        //Then
        assertThat(outcome, is(false));
    }

    @Test
    public void shouldHaveIllegalHeight() throws Exception {
        //Given
        String json = "{\"id\": 1, \"firstName\": \"Sam\", \"lastName\": \"Smith\", \"height\": -5.5, \"birthDate\": \"1999-09-09\", \"username\": \"yolo\", \"password\": \"secret\", \"email\": \"email@domain.no\"}";

        //When
        boolean outcome = validator.validate(json);

        //Then
        assertThat(outcome, is(false));
    }
}