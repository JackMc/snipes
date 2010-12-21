package org.ossnipes.snipes.enums;

/**
 * Snipes events, each of these comes with their own set of event params, got with the methods
 * in the <code>SnipesEventParams.getParamsArr()</code>. The event params and their corisponding
 * numbers in the event params array is outlined in the JavaDoc.
 */
public enum SnipesEvent {
    /**
     * Snipes IRC join event. Called when someone (including us) joins a channel we are in.
     * It would be bad practice to call a joinChannel() in this event, as it would cause infinite
     * recursion.
     * <BR/>
     * <BR/>
     * Params:
     * 0 = The channel that was joined.
     * 1 = The nick joining the channel.
     * 2 = The hostname (after the at-sign) of the person joining.
     * <BR/>
     * <BR/>
     * This is a normal IRC event (non-internal)
     */
    SNIPES_IRC_JOIN,
    /**
     * Snipes channel message event. Sent when the IRC server informs us that someone has
     * sent a PRIVMSG to us. Note: This event is not sent every time a PRIVMSG
     * is. It is only sent when the PRIVMSG is addressed to getNick(), not to a channel we are in.
     * <BR/>
     * <BR/>
     * Params:
     * 0 = The nickname that sent the message.
     * 1 = The hostname (after the at-sign) of the person who sent the message.
     * 2 = The actual message sent to us.
     * <BR/>
     * <BR/>
     * This is a normal IRC event (non-internal)
     */
    SNIPES_IRC_PRIVATEMSG,
    /**
     * Snipes IRC part event. Called when someone (including us) parts a channel we are in.
     * It would be bad practice to call a partChannel() in this event, as it would cause infinite
     * recursion.
     * <BR/>
     * <BR/>
     * Params:
     * 0 = The channel that was parted.
     * 1 = The nick parting the channel.
     * 2 = The hostname (after the at-sign) of the person joining.
     * 3 = The reason.
     * <BR/>
     * <BR/>
     * This is a normal IRC event (non-internal)
     */
    SNIPES_IRC_PART,
    SNIPES_IRC_DISCONNECT,
    SNIPES_IRC_CONNENCT,
    SNIPES_IRC_IDENTSTART,
    SNIPES_IRC_IDENTSTOP,
    /**
     * Snipes IRC action event. Called when someone uses a "/me" or ACTION at us
     * or at one of the channels we are in.
     * <BR/>
     * <BR/>
     * Params:
     * 0 = The nick/channel this ACTION was directed at.
     * (will be getNick() if it was directed at us.)
     * 1 = The nickname sending the event.
     * 2 = The hostname (after the at-sign) of the person who sent the ACTION.
     * 3 = The actual text of the action sent to us.
     * <BR/>
     * <BR/>
     * This is a normal IRC event (non-internal)
     */
    SNIPES_IRC_ACTION,
    /**
     * Snipes server response event. Sent when the IRC server sends a response code out
     * to it's clients. This event is generally not needed, except for plugins that are
     * working with IRC servers that don't use the IRC specification. Note that this event
     * is sent whenever we get something from the server not actually in the context of a channel
     * or PRIVMSG to us, for example: "(insert nick here) :Nickname already in use."
     * <BR/>
     * <BR/>
     * Params:
     * 0 = The response sent to us, in Integer.toString(int) format.
     * 1 = The actual message sent along with the response.
     * <BR/>
     * <BR/>
     * This is a normal IRC event (non-internal)
     */
    SNIPES_IRC_SERVRESPONSE,
    SNIPES_INT_CONSOLEOUT,
    SNIPES_INT_PLUGINLOAD,
    SNIPES_INT_PLUGINUNLOAD,
    /**
     * Snipes channel message event. Sent when the IRC server informs us that someone has
     * sent a PRIVMSG to a channel that we are in.
     * <BR/>
     * <BR/>
     * Params:
     * 0 = The channel the message was sent to.
     * 1 = The nickname that sent the message.
     * 2 = The hostname (after the at-sign) of the person who sent the message.
     * 3 = The actual message sent to the channel
     * <BR/>
     * <BR/>
     * This is a normal IRC event (non-internal)
     */
    SNIPES_IRC_CHANMSG,
    /**
     * Snipes IRC notice event. Called when someone sends us a NOTICE, or sends
     * a NOTICE to a channel we are in.
     * <BR/>
     * <BR/>
     * Params:
     * 0 = The nick that sent us the NOTICE.
     * 1 = The hostname (after the at-sign) of the nick sending the NOTICE.
     * 2 = The channel/nick this was sent to (will be getNick() if it was sent to us.)
     * 3 = The actual text of the notice sent to us.
     * <BR/>
     * <BR/>
     * This is a normal IRC event (non-internal)
     */
    SNIPES_IRC_NOTICE,
    SNIPES_IRC_KICK, SNIPES_IRC_TOPIC_CHANGED, SNIPES_IRC_MODE, SNIPES_IRC_DCCCHAT, /**
     * Snipes IRC nick change event. Called when someone (including us) changes their nick in
     * a channel we are in. It would be bad practice to call a changeNick() in this event,
     * as it would cause infinite recursion.
     * <BR/>
     * <BR/>
     * Params:
     * 0 = The nick before this event occured.
     * 1 = The nick after the event occured.
     * 2 = The hostname (after the at-sign) of the user the event is concerning.
     * <BR/>
     * <BR/>
     * This is a normal IRC event (non-internal)
     */
    SNIPES_IRC_NICKCHANGE
}
