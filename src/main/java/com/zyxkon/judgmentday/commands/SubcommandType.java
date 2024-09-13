package com.zyxkon.judgmentday.commands;

import com.zyxkon.judgmentday.Utils;
import com.zyxkon.judgmentday.injuries.Injury;
import org.bukkit.entity.Player;

public enum SubcommandType {
    INJURY_CHECK(CommandType.INJURY, "CHECK", Player.class),
    INJURY_INFLICT(CommandType.INJURY, "INFLICT",Injury.INJURIES.class, Player.class),
    INJURY_HEAL(CommandType.INJURY, "HEAL" ,Player.class),

    STATS_GET(CommandType.STATS, "GET",Player.class),

    THIRST_SET(CommandType.THIRST, "SET",int.class, Player.class),
    THIRST_GET(CommandType.THIRST, "GET",Player.class),
    THIRST_RESET(CommandType.THIRST, "RESET",Player.class),
    THIRST_LIST(CommandType.THIRST, "LIST"),
    ;

    String params;
    CommandType type;
    String name;
    SubcommandType(CommandType type, String name, Class<?>... args) {
        params = Utils.returnUsage(args);
        this.type = type;
        this.name = name;
        type.addSubcommand(this);
    }
    public String getName(){
        return name;
    }
}