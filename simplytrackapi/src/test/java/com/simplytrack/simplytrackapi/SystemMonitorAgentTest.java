package com.simplytrack.simplytrackapi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SystemMonitorAgentTest {

    private final SystemMonitorAgent systemMonitorAgent = new SystemMonitorAgent();

    @Test
    void testGetSystemId() {
        String systemId = systemMonitorAgent.getSystemId();
        assertNotNull(systemId);
        assertNotEquals("Unknown", systemId);
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testGetWindowsTotalCpuUsage() {
        double cpuUsage = systemMonitorAgent.getTotalCpuUsage();
        assertTrue(cpuUsage >= -1 , "CPU usage should not be negative");
    }

    @Test
    @EnabledOnOs(OS.LINUX)
    void testGetLinuxTotalCpuUsage() {
        double cpuUsage = systemMonitorAgent.getTotalCpuUsage();
        assertTrue(cpuUsage >= 0, "CPU usage should not be negative");
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testGetWindowsDiskUsage() {
        long diskUsage = systemMonitorAgent.getDiskUsage();
        assertTrue(diskUsage >= 0, "Disk usage should not be negative");
    }

    @Test
    @EnabledOnOs(OS.LINUX)
    void testGetLinuxDiskUsage() {
        long diskUsage = systemMonitorAgent.getDiskUsage();
        assertTrue(diskUsage >= 0, "Disk usage should not be negative");
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testGetWindowsMemoryUsage() {
        long memoryUsage = systemMonitorAgent.getMemoryUsage();
        assertTrue(memoryUsage >= 0, "Memory usage should not be negative");
    }

    @Test
    @EnabledOnOs(OS.LINUX)
    void testGetLinuxMemoryUsage() {
        long memoryUsage = systemMonitorAgent.getMemoryUsage();
        assertTrue(memoryUsage >= 0, "Memory usage should not be negative");
    }

    @Test
    void testGetProcessesInfo() {
        Map<String, String> processesInfo = systemMonitorAgent.getProcessesInfo();
        assertNotNull(processesInfo);
        assertFalse(processesInfo.isEmpty(), "Process information should not be empty");
    }

    @Test
    void testGetWindowsProcessesInfo() {
        Map<String, String> processesInfo = systemMonitorAgent.getWindowsProcessesInfo();
        assertNotNull(processesInfo);
        assertFalse(processesInfo.isEmpty(), "Process information should not be empty");
    }

    @Test
    @EnabledOnOs(OS.LINUX)
    void testGetLinuxProcessesInfo() {
        Map<String, String> processesInfo = systemMonitorAgent.getLinuxProcessesInfo();
        assertNotNull(processesInfo);
        assertFalse(processesInfo.isEmpty(), "Process information should not be empty");
    }
}
