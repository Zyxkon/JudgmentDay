package com.zyxkon.judgmentday;

import com.zyxkon.judgmentday.injuries.Injury;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class CommandType {
    public enum INJURY {
        CHECK(),
        INFLICT(Injury.INJURIES.class),
        HEAL();
        // boilerplate
        List<String> params = new ArrayList<>();
        String usage;
        Callable<Boolean> func;
        INJURY(Class<?>... args) {
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
        public boolean call() throws Exception {
            return this.func.call();
        }
    }



    public enum STATS {
        GET();
        // boilerplate
        List<String> params = new ArrayList<>();
        String usage;
        STATS(Class<?>... args) {
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
    }
    public enum THIRST {
        SET(int.class), GET(), RESET(), LIST();
        // boilerplate
        List<String> params = new ArrayList<>();
        String usage;
        THIRST(Class<?>... args) {
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
    }

}