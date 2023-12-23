package com.example.schedulesevice.service.impl;

import com.example.schedulesevice.dto.request.ScheduleDto;
import com.example.schedulesevice.dto.response.*;
import com.example.schedulesevice.entity.*;
import com.example.schedulesevice.repository.*;
import com.example.schedulesevice.service.ScheduleService;
import com.phucvukimcore.base.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private CinemaRepository cinemaRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public Result scheduleShow(ScheduleDto dto) {
        LocalDateTime startTime = convertToLocalDateTimeFromString(dto.getStartTime());
        String error = checkTime(startTime);
        if (error != null)
            return Result.fail(error);
        Optional<Film> op1 = filmRepository.findById(dto.getFilmId());
        if (!op1.isPresent())
            return Result.fail("Không tìm thấy phim");
        Film film = op1.get();
        Integer durations = film.getDuration(); // in seconds
        LocalDateTime endTime = startTime.plusMinutes(durations);
        Optional<Room> op2 = roomRepository.findById(dto.getRoomId());
        if (!op2.isPresent())
            return Result.fail("Không tìm thấy phòng");
        Room room = op2.get();
        Schedule schedule = new Schedule();
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        schedule.setFilm(film);
        schedule.setRoom(room);
        scheduleRepository.save(schedule);
        List<Schedule> schedules = room.getSchedules();
        if (schedules == null) {
            schedules = new ArrayList<>();
        }
        schedules.add(schedule);
        room.setSchedules(schedules);
        roomRepository.save(room);
        return Result.success("Success", schedule);
    }

    @Override
    public Result getScheduleById(Integer scheduleId) {
        Optional<Schedule> op = scheduleRepository.findById(scheduleId);
        if (!op.isPresent())
            return Result.fail("Lịch chiếu không tồn tại");
        else
            return Result.success("Success", op.get());
    }

    @Override
    public Result editSchedule(ScheduleDto dto) {

        Optional<Schedule> optional = scheduleRepository.findById(dto.getId());
        if (!optional.isPresent())
            return Result.fail("Lịch chiếu không tồn tại!");

        if (isBooked(dto.getId()))
            return Result.fail("Không thể chỉnh sửa do lịch chiếu đã được đặt bởi khách hàng!");

        Schedule schedule = optional.get();

        LocalDateTime startTime = convertToLocalDateTimeFromString(dto.getStartTime());
        String error = checkTime(startTime);
        if (error != null)
            return Result.fail(error);
        Optional<Film> op1 = filmRepository.findById(dto.getFilmId());
        if (!op1.isPresent())
            return Result.fail("Không tìm thấy phim");
        Film film = op1.get();
        Integer durations = film.getDuration(); // in seconds
        LocalDateTime endTime = startTime.plusMinutes(durations);
        Optional<Room> op2 = roomRepository.findById(dto.getRoomId());
        if (!op2.isPresent())
            return Result.fail("Không tìm thấy phòng");
        Room room = op2.get();

        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        schedule.setFilm(film);
        schedule.setRoom(room);

        scheduleRepository.save(schedule);

        return Result.success("Success", schedule);
    }

    @Override
    public Result deleteSchedule(Integer scheduleId) {

        Optional<Schedule> optional = scheduleRepository.findById(scheduleId);
        if (!optional.isPresent())
            return Result.fail("Lịch chiếu không tồn tại!");

        if (isBooked(scheduleId))
            return Result.fail("Không thể xóa do lịch chiếu đã được đặt bởi khách hàng!");

        scheduleRepository.delete(optional.get());
        return Result.success();
    }

    @Override
    public Result findAllCurrentScheduleInCinema(Integer cinemaId) {

        Optional<Cinema> op = cinemaRepository.findById(cinemaId);
        if (!op.isPresent()) {
            return Result.fail("Rạp phim không tồn tại");
        }

        List<Schedule> schedules = findScheduleByTimeOption(op.get(), false);
        Collections.sort(schedules, Comparator.comparing(Schedule::getStartTime));
        Collections.reverse(schedules);
        List<ScheduleResponse> responses = new ArrayList<>();

        for (Schedule schedule: schedules) {
            ScheduleResponse response = new ScheduleResponse();
            response.setSchedule(schedule);

            Room room = schedule.getRoom();
            int totalSeats = room.getHorizontalSeats() * room.getVerticalSeats();
            response.setTotalSeats(totalSeats);
            int booked = ticketRepository.countByScheduleId(schedule.getId());
            response.setAvailables(totalSeats - booked);

            responses.add(response);
        }

        return Result.success("Success", responses);
    }

    @Override
    public Result findAllHistoryScheduleInCinema(Integer cinemaId) {
        Optional<Cinema> op = cinemaRepository.findById(cinemaId);
        if (!op.isPresent()) {
            return Result.fail("Rạp phim không tồn tại");
        }

        List<Schedule> schedules = findScheduleByTimeOption(op.get(), true);
        Collections.sort(schedules, Comparator.comparing(Schedule::getStartTime));
        Collections.reverse(schedules);
        List<ScheduleResponse> responses = new ArrayList<>();

        for (Schedule schedule: schedules) {
            ScheduleResponse response = new ScheduleResponse();
            response.setSchedule(schedule);

            Room room = schedule.getRoom();
            int totalSeats = room.getHorizontalSeats() * room.getVerticalSeats();
            response.setTotalSeats(totalSeats);
            int booked = ticketRepository.countByScheduleId(schedule.getId());
            response.setAvailables(totalSeats - booked);

            responses.add(response);
        }
        return Result.success("Success", responses);
    }

    private List<Schedule> findScheduleByTimeOption(Cinema cinema, boolean history) {

        LocalDateTime now = LocalDateTime.now();
        List<Schedule> schedules;
        if (history) {
            schedules = scheduleRepository.findByRoom_CinemaAndEndTimeBefore(cinema, now);
        } else {
            schedules = scheduleRepository.findByRoom_CinemaAndEndTimeAfter(cinema, now);
        }

        return schedules;
    }

    @Override
    public Result findAllScheduleInCinemaByDay(Integer cinemaId, String date) {
        List<Room> rooms = roomRepository.findByCinemaId(cinemaId);
        LocalDateTime dateStart = convertToLocalDateTimeFromString(date + "T00:00:00");
        if (dateStart.isBefore(LocalDateTime.now())) {
            dateStart = LocalDateTime.now();
        }
        LocalDateTime endDate = convertToLocalDateTimeFromString(date + "T23:59:59");
        Map<Integer, Integer> map = new HashMap<>();
        List<ShowResponse> result = new ArrayList<>();
        int index = 0;
        for (Room room: rooms) {
            List<Schedule> schedules = scheduleRepository.findByDateRangeAndRoomId(dateStart, endDate, room.getId());
            for (Schedule schedule: schedules) {
                Film film = schedule.getFilm();
                if (map.containsKey(film.getId())) {
                    int position = map.get(film.getId());
                    ShowResponse response = result.get(position);
                    List<Schedule> sche = response.getSchedules();
                    sche.add(schedule);
                    response.setSchedules(sche);
                    result.set(position, response);
                }
                else {
                    ShowResponse response = new ShowResponse();
                    response.setFilm(film);
                    List<Schedule> list = new ArrayList<>();
                    list.add(schedule);
                    response.setSchedules(list);
                    result.add(response);
                    map.put(film.getId(), index);
                    index++;
                }
            }
        }
        return Result.success("Success", result);
    }

    @Override
    public Result findAllOrdered(Integer scheduleId) {

        List<Ticket> tickets = ticketRepository.findByScheduleId(scheduleId);
        List<Booking> bookings = new ArrayList<>(tickets.stream().map(Ticket::getBooking).collect(Collectors.toSet()));

        List<OrderedResponse> responses = new ArrayList<>();
        Map<Integer, Integer> mark = new HashMap<>();
        int index = 0;
        for (Ticket ticket: tickets) {
            for (Booking booking: bookings) {
                if (ticket.getBooking().getId().equals(booking.getId())) {
                    if (mark.containsKey(booking.getId())) {
                        int currentIndex = mark.get(booking.getId());
                        OrderedResponse order = responses.get(currentIndex);
                        order.setNumberOfTicket(order.getNumberOfTicket() + 1);
                        if (ticket.getTicketClass().equals(0)) {
                            order.setRegulars(order.getRegulars() + 1);
                        } else {
                            order.setVips(order.getVips() + 1);
                        }
                        order.setSeats(order.getSeats() + ", " + ticket.getSeatNumberVertical() + "-" + ticket.getSeatNumberHorizontal());
                        order.setTotalPaid((order.getTotalPaid() + ticket.getPrice()));
                        responses.set(currentIndex, order);
                    } else {
                        OrderedResponse order = new OrderedResponse();
                        User user = booking.getUser();
                        order.setFullname(user.getFullname());
                        order.setEmail(user.getEmail());
                        order.setBookedTime(booking.getBookingTime());
                        order.setNumberOfTicket(1);
                        if (ticket.getTicketClass().equals(0)) {
                            order.setRegulars(1);
                            order.setVips(0);
                        } else {
                            order.setRegulars(0);
                            order.setVips(1);
                        }
                        order.setSeats(ticket.getSeatNumberVertical() + "-" + ticket.getSeatNumberHorizontal());
                        order.setTotalPaid(ticket.getPrice());
                        responses.add(order);
                        mark.put(booking.getId(), index++);
                    }
                }
            }
        }
        return Result.success("Success", responses);
    }

    @Override
    public Result getRevenueStatistic(Integer cinemaId, String startDate, String endDate) {
        Optional<Cinema> op = cinemaRepository.findById(cinemaId);
        if (!op.isPresent()) {
            return Result.fail("Rạp chiếu không tồn tại");
        }
        Cinema cinema = op.get();
        LocalDateTime start = convertToLocalDateTimeFromString(startDate + "T00:00:00");
        LocalDateTime end = convertToLocalDateTimeFromString(endDate + "T23:59:59");
        List<Schedule> schedules = scheduleRepository.findAllByRoom_CinemaAndStartTimeAfterAndStartTimeBefore(cinema, start, end);
        long totalRevenue = 0L;
        List<ScheduleRevenueStatistic> revenueSchedules = new ArrayList<>();
        for (Schedule schedule: schedules) {
            ScheduleRevenueStatistic revenueSchedule = new ScheduleRevenueStatistic();
            revenueSchedule.setFilm(schedule.getFilm());
            revenueSchedule.setRoom(schedule.getRoom());
            revenueSchedule.setShowDate(schedule.getStartTime());

            List<Ticket> tickets = ticketRepository.findByScheduleId(schedule.getId());
            revenueSchedule.setTicketsSold(tickets.size());
            long revenue = tickets.stream()
                            .mapToLong(Ticket::getPrice).sum();
            revenueSchedule.setRevenue(revenue);
            revenueSchedules.add(revenueSchedule);
            totalRevenue += revenue;
        }
        RevenueStatisticResponse response = new RevenueStatisticResponse();
        response.setTotalRevenue(totalRevenue);
        response.setScheduleRevenueStatistic(revenueSchedules);
        return Result.success("Success", response);
    }

    @Override
    public Result getAllBookedSeats(Integer scheduleId) {
        Optional<Schedule> op = scheduleRepository.findById(scheduleId);
        if (!op.isPresent()) {
            return Result.fail("Lịch chiếu không tồn tại");
        }
        Schedule schedule = op.get();
        Room room = schedule.getRoom();
        SeatsStatus seatsStatus = new SeatsStatus();
        seatsStatus.setSchedule(schedule);
        seatsStatus.setRow(room.getVerticalSeats());
        seatsStatus.setColumn(room.getHorizontalSeats());
        List<Ticket> tickets = ticketRepository.findByScheduleId(scheduleId);
        List<String> responses = new ArrayList<>();
        for (Ticket ticket: tickets) {
            responses.add(ticket.getSeatNumberVertical() + "-" + ticket.getSeatNumberHorizontal());
        }
        seatsStatus.setBookedSeats(responses);
        return Result.success("Success", seatsStatus);
    }

    private LocalDateTime convertToLocalDateTimeFromString(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    private String checkTime(LocalDateTime dateTime) {
        // so sanh voi thoi gian hien tai
        LocalDateTime now = LocalDateTime.now();
        if (dateTime.isBefore(now))
            return "Thời gian bắt đầu chiếu không hợp lệ!";
        // neu thoi gian start truoc 6 tieng
        if (dateTime.isBefore(now.plusHours(6)))
            return "Vui lòng lên lịch 6 tiếng trước khi bắt đầu chiếu!";
        return null;
    }

    private boolean isBooked(int scheduleId) {
        int bookedNumber = ticketRepository.countByScheduleId(scheduleId);
        return bookedNumber > 0;
    }
}
