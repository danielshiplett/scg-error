package com.example.scg.errors.app.app;

import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
public class CacheController {

    @GetMapping(value = "/api/not-cached", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> notCached() {
        return ResponseEntity
                .ok(new ResponseDto(UUID.randomUUID()));
    }

    @GetMapping(value = "/api/cached", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> cached() {
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(30, TimeUnit.MINUTES))
                .body(new ResponseDto(UUID.randomUUID()));
    }


    public static class ResponseDto {
        private UUID uuid;

        public ResponseDto(UUID uuid) {
            this.uuid = uuid;
        }

        public UUID getUuid() {
            return uuid;
        }

        public void setUuid(UUID uuid) {
            this.uuid = uuid;
        }
    }
}
