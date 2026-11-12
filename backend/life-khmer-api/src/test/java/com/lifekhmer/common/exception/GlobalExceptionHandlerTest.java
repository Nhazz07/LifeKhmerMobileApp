package com.lifekhmer.common.exception;

import com.lifekhmer.common.response.ApiErrorResponseFactory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GlobalExceptionHandlerTest {
    private MockMvc mvc;

    @BeforeEach void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new ErrorController())
                .setControllerAdvice(new GlobalExceptionHandler(new ApiErrorResponseFactory()))
                .build();
    }

    @Test void returnsNotFoundPayload() throws Exception { assertError(get("/test/not-found"), 404, "Not Found", "Resource is missing"); }
    @Test void returnsBadRequestPayload() throws Exception { assertError(get("/test/bad"), 400, "Bad Request", "Business rule failed"); }
    @Test void returnsUnauthorizedPayload() throws Exception { assertError(get("/test/unauthorized"), 401, "Unauthorized", "Credentials are invalid"); }
    @Test void returnsForbiddenPayload() throws Exception { assertError(get("/test/forbidden"), 403, "Forbidden", "Access is denied"); }
    @Test void returnsConflictPayload() throws Exception { assertError(get("/test/duplicate"), 409, "Conflict", "Email already exists"); }
    @Test void returnsSafeDataIntegrityPayload() throws Exception { assertError(get("/test/data-integrity"), 409, "Conflict", "The request conflicts with existing data"); }
    @Test void returnsSafeUnexpectedPayload() throws Exception { assertError(get("/test/unexpected"), 500, "Internal Server Error", "An unexpected error occurred"); }

    @Test void returnsFieldValidationErrors() throws Exception {
        mvc.perform(post("/test/validation").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.validationErrors.fullName").value("Full name is required"));
    }

    @Test void returnsSafeMalformedJsonPayload() throws Exception {
        mvc.perform(post("/test/validation").contentType(MediaType.APPLICATION_JSON).content("{"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").value("Malformed JSON request"));
    }

    private void assertError(org.springframework.test.web.servlet.RequestBuilder request, int status, String error, String message) throws Exception {
        mvc.perform(request).andExpect(status().is(status)).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(status)).andExpect(jsonPath("$.error").value(error))
                .andExpect(jsonPath("$.message").value(message)).andExpect(jsonPath("$.path").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @RestController @RequestMapping("/test")
    static class ErrorController {
        @GetMapping("/not-found") void missing() { throw new ResourceNotFoundException("Resource is missing"); }
        @GetMapping("/bad") void bad() { throw new BadRequestException("Business rule failed"); }
        @GetMapping("/unauthorized") void unauthorized() { throw new UnauthorizedException("Credentials are invalid"); }
        @GetMapping("/forbidden") void forbidden() { throw new ForbiddenException("Access is denied"); }
        @GetMapping("/duplicate") void duplicate() { throw new DuplicateResourceException("Email already exists"); }
        @GetMapping("/data-integrity") void dataIntegrity() { throw new DataIntegrityViolationException("private database detail"); }
        @GetMapping("/unexpected") void unexpected() { throw new IllegalStateException("private implementation detail"); }
        @PostMapping("/validation") void validation(@Valid @RequestBody TestRequest request) { }
    }
    record TestRequest(@NotBlank(message = "Full name is required") String fullName) { }
}
