package TransactionProcessor;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.DataItem;
import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors;
import org.json.JSONObject;
import sawtooth.sdk.processor.TransactionHandler;
import sawtooth.sdk.processor.Utils;
import sawtooth.sdk.processor.exceptions.InternalError;
import sawtooth.sdk.processor.exceptions.InvalidTransactionException;
import sawtooth.sdk.protobuf.TpProcessRequest;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class DemoTransactionHandler implements TransactionHandler {
    private String transactionFamilyName ="DemoBC";
    private String transactionFamilyVersion ="1.0";
    private List<String> transactionFamilyNameSpace = new ArrayList<String>();
    //Constructor
    public DemoTransactionHandler(){
        try {
            this.transactionFamilyNameSpace.add(Utils.hash512(
                    this.transactionFamilyName().getBytes("UTF-8")).substring(0, 6));
        } catch (UnsupportedEncodingException usee) {
            usee.printStackTrace();
            this.transactionFamilyNameSpace.clear();
        }

    }

    public String transactionFamilyName() {
        return transactionFamilyName;
    }

    public String getVersion() {
        return transactionFamilyVersion;
    }

    public Collection<String> getNameSpaces() {
        return transactionFamilyNameSpace;
    }

    public void apply(TpProcessRequest tpProcessRequest, sawtooth.sdk.processor.Context context) throws InvalidTransactionException, InternalError {
        try {
            TransactionData td = this.getUnpackedTransaction(tpProcessRequest);
            Map<Descriptors.FieldDescriptor, Object> st = tpProcessRequest.getAllFields();
            for (Descriptors.FieldDescriptor i: st.keySet()
                 ) {
                System.out.println(i.toString());
            }
            //Demo business logic
            //checking whether the description containg Engineer
            String description = td.getDescription();
            System.out.println(description);
            System.out.println(!description.toUpperCase().contains("Engineer"));
            if(!description.contains("Engineer")){
                throw new InvalidTransactionException("Sorry the transaction contains the doctor string");
            }
            // Creating String from the transaction
            String forGlobalState = "Name:"+td.getName()+","+"Description:"+td.getDescription();
            byte[] forGlobalStateBytes = forGlobalState.getBytes();
            // Creating the data in the global state
            ByteString finalByte = ByteString.copyFrom(forGlobalStateBytes);
            //Creating the address for the global state
//            String address = Utils.hash512("Letusmaketheworldabetterplacetolivewithpeaceandhappiness".getBytes("UTF-8")).substring(0,70);
            String address = "98446499ea097ef13b5d2a35fc8b2a29fde77a711777a209a217c9ffc061bb3abf1a49";
            System.out.println(address);
            //Trying to store it in the global state
            Map.Entry<String,ByteString> entry = new AbstractMap.SimpleEntry<String, ByteString>(address,finalByte);
            Collection<Map.Entry<String,ByteString>> finalEntry = Collections.singletonList(entry);
            context.setState(finalEntry);
            System.out.println("state saved successfully");
        }
        catch (Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    private TransactionData getUnpackedTransaction(TpProcessRequest transactionRequest)
            throws InvalidTransactionException, CborException {
        String signer = transactionRequest.getHeader().getSignerPublicKey();
        System.out.println("Signer public key"+signer);
        ArrayList<String> payload
                = decodeData(transactionRequest.getPayload().toStringUtf8());
        List<DataItem> decoded = new CborDecoder(new ByteArrayInputStream(transactionRequest.getPayload().toByteArray())).decode();
        System.out.println("Size of the item"+decoded.size());
        JSONObject json = null;
        for (DataItem i: decoded
             ) {
            String str = i.toString();
            System.out.println(str);
            json = new JSONObject(str);
            System.out.println(json.get("Name"));
            System.out.println(json.get("Description"));
        }


//        for (String a: payload
//             ) {
//            System.out.println("payload data"+a);
//        }

        if (payload.size() > 2) {
            throw new InvalidTransactionException("Invalid payload serialization");
        }
        while (payload.size() < 2) {
            payload.add("");
        }
        return new TransactionData((String) json.get("Name"), (String) json.get("Description"), signer);
    }

    private ArrayList<String> decodeData(String toStringUtf8) {
        return new ArrayList<>(Arrays.asList(toStringUtf8.split(",")));
    }

}
