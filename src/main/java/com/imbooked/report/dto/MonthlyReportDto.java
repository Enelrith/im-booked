package com.imbooked.report.dto;

import java.math.BigDecimal;

public record MonthlyReportDto(Long monthlyAppointments,
                               Long monthlyCompletedAppointments,
                               Long monthlyCancelledAppointments,
                               BigDecimal monthlyRevenue,
                               String monthName) {
}
