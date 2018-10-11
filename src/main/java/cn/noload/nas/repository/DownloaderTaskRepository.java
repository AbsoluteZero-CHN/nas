package cn.noload.nas.repository;

import cn.noload.nas.domain.DownloaderTask;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DownloaderTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DownloaderTaskRepository extends JpaRepository<DownloaderTask, String> {

}
