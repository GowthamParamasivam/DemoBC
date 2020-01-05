package Client;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.model.DataItem;
import com.google.protobuf.ByteString;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import sawtooth.sdk.processor.Utils;
import sawtooth.sdk.protobuf.*;
import sawtooth.sdk.signing.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Client {

    public static void main (String... args) throws IOException {
        Secp256k1Context cntxt = new Secp256k1Context();
        PrivateKey privkey = cntxt.newRandomPrivateKey();
        System.out.println("Client private key"+privkey);
        Signer signer = new Signer(cntxt,privkey);
        ByteArrayOutputStream custompayload = new ByteArrayOutputStream();
        try{
            List<DataItem> cb =  new CborBuilder().addMap().put("Name","Gowtham").put("Description","Software Engineer").end().build();
            new CborEncoder(custompayload).encode(cb);
            System.out.println(cb);
        }
        catch(Exception e){
            System.out.println(e);
        }
        //payload in byte array
        byte[] payloadBytes = custompayload.toByteArray();
        //namespace
        String address1 = Utils.hash512("DemoBC".getBytes("UTF-8")).substring(0,6);
        //Address design
        String address2 = Utils.hash512("HelloWorldhowareyouareyoufine".getBytes("UTF-8")).substring(0,64);
        //final address from namespace and address design
        String address = address1+address2;
        System.out.println("Address for the data to be added"+address);
        //Transaction Header
        TransactionHeader header = TransactionHeader.newBuilder().setSignerPublicKey(signer.getPublicKey().hex()).setFamilyName("DemoBC").setFamilyVersion("1.0")
                .addInputs(address).addOutputs(address).setPayloadSha512(hash(payloadBytes)).setBatcherPublicKey(signer.getPublicKey().hex()).setNonce(UUID.randomUUID().toString()
                ).build();
        // Signer signing the transaction header
        String signature = signer.sign(header.toByteArray());
        //Single Transaction
        Transaction transaction = Transaction.newBuilder().setHeader(header.toByteString()).setPayload(ByteString.copyFrom(payloadBytes)).setHeaderSignature(signature).build();
        //Adding the transaction to list
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        //Creating Batch Header
        BatchHeader batchHeader = BatchHeader.newBuilder().setSignerPublicKey(signer.getPublicKey().hex())
                .addAllTransactionIds(transactions.stream().map(Transaction::getHeaderSignature).collect(Collectors.toList())).build();
        //Batch Signature
        String batchSignature = signer.sign(batchHeader.toByteArray());
        //Batch creation
        Batch batch = Batch.newBuilder().setHeader(batchHeader.toByteString()).addAllTransactions(transactions).setHeaderSignature(batchSignature).build();
        //Batch in byte array
        byte[] batchListBytes = BatchList.newBuilder().addBatches(batch).build().toByteArray();
        //Creating Http Client to communicate with the rest api
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://192.168.99.100:8008/batches");
        httpPost.setEntity(new ByteArrayEntity((batchListBytes)));
        httpPost.setHeader("Content-type","application/octet-stream");
        CloseableHttpResponse response = httpClient.execute(httpPost);
        HttpEntity responseHttpEntity= response.getEntity();
        String responselink = EntityUtils.toString(responseHttpEntity);
        //Response
        System.out.println("Getting the response");
        System.out.println(responselink);
    }

    public static String hash(byte[] input){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(input);
            BigInteger no = new BigInteger(1,messageDigest);
            String hashtext = no.toString(16);
            while(hashtext.length() < 32){
                hashtext = "0"+hashtext;
            }
            return hashtext;
        }
        catch (Exception e){
            System.out.println("Exception in hash"+e);
        }
        return null;
    }
}
