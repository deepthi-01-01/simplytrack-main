package com.simplytrack.simplytrackapi.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "system_info")
public class SystemInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "system_id")
    private String systemId;

    @Column(name = "total_cpu_usage")
    private double totalCpuUsage;

    @Column(name = "disk_usage")
    private long diskUsage;

    @Column(name = "memory_usage")
    private long memoryUsage;

    @OneToMany(mappedBy = "systemInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProcessInfoEntity> processInfos = new ArrayList<>();

    // Constructors, getters, and setters

    public SystemInfoEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public List<ProcessInfoEntity> getProcessInfos() {
        return processInfos;
    }

    public void setProcessInfos(List<ProcessInfoEntity> processInfos) {
        this.processInfos = processInfos;
    }
}