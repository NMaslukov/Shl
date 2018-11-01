package birzha;

import com.google.common.hash.Hashing;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;

@ClientEndpoint
@Service
public class AunitWsServiceImpl {

    private String wsUrl = "ws://ec2-18-223-213-72.us-east-2.compute.amazonaws.com:8090";
    private URI WS_SERVER_URL;
    private Session session;
    private volatile RemoteEndpoint.Basic endpoint = null;

    @PostConstruct
    public void init() {


        WS_SERVER_URL = URI.create(wsUrl);
        connectAndSubscribe();
    }

    private void connectAndSubscribe() {
        try {
            session = ContainerProvider.getWebSocketContainer()
                    .connectToServer(this, WS_SERVER_URL);
            session.setMaxBinaryMessageBufferSize(5012000);
            session.setMaxTextMessageBufferSize(5012000);
            session.setMaxIdleTimeout(Long.MAX_VALUE);

            endpoint = session.getBasicRemote();
            subscribeToTransactions();
        } catch (Exception e) {
            System.out.println("gabella");
            e.printStackTrace();
        }
    }

    //{"method":"call","params":[2,"get_objects",[["2.1.0"]]],"id":7}
    private void subscribeToTransactions() throws IOException {
        JSONObject login = new JSONObject();
        login.put("id", 0);
        login.put("method", "call");
        login.put("params", new JSONArray().put(1).put("login").put(new JSONArray().put("").put("")));

        JSONObject db = new JSONObject();
        db.put("id", 1);
        db.put("method", "call");
        db.put("params", new JSONArray().put(1).put("database").put(new JSONArray()));

        JSONObject netw = new JSONObject();
        netw.put("id", 2);
        netw.put("method", "call");
        netw.put("params", new JSONArray().put(1).put("network_broadcast").put(new JSONArray()));

        JSONObject history = new JSONObject();
        history.put("id", 3);
        history.put("method", "call");
        history.put("params", new JSONArray().put(1).put("history").put(new JSONArray()));

        JSONObject orders = new JSONObject();
        orders.put("id", 4);
        orders.put("method", "call");
        orders.put("params", new JSONArray().put(1).put("orders").put(new JSONArray()));

        JSONObject chainId = new JSONObject();
        chainId.put("id", 5);
        chainId.put("method", "call");
        chainId.put("params", new JSONArray().put(2).put("get_chain_id").put(new JSONArray().put(new JSONArray())));

        JSONObject get_object = new JSONObject();
        get_object.put("id", 6);
        get_object.put("method", "call");
        get_object.put("params", new JSONArray().put(2).put("get_objects").put(new JSONArray().put(new JSONArray().put("2.1.0"))));


        JSONObject subscribe = new JSONObject();
        subscribe.put("id", 7);
        subscribe.put("method", "call");
        subscribe.put("params", new JSONArray().put(2).put("set_subscribe_callback").put(new JSONArray().put(0).put(true)));



//1 transaction example {"id":10,"jsonrpc":"2.0","result":{"previous":"001f3ce20118d7962c1cc8f91917431891c0f371","timestamp":"2018-10-05T10:21:54","witness":"1.6.7","transaction_merkle_root":"8b527136e1e39d2773871273c5fac5c19c752c6e","extensions":[],"witness_signature":"1f04d9c1780914727cffaa36ad002a6a8a7f9e4c7018c52d388d4417ab8efa455f3e75bbe8a3ed377344310d237b0653d740835c83f9f4e77ad1a35c00a109258a","transactions":[{"ref_block_num":15586,"ref_block_prefix":2530678785,"expiration":"2018-10-05T10:22:21","operations":[[5,{"fee":{"amount":0,"asset_id":"1.3.0"},"registrar":"1.2.26","referrer":"1.2.26","referrer_percent":0,"name":"a2206b681-8660-4d04-81ad-a7adc0905273","owner":{"weight_threshold":1,"account_auths":[],"key_auths":[["AUNIT7BqnZ1v98bBE1CQNJzSP1iYVyxkRh8atq8rG9uqUUadroxDQY3",1]],"address_auths":[]},"active":{"weight_threshold":1,"account_auths":[],"key_auths":[["AUNIT8hp82A9iRqyN5KMh4Cb2VrdS6vPcYYzibKz93EMAqEb1ihzyVm",1]],"address_auths":[]},"options":{"memo_key":"AUNIT8hp82A9iRqyN5KMh4Cb2VrdS6vPcYYzibKz93EMAqEb1ihzyVm","voting_account":"1.2.5","num_witness":0,"num_committee":0,"votes":[],"extensions":[]},"extensions":{}}]],"extensions":[],"signatures":["202d114a57a15dc6916d87a5cd9031effb827fe723398a7198211dd4cb6f5b703a69278519bcb9e776838d27d46db41c7d06e3c92bc45e957a43b599058b347e1c"],"operation_results":[[1,"1.2.22401"]]}]}}


        JSONObject send_bablo = new JSONObject();
        send_bablo.put("id", 33);
        send_bablo.put("method", "call");
        send_bablo.put("params", new JSONArray().put(3).put("transfer").put(new JSONArray().put("a46865dd7-91af-4d22-9bf3-d695e32efeec")
                .put("dudotest1").put(0.00002).put("AUNIT").put("").put(true)));


        System.out.println(login);
        endpoint.sendText(login.toString());

        System.out.println(db.toString());
        endpoint.sendText(db.toString());

        System.out.println(netw);
        endpoint.sendText(netw.toString());

        System.out.println(history);
        endpoint.sendText(history.toString());

        System.out.println(orders);
        endpoint.sendText(orders.toString());

        System.out.println(chainId);
        endpoint.sendText(chainId.toString());

        System.out.println(subscribe);
        endpoint.sendText(subscribe.toString());

        System.out.println(get_object);
        endpoint.sendText(get_object.toString());

        getBlock(2820317);
        getBlock(2820324);

    }

    private void getBlock(int block_num) throws IOException {
        JSONObject block = new JSONObject();
        block.put("id", 10);
        block.put("method", "call");
        block.put("params", new JSONArray().put(2).put("get_block").put(new JSONArray().put(block_num)));

        endpoint.sendText(block.toString());
    }

    @OnMessage
    public void onMessage(String msg) {
        System.out.println(msg);
        if (msg.contains("notice")){}
        else if (msg.contains("previous")) printHash(msg);
    }

    private void printHash(String msg){
        JSONObject block = new JSONObject(msg);
        if (block.getJSONObject("result").getJSONArray("transactions").length() == 0) return;
        JSONArray transactions = block.getJSONObject("result").getJSONArray("transactions");
        for (int i = 0; i < transactions.length(); i++) {
            JSONObject transaction = transactions.getJSONObject(i).getJSONArray("operations").getJSONArray(0).getJSONObject(1);

            if (transaction.getString("to").equals("1.2.27569")){
                JSONObject memo = transaction.getJSONObject("memo");
                String s = Hashing.sha256()
                        .hashString(memo.toString(), StandardCharsets.UTF_8)
                        .toString();
                System.out.println("HASHIS " + s);
            }

        }
    }
}


