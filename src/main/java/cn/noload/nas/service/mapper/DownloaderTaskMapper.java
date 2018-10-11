package cn.noload.nas.service.mapper;

import cn.noload.nas.domain.*;
import cn.noload.nas.service.dto.DownloaderTaskDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity DownloaderTask and its DTO DownloaderTaskDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DownloaderTaskMapper extends EntityMapper<DownloaderTaskDTO, DownloaderTask> {



    default DownloaderTask fromId(String id) {
        if (id == null) {
            return null;
        }
        DownloaderTask downloaderTask = new DownloaderTask();
        downloaderTask.setId(id);
        return downloaderTask;
    }
}
