package cn.noload.nas.web.rest;

import com.codahale.metrics.annotation.Timed;
import cn.noload.nas.service.DownloaderTaskService;
import cn.noload.nas.web.rest.errors.BadRequestAlertException;
import cn.noload.nas.web.rest.util.HeaderUtil;
import cn.noload.nas.web.rest.util.PaginationUtil;
import cn.noload.nas.service.dto.DownloaderTaskDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing DownloaderTask.
 */
@RestController
@RequestMapping("/api")
public class DownloaderTaskResource {

    private final Logger log = LoggerFactory.getLogger(DownloaderTaskResource.class);

    private static final String ENTITY_NAME = "downloaderTask";

    private final DownloaderTaskService downloaderTaskService;

    public DownloaderTaskResource(DownloaderTaskService downloaderTaskService) {
        this.downloaderTaskService = downloaderTaskService;
    }

    /**
     * POST  /downloader-tasks : Create a new downloaderTask.
     *
     * @param downloaderTaskDTO the downloaderTaskDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new downloaderTaskDTO, or with status 400 (Bad Request) if the downloaderTask has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/downloader-tasks")
    @Timed
    public ResponseEntity<DownloaderTaskDTO> createDownloaderTask(@RequestBody DownloaderTaskDTO downloaderTaskDTO) throws URISyntaxException {
        log.debug("REST request to save DownloaderTask : {}", downloaderTaskDTO);
        if (downloaderTaskDTO.getId() != null) {
            throw new BadRequestAlertException("A new downloaderTask cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DownloaderTaskDTO result = downloaderTaskService.save(downloaderTaskDTO);
        return ResponseEntity.created(new URI("/api/downloader-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /downloader-tasks : Updates an existing downloaderTask.
     *
     * @param downloaderTaskDTO the downloaderTaskDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated downloaderTaskDTO,
     * or with status 400 (Bad Request) if the downloaderTaskDTO is not valid,
     * or with status 500 (Internal Server Error) if the downloaderTaskDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/downloader-tasks")
    @Timed
    public ResponseEntity<DownloaderTaskDTO> updateDownloaderTask(@RequestBody DownloaderTaskDTO downloaderTaskDTO) throws URISyntaxException {
        log.debug("REST request to update DownloaderTask : {}", downloaderTaskDTO);
        if (downloaderTaskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DownloaderTaskDTO result = downloaderTaskService.save(downloaderTaskDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, downloaderTaskDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /downloader-tasks : get all the downloaderTasks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of downloaderTasks in body
     */
    @GetMapping("/downloader-tasks")
    @Timed
    public ResponseEntity<List<DownloaderTaskDTO>> getAllDownloaderTasks(Pageable pageable) {
        log.debug("REST request to get a page of DownloaderTasks");
        Page<DownloaderTaskDTO> page = downloaderTaskService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/downloader-tasks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /downloader-tasks/:id : get the "id" downloaderTask.
     *
     * @param id the id of the downloaderTaskDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the downloaderTaskDTO, or with status 404 (Not Found)
     */
    @GetMapping("/downloader-tasks/{id}")
    @Timed
    public ResponseEntity<DownloaderTaskDTO> getDownloaderTask(@PathVariable String id) {
        log.debug("REST request to get DownloaderTask : {}", id);
        Optional<DownloaderTaskDTO> downloaderTaskDTO = downloaderTaskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(downloaderTaskDTO);
    }

    /**
     * DELETE  /downloader-tasks/:id : delete the "id" downloaderTask.
     *
     * @param id the id of the downloaderTaskDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/downloader-tasks/{id}")
    @Timed
    public ResponseEntity<Void> deleteDownloaderTask(@PathVariable String id) {
        log.debug("REST request to delete DownloaderTask : {}", id);
        downloaderTaskService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
