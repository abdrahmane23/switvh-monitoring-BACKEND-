package com.example.project.Services.Impls;


import com.example.project.Domain.Entities.*;
import com.example.project.Domain.Enums.CONNECTION_STATUS_ENUM;
import com.example.project.Domain.Enums.INTERFACE_STATUS_ENUM;
import com.example.project.Domain.Enums.SWITCH_STATUS_ENUM;
import com.example.project.Event.InterfaceDownEvent;
import com.example.project.Event.UnknownMacAddressEvent;
import com.example.project.Exceptions.SwitchConnectionException;
import com.example.project.Repositories.*;
import com.example.project.Services.EncryptionService;
import com.example.project.Services.MonitoringService;
import com.example.project.Services.SwitchService;
import com.example.project.Util.ResponseParser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.session.ClientSession;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import static com.example.project.Util.ResponseParser.*;//EXTRACT ALL UTILITY METHODS

@Service
@RequiredArgsConstructor
public class MonitoringServiceImpl implements MonitoringService {
    private final SwitchRepo switchRepo;
    private final InterfaceRepo interfaceRepo;
    private final VlanRepo vlanRepo;
    private final OrdinateurRepo ordinateurRepo;
    private final AlertRepo alertRepo;
    private final ApplicationEventPublisher eventPublisher;
    private final SwitchService switchService;
    private final EncryptionService encryptionService;

    @Transactional
    public void monitorSwitch(Switch sw)  {
        Instant start = Instant.now();
        List<String>listOfResponses=new ArrayList<>();
        List<String> listOfCommands = List.of(
                "show vlan brief ",
                "show interfaces status ",
                "show mac address-table "
        );

        if (sw.getSshConnection() != null) {//SSH is considered as the main way of connection
            SshClient sshClient = SshClient.setUpDefaultClient();
            sshClient.start();
            try (ClientSession session =
                         sshClient.connect(sw.getSshConnection().getUsername(), sw.getIp(), 22)
                                 .verify()
                                 .getSession()) {
                session.addPasswordIdentity(encryptionService.decrypt(sw.getSshConnection().getPassword()));
                session.auth().verify();

                session.executeRemoteCommand("terminal length 0");//to disable pagination for that exact session in order to simplify logic and response parsing
                for (int i = 0 ; i< listOfCommands.size();i++)
                    listOfResponses.add(
                            i,
                            session.executeRemoteCommand(listOfCommands.get(i))
                    );
                sw.setStatus(SWITCH_STATUS_ENUM.CONNECTE);

            } catch (IOException ex) {
                //if any error occured during connection we catched and turn it into our buissness related exception
                switchService.makeSwitchDown(sw);//we turn switch status as in_active in order to appear to the client and not take current infos as accurate
                throw new SwitchConnectionException("Erreur lors de la connexion SSH au switch: " + sw.getNom()+" si le problem persist verify les informations de votre switch ");
            }
            finally {
                sshClient.stop();
            }
        }
        else {// if SSH doesnt exist we go to our default connection using Telnet
            TelnetClient telnetClient = new TelnetClient();
            try{

                telnetClient.connect(sw.getIp(), 23);
                OutputStream output = telnetClient.getOutputStream();
                InputStream input = telnetClient.getInputStream();


                executeTelnetAuth(encryptionService.decrypt(sw.getTelnetConnection().getPassword()),input,output);//implement telnet authentication flow because the used library doesnt provide it

                getTelnetResponse("terminal length 0"+System.lineSeparator(),input,output);

                for (int i = 0 ; i< listOfCommands.size();i++)
                    listOfResponses.add(
                            i,
                            extractTelnetCommandOutput(getTelnetResponse(listOfCommands.get(i),input,output))
                    );
                sw.setStatus(SWITCH_STATUS_ENUM.CONNECTE);// onc we get all our responses we set the switch as active


            } catch (IOException e) {
                switchService.makeSwitchDown(sw);//same connection error handling as SSH connection
                throw new SwitchConnectionException("Erreur lors de la connexion telnet au switch: " + sw.getNom()+" si le problem persist verify les informations de votre switch ");
            }
            finally {
                try {
                    if (telnetClient.isConnected()) {
                        telnetClient.disconnect();
                    }
                } catch (IOException e) {
                    System.out.println("Failed to close Telnet connection");// to not let disconnection errors stop our transaction
                }
            }
        }
        synchronizeVlans(sw, listOfResponses.get(0));
        synchronizeInterfaces(sw, listOfResponses.get(1));
        synchronizeMacAddresses(sw, listOfResponses.get(2));
        Instant end = Instant.now();
        System.out.println("Monitoring du switch " + sw.getNom() + " terminé en " + (end.toEpochMilli() - start.toEpochMilli()) + " ms");
    }

