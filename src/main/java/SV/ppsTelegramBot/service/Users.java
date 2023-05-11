package SV.ppsTelegramBot.service;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
@Component
@Data
public class Users {
    private String admin;
    public Map<String, String> users = new HashMap<String, String>();
}
