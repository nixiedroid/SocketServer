package com.nixiedroid.rpc.exceptions;

public class Thrower {
    public static <R> R throwExceptionWithReturn(Throwable exc) {
        throwException(exc);
        return null; //Actually, unreachabe
    }
    @SuppressWarnings("unchecked")
    public static <E extends Throwable> void throwException(Throwable exc) throws E{
        throw (E)exc; //Throws actual exception
    }
}
