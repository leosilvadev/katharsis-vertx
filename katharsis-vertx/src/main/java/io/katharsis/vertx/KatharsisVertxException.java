package io.katharsis.vertx;

import io.katharsis.errorhandling.exception.KatharsisException;

public class KatharsisVertxException extends KatharsisException {

    public KatharsisVertxException(String message) {
        super(message);
    }

    public KatharsisVertxException(Throwable cause) {
        super(cause);
    }

    public KatharsisVertxException(String message, Throwable cause) {
        super(message, cause);
    }

}
