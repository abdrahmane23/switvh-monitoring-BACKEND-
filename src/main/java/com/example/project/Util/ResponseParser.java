package com.example.project.Util;


import com.example.project.Domain.Entities.Interface;
import com.example.project.Domain.Entities.Ordinateur;
import com.example.project.Domain.Entities.Vlan;
import com.example.project.Domain.Enums.INTERFACE_STATUS_ENUM;
import com.example.project.Domain.Enums.VLAN_STATUS_ENUM;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ResponseParser {
    public static List<Vlan> parseVlanInfos(String rawResponse) {

        List<Vlan> vlans = new ArrayList<>();

        Pattern pattern = Pattern.compile("^(\\d+)\\s+(\\S+)\\s+(\\S+)\\s*(.*)$");

        String[] lines = rawResponse.split("\\r?\\n");

        for (String line : lines) {

            line = line.trim();

            if (line.isEmpty()
                    || line.startsWith("VLAN")
                    || line.startsWith("----")) {
                continue;
            }

            Matcher matcher = pattern.matcher(line);

            if (matcher.matches()) {

                int numero = Integer.parseInt(matcher.group(1));
                String name = matcher.group(2);
                String statusStr = matcher.group(3);

                Vlan vlan = new Vlan();
                vlan.setNumero(numero);
                vlan.setNom(name);
                vlan.setStatus(mapStatus(statusStr));

                vlans.add(vlan);
            }
        }

        return vlans;
    }

    private static VLAN_STATUS_ENUM mapStatus(String raw) {

        if (raw == null) {
            return null;
        }

        return switch (raw.trim().toUpperCase()) {
            case "ACTIVE" -> VLAN_STATUS_ENUM.ACTIVE;
            case "ACT/UNSUP" -> VLAN_STATUS_ENUM.NON_ACTIVE;
            default -> null;
        };
    }
    public static List<Interface> parseInterfaceResponse(String rawResponse, Map<Integer, Vlan> vlansByNumero) {

        List<Interface> interfaces = new ArrayList<>();

        String[] lines = rawResponse.split("\\r?\\n");

        for (String line : lines) {

            line = line.trim();

            if (line.isEmpty()
                    || line.startsWith("Port")
                    || line.startsWith("Switch")
                    || line.startsWith("----")) {
                continue;
            }

            String[] tokens = line.split("\\s+");

            if (tokens.length < 6) {
                continue;
            }

            String portName = tokens[0];

            int statusIndex = tokens.length - 5;

            String statusStr = tokens[statusIndex];
            String vlanStr = tokens[statusIndex + 1];

            // skip trunk ports entirely
            if ("trunk".equalsIgnoreCase(vlanStr)) {
                continue;
            }

            // skip monitor-status ports entirely
            if ("monitor".equalsIgnoreCase(statusStr)) {
                continue;
            }
            if ("CPU".equalsIgnoreCase(statusStr)) {
                continue;
            }

            int vlanNumero;
            try {
                vlanNumero = Integer.parseInt(vlanStr);
            } catch (NumberFormatException e) {
                continue;
            }

            Interface iface = new Interface();
            iface.setNom(portName);
            iface.setStatus(mapInterfaceStatus(statusStr));
            iface.setVlan(vlansByNumero.get(vlanNumero));

            interfaces.add(iface);
        }

        return interfaces;
    }

    private static INTERFACE_STATUS_ENUM mapInterfaceStatus(String raw) {

        if (raw == null) {
            return null;
        }

        return switch (raw.trim().toLowerCase()) {
            case "connected", "notconnect", "up" -> INTERFACE_STATUS_ENUM.UP;
            case "disabled", "err-disabled", "inactive", "sfpabsent", "suspended", "down" -> INTERFACE_STATUS_ENUM.DOWN;
            default -> null;
        };
    }
    public static Map<String, Ordinateur> parseMacAddressTable(String rawResponse) {

        Map<String, Ordinateur> result = new LinkedHashMap<>();

        // Matches: <vlan>  <macAddress>  <type>  <portName>
        Pattern pattern = Pattern.compile("^(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s*$");

        String[] lines = rawResponse.split("\\r?\\n");

        for (String line : lines) {

            line = line.trim();

            if (line.isEmpty()
                    || line.startsWith("Mac")
                    || line.startsWith("Vlan")
                    || line.startsWith("----")
                    || line.startsWith("Total")) {
                continue;
            }

            Matcher matcher = pattern.matcher(line);

            if (matcher.matches()) {

                String macAddress = matcher.group(2);
                String portName = matcher.group(4);

                Ordinateur ordinateur = new Ordinateur();
                ordinateur.setMacAdress(macAddress);

                result.put(portName, ordinateur);
            }
        }

        return result;
    }
    public static String extractTelnetCommandOutput(String response){
        String[] lines = response.split("\\R");

        return String.join(
                System.lineSeparator(),
                Arrays.copyOfRange(lines, 1, lines.length - 1)
        );
    }
}
