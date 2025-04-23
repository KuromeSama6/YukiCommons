package moe.protasis.yukicommons.api.exception.command;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OperationNotPermittedException extends CommandExecutionException {
    public OperationNotPermittedException(String msg) {
        super(msg);
    }
}
