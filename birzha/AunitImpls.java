package birzha;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;

//@ClientEndpoint
//@Service
public class AunitImpls extends AunitWsServiceImpl {


    @OnMessage
    @Override
    public void onMessage(String msg) {
        System.out.println(msg);
//        log.info(msg);
//        try {
//            if (msg.contains("last_irreversible_block_num")) setIrreversableBlock(msg);
//            else if (msg.contains("previous")) processIrreversebleBlock(msg);
//            else log.info("unrecogrinzed msg from " + merchantName + "\n" + msg);
//        } catch (Exception e) {
//            log.error("Web socket error" + merchantName + "  : \n" + e.getMessage());
//        }

    }

    @Override
    protected void setIrreversableBlock(String msg) {
        System.out.println("overrided");
//        JSONObject message = new JSONObject(msg);
//        int blockNumber = message.getJSONArray("params").getJSONArray(1).getJSONArray(0).getJSONObject(3).getInt(lastIrreversebleBlockParam);
//        synchronized (this) {
//            if (blockNumber > lastIrreversibleBlockValue) {
//                for (; lastIrreversibleBlockValue <= blockNumber; lastIrreversibleBlockValue++) {
//                    getBlock(lastIrreversibleBlockValue);
//                }
//                merchantSpecParamsDao.updateParam(merchant.getName(), lastIrreversebleBlockParam, String.valueOf(lastIrreversibleBlockValue));
//            }
//        }
    }
}
