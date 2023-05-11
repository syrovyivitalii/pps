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
    private Integer attempts = 1;
    private Integer r = 1;
    private Integer t = 1;
    public ArrayList<Double> results = new ArrayList<>();
    private void clearProtocol (){
        competition = null;
        race = null;
        tracks = null;
        attempts = 1;
        r = 1;
        t = 1;
        results.clear();
    }
    public void getClearProtocol(){
        clearProtocol();
    }

}
