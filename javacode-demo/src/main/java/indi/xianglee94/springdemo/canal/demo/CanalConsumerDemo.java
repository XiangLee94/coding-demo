package indi.xianglee94.javacode.canal.demo;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.Message;

import java.net.InetSocketAddress;
import java.util.List;

public class CanalConsumerDemo {
    public static void main(String[] args) {
        CanalConsumerDemo c = new CanalConsumerDemo();
        c.run();
    }

    public void run() {
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress("172.16.117.73", 11115), "instance_test_ddl", "hive", "hive");
        connector.connect();
        connector.subscribe();
//        CanalUtil.subscribeTables(connector, canalDB, tableToTopic.keySet());//!!
        connector.rollback();

        Thread currentThread = Thread.currentThread();
        while (!currentThread.isInterrupted()) {//!!
            System.out.println("nodata");
            Message message = connector.getWithoutAck(1024);
            long batchId = message.getId();
            List<CanalEntry.Entry> entries = message.getEntries();
            int size = entries.size();
            if (batchId == -1 || size == 0) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    currentThread.interrupt();
                }
            } else {
                processEntries(entries);
            }
            connector.ack(batchId);
        }
    }

    private void processEntries(List<CanalEntry.Entry> entries) {
        for (CanalEntry.Entry entry : entries) {
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND)
                continue;
            try {
                CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
                EventType eventType = rowChange.getEventType();
                if(eventType == EventType.ALTER){
                    System.out.println(rowChange.getSql());
                }

                List<CanalEntry.RowData> rowDatas = rowChange.getRowDatasList();
                for (CanalEntry.RowData rowData : rowDatas) {
                    try {
                        switch (eventType) {
                            case INSERT:
                                System.out.println(rowData.getAfterColumnsList());
//                            result = Executor.executeJob(ConfigTableObserver.getEtlJob(schemaName + "." + tableName), CanalUtil.canalToMap(rowData.getAfterColumnsList()));
                                break;
                            case UPDATE:
                                System.out.println(rowData.getAfterColumnsList());
//                            result = Executor.executeJob(ConfigTableObserver.getEtlJob(schemaName + "." + tableName), CanalUtil.canalToMap(rowData.getAfterColumnsList()));
                                break;
                            case DELETE:
                                System.out.println(rowData.getAfterColumnsList());
                                break;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String transMysqlDDLToPhoenixDDL(String mysqlDdl){

        return null;
    }


}
