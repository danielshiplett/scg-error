# Spring Cloud Gateway Error Demo

A simple demo of Spring Cloud Gateway routing to a normal Spring WebMVC application.  If the WebMVC
application applies the default header cache control customizer, the 'Cache-Control' header is not
properly interpreted by Spring Cloud Gateway's LocalResponseCache filter.

The WebMVC application is configured with:

```java
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .headers(headers-> headers.cacheControl(Customizer.withDefaults()))
                .build();
    }
```

A controller method that doesn't want to be cached can then just return a normal ResponseEntity:

```java
    @GetMapping(value = "/api/not-cached", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> notCached() {
        return ResponseEntity
                .ok(new ResponseDto(UUID.randomUUID()));
    }
```

And the header should be:

```yaml
cache-control: no-cache, no-store, max-age=0, must-revalidate
```

This is not parsed properly by Spring Cloud Gateway's LocalResponseCache filter.  The response will be
cached according to the default settings for its route.

## Testing

Both the `app` and `gateway` modules can be started using `gradle bootrun`.  The application is on
port `8081`.  The gateway is on `8080`.

You can curl to the application directly and verify its headers:

```shell
curl -v http://localhost:8081/api/not-cached
```

You can then curl to it through the gateway and see that it is being cached:

```shell
curl -v http://localhost:8080/app/api/not-cached
```

You can also run the same test again, but this time with `cached` as the endpoint.  In this case,
Spring Cloud Gateway will not use the `max-age=1800` specified by the application.