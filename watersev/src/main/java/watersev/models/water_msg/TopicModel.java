package watersev.models.water_msg;

public class TopicModel {
    public int topic_id;
    public int max_msg_num;
    public int max_distribution_num;//最大派发次数（0不限）
    public int max_concurrency_num;//最大同时派发数(0不限）

    public int alarm_model; //报警模式：0=普通模式；1=不报警
}
