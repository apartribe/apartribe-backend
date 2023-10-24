package kr.apartribebackend.apart.controller;

import jakarta.validation.Valid;
import kr.apartribebackend.apart.dto.*;
import kr.apartribebackend.apart.service.ApartmentService;
import kr.apartribebackend.global.dto.APIResponse;
import kr.apartribebackend.member.principal.AuthenticatedMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
public class ApartmentController {

    private final ApartmentService apartmentService;

    @PostMapping("/api/apartment/auth")
    public ResponseEntity<Void> authenticateApartment(
            @Valid @RequestBody final AuthenticateApartmentReq authenticateApartmentReq,
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember
    ) {
        apartmentService.authenticateApartment(
                authenticatedMember.toDto(),
                authenticateApartmentReq.toDto(),
                authenticatedMember.getOriginalEntity()
        );
        return ResponseEntity.status(CREATED).build();
    }

    @PostMapping("/api/apartment/register")
    public ResponseEntity<Void> constructCommunity(
            @Valid @RequestBody final AppendApartmentReq appendApartmentReq,
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember
    ) {
        apartmentService.constructCommunity(
                authenticatedMember.toDto(),
                appendApartmentReq.toDto(),
                authenticatedMember.getOriginalEntity()
        );
        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping("/api/apartment/{code}/exist")
    public ExistsApartRes apartExistsByCode(@PathVariable final String code) {
        boolean apartExists = apartmentService.existsByCode(code);
        return new ExistsApartRes(apartExists);
    }

    @GetMapping("/api/apartment/{code}")
    public APIResponse<ApartNameRes> findApartByCode(@PathVariable final String code) {
        final ApartmentDto apartmentDto = apartmentService.findApartByCode(code);
        final ApartNameRes apartNameRes = ApartNameRes.from(apartmentDto);
        final APIResponse<ApartNameRes> apiResponse = APIResponse.SUCCESS(apartNameRes);
        return apiResponse;
    }

}

