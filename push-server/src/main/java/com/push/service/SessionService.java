package com.push.service;

import com.push.comps.RedisTemplateComps;
import com.push.constants.Constants;
import com.push.dao.domain.Session;
import com.push.dao.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Service
public class SessionService {

    private final String host;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private RedisTemplateComps redisTemplateComps;

    public SessionService() throws UnknownHostException {
        host = InetAddress.getLocalHost().getHostAddress();
    }

    public void add(Session session) {
        session.setBindTime(System.currentTimeMillis());
        session.setHost(host);
        sessionRepository.save(session);
    }

    public void delete(String uid, String nid) {
        sessionRepository.delete(uid, nid);
    }

    public void deleteLocalhost() {
        sessionRepository.deleteAll(host);
    }

    public void updateState(String uid, String nid, int state) {
        sessionRepository.updateState(uid, nid, state);
    }


    public void openApns(String uid, String deviceToken) {
        redisTemplateComps.openApns(uid, deviceToken);
        sessionRepository.openApns(uid, Constants.CHANNEL_IOS);
    }

    public void closeApns(String uid) {
        redisTemplateComps.closeApns(uid);
        sessionRepository.closeApns(uid, Constants.CHANNEL_IOS);
    }

    public List<Session> findAll() {
        return sessionRepository.findAll();
    }
}
