package com.gabchak.example.security.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Component for extraction host list from application.yml.
 * cors: - prefix
 *   hosts: - list name must be the same as in yml file
 */
@Component
@ConfigurationProperties(prefix = "cors")
@Getter
@RequiredArgsConstructor
public class CorsProperties {
  private final List<String> hosts;
}
