package com.example.project;

import com.example.project.Domain.Entities.Ordinateur;
import com.example.project.Util.ResponseParser;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MacAddressTableParserTest {

    // Replace with the actual class containing parseMacAddressTable
    private final ResponseParser parser = new ResponseParser();

    @Test
    void parsesStandardFullOutput() {
        String raw =
                "          Mac Address Table\n" +
                        "-------------------------------------------\n\n" +
                        "Vlan    Mac Address        Type        Ports\n" +
                        "----    -----------        --------    -----\n" +
                        "7       0050.7966.6800     DYNAMIC     Gi0/1\n" +
                        "3       0050.7966.6801     STATIC      Gi0/4\n" +
                        "9       0050.7966.6802     DYNAMIC     Gi0/2\n" +
                        "Total Mac Addresses for this criterion: 3\n";

        Map<String, Ordinateur> result = parser.parseMacAddressTable(raw);

        assertEquals(3, result.size());
        assertTrue(result.containsKey("Gi0/1"));
        assertEquals("0050.7966.6800", result.get("Gi0/1").getMacAdress());
        assertEquals("0050.7966.6801", result.get("Gi0/4").getMacAdress());
        assertEquals("0050.7966.6802", result.get("Gi0/2").getMacAdress());
    }

    @Test
    void returnsEmptyMapForEmptyInput() {
        Map<String, Ordinateur> result = parser.parseMacAddressTable("");
        assertTrue(result.isEmpty());
    }

    @Test
    void returnsEmptyMapWhenOnlyHeadersPresent() {
        String raw =
                "          Mac Address Table\n" +
                        "-------------------------------------------\n\n" +
                        "Vlan    Mac Address        Type        Ports\n" +
                        "----    -----------        --------    -----\n" +
                        "Total Mac Addresses for this criterion: 0\n";

        Map<String, Ordinateur> result = parser.parseMacAddressTable(raw);
        assertTrue(result.isEmpty());
    }

    @Test
    void ignoresMalformedLinesWithMissingColumns() {
        String raw =
                "Vlan    Mac Address        Type        Ports\n" +
                        "----    -----------        --------    -----\n" +
                        "7       0050.7966.6800     DYNAMIC\n" +      // missing port column
                        "3       0050.7966.6801     STATIC      Gi0/4\n";

        Map<String, Ordinateur> result = parser.parseMacAddressTable(raw);

        assertEquals(1, result.size());
        assertTrue(result.containsKey("Gi0/4"));
        assertFalse(result.containsValue(null));
    }

    @Test
    void handlesExtraWhitespaceBetweenColumns() {
        String raw =
                "Vlan    Mac Address        Type        Ports\n" +
                        "----    -----------        --------    -----\n" +
                        "1        0050.7966.6800        DYNAMIC       Gi0/1\n";

        Map<String, Ordinateur> result = parser.parseMacAddressTable(raw);

        assertEquals(1, result.size());
        assertEquals("0050.7966.6800", result.get("Gi0/1").getMacAdress());
    }

    @Test
    void handlesWindowsLineEndings() {
        String raw =
                "Vlan    Mac Address        Type        Ports\r\n" +
                        "----    -----------        --------    -----\r\n" +
                        "1       0050.7966.6800     DYNAMIC     Gi0/1\r\n";

        Map<String, Ordinateur> result = parser.parseMacAddressTable(raw);

        assertEquals(1, result.size());
        assertEquals("0050.7966.6800", result.get("Gi0/1").getMacAdress());
    }

    @Test
    void overwritesDuplicatePortWithLastEntry() {
        // If the same port shows up twice (e.g. re-learned MAC), last one wins
        String raw =
                "Vlan    Mac Address        Type        Ports\n" +
                        "----    -----------        --------    -----\n" +
                        "1       0050.7966.6800     DYNAMIC     Gi0/1\n" +
                        "2       0050.7966.9999     DYNAMIC     Gi0/1\n";

        Map<String, Ordinateur> result = parser.parseMacAddressTable(raw);

        assertEquals(1, result.size());
        assertEquals("0050.7966.9999", result.get("Gi0/1").getMacAdress());
    }

    @Test
    void ignoresBlankLinesBetweenEntries() {
        String raw =
                "Vlan    Mac Address        Type        Ports\n" +
                        "----    -----------        --------    -----\n" +
                        "\n" +
                        "1       0050.7966.6800     DYNAMIC     Gi0/1\n" +
                        "\n" +
                        "2       0050.7966.6801     STATIC      Gi0/2\n";

        Map<String, Ordinateur> result = parser.parseMacAddressTable(raw);

        assertEquals(2, result.size());
    }
}