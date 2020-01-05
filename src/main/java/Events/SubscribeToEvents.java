package Events;

import com.google.protobuf.InvalidProtocolBufferException;
import org.zeromq.ZContext;
import sawtooth.sdk.messaging.*;
import sawtooth.sdk.protobuf.*;
import zmq.ZMQ;

public class SubscribeToEvents {

    public static void main(String... args) throws InvalidProtocolBufferException {
//        Stream st = new ZmqStream("tcp://192.168.99.100:4004");
//        st.receive();
//        EventFilter ef = EventFilter.newBuilder();
//        ef.
        EventSubscription eventSubscription = EventSubscription.newBuilder()
                .setEventType("sawtooth/state-delta").build();
//        ClientEventsSubscribeRequest clientEventsSubscribeRequest = ClientEventsSubscribeRequest
//                .newBuilder().setSubscriptions(0,eventSubscription).build();
        ClientEventsSubscribeRequest clientEventsSubscribeRequest = ClientEventsSubscribeRequest
                .newBuilder().addSubscriptions(eventSubscription).build();


        ZContext zContext = new ZContext();
        org.zeromq.ZMQ.Socket skt = zContext.createSocket(ZMQ.ZMQ_DEALER);
        skt.connect("tcp://192.168.99.100:4004");
        //correlation id
        String correlation_id = "123";
        //Message
        Message msg = Message.newBuilder().setCorrelationId(correlation_id)
                .setMessageType(Message.MessageType.CLIENT_EVENTS_SUBSCRIBE_REQUEST)
                .setContent(clientEventsSubscribeRequest.toByteString()).build();
        //sending subscribe request to the validator
        skt.send(msg.toByteArray());
        //Continuously listening for the messages
        while(true){
            //Constructing the response
//            ClientEventsSubscribeResponse clientEventsSubscribeResponse =
            byte[] mg = skt.recv();
            Message mg1 = Message.parseFrom(mg);
//            Events events = Event.
            System.out.println(mg1.getContent().toString());

        }

    }
}
