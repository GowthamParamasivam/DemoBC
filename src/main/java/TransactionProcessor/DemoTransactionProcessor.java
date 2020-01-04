package TransactionProcessor;


import sawtooth.sdk.processor.TransactionProcessor;

public class DemoTransactionProcessor {

    public static void main(String... args){
//        TransactionProcessor transactionProcessor0 = new TransactionProcessor("tcp://192.168.99.100:4005");
//        TransactionProcessor transactionProcessor1 = new TransactionProcessor("tcp://192.168.99.100:4006");
//        TransactionProcessor transactionProcessor2 = new TransactionProcessor("tcp://192.168.99.100:4007");
//        TransactionProcessor transactionProcessor3 = new TransactionProcessor("tcp://192.168.99.100:4008");
//        TransactionProcessor transactionProcessor4 = new TransactionProcessor("tcp://192.168.99.100:4009");
//        transactionProcessor0.addHandler(new DemoTransactionHandler());
//        transactionProcessor1.addHandler(new DemoTransactionHandler());
//        transactionProcessor2.addHandler(new DemoTransactionHandler());
//        transactionProcessor3.addHandler(new DemoTransactionHandler());
//        transactionProcessor4.addHandler(new DemoTransactionHandler());
//        Thread thread0 = new Thread(transactionProcessor0);
//        Thread thread1 = new Thread(transactionProcessor1);
//        Thread thread2 = new Thread(transactionProcessor2);
//        Thread thread3 = new Thread(transactionProcessor3);
//        Thread thread4 = new Thread(transactionProcessor4);
//        thread0.start();
//        thread1.start();
//        thread2.start();
//        thread3.start();
//        thread4.start();

        //For one node
        TransactionProcessor transactionProcessor0 = new TransactionProcessor("tcp://192.168.99.100:4004");
        transactionProcessor0.addHandler(new DemoTransactionHandler());
        Thread thread0 = new Thread(transactionProcessor0);
        thread0.start();

    }
}
