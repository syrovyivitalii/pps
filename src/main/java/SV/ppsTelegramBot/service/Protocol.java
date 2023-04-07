package SV.ppsTelegramBot.service;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Protocol {
    private String competition;
    private Integer race;
    private Integer tracks;
    private Integer r = 1;
    private Integer t = 1;

    private Double resultTrackOne;
    private Double resultTrackTwo;
    private Double resultTrackThree;
    private Double resultTrackFourth;
    private Double resultTrackFifth;

}
