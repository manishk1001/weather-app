package weatherApp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequiredArgsConstructor
public class CacheDebugController {

    private final CacheManager cacheManager;

    @GetMapping("/debug/cache/weather")
    public Map<Object, Object> getWeatherCacheContent() {
        Cache cache = cacheManager.getCache("weatherCache");
        if (cache instanceof org.springframework.cache.concurrent.ConcurrentMapCache) {
            ConcurrentMap<Object, Object> nativeCache =
                    ((org.springframework.cache.concurrent.ConcurrentMapCache) cache).getNativeCache();
            return new HashMap<>(nativeCache);
        }
        return Map.of(); // fallback for unsupported cache type
    }

    @GetMapping("/debug/cache/coordinates")
    public Map<Object, Object> getCoordinatesCacheContent() {
        Cache cache = cacheManager.getCache("coordinatesCache");
        if (cache instanceof org.springframework.cache.concurrent.ConcurrentMapCache) {
            ConcurrentMap<Object, Object> nativeCache =
                    ((org.springframework.cache.concurrent.ConcurrentMapCache) cache).getNativeCache();
            return new HashMap<>(nativeCache);
        }
        return Map.of();
    }
}

