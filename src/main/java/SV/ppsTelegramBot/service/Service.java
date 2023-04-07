package SV.ppsTelegramBot.service;

import org.springframework.stereotype.Component;

@Component
public class Service {
    private final Protocol protocol;

    public Service(Protocol protocol) {
        this.protocol = protocol;
    }

    private String wrongData(){
        return "Вибачте, я не можу обробити ваше повідмолення \uD83E\uDD37\u200D♂️";
    }
    public String getWrongData(){
        return wrongData();
    }
    private String startBot(){
        return "Вітаю! Я чат бот PPS \uD83C\uDDFA\uD83C\uDDE6 \n" +
                "Оберіть вид змагань для скаладання протоколу \uD83D\uDC47";
    }
    public String getStartBot(){
        return startBot();
    }
    private String chooseCompetition(){
        return "Ви не обрали вид змагань. Скористайтеся командами чат боту щоб розпочати роботу \uD83D\uDC47";
    }
    public String getChooseCompetition(){
        return chooseCompetition();
    }
    private String race(){
        return  "Введіть кількість забігів \uD83C\uDFC1";
    }
    public String getRace(){
        return race();
    }
    private String tracks(){
        return "Введіть кількість доріжок \uD83C\uDFC1";
    }
    public String getTracks(){
        return tracks();
    }
    private String counter(){
        return "Введіть результати на " + protocol.getTracks() + " дорожці під час " + protocol.getRace() + " забігу";
    }
    public String getCounter(){
        return counter();
    }
}
