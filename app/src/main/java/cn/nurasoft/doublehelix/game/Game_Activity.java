/*
 * Copyright (c) 2018.
 * Developed by Turak Nurahmat
 * Under the guide of  the University of Hertfordshire, Master of Computer Science final project team.
 * SID:15066679
 * License: Apache 2.0
 * All rights reserved!
 */

package cn.nurasoft.doublehelix.game;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import cn.nurasoft.doublehelix.R;
/*
* Adapter class from online resources, got authentication
* */


public class Game_Activity extends Activity implements GameLayout.OnGame2048Listener {
    private GameLayout mGameLayout;

    private TextView mScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mScore = findViewById(R.id.id_score);
        mGameLayout = findViewById(R.id.id_game2048);
        mGameLayout.setOnGame2048Listener(this);
    }

    @Override
    public void onScoreChange(int score) {
        mScore.setText("SCORE: " + score);
    }

    @Override
    public void onGameOver() {
        new AlertDialog.Builder(this).setTitle("GAME OVER")
                .setMessage("YOU HAVE GOT " + mScore.getText())
                .setPositiveButton("RESTART", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mGameLayout.restart();
                    }
                }).setNegativeButton("EXIT", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).show();
    }

}