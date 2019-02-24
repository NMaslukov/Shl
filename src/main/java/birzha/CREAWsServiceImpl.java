package birzha;

import com.google.common.hash.Hashing;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

//@ClientEndpoint
//@Service
@Log4j2
public class CREAWsServiceImpl {

    private String wsUrl = "wss://node1.creary.net/ws/";
    private URI WS_SERVER_URL;
    private Session session;
    private volatile RemoteEndpoint.Basic endpoint = null;
    private int lastIrreversibleBlockValue = 17533463;

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

    private void subscribeToTransactions() throws IOException, InterruptedException {
        JSONObject login = new JSONObject();
        login.put("id", 0);
        login.put("jsonrpc", "2.0");
        login.put("method", "condenser_api.get_dynamic_global_properties");
        login.put("params", new JSONArray());


        endpoint.sendText(login.toString());

    }


    @SneakyThrows
    private void getBlock(int blockNum) {
        JSONObject block = new JSONObject();
        block.put("id", 10);
        block.put("jsonrpc", "2.0");
        block.put("method", "block_api.get_block");
        block.put("params", new JSONObject().put("block_num", blockNum));
        System.out.println("get block:");

        System.out.println(block);
        endpoint.sendText(block.toString());
    }


    @OnMessage
    public void onMessage(String msg) {
        System.out.println("PPY: " + msg);
        getBlock(new JSONObject(msg).getJSONObject("result").getInt("last_irreversible_block_num"));
//        try {
//            if (msg.contains("last_irreversible_block_num")) {
//                setIrreversableBlock(msg);
//            }
//            else if (msg.contains("previous")) processIrreversebleBlock(msg);
//            else log.info("unrecogrinzed msg from aunit \n" + msg);
//        } catch (Exception e) {
//            log.error("Web socket error" + 1 + "  : \n" + e.getMessage());
//        }

    }

    private void processIrreversebleBlock(String trx) {
        JSONObject block = new JSONObject(trx);
        if (block.getJSONObject("result").getJSONArray("transactions").length() == 0) return;
        JSONArray transactions = block.getJSONObject("result").getJSONArray("transactions");

        System.out.println(transactions);
//        List<String> lisfOfMemo = refillService.getListOfValidAddressByMerchantIdAndCurrency(merchant.getId(), currency.getId());
//        try {
//            for (int i = 0; i < transactions.length(); i++) {
//                JSONObject transaction = transactions.getJSONObject(i).getJSONArray("operations").getJSONArray(0).getJSONObject(1);
//
//                if (transaction.getString("to").equals(mainAddressId)) makeRefill(lisfOfMemo, transaction);
//
//            }

//

    }

    private void setIrreversableBlock(String msg) {
//        try {
//            JSONObject message = new JSONObject(msg);
//            int blockNumber = message.getJSONArray("params").getJSONArray(1).getJSONArray(0).getJSONObject(3).getInt(lastIrreversebleBlockParam);
//            synchronized (this) {
//                if (blockNumber > lastIrreversibleBlockValue) {
//                    for (; lastIrreversibleBlockValue <= blockNumber; lastIrreversibleBlockValue++) {
//                        getBlock(lastIrreversibleBlockValue);
//                    }
//
//                    System.out.println("UPDATE");
//                }
//            }
//        } catch (Exception e){
//            e.printStackTrace();
//            System.out.println("The message was: \n" + msg);
//        }
    }


    private final String lastIrreversebleBlockParam = "last_irreversible_block_num";

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

//    {"method":"block_api.get_block","id":10,"jsonrpc":"2.0","params":[{"block_num":17535678}]}
//    {"jsonrpc":"2.0","method":"block_api.get_block","params":{"block_num":564967},"id":6711456019256992}
}


