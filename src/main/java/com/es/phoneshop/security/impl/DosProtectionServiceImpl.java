package com.es.phoneshop.security.impl;

import com.es.phoneshop.security.DosProtectionService;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class DosProtectionServiceImpl implements DosProtectionService {
    private static final Object lock;
    private static final long THRESHOLD = 10;
    public static final long START_MAP_COUNT = 1L;
    public static final int TIME_TO_RESET_REQUEST_COUNT_OF_USERS = 1;
    public static final int TIME_TO_CLEAR_BANNED_IP = 30;
    private static DosProtectionService instance;
    private static Date startDate;
    private static Date endDate;
    private Map<String, Long> countMap;
    private Map<String, Long> bannedIp;

    static {
        lock = new Object();
    }

    private DosProtectionServiceImpl() {
        countMap = new ConcurrentHashMap<>();
        bannedIp = new ConcurrentHashMap<>();
        startDate = new Date();
    }

    public static DosProtectionService getInstance() {
        if (Objects.isNull(instance)) {
            synchronized (lock) {
                instance = new DosProtectionServiceImpl();
            }
        }
        return instance;
    }

    @Override
    public boolean isAllowed(String ip) {
        endDate = new Date();
        if ((endDate.getTime() - startDate.getTime()) > TimeUnit.MINUTES.toMillis(TIME_TO_RESET_REQUEST_COUNT_OF_USERS)) {
            startDate = new Date();
            countMap.clear();
        }
        if (!bannedIp.isEmpty()) {
            Set<String> setOfBannedIp = Set.copyOf(bannedIp.keySet());
            for (String currentIp : setOfBannedIp) {
                Long bannedTime = bannedIp.get(currentIp);
                if ((new Date().getTime() - bannedTime) > TimeUnit.MINUTES.toMillis(TIME_TO_CLEAR_BANNED_IP)) {
                    bannedIp.remove(currentIp);
                }
            }
            if (Objects.nonNull(bannedIp.get(ip))) {
                return false;
            }
        }
        Long requestCount = countMap.get(ip);
        if (Objects.isNull(requestCount)) {
            countMap.put(ip, START_MAP_COUNT);
        } else {
            if (requestCount > THRESHOLD) {
                bannedIp.put(ip, new Date().getTime());
                return false;
            }
            countMap.put(ip, requestCount + TIME_TO_RESET_REQUEST_COUNT_OF_USERS);
        }
        return true;
    }
}
