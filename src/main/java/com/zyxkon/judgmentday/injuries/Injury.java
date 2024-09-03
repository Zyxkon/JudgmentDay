package com.zyxkon.judgmentday.injuries;

public abstract class Injury {

    public enum INJURIES {
        BLOODLOSS, IMPAIRMENT, INFECTION, POISONING, ALL
    }
    public abstract void cancel();
    public abstract Runnable getRunnable();
}
