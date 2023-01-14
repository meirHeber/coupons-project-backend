package com.meir.coupons.enums;

public enum ErrorType {

    GENERAL_ERROR(601, "General error", true),
    INVALID_COMPANY_NAME(602,"The name entered is invalid. Enter a name with between 2 and 10 letters","INVALID_COMPANY_NAME" ),
    NO_AUTHORIZED(603, "You do not have permission to perform this action, please try to login again.", "NO_AUTHORIZED", true),
    NAME_ALREADY_EXISTS(604, "The name you chose already exists. Please enter another name", "NAME_ALREADY_EXISTS", false),
    MUST_ENTER_NAME(605, "Can not insert an null/empty name", false),
    ID_DOES_NOT_EXIST(607, "This ID does not  exist", "ID_DOES_NOT_EXIST", false),
    INVALID_PASSWORD(608, "password is invalid. Enter a password with between 7 and 10 letters and numbers","INVALID_PASSWORD", false),
    NOT_ENOUGH_COUPONS_LEFT(609, "Not enough coupons left to purchase the amount requested", "NOT_ENOUGH_COUPONS_LEFT",false),
    COUPON_EXPIRED(610, "The coupon is expired","COUPON_EXPIRED", false),
    LOGIN_FAILED(611, "Login failed. Check the details you entered and try again","LOGIN_FAILED", false),
    INVALID_PRICE(612, "Coupon price must be more than 0", "INVALID_PRICE",false),
    INVALID_EMAIL(613, "Email address is InValid, Please enter a valid email address", false),
    INVALID_AMOUNT(614, "Coupon's amount must be more than 0","INVALID_AMOUNT", false),
    INVALID_DATES(615, "The dates you've entered are wrong or null","INVALID_DATES",false),
    MUST_INSERT_A_VALUE(616, "Must insert a value", false),
    COULD_NOT_GENERATE_ID(617,"could not generate Id", false ),
    NAME_TOO_SHORT(618, "The name must contain at least two letters" , false),
    INVALID_COUPON_TYPE (619, "Can not insert an null/emptycoupon type", false),
    INVALID_ID(620, "Id can not be empty or null and must contain at least one digit","INVALID_ID", false),
    INVALID_ADDRESS(621, "Address is InValid, Please enter a valid address - between 5 and 25 letters and numbers","INVALID_ADDRESS", false),
    INVALID_PHONE_NUMBER(622, "Phone number is InValid, Please enter a valid phone number","INVALID_PHONE_NUMBER", false),
    NO_PERMISSION_GRANTED(623, "No promission gramted", true),
    ID_ARE_NOT_DIGITS_ONLY(624, "id are not digits only", true),
    FAILED_TO_SET_NEW_VALUE(625, "failed to set the new value",false),
    INVALID_USER_TYPE(626, "Your user type is not possible with the company-id parameter you entered","INVALID_USER_TYPE", true),
    USERNAME_ALREADY_EXIST(627, "this username are already exist in the system","USERNAME_ALREADY_EXIST", false),
    NEGATIVE_NUMBER_ENTERED(628, "negative number - lower than 0 - entered","NEGATIVE_NUMBER_ENTERED", true),
    TEXT_TOO_LONG(629, "text too long","TEXT_TOO_LONG", true),
    TEXT_TOO_SHORT(630, "text too short","TEXT_TOO_SHORT", false),
    TEXT_IS_EMPTY(629, "text is empty","TEXT_IS_EMPTY", false),
    IMPOSSIBLE_TO_PROVIDE_ID(630, "Cannot provide ID in add operation","IMPOSSIBLE_TO_PROVIDE_ID", false),
    INVALID_NAME(631, "The first or last name entered is invalid. Enter a name with between 2 and 10 letters","INVALID_NAME", true),
    NAME_DOES_NOT_EXIST(632, "This NAME does not exist","NAME_DOES_NOT_EXIST", false),
    INVALID_COUPON_TITLE(633, "Insert coupon title, between 2 and 50 letters and numbers","INVALID_COUPON_TITLE", false),
    INVALID_COMPANY_ID(634, "You must enter the ID of an existing company","INVALID_COMPANY_ID", false),
    INVALID_CATEGORY_ID(635, "You must enter the ID of an existing category","INVALID_CATEGORY_ID", false),
    INVALID_DESCRIPTION(636, "The description must be between 2 and 100 letters and numbers","INVALID_DESCRIPTION", false),
    INVALID_AMOUNT_OF_CHILDREN(637, "invalid amount of children","INVALID_AMOUNT_OF_CHILDREN", false),
    INVALID_USER_NAME(638, "The username must be between 3 and 10 letters and numbers","INVALID_USER_NAME", false),
    COUPON_NOT_VALID_YET(639, "The coupon is not valid yet. Wait until it takes effect","COUPON_NOT_VALID_YET", false),
    USERNAME_DOES_NOT_EXIST(640, "This USERNAME does not  exist","USERNAME_DOES_NOT_EXIST", false),
    USER_TYPE_WHO_CANNOT_MAKE_A_PURCHASE(641, "Only a customer can make a purchase","USER_TYPE_WHO_CANNOT_MAKE_A_PURCHASE", false);



    private int errorNumber;
    private String errorName;
    private String errorMessage;
    private boolean isShowStackTrace;

    private ErrorType(int errorNumber, String internalMessage, boolean isShowStackTrace) {
        this.errorNumber = errorNumber;
        this.errorMessage = internalMessage;
        this.isShowStackTrace = isShowStackTrace;
    }

    private ErrorType(int errorNumber, String internalMessage) {
        this.errorNumber = errorNumber;
        this.errorMessage = internalMessage;
    }
    private ErrorType(int errorNumber, String internalMessage, String errorName, boolean isShowStackTrace) {
        this.errorNumber = errorNumber;
        this.errorMessage = internalMessage;
        this.errorName = errorName;
        this.isShowStackTrace = isShowStackTrace;
    }

    private ErrorType(int errorNumber, String internalMessage, String errorName) {
        this.errorNumber = errorNumber;
        this.errorMessage = internalMessage;
        this.errorName = errorName;
    }


    public int getErrorNumber() {
        return errorNumber;
    }

    public void setErrorNumber(int errorNumber) {
        this.errorNumber = errorNumber;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public boolean isShowStackTrace() {
        return isShowStackTrace;
    }

    public void setShowStackTrace(boolean showStackTrace) {
        isShowStackTrace = showStackTrace;
    }
}
