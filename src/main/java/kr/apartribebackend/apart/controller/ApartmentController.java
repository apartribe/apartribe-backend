package kr.apartribebackend.apart.controller;

import jakarta.validation.Valid;
import kr.apartribebackend.apart.dto.AppendApartmentReq;
import kr.apartribebackend.apart.service.ApartmentService;
import kr.apartribebackend.member.principal.AuthenticatedMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
public class ApartmentController {

    private final ApartmentService apartmentService;

    @PostMapping("/api/apartment")
    public ResponseEntity<Void> appendApartment(
            @Valid @RequestBody final AppendApartmentReq appendApartmentReq,
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember
    ) {
        apartmentService.appendApartment(appendApartmentReq.toDto(), authenticatedMember.getOriginalEntity());
        return ResponseEntity.status(CREATED).build();
    }

}
