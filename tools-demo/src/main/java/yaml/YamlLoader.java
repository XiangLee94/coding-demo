package yaml;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class YamlLoader {
    public static void main(String[] args) {
        Map<String,TaskConfigs.TaskConfig> propMap = new HashMap<String,TaskConfigs.TaskConfig>();
        Yaml yaml = new Yaml();
        TaskConfigs taskConfigs = null;
        try {
            taskConfigs = yaml.loadAs(new FileInputStream("E:/config.yaml"), TaskConfigs.class);
            System.out.println(taskConfigs);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}

