package Events;

import sawtooth.sdk.messaging.*;

public class SubscribeToEvents {

    public static void main(String... args){
        Stream st = new ZmqStream("tcp://192.168.99.100:4004");
        st.receive();

    }
}
