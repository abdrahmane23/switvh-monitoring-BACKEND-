package com.example.project.Services.Impls;

import com.example.project.Domain.Dtos.CreateOrdinateurRequestDto;

import com.example.project.Domain.Entities.Ordinateur;
import com.example.project.Domain.Entities.Service;
import com.example.project.Exceptions.OrdinateurAlreadyExistException;
import com.example.project.Exceptions.OrdinateurNotFoundException;
import com.example.project.Exceptions.ServiceNotFoundException;
import com.example.project.Repositories.OrdinateurRepo;
import com.example.project.Repositories.ServiceRepo;
import com.example.project.Services.OrdinateurService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class OrdinateurServiceImpl implements OrdinateurService {
    private final ServiceRepo serviceRepository;
    private final OrdinateurRepo ordinateurRepository;



    @Override
    public void createPc(CreateOrdinateurRequestDto createOrdinateurRequestDto, UUID serviceId) {
        Service service = serviceRepository.findById(serviceId).orElseThrow(
                () -> new ServiceNotFoundException("Service not found")
        );
        //pc need to be unique by name,macaddress and Ipaddress
        checkPcUniqueness(createOrdinateurRequestDto.getNom(), createOrdinateurRequestDto.getMacAdress(), createOrdinateurRequestDto.getAddressIp());

        Ordinateur ordinateur = new Ordinateur();
        ordinateur.setNom(createOrdinateurRequestDto.getNom());
        ordinateur.setMarque(createOrdinateurRequestDto.getMarque());
        ordinateur.setMacAdress(createOrdinateurRequestDto.getMacAdress());
        ordinateur.setAddressIp(createOrdinateurRequestDto.getAddressIp());
        ordinateur.setService(service);

        ordinateurRepository.save(ordinateur);


    }

    @Override
    @Transactional
    public void editPc(CreateOrdinateurRequestDto createOrdinateurRequestDto, UUID ordinateurId) {
        Ordinateur ordinateur = ordinateurRepository.findById(ordinateurId).orElseThrow(
                () -> new OrdinateurNotFoundException("Ordinateur n'existe pas")
        );
        //same logic but those infos need to be in other pc other than the one we are trying to edit
        checkPcUniquenessForEdit(createOrdinateurRequestDto.getNom(),
                createOrdinateurRequestDto.getMacAdress(),
                createOrdinateurRequestDto.getAddressIp(),
                ordinateurId);

        ordinateur.setNom(createOrdinateurRequestDto.getNom());
        ordinateur.setMarque(createOrdinateurRequestDto.getMarque());
        ordinateur.setMacAdress(createOrdinateurRequestDto.getMacAdress());
        ordinateur.setAddressIp(createOrdinateurRequestDto.getAddressIp());


    }

    @Override
    @Transactional
    public Ordinateur getPc(UUID ordinateurId) {
        Ordinateur ordinateur = ordinateurRepository.findById(ordinateurId).orElseThrow(
                () -> new OrdinateurNotFoundException("Ordinateur n'existe pas")
        );
        return ordinateur;

    }

    @Override
    @Transactional
    public void deletePc(UUID ordinateurId) {
        Ordinateur ordinateur = ordinateurRepository.findById(ordinateurId).orElseThrow(
                () -> new OrdinateurNotFoundException("Ordinateur n'existe pas")
        );
        ordinateur.getConnections().clear();
        ordinateurRepository.delete(ordinateur);
    }

    private void checkPcUniqueness(String Nom, String macAdress,String addressIp ) {

        if (ordinateurRepository.existsByNom(Nom)) {
            throw new OrdinateurAlreadyExistException("Un ordinateur avec le même nom existe déjà.");
        }
        if (ordinateurRepository.existsByMacAdress(macAdress)) {
            throw new OrdinateurAlreadyExistException("Un ordinateur avec la même adresse MAC existe déjà.");
        }
        if (ordinateurRepository.existsByAddressIp(addressIp)) {
            throw new OrdinateurAlreadyExistException("Un ordinateur avec la même adresse MAC existe déjà.");
        }


    }
    private void checkPcUniquenessForEdit(String Nom, String macAdress,String addressIp, UUID ordinateurId) {

        Optional<Ordinateur> existingOrdinateurByNom = ordinateurRepository.findByNom(Nom);
        if (existingOrdinateurByNom.isPresent() && !existingOrdinateurByNom.get().getId().equals(ordinateurId)) {
            throw new OrdinateurAlreadyExistException("Un ordinateur avec le même nom existe déjà.");
        }

        Optional<Ordinateur> existingOrdinateurByMac = ordinateurRepository.findByMacAdress(macAdress);
        if (existingOrdinateurByMac.isPresent() && !existingOrdinateurByMac.get().getId().equals(ordinateurId)) {
            throw new OrdinateurAlreadyExistException("Un ordinateur avec la même adresse MAC existe déjà.");
        }
        Optional<Ordinateur> existingOrdinateurByAddressIp = ordinateurRepository.findByAddressIp(addressIp);
        if (existingOrdinateurByAddressIp.isPresent() && !existingOrdinateurByAddressIp.get().getId().equals(ordinateurId)) {
            throw new OrdinateurAlreadyExistException("Un ordinateur avec la même adresse MAC existe déjà.");
        }
    }
}
