package com.zyxkon.judgmentday;

import com.zyxkon.judgmentday.injuries.Injury;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class CommandSystem{

    public enum Commands {
        INJURY, THIRST, STATS;
        static HashMap<Commands, String> commands = new HashMap<>();

        static {
            for (Commands cmdType : values()) {
                putCmd(cmdType);
            }
        }

//        public enum INJURY {
//            CHECK(),
//            INFLICT(Injury.INJURIES.class),
//            HEAL();
//            // boilerplate
//            List<String> params = new ArrayList<>();
//            String usage;
//            Callable<Boolean> func;
//
//            INJURY(Class<?>... args) {
//                Arrays.stream(args).forEach(
//                        arg -> params
//                                .add(
//                                        arg.isEnum()
//                                                ? Arrays.stream(arg.getEnumConstants())
//                                                .map(Object::toString)
//                                                .collect(Collectors.joining("|"))
//                                                : arg.getName()
//                                )
//                );
//                usage = (params.stream()
//                        .map(s -> "[" + s + "]"))
//                        .collect(Collectors.joining(" "))
//                        .concat(" <player>")
//                        .toLowerCase();
//            }
//
//            public boolean call() throws Exception {
//                return this.func.call();
//            }
//        }
//
//        public enum STATS {
//            GET();
//            // boilerplate
//            List<String> params = new ArrayList<>();
//            String usage;
//
//            STATS(Class<?>... args) {
//                Arrays.stream(args).forEach(
//                        arg -> params
//                                .add(
//                                        arg.isEnum()
//                                                ? Arrays.stream(arg.getEnumConstants())
//                                                .map(Object::toString)
//                                                .collect(Collectors.joining("|"))
//                                                : arg.getName()
//                                )
//                );
//                usage = (params.stream()
//                        .map(s -> "[" + s + "]"))
//                        .collect(Collectors.joining(" "))
//                        .concat(" <player>")
//                        .toLowerCase();
//            }
//        }
//
//        public enum THIRST {
//            SET(int.class), GET(), RESET(), LIST();
//            // boilerplate
//            List<String> params = new ArrayList<>();
//            String usage;
//
//            THIRST(Class<?>... args) {
//                Arrays.stream(args).forEach(
//                        arg -> params
//                                .add(
//                                        arg.isEnum()
//                                                ? Arrays.stream(arg.getEnumConstants())
//                                                .map(Object::toString)
//                                                .collect(Collectors.joining("|"))
//                                                : arg.getName()
//                                )
//                );
//                usage = (params.stream()
//                        .map(s -> "[" + s + "]"))
//                        .collect(Collectors.joining(" "))
//                        .concat(" <player>")
//                        .toLowerCase();
//            }
//        }
//
        Commands() {
            putCmd(this);
        }
        public String getLabel(){
            return commands.get(this);
        }
        public static void putCmd(Commands cmd){
            commands.put(cmd, cmd.name());
        }

        public String getUsage(){
            StringBuilder usageMsg = new StringBuilder();
            Subcommands.getSubcommands(this).forEach(s
                    -> usageMsg.append(
                    String.format("/%s %s %s %s\n",
                            Main.commandName, this.getLabel(), s.toString(), s.usage)
            ));
            return usageMsg.toString();
        }

    }
    public enum Subcommands {
        CHECK(Commands.INJURY, Player.class),
        INFLICT(Commands.INJURY, Injury.INJURIES.class ,Player.class),
        HEAL(Commands.INJURY, Player.class),

        GET(Commands.THIRST, Player.class),
        SET(Commands.THIRST, Integer.class ,Player.class),
        LIST(Commands.THIRST, Player.class),
        RESET(Commands.THIRST, Player.class);
        private static final HashMap<Commands, List<Subcommands>> network = new HashMap<>();
        static {
            for (Subcommands s : values()){
                network.get(s.supercmd).add(s);
            }
        }
        Commands supercmd;
        String usage;
        Subcommands(Commands cmd, Class<?>... args) {
            this.supercmd = cmd;
            putCmd(this.supercmd);
            List<String> params = new ArrayList<>();
            Arrays.stream(args).forEach(
                    arg -> params
                            .add(
                                    arg.isEnum()
                                            ? Arrays.stream(arg.getEnumConstants())
                                            .map(Object::toString)
                                            .collect(Collectors.joining("|"))
                                            : arg.getName()
                            )
            );
            usage = (params.stream()
                    .map(s -> "[" + s + "]"))
                    .collect(Collectors.joining(" "))
                    .concat(" <player>")
                    .toLowerCase();
        }
        public static List<Subcommands> getSubcommands(Commands cmd){
            return network.get(cmd);
        }
        public static void putCmd(Commands cmd){
            network.put(cmd, new ArrayList<>());
        }
    }
}