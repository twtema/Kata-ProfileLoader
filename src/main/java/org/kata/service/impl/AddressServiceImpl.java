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

import static org.kata.service.impl.Constants.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressCrudRepository addressCrudRepository;
    private final IndividualCrudRepository individualCrudRepository;
    private final AddressMapper addressMapper;

    @Override
    public AddressDto getAddress(String icp) {
        Optional<Individual> individual = individualCrudRepository.findByIcp(icp);

        if (individual.isPresent()) {
            List<Address> address = individual.get().getAddress();

            if (!address.isEmpty()) {
                Address actualAddress = address.stream()
                        .filter(Address::isActual)
                        .findFirst()
                        .orElseThrow(() -> new AddressNotFoundException(String.format(ERROR_ADDRESS_NOT_FOUND, icp)));

                AddressDto addressDto = addressMapper.toDto(actualAddress);
                addressDto.setIcp(icp);
                return addressDto;
            } else {
                throw new AddressNotFoundException(String.format(ERROR_NO_ADDRESS_FOUND_FOR_INDIVIDUAL, icp));
            }
        } else {
            throw new IndividualNotFoundException(String.format(ERROR_INDIVIDUAL_WITH_ICP_NOT_FOUND, icp));
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

            log.info(LOG_FOR_ICP_CREATED_NEW_ADDRESS, dto.getIcp(), address);

            try {
                addressCrudRepository.save(address);
                log.debug(LOG_SAVED_ADDRESS_TO_DATABASE, address);
            } catch (Exception e) {
                log.warn(LOG_FAILED_TO_SAVE_ADDRESS, e);
            }

            AddressDto addressDto = addressMapper.toDto(address);
            addressDto.setIcp(dto.getIcp());
            return addressDto;
        }).orElseThrow(() -> new IndividualNotFoundException(String.format(ERROR_INDIVIDUAL_WITH_ICP_NOT_FOUND, dto.getIcp())));
    }

    @Override
    public AddressDto  getAddress(String icp, String uuid) {
        if (icp == null || uuid == null) {
            throw new IllegalArgumentException(ERROR_INVALID_ID_OR_TYPE);
        }

        if (uuid.equals(UUID_STRING_VALUE)) {
            return getAddress(icp);
        } else {
            throw new IllegalArgumentException(ERROR_INVALID_TYPE);
        }
    }


    private void markAddressesAsNotActual(List<Address> list) {
        list.forEach(address -> {
            if (address.isActual()) {
                address.setActual(false);
            }
        });
    }
}
