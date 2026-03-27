package dev.gyungmean.newwms.common.exception;

public class WmsException extends RuntimeException {

    private final String messageKey;
    private final Object[] args;

    public WmsException(String messageKey, Object... args) {
        super(messageKey);
        this.messageKey = messageKey;
        this.args = args;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public Object[] getArgs() {
        return args;
    }
}
