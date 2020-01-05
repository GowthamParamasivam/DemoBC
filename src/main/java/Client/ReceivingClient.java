package Client;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.DataItem;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class ReceivingClient {
    public static void main(String ... args) throws IOException, CborException {
        //paste the link below and run the program
        String link = "http://192.168.99.100:8008/transactions?" +
                "id=86d499dad8320ed2992450bc7288a9b5fccfc5f22687e381f1101f290184092f3171afa7dd26c96e308a40de27d3e8e0dcf7b761ea4ddc0848a79754f29eb058";
        //Creating httpclient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(link);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity responseHttpEntity= response.getEntity();
        String responselink = EntityUtils.toString(responseHttpEntity);
        JSONObject json = new JSONObject(responselink);
        System.out.println(json.keySet());
        //getting the data field from the json output
//        System.out.println(json.get("data"));
        String data = json.get("data").toString().replace("[","").replace("]","");
        String data1 = json.get("data").toString();
//        System.out.println(data1);
        data1 =  data1.substring(1,data1.length()-1);
        JSONObject datajson = new JSONObject(data1
        );
        System.out.println(datajson.toString());
        System.out.println(datajson.get("payload"));
        byte[] finalb =Base64.getDecoder().decode(datajson.get("payload").toString());
        List<DataItem> decoded = new CborDecoder(new ByteArrayInputStream(finalb)).decode();
        for (DataItem i: decoded
        ) {
            String str = i.toString();
            System.out.println(str);
            json = new JSONObject(str);
            System.out.println(json.get("Name"));
            System.out.println(json.get("Description"));
        }
    }
}
