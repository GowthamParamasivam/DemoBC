package Events;


import sawtooth.sdk.messaging.*;
import sawtooth.sdk.protobuf.Message;

public class SendEvents {

    public static void main(String... args){
        Stream st = new ZmqStream("tcp://192.168.99.100:4004");
        st.send(null,null);
    }
}
