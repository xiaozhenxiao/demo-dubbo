package enumtest;

/**
 * Created by wangzhen on 2016-07-28.
 */
public enum CallbackMessageType implements MessageType{
    TYPE1("微信支付", 0),TYPE2("支付宝支付", 1),TYPE3("银联支付", 2);

    private String name;
    private Integer type;

    public static String getName(Integer type) {
        for (CallbackMessageType t: CallbackMessageType.values()) {
            if (t.getType() == type) {
                return t.name;
            }
        }
        return null;
    }

    public static CallbackMessageType getStatus(Integer type) {
        for (CallbackMessageType t : CallbackMessageType.values()) {
            if (t.getType() == type) {
                return t;
            }
        }
        return null;
    }

    CallbackMessageType(String name, int type) {
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
        for (CallbackMessageType t : CallbackMessageType.values()) {
            if (t.getType() == type) {
                return t;
            }
        }
        return null;
    }
}
