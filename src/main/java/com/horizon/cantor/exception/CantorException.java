package com.horizon.cantor.exception;

/**
 * @author : David.Song/Java Engineer
 * @date : 2016/1/27 16:53
 * @see
 * @since : 1.0.0
 */
public class CantorException extends RuntimeException{

    private static final long serialVersionUID = 4546479087617548135L;

    public CantorException(String message, Exception e){
        super(message, e);
    }

    public CantorException(Exception e){
        super(e);
    }
}
