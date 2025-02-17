package org.kata.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.AddressDto;
import org.kata.entity.Address;
import org.kata.entity.Individual;
import org.kata.exception.AddressNotFoundException;
import org.kata.exception.IndividualNotFoundException;
import org.kata.repository.AddressCrudRepository;
import org.kata.repository.IndividualCrudRepository;
import org.kata.service.AddressService;
import org.kata.service.mapper.AddressMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressCrudRepository addressCrudRepository;
    private final IndividualCrudRepository individualCrudRepository;
    private final AddressMapper addressMapper;

    public AddressDto getAddress(String icp) {
        Optional<Individual> individual = individualCrudRepository.findByIcp(icp);

        if (individual.isPresent()) {
            List<Address> address = individual.get().getAddress();

            if (!address.isEmpty()) {
                Address actualAddress = address.stream()
                        .filter(Address::isActual)
                        .findFirst()
                        .orElseThrow(() -> new AddressNotFoundException("Address with icp: " + icp + " not found"));

                AddressDto addressDto = addressMapper.toDto(actualAddress);
                addressDto.setIcp(icp);
                return addressDto;
            } else {
                throw new AddressNotFoundException("No address found for individual with icp: " + icp);
            }
        } else {
            throw new IndividualNotFoundException("Individual with icp: " + icp + " not found");
        }
    }


    public AddressDto saveAddress(AddressDto dto) {
        Optional<Individual> individual = individualCrudRepository.findByIcp(dto.getIcp());

        return individual.map(ind -> {
            List<Address> addresses = ind.getAddress();
            markAddressesAsNotActual(addresses);

            Address address = addressMapper.toEntity(dto);
            address.setUuid(UUID.randomUUID().toString());
            address.setActual(true);
            address.setIndividual(ind);

            log.info("For icp {} created new Address: {}", dto.getIcp(), address);

            addressCrudRepository.save(address);

            AddressDto addressDto = addressMapper.toDto(address);
            addressDto.setIcp(dto.getIcp());
            return addressDto;
        }).orElseThrow(() -> new IndividualNotFoundException("Individual with icp: " + dto.getIcp() + " not found"));
    }

    private void markAddressesAsNotActual(List<Address> list) {
        list.forEach(address -> {
            if (address.isActual()) {
                address.setActual(false);
            }
        });
    }
}