    private void executeTelnetAuth(String password, InputStream input, OutputStream output) throws IOException {
        String response = ReadCommandResponse(input);

        if(isEndOfCommand(response))
            return;

        if(!response.endsWith("Password: "))
            throw new IOException();// exception will be suppressed by our business exception so no need to add message

        output.write((password+System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
        output.flush();

        response = ReadCommandResponse(input);

        if (response.endsWith("Password: "))
            throw new IOException();

        if(isEndOfCommand(response))
            return;

        throw new IOException();
    }
    private String ReadCommandResponse(InputStream input ) throws IOException {

        ByteArrayOutputStream response = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];

        while (true) {
            int length = input.read(buffer);

            if (length == -1) {
                throw new IOException();// exception will be suppressed by our business exception so no need to add message
            }

            response.write(buffer, 0, length);

            String text = response.toString(StandardCharsets.UTF_8);

            if (text.endsWith("Password: ") ||
                    text.matches("(?s).*\\r?\\n?[A-Za-z0-9_-]+[>#]$\\s*")) {

                return text;
            }
        }
    }
    private boolean isEndOfCommand(String response) {
        return response.matches("(?s).*\\r?\\n?[A-Za-z0-9_-]+[>#]\\s*$");
    }
    private String getTelnetResponse (String command , InputStream input, OutputStream output ) throws IOException {
        output.write(command.getBytes(StandardCharsets.UTF_8)
        );
        output.flush();

        ByteArrayOutputStream response = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];


        while (true) {
            int length = input.read(buffer);
            if (length== -1){
                throw new IOException();// exception will be suppressed by our business exception so no need to add message
            }
            response.write(buffer, 0, length);

            String current = response.toString(StandardCharsets.UTF_8);

            if (isEndOfCommand(current)) {
                break;
            }
        }

        return response.toString(StandardCharsets.UTF_8);
    }



