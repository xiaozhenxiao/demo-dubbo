package enumtest;

/**
 * Created by wangzhen on 2016-07-28.
 */
public enum TaskMessageType implements MessageType{
    TYPE1("任务1", 0),TYPE2("任务2", 1),TYPE3("任务3", 2);

    private String name;
    private Integer type;

    public static String getName(Integer type) {
        for (TaskMessageType t: TaskMessageType.values()) {
            if (t.getType() == type) {
                return t.name;
            }
        }
        return null;
    }

    public static TaskMessageType getStatus(Integer type) {
        for (TaskMessageType t : TaskMessageType.values()) {
            if (t.getType() == type) {
                return t;
            }
        }
        return null;
    }

    TaskMessageType(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public MessageType getMessageType(int type) {
        for (TaskMessageType t : TaskMessageType.values()) {
            if (t.getType() == type) {
                return t;
            }
        }
        return null;
    }
}
