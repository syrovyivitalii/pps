package SV.ppsTelegramBot.handlers;

import SV.ppsTelegramBot.messagesender.MessageSender;
import SV.ppsTelegramBot.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.apache.commons.lang3.StringUtils;
@Component
public class MessageHandler implements Handler <Message> {
    private final MessageSender messageSender;
    private final Service service;
    private final ReplyKeyboard replyKeyboard;
    private final Protocol protocol;
    private final InlineKeyboard inlineKeyboard;
    private final Users users;

    public MessageHandler(@Lazy MessageSender messageSender, Service service, ReplyKeyboard replyKeyboard, Protocol protocol, InlineKeyboard inlineKeyboard, Users users) {
        this.messageSender = messageSender;
        this.service = service;
        this.replyKeyboard = replyKeyboard;
        this.protocol = protocol;
        this.inlineKeyboard = inlineKeyboard;
        this.users = users;
    }
    @Value("${chat.CHAT_ID}")
    String chatId;
    @Value("${password.pin}")
    String password;

    @Override
    public void choose(Message message) {
        SendMessage sendMessage = new SendMessage();
        String telegramID = String.valueOf(message.getChatId());
        sendMessage.setChatId(telegramID);


        SendMessage sendMessageToChat = new SendMessage();
        sendMessageToChat.setChatId(chatId);

        if (message.hasText()){
            String messageText = message.getText();
            if (users.users.containsKey(telegramID) && users.users.containsValue(password)){
                if (messageText.equals("/start")){
                    sendMessage.setText(service.getStartBot());
                    sendMessage.setReplyMarkup(replyKeyboard.getMenuReplyKeyboard());
                    protocol.setCompetition(null);
                    protocol.setRace(null);
                    protocol.setTracks(null);
                    protocol.setT(1);
                    protocol.setR(1);
                    protocol.results.clear();
                } else if (messageText.equals("/start@UA_PPS_bot")) {
                    sendMessage.setText("\uD83D\uDC68\u200D\uD83D\uDE92");
                } else if (messageText.equals("Штурмова драбина") || messageText.equals("Стометрівка з перешкодами") || messageText.equals("Естафета чотири по сто метрів") || messageText.equals("Бойове розгортання")){
                    protocol.setCompetition(messageText);
                    sendMessage.setText(service.getRace());
                }else {
                    if (protocol.getCompetition() == null){
                        sendMessage.setText(service.getChooseCompetition());
                    } else {
                        if (protocol.getRace() == null){
                            if (!StringUtils.isNumeric(messageText)){
                                sendMessage.setText(service.getIncorrect());
                            }else {
                                protocol.setRace(Integer.valueOf(messageText));
                                sendMessage.setText(service.getTracks());
                            }
                        }else if (protocol.getRace() !=null && protocol.getTracks() == null) {
                            if (!StringUtils.isNumeric(messageText)){
                                sendMessage.setText(service.getIncorrect());
                            }else {
                                protocol.setTracks(Integer.valueOf(messageText));
                                sendMessage.setText(service.getCounter());
                            }
                        }else {
                            if (isDouble(messageText)){
                                int numTracks = protocol.getTracks();

                                protocol.getResults().add(Double.valueOf(messageText));
                                if (protocol.getResults().size() != numTracks) {
                                    protocol.setT(protocol.getT() + 1);
                                    sendMessage.setText(service.getCounter());
                                }else {
                                    sendMessageToChat.setText(service.getResults());
                                    messageSender.sendMessage(sendMessageToChat);
                                    protocol.setR(protocol.getR() + 1);
                                    protocol.setT(1);
                                    protocol.results.clear();
                                    sendMessage.setText(service.getCounter());
                                }
                            }else {
                                sendMessage.setText(service.getIncorrect());
                            }
                        }
                    }
                }
            }else {
                if (!users.users.containsKey(telegramID) || message.isCommand()){
                    sendMessage.setText("Введіть пароль ✍️");
                    users.users.put(String.valueOf(message.getChatId()),"");
                }else{
                    if (!messageText.equals(password)){
                        sendMessage.setText("Невірний пароль \uD83D\uDEAB");
                    }else {
                        users.users.put(telegramID,password);
                        sendMessage.setText("Вірний пароль ✅ \n" +
                                "Для початку роботи скористайтеся командами бота \uD83D\uDC47");
                    }
                }
            }
        }else {
            sendMessage.setText(service.getWrongData());
        }
        messageSender.sendMessage(sendMessage);
    }
    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
