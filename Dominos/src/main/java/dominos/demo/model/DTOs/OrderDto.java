package dominos.demo.model.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;


public class OrderDto {
    private double total_sum;
    private LocalDateTime delivery_time;
    private String status;
    private String city;
    private String address;
}
