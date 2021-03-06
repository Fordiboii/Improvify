package tdt4140.gr1817.serviceprovider.webserver.validation.util;

import java.nio.charset.Charset;
import java.util.Base64;

public class AuthBasicUtil {

    public static final String HEADER_DEFAULT = createHeader("default", "default");
    public static final String HEADER_TEST_123 = createHeader("test", "123");
    public static final String HEADER_ILLEGAL = "nonsense";

    private static String createHeader(String username, String password) {
        String credentials = username + ":" + password;
        final byte[] encodedCredentials = Base64.getEncoder().encode(credentials.getBytes(Charset.forName("utf8")));

        return "Basic " + new String(encodedCredentials, Charset.forName("utf8"));
    }
}
