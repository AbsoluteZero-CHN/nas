package cn.noload.nas.web.rest;

import cn.noload.nas.NasApp;

import cn.noload.nas.config.SecurityBeanOverrideConfiguration;

import cn.noload.nas.domain.DownloaderTask;
import cn.noload.nas.repository.DownloaderTaskRepository;
import cn.noload.nas.service.DownloaderTaskService;
import cn.noload.nas.service.dto.DownloaderTaskDTO;
import cn.noload.nas.service.mapper.DownloaderTaskMapper;
import cn.noload.nas.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;


import static cn.noload.nas.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DownloaderTaskResource REST controller.
 *
 * @see DownloaderTaskResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, NasApp.class})
public class DownloaderTaskResourceIntTest {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATE = 1;
    private static final Integer UPDATED_STATE = 2;

    private static final Instant DEFAULT_CREATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_COMPLETE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COMPLETE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private DownloaderTaskRepository downloaderTaskRepository;


    @Autowired
    private DownloaderTaskMapper downloaderTaskMapper;
    

    @Autowired
    private DownloaderTaskService downloaderTaskService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDownloaderTaskMockMvc;

    private DownloaderTask downloaderTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DownloaderTaskResource downloaderTaskResource = new DownloaderTaskResource(downloaderTaskService);
        this.restDownloaderTaskMockMvc = MockMvcBuilders.standaloneSetup(downloaderTaskResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DownloaderTask createEntity(EntityManager em) {
        DownloaderTask downloaderTask = new DownloaderTask()
            .url(DEFAULT_URL)
            .state(DEFAULT_STATE)
            .createTime(DEFAULT_CREATE_TIME)
            .completeTime(DEFAULT_COMPLETE_TIME);
        return downloaderTask;
    }

    @Before
    public void initTest() {
        downloaderTask = createEntity(em);
    }

    @Test
    @Transactional
    public void createDownloaderTask() throws Exception {
        int databaseSizeBeforeCreate = downloaderTaskRepository.findAll().size();

        // Create the DownloaderTask
        DownloaderTaskDTO downloaderTaskDTO = downloaderTaskMapper.toDto(downloaderTask);
        restDownloaderTaskMockMvc.perform(post("/api/downloader-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(downloaderTaskDTO)))
            .andExpect(status().isCreated());

        // Validate the DownloaderTask in the database
        List<DownloaderTask> downloaderTaskList = downloaderTaskRepository.findAll();
        assertThat(downloaderTaskList).hasSize(databaseSizeBeforeCreate + 1);
        DownloaderTask testDownloaderTask = downloaderTaskList.get(downloaderTaskList.size() - 1);
        assertThat(testDownloaderTask.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testDownloaderTask.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testDownloaderTask.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testDownloaderTask.getCompleteTime()).isEqualTo(DEFAULT_COMPLETE_TIME);
    }

    @Test
    @Transactional
    public void createDownloaderTaskWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = downloaderTaskRepository.findAll().size();

        // Create the DownloaderTask with an existing ID
        downloaderTask.setId(UUID.randomUUID().toString());
        DownloaderTaskDTO downloaderTaskDTO = downloaderTaskMapper.toDto(downloaderTask);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDownloaderTaskMockMvc.perform(post("/api/downloader-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(downloaderTaskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DownloaderTask in the database
        List<DownloaderTask> downloaderTaskList = downloaderTaskRepository.findAll();
        assertThat(downloaderTaskList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDownloaderTasks() throws Exception {
        // Initialize the database
        downloaderTaskRepository.saveAndFlush(downloaderTask);

        // Get all the downloaderTaskList
        restDownloaderTaskMockMvc.perform(get("/api/downloader-tasks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(downloaderTask.getId())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].completeTime").value(hasItem(DEFAULT_COMPLETE_TIME.toString())));
    }
    

    @Test
    @Transactional
    public void getDownloaderTask() throws Exception {
        // Initialize the database
        downloaderTaskRepository.saveAndFlush(downloaderTask);

        // Get the downloaderTask
        restDownloaderTaskMockMvc.perform(get("/api/downloader-tasks/{id}", downloaderTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(downloaderTask.getId()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.toString()))
            .andExpect(jsonPath("$.completeTime").value(DEFAULT_COMPLETE_TIME.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingDownloaderTask() throws Exception {
        // Get the downloaderTask
        restDownloaderTaskMockMvc.perform(get("/api/downloader-tasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDownloaderTask() throws Exception {
        // Initialize the database
        downloaderTaskRepository.saveAndFlush(downloaderTask);

        int databaseSizeBeforeUpdate = downloaderTaskRepository.findAll().size();

        // Update the downloaderTask
        DownloaderTask updatedDownloaderTask = downloaderTaskRepository.findById(downloaderTask.getId()).get();
        // Disconnect from session so that the updates on updatedDownloaderTask are not directly saved in db
        em.detach(updatedDownloaderTask);
        updatedDownloaderTask
            .url(UPDATED_URL)
            .state(UPDATED_STATE)
            .createTime(UPDATED_CREATE_TIME)
            .completeTime(UPDATED_COMPLETE_TIME);
        DownloaderTaskDTO downloaderTaskDTO = downloaderTaskMapper.toDto(updatedDownloaderTask);

        restDownloaderTaskMockMvc.perform(put("/api/downloader-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(downloaderTaskDTO)))
            .andExpect(status().isOk());

        // Validate the DownloaderTask in the database
        List<DownloaderTask> downloaderTaskList = downloaderTaskRepository.findAll();
        assertThat(downloaderTaskList).hasSize(databaseSizeBeforeUpdate);
        DownloaderTask testDownloaderTask = downloaderTaskList.get(downloaderTaskList.size() - 1);
        assertThat(testDownloaderTask.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testDownloaderTask.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testDownloaderTask.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testDownloaderTask.getCompleteTime()).isEqualTo(UPDATED_COMPLETE_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingDownloaderTask() throws Exception {
        int databaseSizeBeforeUpdate = downloaderTaskRepository.findAll().size();

        // Create the DownloaderTask
        DownloaderTaskDTO downloaderTaskDTO = downloaderTaskMapper.toDto(downloaderTask);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restDownloaderTaskMockMvc.perform(put("/api/downloader-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(downloaderTaskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DownloaderTask in the database
        List<DownloaderTask> downloaderTaskList = downloaderTaskRepository.findAll();
        assertThat(downloaderTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDownloaderTask() throws Exception {
        // Initialize the database
        downloaderTaskRepository.saveAndFlush(downloaderTask);

        int databaseSizeBeforeDelete = downloaderTaskRepository.findAll().size();

        // Get the downloaderTask
        restDownloaderTaskMockMvc.perform(delete("/api/downloader-tasks/{id}", downloaderTask.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DownloaderTask> downloaderTaskList = downloaderTaskRepository.findAll();
        assertThat(downloaderTaskList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DownloaderTask.class);
        DownloaderTask downloaderTask1 = new DownloaderTask();
        downloaderTask1.setId(UUID.randomUUID().toString());
        DownloaderTask downloaderTask2 = new DownloaderTask();
        downloaderTask2.setId(downloaderTask1.getId());
        assertThat(downloaderTask1).isEqualTo(downloaderTask2);
        downloaderTask2.setId(UUID.randomUUID().toString());
        assertThat(downloaderTask1).isNotEqualTo(downloaderTask2);
        downloaderTask1.setId(null);
        assertThat(downloaderTask1).isNotEqualTo(downloaderTask2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DownloaderTaskDTO.class);
        DownloaderTaskDTO downloaderTaskDTO1 = new DownloaderTaskDTO();
        downloaderTaskDTO1.setId(UUID.randomUUID().toString());
        DownloaderTaskDTO downloaderTaskDTO2 = new DownloaderTaskDTO();
        assertThat(downloaderTaskDTO1).isNotEqualTo(downloaderTaskDTO2);
        downloaderTaskDTO2.setId(downloaderTaskDTO1.getId());
        assertThat(downloaderTaskDTO1).isEqualTo(downloaderTaskDTO2);
        downloaderTaskDTO2.setId(UUID.randomUUID().toString());
        assertThat(downloaderTaskDTO1).isNotEqualTo(downloaderTaskDTO2);
        downloaderTaskDTO1.setId(null);
        assertThat(downloaderTaskDTO1).isNotEqualTo(downloaderTaskDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(downloaderTaskMapper.fromId(UUID.randomUUID().toString()).getId()).isEqualTo(42);
        assertThat(downloaderTaskMapper.fromId(null)).isNull();
    }
}
