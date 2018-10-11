package cn.noload.nas.service;

import cn.noload.nas.domain.DownloaderTask;
import cn.noload.nas.repository.DownloaderTaskRepository;
import cn.noload.nas.service.dto.DownloaderTaskDTO;
import cn.noload.nas.service.mapper.DownloaderTaskMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing DownloaderTask.
 */
@Service
@Transactional
public class DownloaderTaskService {

    private final Logger log = LoggerFactory.getLogger(DownloaderTaskService.class);

    private final DownloaderTaskRepository downloaderTaskRepository;

    private final DownloaderTaskMapper downloaderTaskMapper;

    public DownloaderTaskService(DownloaderTaskRepository downloaderTaskRepository, DownloaderTaskMapper downloaderTaskMapper) {
        this.downloaderTaskRepository = downloaderTaskRepository;
        this.downloaderTaskMapper = downloaderTaskMapper;
    }

    /**
     * Save a downloaderTask.
     *
     * @param downloaderTaskDTO the entity to save
     * @return the persisted entity
     */
    public DownloaderTaskDTO save(DownloaderTaskDTO downloaderTaskDTO) {
        log.debug("Request to save DownloaderTask : {}", downloaderTaskDTO);
        DownloaderTask downloaderTask = downloaderTaskMapper.toEntity(downloaderTaskDTO);
        downloaderTask = downloaderTaskRepository.save(downloaderTask);
        return downloaderTaskMapper.toDto(downloaderTask);
    }

    /**
     * Get all the downloaderTasks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DownloaderTaskDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DownloaderTasks");
        return downloaderTaskRepository.findAll(pageable)
            .map(downloaderTaskMapper::toDto);
    }


    /**
     * Get one downloaderTask by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<DownloaderTaskDTO> findOne(String id) {
        log.debug("Request to get DownloaderTask : {}", id);
        return downloaderTaskRepository.findById(id)
            .map(downloaderTaskMapper::toDto);
    }

    /**
     * Delete the downloaderTask by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete DownloaderTask : {}", id);
        downloaderTaskRepository.deleteById(id);
    }
}
