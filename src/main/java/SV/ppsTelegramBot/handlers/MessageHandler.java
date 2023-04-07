package SV.ppsTelegramBot.handlers;

import SV.ppsTelegramBot.messagesender.MessageSender;
import SV.ppsTelegramBot.service.InlineKeyboard;
import SV.ppsTelegramBot.service.Protocol;
import SV.ppsTelegramBot.service.ReplyKeyboard;
import SV.ppsTelegramBot.service.Service;
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

    @Override
    public void choose(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));

        if (message.hasText()){
            String messageText = message.getText();
            if (messageText.equals("/start")){
                sendMessage.setText(service.getStartBot());
                sendMessage.setReplyMarkup(replyKeyboard.getMenuReplyKeyboard());
                protocol.setCompetition(null);
                protocol.setRace(null);
                protocol.setTracks(null);
            }else if (messageText.equals("Штурмова драбина")){
                protocol.setCompetition(messageText);
                sendMessage.setText(service.getRace());
            } else {
                if (StringUtils.isNumeric(messageText)){
                    if (protocol.getCompetition() == null){
                        sendMessage.setText(service.getChooseCompetition());
                    } else if (protocol.getCompetition().equals("Штурмова драбина")) {
                        if (protocol.getRace() == null){
                            if (Integer.parseInt(messageText) == 0){
                                sendMessage.setText("Ви ввели не коректні дані. Спробуйте ще раз");
                            }else {
                                protocol.setRace(Integer.valueOf(messageText));
                                sendMessage.setText(service.getTracks());
                            }
                        }else if (protocol.getRace() !=null && protocol.getTracks() == null) {
                            if (Integer.parseInt(messageText) == 0){
                                sendMessage.setText("Ви ввели не коректні дані. Спробуйте ще раз");
                            }else {
                                protocol.setTracks(Integer.valueOf(messageText));
                                sendMessage.setText(readMessageFromUser());
                            }
                        }else {
                            if (protocol.getResultTrackOne() == null){
                                protocol.setResultTrackOne(Double.valueOf(messageText));
                                if (protocol.getTracks() == 1){
                                    sendMessage.setText("Надіслати в інший чат результати");
                                    messageSender.sendMessage(sendMessage);
                                    protocol.setResultTrackOne(null);
                                    protocol.setR(protocol.getR() +1);
                                    sendMessage.setText(readMessageFromUser());
                                }else {
                                    protocol.setT(protocol.getT()+1);
                                    sendMessage.setText(readMessageFromUser());
                                }
                            }
                        }
                    }
                }else {
                    sendMessage.setText("Введено помилкові дані " + messageText);
                }
            }
        }else {
            sendMessage.setText(service.getWrongData());
        }
        messageSender.sendMessage(sendMessage);
    }
    public String readMessageFromUser(){
        String reply;
        if (protocol.getR() > protocol.getRace()){
            reply = "Кінець";
        }else {
            reply = "Введіть результати " + protocol.getT() + " доріжки для " + protocol.getR() + " забігу";
        }
        return reply;
    }
}
