package SV.ppsTelegramBot.handlers;

import SV.ppsTelegramBot.messagesender.MessageSender;
import SV.ppsTelegramBot.service.InlineKeyboard;
import SV.ppsTelegramBot.service.Protocol;
import SV.ppsTelegramBot.service.ReplyKeyboard;
import SV.ppsTelegramBot.service.Service;
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

    public MessageHandler(@Lazy MessageSender messageSender, Service service, ReplyKeyboard replyKeyboard, Protocol protocol, InlineKeyboard inlineKeyboard) {
        this.messageSender = messageSender;
        this.service = service;
        this.replyKeyboard = replyKeyboard;
        this.protocol = protocol;
        this.inlineKeyboard = inlineKeyboard;
    }
    @Value("${chat.CHAT_ID}")
    String chatId;

    @Override
    public void choose(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));


        SendMessage sendMessageToChat = new SendMessage();
        sendMessageToChat.setChatId(chatId);

        if (message.hasText()){
            String messageText = message.getText();
            if (messageText.equals("/start")){
                sendMessage.setText(service.getStartBot());
                sendMessage.setReplyMarkup(replyKeyboard.getMenuReplyKeyboard());
                protocol.setCompetition(null);
                protocol.setRace(null);
                protocol.setTracks(null);
                protocol.setT(1);
                protocol.setR(1);
            }else if (messageText.equals("Штурмова драбина")){
                protocol.setCompetition(messageText);
                sendMessage.setText(service.getRace());
            } else {
                if (isDouble(messageText)){
                    if (protocol.getCompetition() == null){
                        sendMessage.setText(service.getChooseCompetition());
                    } else if (protocol.getCompetition().equals("Штурмова драбина")) {
                        if (protocol.getRace() == null){
                            if (Integer.parseInt(messageText) == 0){
                                sendMessage.setText(service.getIncorrect());
                            }else {
                                protocol.setRace(Integer.valueOf(messageText));
                                sendMessage.setText(service.getTracks());
                            }
                        }else if (protocol.getRace() !=null && protocol.getTracks() == null) {
                            if (Integer.parseInt(messageText) == 0){
                                sendMessage.setText(service.getIncorrect());
                            }else {
                                protocol.setTracks(Integer.valueOf(messageText));
                                sendMessage.setText(service.getCounter());
                            }
                        }else {
                            if (protocol.getResultTrackOne() == null){
                                protocol.setResultTrackOne(Double.valueOf(messageText));
                                if (protocol.getTracks() == 1){
                                    sendMessageToChat.setText(service.getResults(1));
                                    messageSender.sendMessage(sendMessageToChat);
                                    protocol.setResultTrackOne(null);
                                    protocol.setR(protocol.getR() +1);
                                    sendMessage.setText(service.getCounter());
                                }else {
                                    protocol.setT(protocol.getT()+1);
                                    sendMessage.setText(service.getCounter());
                                }
                            }
                        }
                    }
                }else {
                    sendMessage.setText(service.getIncorrect());
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
