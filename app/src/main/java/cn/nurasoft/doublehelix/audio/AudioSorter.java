/*
 * Copyright (c) 2018.
 * Developed by Turak Nurahmat
 * Under the guide of  the University of Hertfordshire, Master of Computer Science final project team.
 * SID:15066679
 * License: Apache 2.0
 * All rights reserved!
 */

package cn.nurasoft.doublehelix.audio;

import android.annotation.SuppressLint;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*
 * written by:Nurahmat   Turak
 * SID:15066679
 */
public class AudioSorter {


    private static File[] fm;
    public List<String> filelist = new ArrayList<>();
    public List<String> data = new ArrayList<>();
    public List<Integer> id = new ArrayList<>();

    private static void printAllInfo(File dir) {
        StringBuilder fileInfo = new StringBuilder();
        fm = dir.listFiles();

        for (File file : fm) {
            if (file.isFile()) {
                fileInfo.append(file.getName()).append(" ");
            }
        }
    }

    public List<String> GetCount() {

        @SuppressLint("SdCardPath")
        File dir = new File("/data/data/cn.nurasoft.doublehelix/audio/");

        printAllInfo(dir);

        for (File file : fm) {
            if (file.getName().contains("amr")) {

                if (file.isFile()) {

                    filelist.add(file.getName() + "");
                }
            }
        }
        return filelist;
    }

    public void DateSort() {
        for (String s : filelist) {
            String tmp1 = s.substring(0, 4) + "-" + s.substring(4, 6) + "-" + s.substring(6, 8);
            String tmp2 = s.substring(8, 10) + ":" + s.substring(10, 12) + ":" + s.substring(12, 14);
            String tmp3 = tmp1 + " " + tmp2;
            data.add(tmp3);
        }

        for (int i = 1; i <= data.size(); i++) {
            id.add(i);
        }
    }
}
