package moe.protasis.yukicommons.api.exception.command;

public class InvalidCommandException extends CommandExecutionException{
    public InvalidCommandException(String msg) {
        super(msg);
    }
}
