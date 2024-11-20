package com.simplytrack.simplytrackapi.models;

import java.util.List;

public class SystemInfo {
    private String systemId;
    private double totalCpuUsage;
    private long diskUsage;
    private long memoryUsage;
    private List<ProcessInfo> processInfos;

    // Constructors, getters, and setters

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public double getTotalCpuUsage() {
        return totalCpuUsage;
    }

    public void setTotalCpuUsage(double totalCpuUsage) {
        this.totalCpuUsage = totalCpuUsage;
    }

    public long getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(long diskUsage) {
        this.diskUsage = diskUsage;
    }

    public long getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(long memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public List<ProcessInfo> getProcessInfos() {
        return processInfos;
    }

    public void setProcessInfos(List<ProcessInfo> processInfos) {
        this.processInfos = processInfos;
    }
}