    private void synchronizeVlans(Switch sw, String vlanBrief) {
        List<Vlan> retreivedVlans = parseVlanInfos(vlanBrief);// get the vlans existing in the response
        List<Vlan> existingVlans = sw.getVlans();//get vlans that existts in our database for that exact switch


        for (Vlan vlan : retreivedVlans) {
            if (!existingVlans.contains(vlan)) {//if a vlan doesnt exist in database we add it
                vlan.setSwitchEntity(sw);
                existingVlans.add(vlan);
                vlanRepo.save(vlan);
            }else{//if a vlan do exist in database we need to update its state
                Vlan existingVlan = existingVlans.get(existingVlans.indexOf(vlan));
                existingVlan.setNumero(vlan.getNumero());
                existingVlan.setStatus(vlan.getStatus());
            }
        }

        List<Vlan> vlansToRemove = new ArrayList<>();//create a list of vlans to remove to not have concurrent modification exception

        for (Vlan vlan : existingVlans) {// if a database vlan doesnt exist in response it will be removed with it corresponding interfaces also
            if (!retreivedVlans.contains(vlan) && vlan.getNumero()!=0) {
                vlansToRemove.add(vlan);
            }
        }
        for (Vlan vlan: vlansToRemove){
            existingVlans.remove(vlan);
            vlan.setSwitchEntity(null);
        }
        switchRepo.save(sw);

    }
    private void synchronizeInterfaces(Switch sw, String interfaceInfos) {
        Map<Integer,Vlan> vlanByNumber = sw.getVlans().stream().collect(Collectors.toMap(Vlan::getNumero, v -> v));
        List<Interface> retreivedInterfaces = parseInterfaceResponse(interfaceInfos,vlanByNumber)
                .stream()
                .map(
            interf -> {
                            interf.setNom(interf.getNom() + "_" + sw.getNom());
                            return interf;
                        }).toList();//add the switch name as prefix to insure interface name uniqueness.we already inforced switch name uniqueness during creation

        List<Interface> existingInterfaces =interfaceRepo.findByNomEndingWith("_" + sw.getNom());//get all interfaces related to that exact interface
        for (Interface interf : retreivedInterfaces) {
            if (!existingInterfaces.contains(interf)) {//same vlan logic implemented here
                Vlan vlan = sw.getVlans().stream()
                        .filter(v -> v.getNumero()== interf.getVlan().getNumero())
                        .findFirst().get();
                    vlan.getInterfaces().add(interf);
                    interf.setVlan(vlan);
                    interfaceRepo.save(interf);

            } else{
                Interface existingInterface = existingInterfaces.get(existingInterfaces.indexOf(interf));
                if (existingInterface.getStatus()== INTERFACE_STATUS_ENUM.UP &&
                        interf.getStatus() == INTERFACE_STATUS_ENUM.DOWN){
                    eventPublisher.publishEvent(new InterfaceDownEvent(existingInterface.getNom()));// notify that the interface is down
                }

                existingInterface.setStatus(interf.getStatus());
                Vlan vlan = sw.getVlans().stream()
                        .filter(v -> v.getNumero()== interf.getVlan().getNumero())
                        .findFirst().get();// we are insuring that the vlan exists because of our parsing logic
                vlan.getInterfaces().add(existingInterface);
                existingInterface.setVlan(vlan);


            }
        }

        for (Interface interf : existingInterfaces) {
            if (!retreivedInterfaces.contains(interf)) {
                Vlan existingVlan = interf.getVlan();
                    existingVlan.getInterfaces().remove(interf);
                    interf.setVlan(null);
            }
        }
    }
    private void synchronizeMacAddresses(Switch sw, String macAdresse) {
        Map<String, Ordinateur> MacTable = parseMacAddressTable(macAdresse);// get the mac adress infos from the response

        for (Map.Entry<String, Ordinateur> entry : MacTable.entrySet()) {//loop through the mac address table
            String port = entry.getKey();
            Ordinateur ordinateur = entry.getValue();

            Optional<Interface> candidateInterface = interfaceRepo.findByNom(port+"_"+sw.getNom());
            if (candidateInterface.isEmpty()){//if the interface does not exist in our database we skip it this is useful when we have a mac address that is assigned to CPU for example.
                continue;
            }
            Interface exestingInterface = candidateInterface.get();
            Optional<Connection> interfaceConnection = exestingInterface.getConnections()
                    .stream()
                    .filter(c->c.getStatus()== CONNECTION_STATUS_ENUM.ACTIVE)
                    .findFirst();//fetch active connections for that interface if exists
            if (interfaceConnection.isPresent() &&
                    interfaceConnection
                    .get().getOrdinateur()
                    .getMacAdress().equals(ordinateur.getMacAdress())) {// if exists and the database has that exact connection.no changes are applied we only log
                System.out.println("Connection already exists for interface " + port + " and MAC " + ordinateur.getMacAdress());

            }else {
                if(interfaceConnection.isPresent()) {//if the connection exists and its not the exact mac address in the table we make the current connection in_active
                    Connection existingConnection = interfaceConnection.get();
                    existingConnection.setStatus(CONNECTION_STATUS_ENUM.INACTIVE);
                }
                //unified logic between not having an active connection in the first place or making previous connection inactive due to mismatch
                Optional<Ordinateur> existingOrdinateur = ordinateurRepo.findByMacAdress(ordinateur.getMacAdress());
                if (existingOrdinateur.isPresent()){// we look for the pc for that exact mac adress if exists we create an active connection between the two
                    Connection newConnection = new Connection();
                    newConnection.setOrdinateur(existingOrdinateur.get());
                    newConnection.setStatus(CONNECTION_STATUS_ENUM.ACTIVE);
                    newConnection.setSwitchInterface(exestingInterface);
                    exestingInterface.getConnections().add(newConnection);
                    interfaceRepo.save(exestingInterface);
                }else{//if it does not exist we create an alert tied to that exact interface
                    System.out.println("Ordinateur with MAC " + ordinateur.getMacAdress() + " not found in the database.");
                    alertRepo.deleteAllByswitchInterface(exestingInterface);
                    Alert alert = new Alert();
                    alert.setSwitchInterface(exestingInterface);
                    alert.setMacAddress(ordinateur.getMacAdress());
                    alertRepo.save(alert);
                    eventPublisher.publishEvent(new UnknownMacAddressEvent(ordinateur.getMacAdress(), exestingInterface.getNom()));//notify about unknown mac address
                }
            }

        }


    }


}
