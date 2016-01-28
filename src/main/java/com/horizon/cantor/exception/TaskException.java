package com.horizon.cantor.exception;

/**
 * @author : David.Song/Java Engineer
 * @date : 2016/1/27 16:53
 * @see
 * @since : 1.0.0
 */
public class TaskException extends RuntimeException{

    private static final long serialVersionUID = 4546479087617548135L;

    public TaskException(String message,Exception e){
        super(message, e);
    }

    public TaskException(Exception e){
        super(e);
    }
}
