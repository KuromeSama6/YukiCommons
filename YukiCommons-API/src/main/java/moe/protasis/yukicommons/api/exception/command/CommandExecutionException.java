package moe.protasis.yukicommons.api.exception.command;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CommandExecutionException extends RuntimeException {
    public CommandExecutionException(String msg) {
        super(msg);
    }

    public CommandExecutionException(Throwable cause) {
        super(cause);
    }

    public CommandExecutionException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
