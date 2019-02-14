package codingdemo.springbootdemo.config;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

import static codingdemo.springbootdemo.config.ApolloClient.APPLICATION;

@Configuration
@EnableApolloConfig({APPLICATION})
public class ApolloClient {

    public static final String APPLICATION = "normal_etl_jobs";

    @ApolloConfig(APPLICATION)
    private Config applicationConfig;

    @ApolloConfigChangeListener(APPLICATION)
    private void flusherConfigChanged(ConfigChangeEvent changeEvent){
        final Set<String> changedKeys = changeEvent.changedKeys();
        for (String changedKey : changedKeys) {
            final ConfigChange change = changeEvent.getChange(changedKey);
            final String changeType = change.getChangeType().toString();
            System.out.println("changedKey is "+changedKey);
        }
    }

    public static void main(String[] args) {
        Config config = ConfigService.getAppConfig(); //config instance is singleton for each namespace and is never null
        String someKey = "name";
        String someDefaultValue = "someDefaultValueForTheKey";
        String value = config.getProperty(someKey, someDefaultValue);
        System.out.println(value);
    }
}
