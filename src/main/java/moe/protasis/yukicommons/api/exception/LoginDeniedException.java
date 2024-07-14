package moe.protasis.yukicommons.api.exception;

public class LoginDeniedException extends RuntimeException {
    public LoginDeniedException(String msg) {
        super(msg);
    }
}
