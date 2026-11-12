package com.lifekhmer.security;

import java.util.UUID;

/** Typed principal established only after a validated access JWT is accepted. */
public record CurrentUser(UUID id) { }
