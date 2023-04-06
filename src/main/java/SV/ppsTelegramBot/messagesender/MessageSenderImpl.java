package SV.ppsTelegramBot.messagesender;

import SV.ppsTelegramBot.service.PPSBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
@Service
public class MessageSenderImpl implements MessageSender {
    private PPSBot ppsBot;
    @Override
    public void sendMessage(SendMessage sendMessage) {
        try{
            ppsBot.execute(sendMessage);
        }catch(TelegramApiException e){
            e.printStackTrace();
        }
    }
    @Autowired
    public void setPpsBot(PPSBot ppsBot) {
        this.ppsBot = ppsBot;
    }
}
