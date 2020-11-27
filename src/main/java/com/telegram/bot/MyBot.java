package com.telegram.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Component
public class MyBot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "AllUsernamesAreOccupiedbot";
    }

    @Override
    public String getBotToken() {
        return "788953136:AAFn95hQm9vStPzrgP-fKRrHKi7srivwcYE";
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasCallbackQuery()) {
                var chatId = update.getCallbackQuery().getMessage().getChatId();
                String data = update.getCallbackQuery().getData();
                var x1 = update.getCallbackQuery().getMessage().getMessageId();
                String workingEx = "https://www.befunky.com/images/wp/wp-2014-08-milky-way-1023340_1280.jpg?auto=webp&format=jpg&width=1184";
                var file = new File("C:\\Users\\ezzva\\Pictures\\Screenshots\\Screenshot (2).png");
                InputMediaPhoto photo = InputMediaPhoto.builder()
                        .mediaName(file.getName())
                        .media("attach://" + file.getName())
                        .caption("New")
                        .isNewMedia(true)
                        .newMediaFile(file)
                        .build();
                EditMessageMedia editMessageMedia = EditMessageMedia.builder()
                        .messageId(x1)
                        .chatId(chatId.toString())
                        .media(photo)
                        .build();
                execute(editMessageMedia);
            }
            // We check if the update has a message and the message has text
            if (update.hasMessage()) {
                if ("/start".equals(update.getMessage().getText())) {
                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();



                    InlineKeyboardButton b1 = new InlineKeyboardButton();
                    b1.setText("Тык");
                    b1.setCallbackData("Button \"Тык\" has been pressed");
                    b1.setSwitchInlineQuery("Switch");
                    List<InlineKeyboardButton> keyboardButtonsRow1 = Arrays.asList(b1);

                    inlineKeyboardMarkup.setKeyboard(Arrays.asList(keyboardButtonsRow1));

                    var x = SendPhoto.builder()
                            .chatId(update.getMessage().getChatId().toString())
                            .photo(new InputFile(new File("C:\\Users\\ezzva\\Pictures\\Screenshots\\Screenshot (4).png")))
                            .caption("Caption")
                            .replyMarkup(inlineKeyboardMarkup)
                            .build();
                    execute(x);

                }
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
