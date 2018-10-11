package cn.noload.nas.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DownloaderTask.
 */
@Entity
@Table(name = "t_nas_downloader_task")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DownloaderTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator="uuid2")
    @GenericGenerator(name="uuid2",strategy="uuid2")
    @Column(name="id", unique=true, nullable=false, updatable=false, length = 36)
    private String id;

    @Column(name = "url")
    private String url;

    @Column(name = "state")
    private Integer state;

    @Column(name = "create_time")
    private Instant createTime;

    @Column(name = "complete_time")
    private Instant completeTime;

    @Column(name = "timing")
    private Boolean timing;

    @Column(name = "start_time")
    private Instant startTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public DownloaderTask url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getState() {
        return state;
    }

    public DownloaderTask state(Integer state) {
        this.state = state;
        return this;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public DownloaderTask createTime(Instant createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public Instant getCompleteTime() {
        return completeTime;
    }

    public DownloaderTask completeTime(Instant completeTime) {
        this.completeTime = completeTime;
        return this;
    }

    public void setCompleteTime(Instant completeTime) {
        this.completeTime = completeTime;
    }

    public Boolean getTiming() {
        return timing;
    }

    public void setTiming(Boolean timing) {
        this.timing = timing;
    }

    public DownloaderTask timing(Boolean timing) {
        this.timing = timing;
        return this;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public DownloaderTask startTime(Instant startTime) {
        this.startTime = startTime;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DownloaderTask downloaderTask = (DownloaderTask) o;
        if (downloaderTask.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), downloaderTask.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DownloaderTask{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", state=" + getState() +
            ", createTime='" + getCreateTime() + "'" +
            ", completeTime='" + getCompleteTime() + "'" +
            "}";
    }
}
