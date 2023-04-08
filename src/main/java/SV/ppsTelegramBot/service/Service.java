package SV.ppsTelegramBot.service;

import org.springframework.stereotype.Component;

@Component
public class Service {
    private final Protocol protocol;

    public Service(Protocol protocol) {
        this.protocol = protocol;
    }

    private String wrongData(){
        return "Вибачте, я не можу обробити ваше повідомлення \uD83E\uDD37\u200D♂️";
    }
    public String getWrongData(){
        return wrongData();
    }
    private String startBot(){
        return "Вітаю! Я чат-бот ППС \uD83C\uDDFA\uD83C\uDDE6 \n" +
                "Оберіть вид змагань для скаладання протоколу \uD83D\uDC47";
    }
    public String getStartBot(){
        return startBot();
    }
    private String chooseCompetition(){
        return "Ви не обрали вид змагань. Скористайтеся командами чат-бота щоб розпочати роботу \uD83D\uDC47";
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
    private String incorrect(){
        return "Ви ввели не коректні дані. Спробуйте ще раз \uD83D\uDC47";
    }
    public String getIncorrect(){
        return incorrect();
    }
    private String results(){
        String[] sticker = {"\uD83D\uDD39","\uD83D\uDD38"};
        StringBuilder result = new StringBuilder(protocol.getCompetition() + "\uD83C\uDFC6 \n\n" +
                "Результати проведення " + protocol.getR() + " забігу: \uD83C\uDFC3\u200D♂️ \n");
        for (int i = 1,j=0;i<=protocol.results.size();i++,j++){
            try {
                result.append(sticker[j]).append(" Доріжка №").append(i).append(" - ").append(protocol.results.get(i - 1)).append("\n");
            }catch (ArrayIndexOutOfBoundsException exception){
                j=0;
                result.append(sticker[j]).append(" Доріжка №").append(i).append(" - ").append(protocol.results.get(i - 1)).append("\n");
            }
        }
        return result.toString();
    }
    public String getResults(){
        return results();
    }
    private String counter(){
        String reply;
        if (protocol.getR() > protocol.getRace()){
            reply = "Протокол складено ✅ \n" +
                    "Для початку роботи скористайтеся командами чат-бота \uD83D\uDC47";
        }else {
            reply = "Введіть результати забігу №" + protocol.getR() + " на доріжці №" + protocol.getT() + " \uD83D\uDC68\u200D\uD83D\uDE92";
        }
        return reply;
    }
    public String getCounter(){
        return counter();
    }
}
