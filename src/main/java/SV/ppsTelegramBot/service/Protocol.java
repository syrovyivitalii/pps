package SV.ppsTelegramBot.service;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Data
public class Protocol {
    private String competition;
    private Integer race;
    private Integer tracks;
    private Integer teams;
    private Integer r = 1;
    private Integer t = 1;
    public ArrayList<Double> results = new ArrayList<>();

}
