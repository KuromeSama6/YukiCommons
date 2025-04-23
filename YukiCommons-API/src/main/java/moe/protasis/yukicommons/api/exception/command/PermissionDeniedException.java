package moe.protasis.yukicommons.api.exception.command;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PermissionDeniedException extends CommandExecutionException{
    public PermissionDeniedException(String msg) {
        super(msg);
    }
}
