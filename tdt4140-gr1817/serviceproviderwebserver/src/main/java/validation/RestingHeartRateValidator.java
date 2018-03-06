package validation;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import tdt4140.gr1817.ecosystem.persistence.data.RestingHeartRate;
import tdt4140.gr1817.ecosystem.persistence.data.User;

import java.util.Date;

public class RestingHeartRateValidator implements Validator {

    private Gson gson;

    public RestingHeartRateValidator() {
        this.gson = new Gson();
    }

    @Override
    public boolean validate(String json) {
        try {
            RestingHeartRate restingHeartRate = gson.fromJson(json, RestingHeartRate.class);

            return (isValidHeartRate(restingHeartRate.getHeartRate()) &&
                    isValidDate(restingHeartRate.getMeasuredAt()) &&
                    isValidID(restingHeartRate.getId()) &&
                    isValidUser(restingHeartRate.getMeasuredBy()));

        } catch (JsonSyntaxException | NullPointerException e) {
            return false;
        }
    }

    /**
     * Checks that the ID is positive.
     *
     * @param id The ID to be checked.
     * @return If the ID is valid.
     */
    private boolean isValidID(int id) {
        return id > 0;
    }

    /**
     * Checks that the heart rate is positive.
     *
     * @param heartRate The heart rate to be checked.
     * @return If the heart rate is valid.
     */
    private boolean isValidHeartRate(float heartRate) {
        return heartRate > 0;
    }

    /**
     * Checks that the date is in the past.
     *
     * @param date The date to be checked.
     * @return If the date is valid.
     */
    private boolean isValidDate(Date date) {
        return date.before(new Date());
    }

    /**
     * Checks that the user exists (is not null).
     *
     * @param user The user to be checked.
     * @return If the user is valid.
     */
    private boolean isValidUser(User user) {
        return user != null;
    }
}
