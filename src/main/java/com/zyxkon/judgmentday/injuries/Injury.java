package com.zyxkon.judgmentday.injuries;

public abstract class Injury {
    public abstract void cancel();
    public abstract Runnable getRunnable();
}
