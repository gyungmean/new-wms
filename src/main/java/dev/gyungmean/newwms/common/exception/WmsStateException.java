package dev.gyungmean.newwms.common.exception;

public class WmsStateException extends WmsException {

    public WmsStateException(String messageKey, Object... args) {
        super(messageKey, args);
    }
}
