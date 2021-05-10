package io.github.metalturtle18.scanblockhunt.util.enums;

import lombok.Getter;

public enum CommandTabCompletion {
    ADDPLAYER(new String[]{"*player*"}),
    ENDGAME(),
    ENDROUND(),
    GAMEINFO(),
    CREATE(),
    NEXT(new String[]{"*material*"}),
    REMOVEPLAYER(new String[]{"*player*"}),
    RESET(),
    SETSCORE(new String[]{"*player*"});

    @Getter
    final String[][] args;

    CommandTabCompletion(String[]... args) {
        this.args = args;
    }

    public static CommandTabCompletion get(String s) {
        for (CommandTabCompletion command : CommandTabCompletion.values()) {
            if (command.name().equalsIgnoreCase(s)) {
                return command;
            }
        }
        return null;
    }
}
