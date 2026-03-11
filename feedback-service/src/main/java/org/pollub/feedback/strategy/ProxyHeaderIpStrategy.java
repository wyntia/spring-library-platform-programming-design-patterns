package org.pollub.feedback.strategy;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Set;

//start L3 Strategy Design Pattern - Strategy for identifying client IP address considering proxy headers
public class ProxyHeaderIpStrategy implements IpIdentificationStrategy {
    private final Set<String> trustedProxies;

    public ProxyHeaderIpStrategy(Set<String> trustedProxies) {
        this.trustedProxies = trustedProxies;
    }

    @Override
    public String identify(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        if (trustedProxies != null && !trustedProxies.isEmpty() && trustedProxies.contains(remoteAddr)) {
            String xForwardedFor = request.getHeader("X-Forwarded-For");
            if (xForwardedFor != null && !xForwardedFor.isBlank()) {
                String clientIp = xForwardedFor.split(",")[0].trim();
                if (isValidIp(clientIp)) return clientIp;
            }
            String xRealIp = request.getHeader("X-Real-IP");
            if (xRealIp != null && !xRealIp.isBlank() && isValidIp(xRealIp.trim())) {
                return xRealIp.trim();
            }
        }
        return remoteAddr;
    }

    private boolean isValidIp(String ip) {
        if (ip == null || ip.isBlank()) return false;
        return ip.matches("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$") ||
                (ip.contains(":") && ip.length() <= 45);
    }
}