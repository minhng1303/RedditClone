package com.hanu.reddit.exception;

public class SpringRedditException extends RuntimeException  {
    public SpringRedditException(String s) {
        super(s);
    }

    public SpringRedditException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }
}
