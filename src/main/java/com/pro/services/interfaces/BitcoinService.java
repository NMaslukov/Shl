package com.pro.services.interfaces;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.client.BtcdClient;
import com.neemre.btcdcli4j.core.client.BtcdClientImpl;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

public class BitcoinService {
    public static final String addrWithBablo = "2NEpZpjrFajV8PznMnF3vA41wVPbgkudF9H";
    public static final String pass = "pass123";
    private final String ticker;

    private BtcdClient btcdClient;

    public BitcoinService(String ticker) throws BitcoindException, IOException, CommunicationException {
        this.ticker = ticker;
        build(); //todo remove
    }

    @PostConstruct
    private void build() throws BitcoindException, CommunicationException, IOException {
        try {
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();

            RequestConfig.Builder rc = RequestConfig.custom().setConnectTimeout(3000).setConnectionRequestTimeout(3000);

            CloseableHttpClient httpProvider = HttpClients.custom().setConnectionManager(cm).setDefaultRequestConfig(rc.build())
                    .build();
            Properties nodeConfig = new Properties();
            nodeConfig.load(getClass().getClassLoader().getResourceAsStream("node_configs/BTC_node_config.properties"));

            btcdClient = new BtcdClientImpl(httpProvider, nodeConfig);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void run() throws BitcoindException, IOException, CommunicationException {
//        BtcdClient btcdClient = getBtcdClient();
//        btcdClient.walletPassphrase("pass123", 10000);
//        BigDecimal unconfirmedBalance = btcdClient.getBalance(addrWithBablo);
//        System.out.println(btcdClient.getBalance(addrWithBablo));
//        System.out.println(btcdClient.getUnconfirmedBalance());
//        System.out.println(btcdClient.listAccounts());
//        System.out.println(btcdClient.getBalance());
//        System.out.println(btcdClient.getBalance(addrWithBablo));
//        btcdClient.setAccount(addrWithBablo,"bablo");
        String newAddress = btcdClient.getNewAddress();
//        btcdClient.setAccount(newAddress,"test");
        btcdClient.setAccount(addrWithBablo, "bablo");
        Map<String, BigDecimal> accounts = btcdClient.listAccounts();

        System.out.println(accounts);
    }


    String generateNewAddress() throws BitcoindException, IOException, CommunicationException {
        return btcdClient.getNewAddress();
    }

    public static void main(String[] args) throws BitcoindException, IOException, CommunicationException {
        BitcoinService a = new BitcoinService("BTC");
        a.run();
    }
}
