package moe.protasis.yukicommons.api.exception.command;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CommandExecutionException extends RuntimeException {
    public CommandExecutionException(String msg) {
        super(msg);
    }

}
