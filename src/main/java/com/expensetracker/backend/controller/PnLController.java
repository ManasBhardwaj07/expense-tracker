package com.expensetracker.backend.controller;

import com.expensetracker.backend.service.PnLService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/pnl")
public class PnLController {

    private final PnLService pnlService;

    public PnLController(PnLService pnlService) {
        this.pnlService = pnlService;
    }

    // =========================
    // GET TOTAL PnL
    // =========================
    @GetMapping
    public ResponseEntity<Map<String, BigDecimal>> getTotalPnL() {

        Long userId = getCurrentUserId();

        BigDecimal pnl = pnlService.calculateTotalPnL(userId);

        return ResponseEntity.ok(
                Map.of("totalPnL", pnl)
        );
    }

    // =========================
    // HELPER
    // =========================
    private Long getCurrentUserId() {
        return (Long) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
