package Channel;
import Message.Message;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Channel {
    public static HashMap<String, Channel> channels = new HashMap<String, Channel>();

    public static Channel getChannel(String name) {
        if (channels.containsKey(name)) {
            return channels.get(name);
        } else {
            Channel c = new Channel(name);
            channels.put(name, c);
            return c;
        }
    }

    private String name;
    private int subscriberCount;
    private HashMap<String, Session> subscribers;

    private Channel(String name) {
        this.name = name;
        this.subscriberCount = 0;
        this.subscribers = new HashMap<String, Session>();
    }

    public void addSubscriber(Session subscriber) {
        if (! subscribers.containsKey(subscriber.getId())) {
            this.subscriberCount++;
            subscribers.put(subscriber.getId(), subscriber);
        }
    }

    public void removeSubscriber(String id) {
        subscribers.remove(id);
        this.subscriberCount--;
        if (subscriberCount == 0) {
            channels.remove(this.name);
        }
    }

    public void broadcastMessage(String id, Message payload) throws IOException, EncodeException {
        for (Map.Entry<String, Session> entry : subscribers.entrySet()) {
            System.out.println(entry.getKey());
            if (entry.getKey() != id) {
                entry.getValue().getBasicRemote().sendObject(payload);
            }
        }
    }

    public String getName() {
        return this.name;
    }


}
