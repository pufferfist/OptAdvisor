package utf8.citicup.domain.common;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize
public enum Type {
    //资产配置，套期保值，
    RECOMMMEND_PORTFOLIO,HEDGE,DIY;

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}
