package za.co.entelect.devcamp.productcatalog.enums;

public enum CustomerChecksEnum {
    KYC_CHECK(1L, "KYC Check"),
    FRAUD_CHECK(2L, "Fraud Check"),
    LIVING_STATUS_CHECK(3L, "Living Status Check"),
    DUPLICATE_ID_STATUS_CHECK(4L, "Duplicate ID Status Check"),
    MARITAL_STATUS_CHECK(5L, "Marital Status Check"),
    CREDIT_CHECK(6L, "Credit Check");

    private final Long id;
    private final String name;

    CustomerChecksEnum(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static String getNameById(Long id) {
        for (CustomerChecksEnum check : CustomerChecksEnum.values()) {
            if (check.getId().equals(id)) {
                return check.getName();
            }
        }

        return null;
    }
}