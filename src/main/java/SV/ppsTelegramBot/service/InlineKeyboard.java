package SV.ppsTelegramBot.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class InlineKeyboard {
    private InlineKeyboardMarkup inlineProtocolKeyboard(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Collections.singletonList(InlineKeyboardButton.builder().text("Скласти протокол").callbackData("Скласти протокол").build()));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        return inlineKeyboardMarkup;
    }
    public InlineKeyboardMarkup getInlineProtocolKeyboard(){
        return inlineProtocolKeyboard();
    }
}
