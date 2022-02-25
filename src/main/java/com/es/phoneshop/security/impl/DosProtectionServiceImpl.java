package com.es.phoneshop.security.impl;

import com.es.phoneshop.security.DosProtectionService;
import org.apache.commons.lang3.math.NumberUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

public class DosProtectionServiceImpl implements DosProtectionService {
    private static final Object LOCK;
    private static final long THRESHOLD;
    private static final long START_MAP_COUNT = 1L;
    private static final int TIME_TO_RESET_REQUEST_COUNT_OF_USERS = 1;
    private static final int TIME_TO_CLEAR_BANNED_IP;
    private static final ResourceBundle resourceBundle;
    private static final String CONSTANT_PROPERTIES = "constant";
    private static final String THRESHOLD_VALUE_PROPERTY = "threshold.value";
    private static final String TIME_TO_CLEAR_BANNED_IP_PROPERTY = "time.to.clear.banned.ip";

    private static DosProtectionService instance;
    private static LocalDateTime startDate;

    private Map<String, Long> countMap;
    private Map<String, LocalDateTime> bannedIp;

    static {
        LOCK = new Object();
        resourceBundle = ResourceBundle.getBundle(CONSTANT_PROPERTIES);
        THRESHOLD = Long.parseLong(resourceBundle.getString(THRESHOLD_VALUE_PROPERTY));
        TIME_TO_CLEAR_BANNED_IP = Integer.parseInt(resourceBundle.getString(TIME_TO_CLEAR_BANNED_IP_PROPERTY));
    }

    private DosProtectionServiceImpl() {
        countMap = new ConcurrentHashMap<>();
        bannedIp = new ConcurrentHashMap<>();
        startDate = LocalDateTime.now();
    }

    public static DosProtectionService getInstance() {
        if (Objects.isNull(instance)) {
            synchronized (LOCK) {
                instance = new DosProtectionServiceImpl();
            }
        }
        return instance;
    }

    @Override
    public boolean isAllowed(String ip) {
        if (ChronoUnit.MINUTES.between(startDate, LocalDateTime.now()) >= TIME_TO_RESET_REQUEST_COUNT_OF_USERS) {
            startDate = LocalDateTime.now();
            countMap.clear();
        }
        if (!bannedIp.isEmpty()) {
            bannedIp.keySet().forEach(key -> {
                if (ChronoUnit.MINUTES.between(bannedIp.get(key), LocalDateTime.now()) >= TIME_TO_CLEAR_BANNED_IP) {
                    bannedIp.remove(key);
                }
            });
            if (Objects.nonNull(bannedIp.get(ip))) {
                return false;
            }
        }
        Long requestCount = countMap.get(ip);
        if (Objects.isNull(requestCount)) {
            countMap.put(ip, START_MAP_COUNT);
        } else {
            if (requestCount > THRESHOLD) {
                bannedIp.put(ip, LocalDateTime.now());
                return false;
            }
            countMap.put(ip, requestCount + NumberUtils.INTEGER_ONE);
        }
        return true;
    }
}
