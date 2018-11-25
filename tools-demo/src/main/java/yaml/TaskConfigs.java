package yaml;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public  class TaskConfigs implements Serializable {

    private List<TaskConfig> tasks;

    public List<TaskConfig> getTasks() {
        return tasks;
    }


    public void setTasks(List<TaskConfig> tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskConfigs)) return false;
        TaskConfigs that = (TaskConfigs) o;
        return Objects.equals(getTasks(), that.getTasks());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getTasks());
    }

    @Override
    public String toString() {
        return "TaskConfigs{" +
                "tasks=" + tasks +
                '}';
    }

    public static class TaskConfig implements Serializable {

        private String name;
        private Map<String,Output> output;
        private Input input;
        private boolean isRunning = true;

        @Override
        public String toString() {
            return "TaskConfig{" +
                    "name='" + name + '\'' +
                    ", output=" + output +
                    ", input=" + input +
                    ", isRunning=" + isRunning +
                    '}';
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map<String, Output> getOutput() {
            return output;
        }

        public void setOutput(Map<String, Output> output) {
            this.output = output;
        }

        public Input getInput() {
            return input;
        }

        public void setInput(Input input) {
            this.input = input;
        }

        public boolean isRunning() {
            return isRunning;
        }

        public void setRunning(boolean running) {
            isRunning = running;
        }

        public static class Output implements Serializable {
            private List<String> whiteList;
            private List<String> rowkeyMap;
            private Map<String, String> configMap;
            private Map<String, List<String>> transHrowKeyMap;

            @Override
            public String toString() {
                return "Output{" +
                        "whiteList=" + whiteList +
                        ", rowkeyMap=" + rowkeyMap +
                        ", configMap=" + configMap +
                        ", transHrowKeyMap=" + transHrowKeyMap +
                        '}';
            }

            public List<String> getWhiteList() {
                return whiteList;
            }

            public void setWhiteList(List<String> whiteList) {
                this.whiteList = whiteList;
            }

            public List<String> getRowkeyMap() {
                return rowkeyMap;
            }

            public void setRowkeyMap(List<String> rowkeyMap) {
                this.rowkeyMap = rowkeyMap;
            }

            public Map<String, String> getConfigMap() {
                return configMap;
            }

            public void setConfigMap(Map<String, String> configMap) {
                this.configMap = configMap;
            }

            public Map<String, List<String>> getTransHrowKeyMap() {
                return transHrowKeyMap;
            }

            public void setTransHrowKeyMap(Map<String, List<String>> transHrowKeyMap) {
                this.transHrowKeyMap = transHrowKeyMap;
            }
        }


        public static class Input implements  Serializable{
            private Map<String,String> tableNameMap;
            private String host;
            private String instance;

            @Override
            public String toString() {
                return "Input{" +
                        "tableNameMap=" + tableNameMap +
                        ", host='" + host + '\'' +
                        ", instance='" + instance + '\'' +
                        '}';
            }

            public Map<String, String> getTableNameMap() {
                return tableNameMap;
            }

            public void setTableNameMap(Map<String, String> tableNameMap) {
                this.tableNameMap = tableNameMap;
            }

            public String getHost() {
                return host;
            }

            public void setHost(String host) {
                this.host = host;
            }

            public String getInstance() {
                return instance;
            }

            public void setInstance(String instance) {
                this.instance = instance;
            }
        }
    }
}

