package com.pro.services.interfaces;

public class BitcoinService {
//    public static final String addrWithBablo = "2NEpZpjrFajV8PznMnF3vA41wVPbgkudF9H";
//    public static final String pass = "pass123";
//    private final String ticker;
//
//    private BtcdClient btcdClient;
//
//    public BitcoinService(String ticker) {
//        this.ticker = ticker;
//    }
//
//    @PostConstruct
//    private void build() throws BitcoindException, CommunicationException, IOException {
//        try {
//            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
//
//            RequestConfig.Builder rc = RequestConfig.custom().setConnectTimeout(3000).setConnectionRequestTimeout(3000);
//
//            CloseableHttpClient httpProvider = HttpClients.custom().setConnectionManager(cm).setDefaultRequestConfig(rc.build())
//                    .build();
//            Properties nodeConfig = new Properties();
//            nodeConfig.load(getClass().getClassLoader().getResourceAsStream("node_configs/BTC_node_config.properties"));
//
//            btcdClient = new BtcdClientImpl(httpProvider, nodeConfig);
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    private void run() throws BitcoindException, IOException, CommunicationException {
////        BtcdClient btcdClient = getBtcdClient();
////        btcdClient.walletPassphrase("pass123", 10000);
////        BigDecimal unconfirmedBalance = btcdClient.getBalance(addrWithBablo);
////        System.out.println(btcdClient.getBalance(addrWithBablo));
////        System.out.println(btcdClient.getUnconfirmedBalance());
////        System.out.println(btcdClient.listAccounts());
////        System.out.println(btcdClient.getBalance());
////        System.out.println(btcdClient.getBalance(addrWithBablo));
////        btcdClient.setAccount(addrWithBablo,"bablo");
//        build();
////        String newAddress = btcdClient.getNewAddress();
////        btcdClient.setAccount(newAddress,"test");
////        btcdClient.setAccount(addrWithBablo, "bablo");
//        Map<String, BigDecimal> accounts = btcdClient.listAccounts();
//
//
//        System.out.println(accounts);
//        System.out.println(btcdClient.getAddressesByAccount(""));
////        btcdClient.walletPassphrase("pass123", 100);
////        btcdClient.sendToAddress("2MstsiDrAgJsacMDvtQ4caroiuKDij2LAGM", new BigDecimal("0.001"));
////        btcdClient.sendToAddress("2MstsiDrAgJsacMDvtQ4caroiuKDij2LAGM", new BigDecimal("0.001"));
//    }
//
//
//    String generateNewAddress() throws BitcoindException, IOException, CommunicationException {
//        return btcdClient.getNewAddress();
//    }
//
//    public static void main(String[] args) throws BitcoindException, IOException, CommunicationException {
//        BitcoinService a = new BitcoinService("BTC");
//        a.run();
//    }
}