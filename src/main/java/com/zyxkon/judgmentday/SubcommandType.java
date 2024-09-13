package com.zyxkon.judgmentday;

import com.zyxkon.judgmentday.injuries.Injury;
import org.bukkit.entity.Player;

public enum SubcommandType {
    INJURY_CHECK("CHECK", Player.class),
    INJURY_INFLICT("INFLICT",Injury.INJURIES.class, Player.class),
    INJURY_HEAL("HEAL" ,Player.class),

    STATS_GET("GET",Player.class),

    THIRST_SET("SET",int.class, Player.class),
    THIRST_GET("GET",Player.class),
    THIRST_RESET("RESET",Player.class),
    THIRST_LIST("LIST"),
    ;

    String params;
    private final String simpleName;
    CommandType type;
    SubcommandType(String name, Class<?>... args) {
        params = Utils.returnUsage(args);
        this.simpleName = name;
    }
    public String getSimpleName(){
        return simpleName;
    }
}