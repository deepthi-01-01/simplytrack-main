package com.simplytrack.simplytrackapi;

import com.simplytrack.simplytrackapi.dao.SystemMonitorDao;
import com.simplytrack.simplytrackapi.models.ProcessInfoEntity;
import com.simplytrack.simplytrackapi.models.SystemInfo;
import com.simplytrack.simplytrackapi.models.SystemInfoEntity;
import com.simplytrack.simplytrackapi.services.SystemMonitorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class SystemMonitorServiceTest {

    @Mock
    private SystemMonitorDao systemMonitorDao;

    @InjectMocks
    private SystemMonitorService systemMonitorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllSystemInfo() {
        List<SystemInfoEntity> systemInfoEntities = createSystemInfoEntities();
        when(systemMonitorDao.getAllSystemInfo()).thenReturn(systemInfoEntities);

        List<SystemInfo> systemInfos = systemMonitorService.getAllSystemInfo();
        assertEquals(systemInfoEntities.size(), systemInfos.size());

    }

    @Test
    void testGetLatestSystemInfo() {
        List<SystemInfoEntity> systemInfoEntities = createSystemInfoEntities();
        when(systemMonitorDao.getAllSystemInfo()).thenReturn(systemInfoEntities);

        SystemInfo latestSystemInfo = systemMonitorService.getLatestSystemInfo();
        assertNotNull(latestSystemInfo);
        assertEquals(systemInfoEntities.get(systemInfoEntities.size() - 1).getSystemId(), latestSystemInfo.getSystemId());

    }

    @Test
    void testGetLatestSystemInfoWithEmptyList() {
        when(systemMonitorDao.getAllSystemInfo()).thenReturn(Collections.emptyList());

        SystemInfo latestSystemInfo = systemMonitorService.getLatestSystemInfo();
        assertNull(latestSystemInfo);
    }

    private List<SystemInfoEntity> createSystemInfoEntities() {
        SystemInfoEntity systemInfoEntity1 = new SystemInfoEntity();
        systemInfoEntity1.setSystemId("system1");
        systemInfoEntity1.setTotalCpuUsage(50.0);
        systemInfoEntity1.setDiskUsage(1000000000);
        systemInfoEntity1.setMemoryUsage(2000000000);
        ProcessInfoEntity processInfoEntity1 = new ProcessInfoEntity("1234", "process1.exe", "10.0", "100");
        systemInfoEntity1.getProcessInfos().add(processInfoEntity1);

        SystemInfoEntity systemInfoEntity2 = new SystemInfoEntity();
        systemInfoEntity2.setSystemId("system2");
        systemInfoEntity2.setTotalCpuUsage(70.0);
        systemInfoEntity2.setDiskUsage(2000000000);
        systemInfoEntity2.setMemoryUsage(300000000);
        ProcessInfoEntity processInfoEntity2 = new ProcessInfoEntity("5678", "process2.exe", "20.0", "200");
        systemInfoEntity2.getProcessInfos().add(processInfoEntity2);

        return Arrays.asList(systemInfoEntity1, systemInfoEntity2);
    }
}
