package com.imbooked.report;

import com.imbooked.report.dto.MonthlyReportDto;
import com.imbooked.report.dto.ReportDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Validated
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/{businessId}")
    public ResponseEntity<ReportDto> getBusinessReport(@PathVariable UUID businessId) {

        return ResponseEntity.ok(reportService.getBusinessReport(businessId));
    }

    @GetMapping("/{businessId}/previous")
    public ResponseEntity<List<MonthlyReportDto>> getPreviousMonthBusinessReports(@PathVariable UUID businessId) {

        return ResponseEntity.ok(reportService.getPreviousMonthBusinessReports(businessId));
    }
}
