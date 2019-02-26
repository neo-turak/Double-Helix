/*
 * Copyright (c) 2018.
 * Developed by Turak Nurahmat
 * Under the guide of  the University of Hertfordshire, Master of Computer Science final project team.
 * SID:15066679
 * License: Apache 2.0
 * All rights reserved!
 */

package cn.nurasoft.doublehelix;


import java.util.ArrayList;
import java.util.Random;

import cn.nurasoft.doublehelix.chat.ChatModel;
import cn.nurasoft.doublehelix.chat.ItemModel;

/**
 * created by:Miro
 */
class TestData {

    /*
     *all pictures are came from my domain, and also taken picture by myself
     * @description: this class just for chatting history;
     */
    static ArrayList<ItemModel> getTestAdData() {
        ArrayList<ItemModel> models = new ArrayList<>();
        ChatModel model = new ChatModel();
        model.setContent("Hello！");
        model.setIcon("https://donghominhtan.com/wp-content/uploads/2018/05/icon-facebook-buon.jpg");
        models.add(new ItemModel(ItemModel.CHAT_A, model));

        ChatModel model2 = new ChatModel();
        model2.setContent("Who is there？");
        model2.setIcon("https://donghominhtan.com/wp-content/uploads/2018/05/y-nghia-icon-facebook-100x100.jpg");
        models.add(new ItemModel(ItemModel.CHAT_B, model2));
        ChatModel model3 = new ChatModel();
        model3.setContent("I am robot");
        model3.setIcon("https://donghominhtan.com/wp-content/uploads/2018/05/icon-facebook-buon.jpg");
        models.add(new ItemModel(ItemModel.CHAT_A, model3));

        ChatModel model4 = new ChatModel();
        model4.setContent("What?are you kidding me?");
        model4.setIcon("https://donghominhtan.com/wp-content/uploads/2018/05/y-nghia-icon-facebook-100x100.jpg");
        models.add(new ItemModel(ItemModel.CHAT_B, model4));

        ChatModel model5 = new ChatModel();
        model5.setContent("No, I am not");
        model5.setIcon("https://donghominhtan.com/wp-content/uploads/2018/05/icon-facebook-buon.jpg");
        models.add(new ItemModel(ItemModel.CHAT_A, model5));

        ChatModel model6 = new ChatModel();
        model6.setContent("OK,");
        model6.setIcon("https://donghominhtan.com/wp-content/uploads/2018/05/y-nghia-icon-facebook-100x100.jpg");
        models.add(new ItemModel(ItemModel.CHAT_B, model6));

        return models;
    }


    /*
    create random number;
     */
    static Integer randomNum() {
        int x;
        Random random = new Random();
        x = random.nextInt(4);
        switch (x) {
            case 0:
                return R.drawable.a4;
            case 1:
                return R.drawable.a1;
            case 2:
                return R.drawable.a2;
            case 3:
                return R.drawable.a3;
            default:
                return R.drawable.a4;
        }
    }
}
