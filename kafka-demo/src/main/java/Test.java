import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Deque;

public class Test {
    public static void main(String[] args) {
        ProducerDemo producer = new ProducerDemo("192.168.0.80:9092");
        producer.sendAndAsycCallBack("laolitest","111","{\"etlTime\":1541260801759,\"jsonData\":\"{\\\"advertiser\\\":\\\"\\\",\\\"carrier\\\":\\\"\\\",\\\"chat_id\\\":\\\"46056523\\\",\\\"city\\\":\\\"乌鲁木齐\\\",\\\"city_id\\\":\\\"310\\\",\\\"consultant_account\\\":\\\"\\\",\\\"create_time\\\":\\\"2018-11-03 23:59:59\\\",\\\"create_user\\\":\\\"zhaoliangqian-pq\\\",\\\"first_proj\\\":\\\"自考\\\",\\\"first_proj_id\\\":\\\"32\\\",\\\"id\\\":\\\"9142328\\\",\\\"mobile\\\":\\\"18999711950\\\",\\\"mobile_bak\\\":\\\"\\\",\\\"next_contact_time\\\":\\\"\\\",\\\"opportunity_id\\\":\\\"33931834\\\",\\\"phone\\\":\\\"\\\",\\\"project_manager\\\":\\\"\\\",\\\"promotion_method\\\":\\\"\\\",\\\"promotion_type\\\":\\\"\\\",\\\"province\\\":\\\"新疆\\\",\\\"province_id\\\":\\\"15\\\",\\\"qq\\\":\\\"\\\",\\\"remark\\\":\\\"\\\",\\\"site_id\\\":\\\"700118723\\\",\\\"source_code\\\":\\\"ONLINE_CS_GREATBEAR\\\",\\\"stu_city_id\\\":\\\"310\\\",\\\"stu_province_id\\\":\\\"15\\\",\\\"student_id\\\":\\\"\\\",\\\"student_name\\\":\\\"50\\\",\\\"tableName\\\":\\\"drag_opportunity_history\\\",\\\"wechat\\\":\\\"\\\",\\\"zone_code\\\":\\\"\\\"}\",\"opr\":\"INSERT\",\"rowKey\":\"9142328\",\"seqNum\":1541260801759,\"tblName\":\"dragnet_opportunity_history\"}");
        producer.close();
    }
    public void test(){
    }
}
