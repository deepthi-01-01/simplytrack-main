package com.simplytrack.simplytrackapi;

import com.simplytrack.simplytrackapi.controllers.SystemMonitorController;
import com.simplytrack.simplytrackapi.models.SystemInfo;
import com.simplytrack.simplytrackapi.services.SystemMonitorService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SystemMonitorControllerTest {

    @Test
    public void testGetAllSystemInfo() {
        // Mock the service
        SystemMonitorService mockService = mock(SystemMonitorService.class);

        // Create sample data
        List<SystemInfo> systemInfoList = new ArrayList<>();
        SystemInfo systemInfo1 = new SystemInfo();
        SystemInfo systemInfo2 = new SystemInfo();
        systemInfoList.add(systemInfo1);
        systemInfoList.add(systemInfo2);

        // Mock the service method
        when(mockService.getAllSystemInfo()).thenReturn(systemInfoList);

        // Call the controller method
        SystemMonitorController controller = new SystemMonitorController(mockService);
        ResponseEntity<List<SystemInfo>> response = controller.getAllSystemInfo(null);

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(systemInfoList, response.getBody());
    }

    @Test
    public void testGetLatestSystemInfo() {
        // Mock the service
        SystemMonitorService mockService = mock(SystemMonitorService.class);

        // Create sample data
        SystemInfo systemInfo = new SystemInfo();

        // Mock the service method
        when(mockService.getLatestSystemInfo()).thenReturn(systemInfo);

        // Call the controller method
        SystemMonitorController controller = new SystemMonitorController(mockService);
        ResponseEntity<SystemInfo> response = controller.getLatestSystemInfo();

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(systemInfo, response.getBody());
    }
}
