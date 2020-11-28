package com.telegram.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntBinaryOperator;

@Service
public class ImagesScrollService {

    @Value("${images.folder}")
    private String filesFolderPath;

    @Autowired
    private FilesProvider filesProvider;

    private Map<Integer, Integer> userIdToCurrentImageIndex = new HashMap<>();

    private Map<String, IntBinaryOperator> callbackDataToAction = new HashMap<>();

    @PostConstruct
    public void init() {
        callbackDataToAction.put("left", this::moveLeft);
        callbackDataToAction.put("right", this::moveRight);
    }

    public int scroll(CallbackQuery callbackQuery) {
        var data = callbackQuery.getData();
        var userId = callbackQuery.getFrom().getId();
        Integer prevUserIndex = userIdToCurrentImageIndex.get(userId);
        Integer newUserIndex;
        if (prevUserIndex == null) {
            newUserIndex = 0;
        } else {
            IntBinaryOperator action = callbackDataToAction.get(data);
            if (action == null) {
                return prevUserIndex;
            }
            newUserIndex = action.applyAsInt(prevUserIndex, filesProvider.getAmountOfFiles());
        }
        userIdToCurrentImageIndex.put(userId, newUserIndex);
        return newUserIndex;
    }

    public String openFirstPicture(Integer userId) {
        userIdToCurrentImageIndex.put(userId, 0);
        return filesProvider.getPathByIndex(0);
    }

    private int moveLeft(int index, int amountOfFiles) {
        int newIndex;
        if (index - 1 < 0) {
            newIndex = amountOfFiles - 1;
        } else {
            newIndex = index - 1;
        }
        return newIndex;
    }

    private Integer moveRight(int index, int amountOfFiles) {
        int newIndex;
        if (index == amountOfFiles - 1) {
            newIndex = 0;
        } else {
            newIndex = index + 1;
        }
        return newIndex;
    }
}
