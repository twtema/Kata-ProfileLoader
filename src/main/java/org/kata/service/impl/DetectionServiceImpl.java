package org.kata.service.impl;

import org.kata.controller.dto.IndividualDto;
import org.kata.exception.IndividualNotFoundException;
import org.kata.service.DetectionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class DetectionServiceImpl implements DetectionService {


    public void checkIndividual(IndividualDto dto) {
        terroristDetection(dto);
        intrudersDetection(dto);
        debtorsDetection(dto);
        fraudstersDetection(dto);
    }

    @Override
    public void terroristDetection(IndividualDto dto) {
        if (dto != null) {
            return loaderWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(urlProperties.getProfileLoaderGetIndividual())
                            .queryParam("icp", dto.getIcp())
                            .build())
                    .retrieve()
                    .onStatus(HttpStatus::isError, response ->
                            Mono.error(new IndividualNotFoundException(
                                    "Individual with icp " + icp + " not found")
                            )
                    )
                    .bodyToMono(IndividualDto.class)
                    .block();

        } else {
            throw new IllegalArgumentException("ERROR");
        }

    }

    @Override
    public void intrudersDetection(IndividualDto dto) {

    }

    @Override
    public void debtorsDetection(IndividualDto dto) {

    }

    @Override
    public void fraudstersDetection(IndividualDto dto) {

    }
}
