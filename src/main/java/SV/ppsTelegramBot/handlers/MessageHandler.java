package SV.ppsTelegramBot.handlers;

import SV.ppsTelegramBot.messagesender.MessageSender;
import SV.ppsTelegramBot.service.Protocol;
import SV.ppsTelegramBot.service.ReplyKeyboard;
import SV.ppsTelegramBot.service.Service;
import SV.ppsTelegramBot.service.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.apache.commons.lang3.StringUtils;
@Component
@Slf4j
public class MessageHandler implements Handler <Message> {
    private final MessageSender messageSender;
    private final Service service;
    private final ReplyKeyboard replyKeyboard;
    private final Protocol protocol;
    private final Users users;

    public MessageHandler(@Lazy MessageSender messageSender, Service service, ReplyKeyboard replyKeyboard, Protocol protocol, Users users) {
        this.messageSender = messageSender;
        this.service = service;
        this.replyKeyboard = replyKeyboard;
        this.protocol = protocol;
        this.users = users;
    }

    // id of group where bot sends results
    @Value("${chat.CHAT_ID}")
    String chatId;
    @Value("${password.pin}")
    String password;

    @Override
    public void choose(Message message) {
        // send a reply to the admin
        SendMessage sendMessage = new SendMessage();
        String telegramID = String.valueOf(message.getChatId());
        sendMessage.setChatId(telegramID);

        // send the results to the group
        SendMessage sendMessageToChat = new SendMessage();
        sendMessageToChat.setChatId(chatId);
        sendMessageToChat.setParseMode(ParseMode.HTML);

        // only admin can send message to bot. Bot will not reply when users in group sends a messages
        if (!telegramID.equals(chatId)){
            if (message.hasText()){
                // send user's message in console
                log.info("{}:{}", telegramID, message.getText());
                String messageText = message.getText();
                //check if admin already exist
                if (users.getAdmin() == null || users.getAdmin().equals(telegramID)){
                    //check password
                    if (users.users.containsKey(telegramID) && users.users.get(telegramID).equals(password)){
                        if (messageText.equals("/start")){
                            sendMessage.setText(service.getStartBot());
                            sendMessage.setReplyMarkup(replyKeyboard.getMenuReplyKeyboard());
                            protocol.getClearProtocol();
                        }else if (messageText.equals("Штурмова драбина") || messageText.equals("100-м смуга з перешкодами") || messageText.equals("Пожежна естафета") || messageText.equals("Бойове розгортання")){
                            protocol.setCompetition(messageText);
                            sendMessage.setText(service.getRace());
                        }else {
                            if (protocol.getCompetition() == null){
                                sendMessage.setText(service.getChooseCompetition());
                            } else {
                                // set race
                                if (protocol.getRace() == null){
                                    if (!StringUtils.isNumeric(messageText)){
                                        sendMessage.setText(service.getIncorrect());
                                    }else {
                                        protocol.setRace(Integer.valueOf(messageText));
                                        sendMessage.setText(service.getTracks());
                                    }
                                // set track
                                }else if (protocol.getRace() !=null && protocol.getTracks() == null) {
                                    if (!StringUtils.isNumeric(messageText)){
                                        sendMessage.setText(service.getIncorrect());
                                    }else {
                                        protocol.setTracks(Integer.valueOf(messageText));
                                        sendMessage.setText(service.getCounter());
                                    }
                                }else {
                                    if (isDouble(messageText)){
                                        // set results of track for each race
                                        int numTracks = protocol.getTracks();
                                        protocol.getResults().add(Double.valueOf(messageText));
                                        if (protocol.getResults().size() != numTracks) {
                                            protocol.setT(protocol.getT() + 1);
                                            sendMessage.setText(service.getCounter());
                                        }else {
                                            // send message with results on tracks to another chat, increase number of races. Check and increase number of attempts
                                            if (protocol.getCompetition().equals("Штурмова драбина") || protocol.getCompetition().equals("100-м смуга з перешкодами")){
                                                sendMessageToChat.setText(service.getResults());
                                                messageSender.sendMessage(sendMessageToChat);
                                                protocol.setR(protocol.getR() + 1);
                                                if (protocol.getR()>protocol.getRace()){
                                                    protocol.setAttempts(protocol.getAttempts()+1);
                                                    protocol.setR(1);
                                                }
                                                protocol.setT(1);
                                                protocol.results.clear();
                                                sendMessage.setText(service.getCounter());
                                            }else {
                                                // send message with results on tracks to another chat, increase number of races
                                                sendMessageToChat.setText(service.getResults());
                                                messageSender.sendMessage(sendMessageToChat);
                                                protocol.setR(protocol.getR() + 1);
                                                protocol.setT(1);
                                                protocol.results.clear();
                                                sendMessage.setText(service.getCounter());
                                            }
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
                                users.setAdmin(telegramID);
                                sendMessage.setText(service.getStartBot());
                                sendMessage.setReplyMarkup(replyKeyboard.getMenuReplyKeyboard());
                            }
                        }
                    }
                }else {
                    sendMessage.setText("Адміністратора призначено \uD83D\uDCBB");
                }
            }else {
                sendMessage.setText(service.getWrongData());
            }
            messageSender.sendMessage(sendMessage);
        }

    }
    // check if received message is double
    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
