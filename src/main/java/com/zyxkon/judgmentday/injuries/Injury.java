package com.zyxkon.judgmentday.injuries;

import com.zyxkon.judgmentday.Utils;

public abstract class Injury {

    public enum INJURIES {
        BLOODLOSS, IMPAIRMENT, INFECTION, POISONING, ALL
    }
    public abstract void cancel();
    public abstract Runnable getRunnable();
    public static INJURIES getValue(String name){
        for (INJURIES i : INJURIES.values()){
            if (Utils.equatesTo(name, i.name())){
                return i;
            }
        }
        return null;
    }
}
