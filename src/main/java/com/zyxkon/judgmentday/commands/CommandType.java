package com.zyxkon.judgmentday.commands;

import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;
import com.zyxkon.judgmentday.commands.types.InjuryCommand;
import com.zyxkon.judgmentday.commands.types.StatsCommand;
import com.zyxkon.judgmentday.commands.types.ThirstCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum CommandType {
    INJURY, THIRST, STATS;
    CommandType() {
    }

    public String getUsage() {
        List<String> usageMsg = new ArrayList<>();
        usageMsg.add("/"+ Main.commandName);
        usageMsg.add(this.name());
        switch (this){
            case INJURY: {
                usageMsg.add(InjuryCommand.getUsages());
                break;
            }
            case STATS: {
                usageMsg.add(StatsCommand.getUsages());
                break;
            }
            case THIRST: {
                usageMsg.add(ThirstCommand.getUsages());
                break;
            }
        }
        return String.join(" ", usageMsg);
    }
}