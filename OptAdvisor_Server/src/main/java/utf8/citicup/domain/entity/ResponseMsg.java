package utf8.citicup.domain.entity;

public class ResponseMsg {

    private int code;
    private String msg;
    private Object data;

    public ResponseMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseMsg(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
