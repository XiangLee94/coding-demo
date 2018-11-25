import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.HTTPServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;



public class PrometheusClient {

    private static final Logger logger  = LoggerFactory.getLogger(PrometheusClient.class);

    private static HTTPServer server;

    //public static final Counter INPUTCOUNT = Counter.build().name("input_row_count").labelNames("instance").help("The count of  all canal input").register();

    public static final Counter INPUTCOUNT = Counter.build().name("input_row_count").help("The count of  all canal input").register();

    public static final Counter HBASEOUTALL = Counter.build().name("hbase_output_row_count_all").labelNames("opr").help("The count of  all hbase output all").register();

    public static final Counter KAFKAOUTALL = Counter.build().name("kafka_output_row_count_all").help("The count of  all kafka output all").register();

    public static final Counter HBASEOUTFAIL = Counter.build().name("hbase_output_row_count_fail").labelNames("opr").help("The count of  all hbase output fail").register();

    public static final Counter KAFKAOUTFAIL = Counter.build().name("kafka_output_row_count_fail").help("The count of  all kafka output fail ").register();

    public static final Counter BATCHFAIL = Counter.build().name("canal_batch_output_fail").labelNames("channel").help("The count of  canal batch output fail ").register();

    public static final Gauge INSTANCECOUINT = Gauge.build().name("current_instance_count").help("The count of cuerrent instance ").register();

    public static final Gauge TASKTIMECOST = Gauge.build().name("task_time_cost").labelNames("taskname").help("The cost time (ms) of task ").register();


    public void init() {
        try {
            logger.info("Start prometheus HTTPServer on port {}.", 4567);
            server = new HTTPServer(4567);
        } catch (IOException e) {
            logger.warn("Unable to start prometheus HTTPServer.", e);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        PrometheusClient p = new PrometheusClient();
        p.init();
        while (true){
            INPUTCOUNT.inc();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



    }


}
