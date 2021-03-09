package io.tackle.pathfinder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.identity.SecurityIdentity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.enterprise.inject.spi.CDI;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.Instant;
import java.util.Objects;

@MappedSuperclass
public abstract class AbstractEntity extends PanacheEntity {
    //    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSSx")
    @JsonIgnore
    @CreationTimestamp
    @Column(updatable=false)
    public Instant createTime;
    //    @JsonIgnore
    public String createUser;
    //    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSSx")
    @JsonIgnore
    @UpdateTimestamp
    public Instant updateTime;
    //    @JsonIgnore
    public String updateUser;
    @JsonIgnore
    public Boolean deleted = false;

    @PrePersist
    protected void onCreate() {
        final String username = getUsername();
        createUser = username;
        updateUser = username;
    }

    @PreUpdate
    protected void onUpdate() {
        updateUser = getUsername();
    }

    private String getUsername() {
        // based on the Quarkus issue
        // https://github.com/quarkusio/quarkus/issues/6948#issuecomment-619872942
        SecurityIdentity context = CDI.current().select(SecurityIdentity.class).get();
        if (Objects.nonNull(context)) {
            String username = context.getPrincipal().getName();
            if (Objects.nonNull(username)) {
                return username;
            }
        }
        // since all service are authenticated, it should never get here
        // so maybe worth evaluating to throw an exception? food for thoughts
        return "";
    }

}

