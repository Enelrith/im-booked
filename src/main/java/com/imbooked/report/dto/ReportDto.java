package com.imbooked.report.dto;

import java.math.BigDecimal;

public record ReportDto(Long totalAppointments,
                        Long completedAppointments,
                        Long cancelledAppointments,
                        Long scheduledAppointments,
                        BigDecimal totalRevenue,
                        Long monthlyAppointments,
                        Long monthlyCompletedAppointments,
                        Long monthlyCancelledAppointments,
                        BigDecimal monthlyRevenue,
                        String monthName
) {
}
