package com.zyxkon.judgmentday.commands;

import com.zyxkon.judgmentday.Main;

import java.util.ArrayList;
import java.util.List;


public enum CommandType {
    INJURY, THIRST, STATS;
    private final List<SubcommandType> subcommandTypeList = new ArrayList<>();
    CommandType() {
    }

    public void addSubcommand(SubcommandType subcommand){
        this.subcommandTypeList.add(subcommand);
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
            strs.add(s.getName());
            strs.add(s.params);
            uses.add(String.join(" ", strs));
        }
        return String.join("\n", uses);
    }
}