package com.example.project;

import com.example.project.Domain.Entities.Vlan;
import com.example.project.Domain.Enums.VLAN_STATUS_ENUM;
import com.example.project.Util.ResponseParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VlanParserTest {

    private final ResponseParser parser = new ResponseParser();

    @Test
    void parsesStandardSwitchOutput() {

        String raw =
                "VLAN Name                             Status    Ports\n" +
                        "---- -------------------------------- --------- -------------------------------\n" +
                        "1    default                          ACTIVE    \n" +
                        "10   USERS                            ACTIVE    GigabitEthernet0/1, GigabitEthernet0/2\n" +
                        "20   SERVERS                          ACTIVE    GigabitEthernet0/3, GigabitEthernet0/4\n" +
                        "30   MANAGEMENT                       ACTIVE    \n";

        List<Vlan> vlans = parser.parseVlanInfos(raw);

        assertEquals(4, vlans.size());
        assertEquals(VLAN_STATUS_ENUM.ACTIVE, vlans.get(0).getStatus());
        assertEquals(VLAN_STATUS_ENUM.ACTIVE, vlans.get(1).getStatus());
    }

    @Test
    void mapsActiveStatusCorrectly() {

        String raw = "1    default                          ACTIVE    \n";

        List<Vlan> vlans = parser.parseVlanInfos(raw);

        assertEquals(1, vlans.size());
        assertEquals(VLAN_STATUS_ENUM.ACTIVE, vlans.get(0).getStatus());
    }

    @Test
    void mapsActUnsupStatusToNonActive() {

        String raw = "10   USERS                            act/unsup    \n";

        List<Vlan> vlans = parser.parseVlanInfos(raw);

        assertEquals(1, vlans.size());
        assertEquals(VLAN_STATUS_ENUM.NON_ACTIVE, vlans.get(0).getStatus());
    }

    @Test
    void mapsActUnsupStatusRegardlessOfCase() {

        String raw =
                "10   USERS                            ACT/UNSUP    \n" +
                        "20   SERVERS                          Act/Unsup    \n" +
                        "30   MANAGEMENT                       act/unsup    \n";

        List<Vlan> vlans = parser.parseVlanInfos(raw);

        assertEquals(3, vlans.size());
        assertEquals(VLAN_STATUS_ENUM.NON_ACTIVE, vlans.get(0).getStatus());
        assertEquals(VLAN_STATUS_ENUM.NON_ACTIVE, vlans.get(1).getStatus());
        assertEquals(VLAN_STATUS_ENUM.NON_ACTIVE, vlans.get(2).getStatus());
    }

    @Test
    void mapsActiveStatusRegardlessOfCase() {

        String raw =
                "1    default                          active    \n" +
                        "2    test                              Active    \n";

        List<Vlan> vlans = parser.parseVlanInfos(raw);

        assertEquals(2, vlans.size());
        assertEquals(VLAN_STATUS_ENUM.ACTIVE, vlans.get(0).getStatus());
        assertEquals(VLAN_STATUS_ENUM.ACTIVE, vlans.get(1).getStatus());
    }

    @Test
    void handlesUnknownStatusGracefully() {

        String raw = "1    default                          WEIRDSTATUS    \n";

        List<Vlan> vlans = parser.parseVlanInfos(raw);

        assertEquals(1, vlans.size());
        assertNull(vlans.get(0).getStatus());
    }

    @Test
    void handlesMixedKnownAndUnknownStatuses() {

        String raw =
                "1    default                          ACTIVE       \n" +
                        "10   USERS                            act/unsup    \n" +
                        "20   SERVERS                          SUSPENDED    \n";

        List<Vlan> vlans = parser.parseVlanInfos(raw);

        assertEquals(3, vlans.size());
        assertEquals(VLAN_STATUS_ENUM.ACTIVE, vlans.get(0).getStatus());
        assertEquals(VLAN_STATUS_ENUM.NON_ACTIVE, vlans.get(1).getStatus());
        assertNull(vlans.get(2).getStatus());
    }

    @Test
    void ignoresHeaderAndSeparatorLines() {

        String raw =
                "VLAN Name                             Status    Ports\n" +
                        "---- -------------------------------- --------- -------------------------------\n" +
                        "1    default                          ACTIVE    \n";

        List<Vlan> vlans = parser.parseVlanInfos(raw);

        assertEquals(1, vlans.size());
    }

    @Test
    void handlesEmptyInput() {

        List<Vlan> vlans = parser.parseVlanInfos("");

        assertTrue(vlans.isEmpty());
    }
}