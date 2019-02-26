/*
 * Copyright (c) 2018.
 * Developed by Turak Nurahmat
 * Under the guide of  the University of Hertfordshire, Master of Computer Science final project team.
 * SID:15066679
 * License: Apache 2.0
 * All rights reserved!
 */

package cn.nurasoft.doublehelix.chat;

import java.io.Serializable;

/**
 * Created by Miro on 2017/6/28.
 **/
public class ItemModel implements Serializable {

    public static final int CHAT_A = 1001;
    public static final int CHAT_B = 1002;
    public int type;
    public Object object;

    public ItemModel(int type, Object object) {
        this.type = type;
        this.object = object;
    }
}
