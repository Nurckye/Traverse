package Channel;

import Channel.Channel;
import Message.Message;
import Message.MessageDecoder;
import Message.MessageEncoder;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(
        value = "/channel/{name}",
        decoders = MessageDecoder.class,
        encoders = MessageEncoder.class)
public class ChannelEndpoint {
    Channel channel;

    @OnOpen
    public void onOpen(Session session, @PathParam("name") String name) throws IOException {
        this.channel = Channel.getChannel(name);
        this.channel.addSubscriber(session);
    }

    @OnMessage
    public void onMessage(Session session, Message message) throws IOException, EncodeException {
        System.out.println("MESAGE " + session.getId() + channel.getName());
        channel.broadcastMessage(session.getId(), message);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        channel.removeSubscriber(session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        channel.removeSubscriber(session.getId());
    }
}
