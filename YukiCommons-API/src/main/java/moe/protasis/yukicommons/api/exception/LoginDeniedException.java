package moe.protasis.yukicommons.api.exception;

/**
 * Thrown by the implementing class during the login progress when the login
 * needs to be denied.
 */
public class LoginDeniedException extends RuntimeException {
    public LoginDeniedException(String msg) {
        super(msg);
    }
}
