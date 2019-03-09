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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ClientEndpoint
@Service
@Log4j2
public class CREAWsServiceImpl {

    private String wsUrl = "wss://nodes.creary.net/ws/";
    private URI WS_SERVER_URL;
    private Session session;
    private volatile RemoteEndpoint.Basic endpoint = null;
    private int lastIrreversibleBlockValue = 327210;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void init() {
        WS_SERVER_URL = URI.create(wsUrl);
        connectAndSubscribe();
        scheduler.scheduleAtFixedRate(this::requestLastIrreversibleBlock, 0, 3L , TimeUnit.SECONDS); //var

    }

    private void connectAndSubscribe() {
        try {
            session = ContainerProvider.getWebSocketContainer()
                    .connectToServer(this, WS_SERVER_URL);
            session.setMaxBinaryMessageBufferSize(5012000);
            session.setMaxTextMessageBufferSize(5012000);
            session.setMaxIdleTimeout(Long.MAX_VALUE);

            endpoint = session.getBasicRemote();
//            subscribeToTransactions();
            System.out.println("Getting block 327969");
            getBlock(327969);
        } catch (Exception e) {
            System.out.println("gabella");
            e.printStackTrace();
        }
    }

    private void subscribeToTransactions() throws IOException, InterruptedException {
    }


    @OnMessage
    public void onMessage(String msg) {
        try {
            System.out.println(msg);
            if (isContainsLastIrreversibleBlockInfo(msg)) getUnprocessedBlocks(msg);
            else if (isIrreversibleBlockInfo(msg)) processIrreversebleBlock(msg);
            else log.info("unrecogrinzed msg from " + "\n" + msg);
        } catch (Exception e) {
            log.error("Web socket error"  + "  : \n" + e.getMessage());
        }

    }

    private boolean isIrreversibleBlockInfo(String msg) {
        return msg.contains("previous");
    }


    protected void getUnprocessedBlocks(String msg) throws IOException {
        int currentLastIrreversibleBlock = getLastIrreversableBlock(msg);
        synchronized (this) {
            if (currentLastIrreversibleBlock > lastIrreversibleBlockValue) {
                for (; lastIrreversibleBlockValue <= currentLastIrreversibleBlock; lastIrreversibleBlockValue++) {
                    getBlock(lastIrreversibleBlockValue);
                }
            }
        }
    }

    private void requestLastIrreversibleBlock() {
        try {
            JSONObject getLastIrreversibleBlock = new JSONObject();
            getLastIrreversibleBlock.put("id", 0);
            getLastIrreversibleBlock.put("jsonrpc", "2.0");
            getLastIrreversibleBlock.put("method", "condenser_api.get_dynamic_global_properties");
            getLastIrreversibleBlock.put("params", new JSONArray());

            endpoint.sendText(getLastIrreversibleBlock.toString());
        } catch (Exception e){
            log.error(e);
        }
    }
//[{"ref_block_prefix":2158607092,"extensions":[],
// "operations":[{"type":"transfer_operation","value":{"amount":{"amount":"333","precision":3,"nai":"@@000000021"},"memo":"tester","from":"exrates-test","to":"crea"}}],"expiration":"2019-03-04T21:03:33","ref_block_num":262,"signatures":["1f70c5f1925e65d98b7fcddcb3368609ce1ea60d0fea010cbe6bd2c4557f680ae9443305ad90e5d92b8b036505c74dc787a59f09ea49b7ea6da641d6fb7fdc6810"]}]

    protected boolean isContainsLastIrreversibleBlockInfo(String jsonRpc){
        return jsonRpc.contains("last_irreversible_block_num");
    }

    public int getLastIrreversableBlock(String msg){
        return new JSONObject(msg).getJSONObject("result").getInt("last_irreversible_block_num");
    }

    protected void getBlock(int blockNum) throws IOException {
        JSONObject block = new JSONObject();
        block.put("id", 10);
        block.put("jsonrpc", "2.0");
        block.put("method", "block_api.get_block");
        block.put("params", new JSONObject().put("block_num", blockNum));

        endpoint.sendText(block.toString());
    }

    protected JSONArray extractTransactionsFromBlock(JSONObject block) {
        return block.getJSONObject("result").getJSONObject("block").getJSONArray("transactions");
    }

    private void processIrreversebleBlock(String trx) {
        JSONObject block = new JSONObject(trx);

        JSONArray transactions = extractTransactionsFromBlock(block);
        if (transactions.length() == 0) return;

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

    public static void main(String[] args) {
        String json = "{\"jsonrpc\":\"2.0\",\"result\":{\"block\":{\"previous\":\"000501216fa638faa6bc52b0c5729fc4d832bc20\",\"timestamp\":\"2019-03-04T20:53:39\",\"witness\":\"jbeuys\",\"transaction_merkle_root\":\"03f8c77f7be98a4f083f7be387015180fe9058a7\",\"extensions\":[],\"witness_signature\":\"1f474353a259e6df85418e9168c06b98589315bf1ff87076f765d900dc35bd724172f966a7dad4e3d29d2cc1a133ebe8a1a1c9a6828b071e12060445f9361c0a92\",\"transactions\":[{\"ref_block_num\":262,\"ref_block_prefix\":2158607092,\"expiration\":\"2019-03-04T21:03:33\",\"operations\":[{\"type\":\"transfer_operation\",\"value\":{\"from\":\"exrates-test\",\"to\":\"crea\",\"amount\":{\"amount\":\"333\",\"precision\":3,\"nai\":\"@@000000021\"},\"memo\":\"tester\"}}],\"extensions\":[],\"signatures\":[\"1f70c5f1925e65d98b7fcddcb3368609ce1ea60d0fea010cbe6bd2c4557f680ae9443305ad90e5d92b8b036505c74dc787a59f09ea49b7ea6da641d6fb7fdc6810\"]}],\"block_id\":\"000501224341cb2795ab9960dcd1fa46cc70026d\",\"signing_key\":\"CREA5tXhVnaUg3amsTPyR5w1S4LdLbcy7ftEHnkD6rFWdzJ3yzVj9K\",\"transaction_ids\":[\"681b473e0d257e8920fedf54c7a43c4a395e1247\"]}},\"id\":10}\n" +
                "[{\"ref_block_prefix\":2158607092,\"extensions\":[],\"operations\":[{\"type\":\"transfer_operation\",\"value\":{\"amount\":{\"amount\":\"333\",\"precision\":3,\"nai\":\"@@000000021\"},\"memo\":\"tester\",\"from\":\"exrates-test\",\"to\":\"crea\"}}],\"expiration\":\"2019-03-04T21:03:33\",\"ref_block_num\":262,\"signatures\":[\"1f70c5f1925e65d98b7fcddcb3368609ce1ea60d0fea010cbe6bd2c4557f680ae9443305ad90e5d92b8b036505c74dc787a59f09ea49b7ea6da641d6fb7fdc6810\"]}]\n";
        JSONObject object = new JSONObject(json);
        JSONArray jsonArray = object.getJSONObject("result").getJSONObject("block").getJSONArray("transactions");
        System.out.println(jsonArray);
    }
}


