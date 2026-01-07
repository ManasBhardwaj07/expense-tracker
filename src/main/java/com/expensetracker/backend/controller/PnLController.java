package com.expensetracker.backend.controller;

import com.expensetracker.backend.service.PnLService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/pnl")
public class PnLController {

    private final PnLService pnlService;

    public PnLController(PnLService pnlService) {
        this.pnlService = pnlService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, BigDecimal>> getPnL(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        BigDecimal pnl = pnlService.calculatePnL(userId, from, to);
        return ResponseEntity.ok(Map.of("pnl", pnl));
    }
}
