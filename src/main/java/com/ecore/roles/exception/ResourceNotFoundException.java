package com.ecore.roles.exception;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class ResourceNotFoundException extends RuntimeException {

    public <T> ResourceNotFoundException(Class<T> resource, UUID id) {
        super(format("%s %s not found", resource.getSimpleName(), id));
    }

    public <T> ResourceNotFoundException(Class<T> resource, List<UUID> ids) {
        super(format("%s not found for %s", resource.getSimpleName(),
                ids.stream()
                        .map(UUID::toString)
                        .collect(Collectors.joining(", "))));
    }

}
