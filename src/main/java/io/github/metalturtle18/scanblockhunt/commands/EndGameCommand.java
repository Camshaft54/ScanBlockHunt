package io.github.metalturtle18.scanblockhunt.commands;

import io.github.metalturtle18.scanblockhunt.ScanBlockHunt;
import io.github.metalturtle18.scanblockhunt.util.BlockHuntCommand;
import io.github.metalturtle18.scanblockhunt.util.Messenger;
import io.github.metalturtle18.scanblockhunt.util.enums.MessageSeverity;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

public class EndGameCommand implements BlockHuntCommand {
    @Override
    public String getCommandName() {
        return "endgame";
    }

    @Override
    public void runCommand(Player player, String[] args) {
        if (ScanBlockHunt.runningGame == null) {
            Messenger.sendMessage(player, "A game must be running in order to stop it!", MessageSeverity.INCORRECT_COMMAND_USAGE);
            return;
        } else if (!ScanBlockHunt.runningGame.getGameHost().equals(player)) {
            Messenger.sendMessage(player, "You can only end the game if you are the host!", MessageSeverity.INCORRECT_COMMAND_USAGE);
            return;
        }
        ConversationFactory factory = new ConversationFactory(ScanBlockHunt.getPlugin())
                .withFirstPrompt(new WarningPrompt())
                .withLocalEcho(false);
        Conversation conversation = factory.buildConversation(player);
        conversation.begin();
    }

    // All of the code below is a derivative of IdleBot (https://github.com/IdleBot-Development/IdleBot)
    private static class WarningPrompt extends StringPrompt {
        @Override
        public String getPromptText(ConversationContext conversationContext) {
            return "Are you sure you want to stop the currently running game? Type \"y\" to confirm or \"n\" to reject.";
        }

        @Override
        public Prompt acceptInput(ConversationContext conversationContext, String s) {
            if (s != null && (s.equalsIgnoreCase("y") || s.equalsIgnoreCase("yes"))) {
                ScanBlockHunt.runningGame.endGame();
                return new YesPrompt();
            } else {
                return new NoPrompt();
            }
        }
    }

    private static class YesPrompt extends MessagePrompt {
        @Override
        protected Prompt getNextPrompt(ConversationContext conversationContext) {
            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        public String getPromptText(ConversationContext conversationContext) {
            return "Stopped game!";
        }
    }

    private static class NoPrompt extends MessagePrompt {
        @Override
        protected Prompt getNextPrompt(ConversationContext conversationContext) {
            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        public String getPromptText(ConversationContext conversationContext) {
            return "Game not cancelled.";
        }
    }
}
