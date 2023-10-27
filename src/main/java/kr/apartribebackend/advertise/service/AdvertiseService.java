package kr.apartribebackend.advertise.service;

import kr.apartribebackend.advertise.domain.Advertise;
import kr.apartribebackend.advertise.domain.AdvertiseTokenStatus;
import kr.apartribebackend.advertise.dto.AdvertiseDto;
import kr.apartribebackend.advertise.exception.AdvertiseExpiredTokenException;
import kr.apartribebackend.advertise.exception.AdvertiseIntegrityException;
import kr.apartribebackend.advertise.exception.AdvertiseInvalidTokenException;
import kr.apartribebackend.advertise.exception.AdvertiseReusedTokenException;
import kr.apartribebackend.advertise.repository.AdvertiseRepository;
import kr.apartribebackend.token.email.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class AdvertiseService {

    private final AdvertiseRepository advertiseRepository;
    private final EmailSenderService emailSenderService;

    public void advertiseSendEmail(final AdvertiseDto advertiseDto) {
        advertiseRepository.findByEmailAndName(advertiseDto.getEmail(), advertiseDto.getName()).ifPresentOrElse(
                advertise -> {
                    log.info("ADVERTISE TOKEN : {}", advertiseDto.getToken());
                    emailSenderService.send(advertise.getEmail(), advertiseDto.getToken());
                    advertise.updateTokenValue(advertiseDto.getToken());
                },
                () -> {
                    final Advertise advertise = advertiseDto.toEntity();
                    log.info("ADVERTISE TOKEN : {}", advertise.getToken());
                    emailSenderService.send(advertise.getEmail(), advertise.getToken());
                    advertiseRepository.save(advertise);
                }
        );
    }

    public void confirmAdvertiseToken(final AdvertiseDto advertiseDto) {
        final Advertise advertise = advertiseRepository
                .findByEmailAndNameAndToken(advertiseDto.getEmail(), advertiseDto.getName(), advertiseDto.getToken())
                .orElseThrow(AdvertiseInvalidTokenException::new);
        if (advertise.getAdvertiseTokenStatus() == AdvertiseTokenStatus.COMPLETED) {
            throw new AdvertiseReusedTokenException();
        }
        if (advertise.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new AdvertiseExpiredTokenException();
        }
        advertise.authenticateToken();
    }

    public void saveAdvertiseRequest(final AdvertiseDto advertiseDto) {
        final Advertise advertise = advertiseRepository
                .findByEmailAndNameAndToken(advertiseDto.getEmail(), advertiseDto.getName(), advertiseDto.getToken())
                .filter(ad -> !(ad.getExpiredAt().isBefore(LocalDateTime.now())))
                .orElseThrow(AdvertiseIntegrityException::new);
        advertise.updateAdvertise(advertiseDto.isCollectData(), advertiseDto.getTitle(), advertiseDto.getContent());
    }

}
