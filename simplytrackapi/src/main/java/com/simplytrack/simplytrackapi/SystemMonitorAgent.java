package com.simplytrack.simplytrackapi;


import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SystemMonitorAgent {


    // Method to get system ID (using hostname)
    public String getSystemId() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    // Method to get total CPU usage
    public double getTotalCpuUsage() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("windows")) {
            return getWindowsTotalCpuUsage();
        } else if (osName.contains("linux")) {
            return getLinuxTotalCpuUsage();
        } else {
            System.err.println("Unsupported operating system: " + osName);
            return 0.0;
        }
    }

    public double getWindowsTotalCpuUsage() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("powershell.exe", "-Command", "(Get-Counter -Counter '\\Processor(_Total)\\% Processor Time').CounterSamples.CookedValue");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            reader.close();
            if (line != null) {
                return Double.parseDouble(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public double getLinuxTotalCpuUsage() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", "top -bn1 | grep 'Cpu(s)' | awk '{print $2 + $4}'");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            reader.close();
            if (line != null) {
                return Double.parseDouble(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // Method to get disk usage
    public long getDiskUsage() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("windows")) {
            return getWindowsDiskUsage();
        } else if (osName.contains("linux")) {
            return getLinuxDiskUsage();
        } else {
            System.err.println("Unsupported operating system: " + osName);
            return 0;
        }
    }

    public long getWindowsDiskUsage() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("powershell.exe", "-Command", "$disk = Get-WmiObject Win32_LogicalDisk | Where-Object { $_.DeviceID -eq $env:SystemDrive }; ($disk.Size - $disk.FreeSpace);" + " $disk.FreeSpace");
            Process process = processBuilder.start();

            // Capture the error stream
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.err.println("PowerShell Error: " + errorLine);
            }
            errorReader.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            long usedSpace = 0;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    usedSpace = Long.parseLong(line); // In Bytes
//                    break;
                }
            }
            reader.close();
            return usedSpace;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public long getLinuxDiskUsage() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", "df -k / | awk 'NR==2 {print $3 * 1024}'");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            reader.close();
            if (line != null) {
                return Long.parseLong(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Method to get memory usage
    public long getMemoryUsage() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("windows")) {
            return getWindowsMemoryUsage();
        } else if (osName.contains("linux")) {
            return getLinuxMemoryUsage();
        } else {
            System.err.println("Unsupported operating system: " + osName);
            return 0;
        }
    }

    public long getWindowsMemoryUsage() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("powershell.exe", "-Command", "$totalMemory = (Get-WmiObject -Class Win32_OperatingSystem).TotalVisibleMemorySize / 1MB; " + "$availableMemory = (Get-Counter '\\Memory\\Available MBytes').CounterSamples.CookedValue / 1024; " + "$usedMemory = $totalMemory - $availableMemory; " + "$usedMemory");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            reader.close();
            if (line != null) {
                double usedMemoryInGB = Double.parseDouble(line);
                long usedMemoryInMB = (long) (usedMemoryInGB * 1024);
                return usedMemoryInMB;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public long getLinuxMemoryUsage() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", "free -m | awk 'NR==2 {print $3}'");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            reader.close();
            if (line != null) {
                return Long.parseLong(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Map<String, String> getProcessesInfo() {
        Map<String, String> processesInfo = new HashMap<>();

        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("windows")) {
            processesInfo = getWindowsProcessesInfo();
        } else if (osName.contains("linux")) {
            processesInfo = getLinuxProcessesInfo();
        } else {
            System.err.println("Unsupported operating system: " + osName);
        }

        return processesInfo;
    }

    public Map<String, String> getWindowsProcessesInfo() {
        Map<String, String> processesInfo = new HashMap<>();

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("powershell.exe", "-Command", "Get-Process | Select-Object Id, ProcessName, @{Name='CPU';Expression={'{0:N2}' -f ($_.CPU / 100)}}, @{Name='Memory';Expression={'{0:N2} MB' -f ($_.WorkingSet64 / 1MB)}}");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+", 4);
                if (parts.length >= 4) {
                    String pid = parts[0];
                    String info = parts[1] + "," + parts[2] + "," + extractFirstValue(parts[3]);
                    processesInfo.put(pid, info);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Failed to get process information, exit code: " + exitCode);
            }

            reader.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return processesInfo;
    }
    public static String extractFirstValue(String input) {
        Pattern pattern = Pattern.compile("^\\d+\\.?\\d*");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
    public Map<String, String> getLinuxProcessesInfo() {
        Map<String, String> processesInfo = new HashMap<>();

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("ps", "-e", "-o", "pid,comm,%mem,%cpu");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            // Skip the header line
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+", 4);
                if (parts.length == 4) {
                    String pid = parts[0];
                    String processName = parts[1];
                    String memoryUsage = extractFirstValue(parts[2]);
                    String cpuUsage = parts[3];
                    String info = processName + "," + cpuUsage + "," + memoryUsage;
                    processesInfo.put(pid, info);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Failed to get process information, exit code: " + exitCode);
            }

            reader.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return processesInfo;
    }
}