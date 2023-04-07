package SV.ppsTelegramBot.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
@Component
public class ReplyKeyboard {
    private ReplyKeyboardMarkup menuReplyKeyboard(){
        //створюємо клавіатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        //створюємо список рядків клавіатури
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        //створюємо перший рядок
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("Штурмова драбина");
        keyboardRow.add("Стометрівка з перешкодами");
        //створюємо другий рядок
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add("Естафета чотири по сто метрів");
        keyboardSecondRow.add("Бойове розгортання");
        //додаємо всі рядки в наш список
        keyboardRows.add(keyboardRow);
        keyboardRows.add(keyboardSecondRow);
        //встановлюємо цей список нашій клавіатурі
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        return replyKeyboardMarkup;
    }
    public ReplyKeyboardMarkup getMenuReplyKeyboard(){
        return menuReplyKeyboard();
    }
}
