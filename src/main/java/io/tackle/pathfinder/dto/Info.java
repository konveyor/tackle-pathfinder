package io.tackle.pathfinder.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

import java.time.Instant;
import java.util.Date;


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
@Builder
public class Info {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    @JsonProperty("date")
    private Instant date;

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
