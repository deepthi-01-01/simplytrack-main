package com.simplytrack.simplytrackapi.controllers;

import com.simplytrack.simplytrackapi.models.SystemInfo;
import com.simplytrack.simplytrackapi.services.SystemMonitorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class SystemMonitorController {

    @Autowired
    private final SystemMonitorService systemMonitorService;

    public SystemMonitorController(SystemMonitorService systemMonitorService) {
        this.systemMonitorService = systemMonitorService;
    }

    @GetMapping("/api/system/all")
    public ResponseEntity<List<SystemInfo>> getAllSystemInfo(@RequestParam(name = "systemId", required = false) String systemId) {
        List<SystemInfo> systemInfoList;
        if (systemId != null) {
            systemInfoList = systemMonitorService.getSystemInfoById(systemId);
        } else {
            systemInfoList = systemMonitorService.getAllSystemInfo();
        }
        return ResponseEntity.ok(systemInfoList);
    }


    @GetMapping("/api/system/latest")
    public ResponseEntity<SystemInfo> getLatestSystemInfo() {
        SystemInfo latestSystemInfo = systemMonitorService.getLatestSystemInfo();
        if (latestSystemInfo != null) {
            return ResponseEntity.ok(latestSystemInfo);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
    @RequestMapping("/api/getUserName")
    public String getUserName(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // Retrieve the session
        if (session != null) {
            String sessionId = session.getId(); // Get the session ID
            Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // Retrieve authentication object
            if (auth != null && auth.isAuthenticated()) {
                String username = auth.getName(); // Get the username from the authentication object
                return username;
            }
        }
        return "Unknown";
    }
    @GetMapping("/")
    public ResponseEntity<Void> fallback() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            // Redirect authenticated users to dashboard
            return ResponseEntity.status(302).header("Location", "http://localhost:3000").build();
        } else {
            // Return a 404 Not Found if the user is not authenticated
            return ResponseEntity.notFound().build();
        }
    }
}