package com.telegram.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;

@Service
public class InlineKeyboardBuilderService {

    @Autowired
    private FilesProvider filesProvider;

    public InlineKeyboardMarkup build(int index) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton b1 = getInlineKeyboardButton("left");
        InlineKeyboardButton b2 = getInlineKeyboardButton("[" + (index + 1) + "/" + filesProvider.getAmountOfFiles() + "]");
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
