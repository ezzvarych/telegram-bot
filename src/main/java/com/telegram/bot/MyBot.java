package com.telegram.bot;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.File;

@Component
public class MyBot extends TelegramLongPollingBot {

    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.name}")
    private String botUsername;

    @Autowired
    private ImagesScrollService imagesScrollService;

    @Autowired
    private InlineKeyboardBuilderService inlineKeyboardBuilderService;

    @Autowired
    private FilesProvider filesProvider;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            var chatId = update.getCallbackQuery().getMessage().getChatId();
            var messageId = update.getCallbackQuery().getMessage().getMessageId();
            int newIndex = imagesScrollService.scroll(update.getCallbackQuery());
            String filePath = filesProvider.getPathByIndex(newIndex);

            var file = new File(filePath);
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
                    .replyMarkup(inlineKeyboardBuilderService.build(newIndex))
                    .build();
            execute(editMessageMedia);
        }

        // We check if the update has a message and the message has text
        if (update.hasMessage()) {
            if ("/start".equals(update.getMessage().getText())) {
                InlineKeyboardMarkup inlineKeyboardMarkup = inlineKeyboardBuilderService.build(0);

                String firstPicture = imagesScrollService.openFirstPicture(update.getMessage().getFrom().getId());

                var sendPhoto = SendPhoto.builder()
                        .chatId(update.getMessage().getChatId().toString())
                        .photo(new InputFile(new File(firstPicture)))
                        .caption("Caption")
                        .replyMarkup(inlineKeyboardMarkup)
                        .build();
                execute(sendPhoto);

            }
        }
    }
}
