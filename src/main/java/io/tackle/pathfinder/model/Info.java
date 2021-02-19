package io.tackle.pathfinder.model;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Root Type for info
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "date",
    "uptime"
})
public class Info {

    @JsonProperty("date")
    private Date date;
    @JsonProperty("uptime")
    private Integer uptime;

    @JsonProperty("date")
    public Date getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(Date date) {
        this.date = date;
    }

    @JsonProperty("uptime")
    public Integer getUptime() {
        return uptime;
    }

    @JsonProperty("uptime")
    public void setUptime(Integer uptime) {
        this.uptime = uptime;
    }

}
