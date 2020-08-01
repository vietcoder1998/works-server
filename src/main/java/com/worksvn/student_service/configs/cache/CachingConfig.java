package com.worksvn.student_service.configs.cache;

import com.worksvn.student_service.constants.CacheValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.AbstractCacheResolver;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
@EnableCaching
@EnableScheduling
public class CachingConfig {
    private static final Logger logger = LoggerFactory.getLogger(CachingConfig.class);

    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager(cacheNames().toArray(new String[0]));
        cacheManager.setAllowNullValues(true);
        cacheManager.setStoreByValue(false);
        return cacheManager;
    }

    @Bean
    public CacheResolver customCacheResolver(CacheManager cacheManager) {
        return new AbstractCacheResolver(cacheManager) {
            @Override
            protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
                return cacheNames();
            }
        };
    }

    private Collection<String> cacheNames() {
        Field[] fields = CacheValue.class.getDeclaredFields();
        List<String> cacheNames = new ArrayList<>();
        try {
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers())) {
                    cacheNames.add((String) field.get(null));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return cacheNames;
    }

    @CacheEvict(allEntries = true, cacheResolver = "customCacheResolver")
    @Scheduled(fixedDelayString = "${application.caching.internal-service.ttl.millis:300000}")
    public void clearCache() {
        logger.info("ALL APPLICATION CACHE CLEARED");
    }
}
