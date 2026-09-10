package com.example.project.Services.Impls;

import com.example.project.Domain.Dtos.CreateSwitchRequestDto;
import com.example.project.Domain.Entities.*;
import com.example.project.Domain.Enums.SWITCH_STATUS_ENUM;
import com.example.project.Domain.Enums.VLAN_STATUS_ENUM;
import com.example.project.Event.SwitchDownEvent;
import com.example.project.Exceptions.OrdinateurAlreadyExistException;
import com.example.project.Exceptions.SwitchAlreadyExistsException;
import com.example.project.Repositories.SshconnectionRepo;
import com.example.project.Repositories.SwitchRepo;
import com.example.project.Repositories.TelnetConnectionRepo;
import com.example.project.Repositories.VlanRepo;
import com.example.project.Services.EncryptionService;
import com.example.project.Services.SwitchService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class SwitchServiceImpl implements SwitchService {
    private final SwitchRepo switchRepo;
    private final SshconnectionRepo sshconnectionRepo;
    private final TelnetConnectionRepo telnetConnectionRepo;
    private final VlanRepo vlanRepo;
    private final ApplicationEventPublisher eventPublisher;
    private final EncryptionService encryptionService;

    @Override
    @Transactional
    public void createSwitch(CreateSwitchRequestDto newSwitch) {
        if (switchRepo.existsByNom(newSwitch.getNom())) {
            throw new SwitchAlreadyExistsException("Switch avec ce nom  existe déjà");
        }
        if (switchRepo.existsByIp(newSwitch.getIp())){
            throw new SwitchAlreadyExistsException("Switch avec cette IP existe déjà");
        }
        Switch switchToSave = new Switch();
        switchToSave.setNom(newSwitch.getNom());
        switchToSave.setIp(newSwitch.getIp());
        switchToSave.setStatus(SWITCH_STATUS_ENUM.NON_CONNECTE);
        switchRepo.save(switchToSave);
        TelnetConnection telnetConnection = new TelnetConnection();
        telnetConnection.setPassword(encryptionService.encrypt(newSwitch.getTelnet().getPassword()));
        telnetConnectionRepo.save(telnetConnection);
        switchToSave.setTelnetConnection(telnetConnection);
        if (newSwitch.getSsh()!= null) {
            SshConnection sshConnection = new SshConnection();
            sshConnection.setUsername(newSwitch.getSsh().getUsername());
            sshConnection.setPassword(encryptionService.encrypt(newSwitch.getSsh().getPassword()));
            sshconnectionRepo.save(sshConnection);
            switchToSave.setSshConnection(sshConnection);
        }

        Vlan trunkVlan = new Vlan();
        trunkVlan.setNom("trunk");
        trunkVlan.setNumero(0);
        trunkVlan.setStatus(VLAN_STATUS_ENUM.ACTIVE);
        trunkVlan.setSwitchEntity(switchToSave);
        vlanRepo.save(trunkVlan);
        switchToSave.getVlans().add(trunkVlan);
    }

    @Override
    public List<Switch> getAllSwitches() {
        return switchRepo.findAll();
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void makeSwitchDown(Switch sw) {
        sw.setStatus(SWITCH_STATUS_ENUM.NON_CONNECTE);
        switchRepo.save(sw);
        eventPublisher.publishEvent(new SwitchDownEvent(sw.getNom()));
    }

    @Override
    @Transactional
    public void deleteSwitch(UUID switchId) {
        switchRepo.deleteById(switchId);
    }

    @Override
    public void editSwitch(CreateSwitchRequestDto editSwitchRequestDto,UUID switchId) {
        Switch existingSwitch = switchRepo.findById(switchId).orElseThrow(
                ()->  new RuntimeException("switch not found")
                );
        checkSwitchUniquenessForEdit(editSwitchRequestDto.getNom(), editSwitchRequestDto.getIp(),switchId );
        existingSwitch.setNom(editSwitchRequestDto.getNom());
        existingSwitch.setIp(editSwitchRequestDto.getIp());
        TelnetConnection newTelnetconnection = new TelnetConnection();
        newTelnetconnection.setPassword(encryptionService.encrypt(editSwitchRequestDto.getTelnet().getPassword()));
        telnetConnectionRepo.save(newTelnetconnection);
        existingSwitch.setTelnetConnection(newTelnetconnection);

        if (editSwitchRequestDto.getSsh()!=null){
            SshConnection newSshConnection = new SshConnection();
            newSshConnection.setUsername(editSwitchRequestDto.getSsh().getUsername());
            newSshConnection.setPassword(encryptionService.encrypt(editSwitchRequestDto.getSsh().getPassword()));
            sshconnectionRepo.save(newSshConnection);
            existingSwitch.setSshConnection(newSshConnection);
        }
    }
    private void checkSwitchUniquenessForEdit(String nom , String ip, UUID switchId) {
        Optional<Switch> existingSwitchByNom = switchRepo.findByNom(nom);
        if (existingSwitchByNom.isPresent() && !existingSwitchByNom.get().getId().equals(switchId)) {
            throw new SwitchAlreadyExistsException("Un ordinateur avec le même nom existe déjà.");
        }

        Optional<Switch> existingSwitchByIp = switchRepo.findByIp(ip);
        if (existingSwitchByIp.isPresent() && !existingSwitchByIp.get().getId().equals(switchId)) {
            throw new SwitchAlreadyExistsException("Un ordinateur avec la même adresse MAC existe déjà.");
        }
    }
}
