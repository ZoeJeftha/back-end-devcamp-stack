package za.co.entelect.devcamp.authenticationservice.helpers;

public class CustomerHelper {

    public static String maskIdNumber(String idNumber) {

        if (idNumber == null || idNumber.length() <= 4) {
            return idNumber;
        }

        return "*".repeat(idNumber.length() - 4)
                + idNumber.substring(idNumber.length() - 4);
    }
}
