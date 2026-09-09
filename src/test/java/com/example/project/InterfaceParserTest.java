package com.example.project;

import com.example.project.Domain.Entities.Interface;
import com.example.project.Domain.Entities.Vlan;
import com.example.project.Domain.Enums.INTERFACE_STATUS_ENUM;
import com.example.project.Util.ResponseParser;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class InterfaceParserTest {

    private final ResponseParser parser = new ResponseParser();

    private Vlan buildVlan(int numero, String nom) {
        Vlan vlan = new Vlan();
        vlan.setNumero(numero);
        vlan.setNom(nom);
        return vlan;
    }

    @Test
    void parsesLinesWithEmptyNameColumn() {

        String raw =
                "Port      Name               Status       Vlan       Duplex  Speed Type\n" +
                        "GigabitEthernet0/1                   UP           10         auto    auto  1000BaseTX\n" +
                        "GigabitEthernet0/2                   UP           10         auto    auto  1000BaseTX\n" +
                        "GigabitEthernet0/3                   DOWN         20         auto    auto  1000BaseTX\n" +
                        "GigabitEthernet0/4                   UP           20         auto    auto  1000BaseTX\n";

        Map<Integer, Vlan> vlansByNumero = new HashMap<>();
        vlansByNumero.put(10, buildVlan(10, "USERS"));
        vlansByNumero.put(20, buildVlan(20, "SERVERS"));

        List<Interface> interfaces = parser.parseInterfaceResponse(raw, vlansByNumero);

        assertEquals(4, interfaces.size());

        assertEquals("GigabitEthernet0/1", interfaces.get(0).getNom());
        assertEquals(INTERFACE_STATUS_ENUM.UP, interfaces.get(0).getStatus());
        assertEquals("USERS", interfaces.get(0).getVlan().getNom());

        assertEquals("GigabitEthernet0/3", interfaces.get(2).getNom());
        assertEquals(INTERFACE_STATUS_ENUM.DOWN, interfaces.get(2).getStatus());
        assertEquals("SERVERS", interfaces.get(2).getVlan().getNom());
    }

    @Test
    void parsesLinesWithSingleWordName() {

        String raw = "Gi0/1 Uplink connected 10 full 1000 10/100/1000BaseTX\n";

        Map<Integer, Vlan> vlansByNumero = new HashMap<>();
        vlansByNumero.put(10, buildVlan(10, "USERS"));

        List<Interface> interfaces = parser.parseInterfaceResponse(raw, vlansByNumero);

        assertEquals(1, interfaces.size());
        assertEquals("Gi0/1", interfaces.get(0).getNom());
        assertEquals(INTERFACE_STATUS_ENUM.UP, interfaces.get(0).getStatus());
    }

    @Test
    void parsesLinesWithMultiWordName() {

        String raw = "Gi0/2 Uplink to core connected 10 full 1000 10/100/1000BaseTX\n";

        Map<Integer, Vlan> vlansByNumero = new HashMap<>();
        vlansByNumero.put(10, buildVlan(10, "USERS"));

        List<Interface> interfaces = parser.parseInterfaceResponse(raw, vlansByNumero);

        assertEquals(1, interfaces.size());
        assertEquals("Gi0/2", interfaces.get(0).getNom());
        assertEquals(INTERFACE_STATUS_ENUM.UP, interfaces.get(0).getStatus());
        assertEquals("USERS", interfaces.get(0).getVlan().getNom());
    }

    @Test
    void mapsConnectedNotconnectAndUpToUp() {

        String raw =
                "Gi0/1 connected 10 full 1000 type\n" +
                        "Gi0/2 notconnect 10 full 1000 type\n" +
                        "Gi0/3 UP 10 full 1000 type\n";

        Map<Integer, Vlan> vlansByNumero = new HashMap<>();
        vlansByNumero.put(10, buildVlan(10, "USERS"));

        List<Interface> interfaces = parser.parseInterfaceResponse(raw, vlansByNumero);

        assertEquals(3, interfaces.size());
        for (Interface iface : interfaces) {
            assertEquals(INTERFACE_STATUS_ENUM.UP, iface.getStatus());
        }
    }

    @Test
    void mapsDownVocabularyToDown() {

        String raw =
                "Gi0/1 disabled 10 full 1000 type\n" +
                        "Gi0/2 err-disabled 10 full 1000 type\n" +
                        "Gi0/3 inactive 10 full 1000 type\n" +
                        "Gi0/4 sfpAbsent 10 full 1000 type\n" +
                        "Gi0/5 suspended 10 full 1000 type\n" +
                        "Gi0/6 DOWN 10 full 1000 type\n";

        Map<Integer, Vlan> vlansByNumero = new HashMap<>();
        vlansByNumero.put(10, buildVlan(10, "USERS"));

        List<Interface> interfaces = parser.parseInterfaceResponse(raw, vlansByNumero);

        assertEquals(6, interfaces.size());
        for (Interface iface : interfaces) {
            assertEquals(INTERFACE_STATUS_ENUM.DOWN, iface.getStatus());
        }
    }

    @Test
    void mapsStatusRegardlessOfCase() {

        String raw =
                "Gi0/1 CONNECTED 10 full 1000 type\n" +
                        "Gi0/2 Err-Disabled 10 full 1000 type\n";

        Map<Integer, Vlan> vlansByNumero = new HashMap<>();
        vlansByNumero.put(10, buildVlan(10, "USERS"));

        List<Interface> interfaces = parser.parseInterfaceResponse(raw, vlansByNumero);

        assertEquals(2, interfaces.size());
        assertEquals(INTERFACE_STATUS_ENUM.UP, interfaces.get(0).getStatus());
        assertEquals(INTERFACE_STATUS_ENUM.DOWN, interfaces.get(1).getStatus());
    }

    @Test
    void skipsMonitorStatusInterfaces() {

        String raw = "Gi0/1 monitor 10 full 1000 type\n";

        Map<Integer, Vlan> vlansByNumero = new HashMap<>();
        vlansByNumero.put(10, buildVlan(10, "USERS"));

        List<Interface> interfaces = parser.parseInterfaceResponse(raw, vlansByNumero);

        assertTrue(interfaces.isEmpty());
    }

    @Test
    void skipsMonitorStatusRegardlessOfCase() {

        String raw =
                "Gi0/1 MONITOR 10 full 1000 type\n" +
                        "Gi0/2 Monitor 10 full 1000 type\n";

        Map<Integer, Vlan> vlansByNumero = new HashMap<>();
        vlansByNumero.put(10, buildVlan(10, "USERS"));

        List<Interface> interfaces = parser.parseInterfaceResponse(raw, vlansByNumero);

        assertTrue(interfaces.isEmpty());
    }

    @Test
    void handlesUnknownStatusGracefully() {

        String raw = "Gi0/1 weirdstatus 10 full 1000 type\n";

        Map<Integer, Vlan> vlansByNumero = new HashMap<>();
        vlansByNumero.put(10, buildVlan(10, "USERS"));

        List<Interface> interfaces = parser.parseInterfaceResponse(raw, vlansByNumero);

        assertEquals(1, interfaces.size());
        assertNull(interfaces.get(0).getStatus());
    }

    @Test
    void skipsTrunkPorts() {

        String raw =
                "Gi0/1 connected 10 full 1000 type\n" +
                        "Gi0/2 connected trunk full 1000 type\n";

        Map<Integer, Vlan> vlansByNumero = new HashMap<>();
        vlansByNumero.put(10, buildVlan(10, "USERS"));

        List<Interface> interfaces = parser.parseInterfaceResponse(raw, vlansByNumero);

        assertEquals(1, interfaces.size());
        assertEquals("Gi0/1", interfaces.get(0).getNom());
    }

    @Test
    void skipsLinesWithInvalidVlanNumber() {

        String raw = "Gi0/1 connected notanumber full 1000 type\n";

        List<Interface> interfaces = parser.parseInterfaceResponse(raw, new HashMap<>());

        assertTrue(interfaces.isEmpty());
    }

    @Test
    void skipsLinesWithTooFewTokens() {

        String raw = "Gi0/1 connected 10\n";

        List<Interface> interfaces = parser.parseInterfaceResponse(raw, new HashMap<>());

        assertTrue(interfaces.isEmpty());
    }

    @Test
    void ignoresHeaderAndSeparatorLines() {

        String raw =
                "Port      Name               Status       Vlan       Duplex  Speed Type\n" +
                        "Switch#\n" +
                        "--------- ------------------ ------------ ---------- ------- ----- ----\n" +
                        "Gi0/1 connected 10 full 1000 type\n";

        Map<Integer, Vlan> vlansByNumero = new HashMap<>();
        vlansByNumero.put(10, buildVlan(10, "USERS"));

        List<Interface> interfaces = parser.parseInterfaceResponse(raw, vlansByNumero);

        assertEquals(1, interfaces.size());
    }

    @Test
    void handlesEmptyInput() {

        List<Interface> interfaces = parser.parseInterfaceResponse("", new HashMap<>());

        assertTrue(interfaces.isEmpty());
    }

    @Test
    void handlesUnknownVlanNumeroGracefully() {

        String raw = "Gi0/1 connected 99 full 1000 type\n";

        Map<Integer, Vlan> vlansByNumero = new HashMap<>();
        vlansByNumero.put(10, buildVlan(10, "USERS"));

        List<Interface> interfaces = parser.parseInterfaceResponse(raw, vlansByNumero);

        assertEquals(1, interfaces.size());
        assertNull(interfaces.get(0).getVlan());
    }

    @Test
    void mixOfTrunkMonitorAndValidInterfaces() {

        String raw =
                "Gi0/1 connected 10 full 1000 type\n" +
                        "Gi0/2 connected trunk full 1000 type\n" +
                        "Gi0/3 monitor 10 full 1000 type\n" +
                        "Gi0/4 DOWN 20 full 1000 type\n";

        Map<Integer, Vlan> vlansByNumero = new HashMap<>();
        vlansByNumero.put(10, buildVlan(10, "USERS"));
        vlansByNumero.put(20, buildVlan(20, "SERVERS"));

        List<Interface> interfaces = parser.parseInterfaceResponse(raw, vlansByNumero);

        assertEquals(2, interfaces.size());
        assertEquals("Gi0/1", interfaces.get(0).getNom());
        assertEquals("Gi0/4", interfaces.get(1).getNom());
    }
}