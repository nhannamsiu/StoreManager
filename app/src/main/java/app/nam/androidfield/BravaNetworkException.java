package app.nam.androidfield;

public class BravaNetworkException extends Exception
{
    public enum ErrorType {
        RESPONSE_FAIL, BAD_TOKEN, BAD_JSON, WEDNESDAY,
        THURSDAY, FRIDAY, SATURDAY
    }

    public ErrorType errorType;

    BravaNetworkException(String s, ErrorType e){
        super(s);
        errorType = e;
    }


}
