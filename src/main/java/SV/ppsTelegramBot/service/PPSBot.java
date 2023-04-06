package SV.ppsTelegramBot.service;

import SV.ppsTelegramBot.processors.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
@Component
public class PPSBot extends TelegramLongPollingBot {
    private Processor processor;
    @Value("${bot.BOT_TOKEN}")
    private String botToken;

    @Value("${bot.BOT_USERNAME}")
    private String botUsername;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        processor.process(update);
    }
    @Autowired
    public void setProcessor(Processor processor) {
        this.processor = processor;
    }
}
