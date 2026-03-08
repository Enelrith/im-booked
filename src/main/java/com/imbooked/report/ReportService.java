package com.imbooked.report;

import com.imbooked.appointment.AppointmentRepository;
import com.imbooked.appointment.AppointmentStatus;
import com.imbooked.business.BusinessRepository;
import com.imbooked.business.exception.BusinessNotFoundException;
import com.imbooked.report.dto.MonthlyReportDto;
import com.imbooked.report.dto.ReportDto;
import com.imbooked.shared.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReportService {
    private final AppointmentRepository appointmentRepository;
    private final BusinessRepository businessRepository;

    public ReportDto getBusinessReport(UUID businessId) {
        var userEmail = SecurityUtils.getCurrentUserEmail();
        var currentMonth = YearMonth.now(ZoneOffset.UTC);
        var monthStart = currentMonth.atDay(1).atStartOfDay(ZoneOffset.UTC).toInstant();
        var monthEnd = currentMonth.plusMonths(1).atDay(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        if (!businessRepository.existsByIdAndUser_Email(businessId, userEmail)) throw new BusinessNotFoundException(businessId);

        var report = createReport(businessId, userEmail, currentMonth.getMonth().name(), monthStart, monthEnd);
        log.info("Report generated for business with id: {}", businessId);

        return report;
    }

    public List<MonthlyReportDto> getPreviousMonthBusinessReports(UUID businessId) {
        var userEmail = SecurityUtils.getCurrentUserEmail();
        var business =  businessRepository.findByIdAndUser_Email(businessId, userEmail)
                .orElseThrow(() -> new BusinessNotFoundException(businessId));
        var businessCreationDate = business.getCreatedAt().atZone(ZoneOffset.UTC).toLocalDate();
        var monthAmount = ChronoUnit.MONTHS.between(businessCreationDate, LocalDate.now(ZoneOffset.UTC));
        var currentMonth = YearMonth.now(ZoneOffset.UTC);

        List<MonthlyReportDto> reportDtoSet = new ArrayList<>();
        for (long i = 0; i <= monthAmount; i++) {
            var month = currentMonth.minusMonths(i + 1);
            var monthStart = month.atDay(1).atStartOfDay(ZoneOffset.UTC).toInstant();
            var monthEnd = month.plusMonths(1).atDay(1).atStartOfDay(ZoneOffset.UTC).toInstant();

            var report = createMonthlyReportDto(businessId, userEmail, month.getMonth().name(), monthStart, monthEnd);
            reportDtoSet.add(report);
        }

        return reportDtoSet;
    }

    private ReportDto createReport(UUID businessId, String userEmail, String monthName, Instant monthStart, Instant monthEnd) {
        var totalAppointments = appointmentRepository.countAllByBusiness_IdAndBusiness_User_Email(businessId, userEmail);
        var completedAppointments = appointmentRepository.countAllByStatusAndBusiness_IdAndBusiness_User_Email(
                AppointmentStatus.COMPLETED, businessId, userEmail
        );
        var cancelledAppointments = appointmentRepository.countAllByStatusAndBusiness_IdAndBusiness_User_Email(
                AppointmentStatus.CANCELLED, businessId, userEmail
        );
        var scheduledAppointments = appointmentRepository.countAllByStatusAndBusiness_IdAndBusiness_User_Email(
                AppointmentStatus.SCHEDULED, businessId, userEmail
        );
        var totalRevenue = appointmentRepository.sumRevenueByStatus(
                AppointmentStatus.COMPLETED, businessId, userEmail
        ).orElse(BigDecimal.ZERO);
        var monthlyAppointments = appointmentRepository.countAllByBusiness_IdAndBusiness_User_EmailAndAppointmentStartBetween(
                businessId, userEmail, monthStart, monthEnd
        );
        var monthlyCompletedAppointments = appointmentRepository.countAllByStatusAndBusiness_IdAndBusiness_User_EmailAndAppointmentStartBetween(
                AppointmentStatus.COMPLETED, businessId, userEmail, monthStart, monthEnd
        );
        var monthlyCancelledAppointments = appointmentRepository.countAllByStatusAndBusiness_IdAndBusiness_User_EmailAndAppointmentStartBetween(
                AppointmentStatus.CANCELLED, businessId, userEmail, monthStart, monthEnd
        );
        var monthlyRevenue = appointmentRepository.sumMonthlyRevenueByStatus(
                AppointmentStatus.COMPLETED, businessId, userEmail, monthStart, monthEnd
        ).orElse(BigDecimal.ZERO);

        return new ReportDto(totalAppointments, completedAppointments, cancelledAppointments, scheduledAppointments, totalRevenue,
                monthlyAppointments, monthlyCompletedAppointments, monthlyCancelledAppointments, monthlyRevenue, monthName
        );
    }

    private MonthlyReportDto createMonthlyReportDto(UUID businessId, String userEmail, String monthName, Instant monthStart, Instant monthEnd) {
        var monthlyAppointments = appointmentRepository.countAllByBusiness_IdAndBusiness_User_EmailAndAppointmentStartBetween(
                businessId, userEmail, monthStart, monthEnd
        );
        var monthlyCompletedAppointments = appointmentRepository.countAllByStatusAndBusiness_IdAndBusiness_User_EmailAndAppointmentStartBetween(
                AppointmentStatus.COMPLETED, businessId, userEmail, monthStart, monthEnd
        );
        var monthlyCancelledAppointments = appointmentRepository.countAllByStatusAndBusiness_IdAndBusiness_User_EmailAndAppointmentStartBetween(
                AppointmentStatus.CANCELLED, businessId, userEmail, monthStart, monthEnd
        );
        var monthlyRevenue = appointmentRepository.sumMonthlyRevenueByStatus(
                AppointmentStatus.COMPLETED, businessId, userEmail, monthStart, monthEnd
        ).orElse(BigDecimal.ZERO);

        return new MonthlyReportDto(
                monthlyAppointments, monthlyCompletedAppointments, monthlyCancelledAppointments, monthlyRevenue, monthName
        );
    }
}
