package kr.apartribebackend.advertise.controller;

import jakarta.validation.Valid;
import kr.apartribebackend.advertise.dto.AdvertiseConfirmTokenReq;
import kr.apartribebackend.advertise.dto.AdvertiseDto;
import kr.apartribebackend.advertise.dto.AdvertiseSendTokenReq;
import kr.apartribebackend.advertise.dto.AppendAdvertiseReq;
import kr.apartribebackend.advertise.service.AdvertiseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;


@RestController
@RequiredArgsConstructor
public class AdvertiseController {

    private final AdvertiseService advertiseService;

    @PostMapping("/api/advertise/email/send")
    public void advertiseSendToken(
            @Valid @RequestBody final AdvertiseSendTokenReq advertiseSendTokenReq
    ) {
        advertiseService.advertiseSendEmail(advertiseSendTokenReq.toDto());
    }

    @GetMapping("/api/advertise/email/confirm")
    public void confirmAdvertiseToken(
            @Valid AdvertiseConfirmTokenReq advertiseConfirmTokenReq
    ) {
        advertiseService.confirmAdvertiseToken(advertiseConfirmTokenReq.toDto());
    }

    @PostMapping("/api/advertise/add")
    public ResponseEntity<Void> saveAdvertiseRequest(
            @Valid @RequestBody final AppendAdvertiseReq appendAdvertiseReq
    ) {
        final AdvertiseDto advertiseDto = appendAdvertiseReq.toDto();
        advertiseService.saveAdvertiseRequest(advertiseDto);
        return ResponseEntity.status(CREATED).build();
    }

}
