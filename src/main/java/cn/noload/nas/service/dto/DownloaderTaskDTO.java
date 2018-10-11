package cn.noload.nas.service.dto;

import cn.noload.nas.domain.DownloaderTask;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the DownloaderTask entity.
 */
public class DownloaderTaskDTO implements Serializable {

    private String id;

    private String url;

    private Integer state;

    private String stateName;

    private Instant createTime;

    private Instant completeTime;

    private Boolean timing;

    private Instant startTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        setStateNameByState(state);
        this.state = state;
    }

    public String getStateName() {
        return stateName;
    }

    public DownloaderTaskDTO setStateName(String stateName) {
        this.stateName = stateName;
        return this;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public Instant getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Instant completeTime) {
        this.completeTime = completeTime;
    }

    public DownloaderTaskDTO id(String id) {
        this.id = id;
        return this;
    }

    public DownloaderTaskDTO url(String url) {
        this.url = url;
        return this;
    }

    public DownloaderTaskDTO completeTime(Instant completeTime) {
        this.completeTime = completeTime;
        return this;
    }

    public DownloaderTaskDTO stateName(String stateName) {
        this.stateName = stateName;
        return this;
    }

    public DownloaderTaskDTO state(Integer state) {
        setStateNameByState(state);
        this.state = state;
        return this;
    }

    public DownloaderTaskDTO createTime(Instant createTime) {
        this.createTime = createTime;
        return this;
    }

    public Boolean getTiming() {
        return timing;
    }

    public void setTiming(Boolean timing) {
        this.timing = timing;
    }

    public DownloaderTaskDTO timing(Boolean timing) {
        this.timing = timing;
        return this;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public DownloaderTaskDTO startTime(Instant startTime) {
        this.startTime = startTime;
        return this;
    }

    private void setStateNameByState(Integer state) {
        if(state == null) {
            return;
        } else if(state == 0) {
            this.stateName = "任务已完成";
        } else if(state == 1) {
            this.stateName = "下载中";
        }else if(state == 2) {
            this.stateName = "任务暂停";
        }else if(state == 3) {
            this.stateName = "任务失败";
        } else if(state == 4) {
            this.stateName = "任务已取消";
        } else if(state == 5) {
            this.stateName = "任务已暂停";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DownloaderTaskDTO downloaderTaskDTO = (DownloaderTaskDTO) o;
        if (downloaderTaskDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), downloaderTaskDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DownloaderTaskDTO{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", state=" + getState() +
            ", createTime='" + getCreateTime() + "'" +
            ", completeTime='" + getCompleteTime() + "'" +
            "}";
    }
}
