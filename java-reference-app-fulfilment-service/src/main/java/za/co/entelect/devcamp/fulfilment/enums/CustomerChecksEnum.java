package za.co.entelect.devcamp.fulfilment.enums;

public enum CustomerChecksEnum
{
    KYC_CHECK("Kyc Check"),
    FRAUD_CHECK("Fraud Check"),
    LIVING_STATUS_CHECK("Living Status Check"),
    DUPLICATE_ID_STATUS_CHECK("Duplicate Id Status Check"),
    MARITAL_STATUS_CHECK("Marital Status Check"),
    CREDIT_CHECK("Credit Check")

    private final String name;

    CustomerChecksEnum(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}