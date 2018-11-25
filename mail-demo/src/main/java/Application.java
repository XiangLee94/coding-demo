
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com/sunlands/datacenter/mail"})
@EnableAutoConfiguration
@MapperScan("canal2mysql.dao")
@EnableScheduling
public class Application  {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}