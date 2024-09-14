package com.zyxkon.judgmentday;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;


public enum CommandType {
    INJURY(
            SubcommandType.INJURY_CHECK,
            SubcommandType.INJURY_HEAL,
            SubcommandType.INJURY_INFLICT
    ),

    THIRST
            (
            SubcommandType.THIRST_GET,
            SubcommandType.THIRST_SET,
            SubcommandType.THIRST_RESET
    )
            ,

    STATS(
            SubcommandType.STATS_GET
    );
    private final List<SubcommandType> subcommandTypeList = new ArrayList<>();
    static {
        for (CommandType c : CommandType.values()){
            for (SubcommandType t : SubcommandType.values()) {
                String[] cmdLabels = t.name().split("_");
                if (cmdLabels[0].equals(c.name())) {
                    if (c.addSubcommand(t)) {
                        Main.log(Level.INFO, String.format("Successfully added subcommand %s(%s) to command %s",
                                t.getSimpleName(), t.name(), c.name()));
                    } else {
                        Main.log(Level.INFO, String.format("Could not add subcommand %s(%s) to command %s",
                                t.getSimpleName(), t.name(), c.name()));
                    }
                }
            }
        }
    }
    CommandType(SubcommandType... subcommands) {
        // technically, based off the code above, :CommandType: does not require any sort of subcommands
        // but enums explicitly stated in its initialization will always be added to it
        for (SubcommandType t : subcommands){
            if (this.addSubcommand(t)) {
                Main.log(Level.INFO, String.format("Successfully added subcommand %s(%s) to command %s",
                        t.getSimpleName(), t.name(), this.name()));
            } else {
                Main.log(Level.INFO, String.format("Could not add subcommand %s(%s) to command %s",
                        t.getSimpleName(), t.name(), this.name()));
            }
        }

    }

    public boolean addSubcommand(SubcommandType subcommand){
        if (!this.subcommandTypeList.contains(subcommand)){
            this.subcommandTypeList.add(subcommand);
            return true;
        }
        return false;
    }
    public List<SubcommandType> getSubcommands(){
        return this.subcommandTypeList;
    }
    public String getUsage(){
        List<String> uses = new ArrayList<>();
        for (SubcommandType s : this.getSubcommands()){
            List<String> strs = new ArrayList<>();
            strs.add("/"+ Main.commandName);
            strs.add(this.name());
            strs.add(s.getSimpleName());
            strs.add(s.params);
            uses.add(String.join(" ", strs));
        }
        return String.join("\n", uses);
    }
}