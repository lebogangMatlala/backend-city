package org.city.alert.alert.service.dto;

import org.city.alert.alert.service.enums.Status;

public record StatusCountDTO(Status status, Long count) {}

