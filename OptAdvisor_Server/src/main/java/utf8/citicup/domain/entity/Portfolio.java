package utf8.citicup.domain.entity;

import javax.persistence.*;

@Entity
public class Portfolio {

    public Portfolio(String username, Option[] options, Enum type, boolean trackingStatus){
        this.username = username;
        this.options = options;
        this.type = type;
        this.trackingStatus = trackingStatus;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,mappedBy = "portfolio")
    @OrderColumn
    private Option[] options;

    private Enum type; //type指1：资产配置组合 2：套期保值组合 3：DIY组合

    private boolean trackingStatus;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Option[] getOptions() {
        return options;
    }

    public void setOptions(Option[] options) {
        this.options = options;
    }

    public Enum getType() {
        return type;
    }

    public void setType(Enum type) {
        this.type = type;
    }

    public boolean isTrackingStatus() {
        return trackingStatus;
    }

    public void setTrackingStatus(boolean trackingStatus) {
        this.trackingStatus = trackingStatus;
    }

    public Long getId() { return id; }
}
