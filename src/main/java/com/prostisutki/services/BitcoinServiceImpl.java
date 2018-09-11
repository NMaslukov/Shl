package com.prostisutki.services;

import com.neemre.btcdcli4j.core.client.BtcdClient;
import com.neemre.btcdcli4j.core.client.BtcdClientImpl;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.Properties;

public class BitcoinServiceImpl {

    public static void main(String[] args) throws Exception {
        BitcoinServiceImpl bitcoinService = new BitcoinServiceImpl();
        bitcoinService.generateNewAddress();
    }

    void generateNewAddress() throws Exception{
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        CloseableHttpClient httpProvider = HttpClients.custom().setConnectionManager(cm)
                .build();
        Properties nodeConfig = new Properties();
        nodeConfig.load(getClass().getClassLoader().getResourceAsStream("node_configs/sic_node_config.properties"));
        BtcdClient client = new BtcdClientImpl(httpProvider, nodeConfig);
        String newAddress = client.getNewAddress();
        System.out.println(newAddress);
        System.out.println(client.getInfo());
    }
}
