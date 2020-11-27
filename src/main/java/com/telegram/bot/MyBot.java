package com.telegram.bot;

import org.springframework.core.io.ClassPathResource;
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
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MyBot extends TelegramLongPollingBot {

    private File f = new ClassPathResource("img/").getFile().getCanonicalFile();
    private List<String> imgPaths = Arrays.stream(f.list())
            .map(name -> f.getAbsolutePath() + name)
            .collect(Collectors.toList());
    private static int index = 0;

    public MyBot() throws IOException {
        for (var path: imgPaths) {
            System.out.println(path);
        }
    }

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
                var messageId = update.getCallbackQuery().getMessage().getMessageId();
                var callbackData = update.getCallbackQuery().getData();
                if (callbackData.equals("left")) {
                    if (index - 1 < 0) {
                        index = imgPaths.size() - 1;
                    } else {
                        index--;
                    }

                } else if (callbackData.equals("right")) {
                    if (index == imgPaths.size() - 1) {
                        index = 0;
                    } else {
                        index++;
                    }
                }

                var file = new File(imgPaths.get(index));
                InputMediaPhoto photo = InputMediaPhoto.builder()
                        .mediaName(file.getName())
                        .media("attach://" + file.getName())
                        .caption("New")
                        .isNewMedia(true)
                        .newMediaFile(file)
                        .build();
                EditMessageMedia editMessageMedia = EditMessageMedia.builder()
                        .messageId(messageId)
                        .chatId(chatId.toString())
                        .media(photo)
                        .replyMarkup(getInlineKeyboardMarkup())
                        .build();
                execute(editMessageMedia);
            }

            // We check if the update has a message and the message has text
            if (update.hasMessage()) {
                if ("/start".equals(update.getMessage().getText())) {
                    InlineKeyboardMarkup inlineKeyboardMarkup = getInlineKeyboardMarkup();

                    var sendPhoto = SendPhoto.builder()
                            .chatId(update.getMessage().getChatId().toString())
                            .photo(new InputFile(new File(imgPaths.get(index))))
                            .caption("Caption")
                            .replyMarkup(inlineKeyboardMarkup)
                            .build();
                    execute(sendPhoto);

                }
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton b1 = getInlineKeyboardButton("left");
        InlineKeyboardButton b2 = getInlineKeyboardButton("[" + (index + 1) + "/" + imgPaths.size() + "]");
        InlineKeyboardButton b3 = getInlineKeyboardButton("right");

        inlineKeyboardMarkup.setKeyboard(Arrays.asList(Arrays.asList(b1, b2, b3)));
        return inlineKeyboardMarkup;
    }

    private InlineKeyboardButton getInlineKeyboardButton(String buttonText) {
        InlineKeyboardButton b1 = new InlineKeyboardButton();
        b1.setText(buttonText);
        b1.setCallbackData(buttonText);
        return b1;
    }
}
