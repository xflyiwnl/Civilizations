package me.xflyiwnl.civilizations.object.helper;

import me.xflyiwnl.civilizations.object.helper.reply.ReplyAction;

public class CivReply {

    private String question;
    private ReplyAction chatAction;

    public CivReply(String question, ReplyAction chatAction) {
        this.question = question;
        this.chatAction = chatAction;
    }

    public String getQuestion() {
        return question;
    }

    public ReplyAction getChatAction() {
        return chatAction;
    }

}
