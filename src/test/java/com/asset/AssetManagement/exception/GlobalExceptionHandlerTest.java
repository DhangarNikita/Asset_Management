package com.asset.AssetManagement.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler handler;

    @Mock
    private HttpServletRequest request;

    @Test
    void testHandleDuplicateAsset() {
        when(request.getRequestURI()).thenReturn("/asset/dup");
        DuplicateAssetException ex = new DuplicateAssetException("Duplicate serial");
        ResponseEntity<ErrorResponse> response = handler.handleDuplicateAsset(ex, request);
        assertEquals(409, response.getStatusCode().value());
        assertEquals("DUPLICATE_ASSET", response.getBody().getErrorCode());
        assertEquals("/asset/dup", response.getBody().getPath());
    }

    @Test
    void testHandleInvalidDates() {
        when(request.getRequestURI()).thenReturn("/asset/date");
        InvalidAssetDateException ex = new InvalidAssetDateException("Invalid date");
        ResponseEntity<ErrorResponse> response = handler.handleInvalidDates(ex, request);
        assertEquals(400, response.getStatusCode().value());
        assertEquals("INVALID_DATE", response.getBody().getErrorCode());
    }

    @Test
    void testHandleResourceNotFound() {
        when(request.getRequestURI()).thenReturn("/asset/notfound");
        ResourceNotFoundException ex = new ResourceNotFoundException("Asset not found");
        ResponseEntity<ErrorResponse> response = handler.handleResourceNotFound(ex, request);
        assertEquals(404, response.getStatusCode().value());
        assertEquals("RESOURCE_NOT_FOUND", response.getBody().getErrorCode());
    }

    @Test
    void testHandleInvalidAssignment() {
        when(request.getRequestURI()).thenReturn("/asset/assign");
        InvalidAssetAssignmentException ex = new InvalidAssetAssignmentException("Invalid assignment");
        ResponseEntity<ErrorResponse> response = handler.handleInvalidAssignment(ex, request);
        assertEquals(400, response.getStatusCode().value());
        assertEquals("INVALID_ASSIGNMENT", response.getBody().getErrorCode());
    }

    @Test
    void testHandleGeneralException() {
        when(request.getRequestURI()).thenReturn("/asset/upload");
        Exception ex = new Exception("Something went wrong");
        ResponseEntity<ErrorResponse> response = handler.handleGeneralException(ex, request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        //assertEquals("Something went wrong", response.getBody().getMessage());
        assertEquals("/asset/upload", response.getBody().getPath());
        assertEquals("INTERNAL_ERROR", response.getBody().getErrorCode());
    }

}
