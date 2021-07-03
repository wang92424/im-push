package com.push.dao.repository;

import com.push.constants.Constants;
import com.push.dao.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional(rollbackFor = Exception.class)
public interface SessionRepository extends JpaRepository<Session, Long> {

	@Modifying
	@Query("delete from Session where uid = ?1 and nid = ?2")
	void delete(String uid, String nid);

	@Modifying
	@Query("delete from Session where host = ?1 ")
	void deleteAll(String host);

	@Modifying
	@Query("update Session set state = ?3 where uid = ?1 and nid = ?2")
	void updateState(String uid, String nid, int state);

	@Modifying
	@Query("update Session set state = " + Constants.STATE_APNS + " where uid = ?1 and channel = ?2")
	void openApns(String uid, String channel);

	@Modifying
	@Query("update Session set state = " + Constants.STATE_ACTIVE + " where uid = ?1 and channel = ?2")
	void closeApns(String uid, String channel);
}
