package com.wikmind.service.workspace.entity.dto;

import jakarta.validation.constraints.NotNull;

public record WorkspaceCreationRequestDTO(@NotNull String name) {}
