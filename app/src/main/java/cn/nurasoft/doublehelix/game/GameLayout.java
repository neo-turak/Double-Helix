/*
 * Copyright (c) 2018.
 * Developed by Turak Nurahmat
 * Under the guide of  the University of Hertfordshire, Master of Computer Science final project team.
 * SID:15066679
 * License: Apache 2.0
 * All rights reserved!
 */

package cn.nurasoft.doublehelix.game;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * /*
 * Adapter class from online resources, got authentication
 * */

public class GameLayout extends RelativeLayout {

    /**
     * set column number to 4
     */
    private int mColumn = 4;
    /**
     * save all items
     */
    private GameItem[] mGameItems;

    /**
     * Item distace
     */
    private int mMargin = 10;
    /**
     * the board padding
     */
    private int mPadding;
    /**
     * detect user finger activity
     */
    private GestureDetector mGestureDetector;

    // detect needs new value or not 
    private boolean isMergeHappen = true;
    private boolean isMoveHappen = true;

    /**
     * record the record
     */
    private int mScore;
    private OnGame2048Listener mGame2048Listener;
    private boolean once;

    public GameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                mMargin, getResources().getDisplayMetrics());
        // set the inner layout distace, make sure divided properly
        mPadding = min(getPaddingLeft(), getPaddingTop(), getPaddingRight(),
                getPaddingBottom());

        mGestureDetector = new GestureDetector(context, new MyGestureDetector());

    }

    public GameLayout(Context context) {
        this(context, null);
    }

    public GameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void setOnGame2048Listener(OnGame2048Listener mGame2048Listener) {
        this.mGame2048Listener = mGame2048Listener;
    }

    /**
     * According to the user movement, the overall mobile consolidation value
     */
    private void action(ACTION action) {
        // column|row
        for (int i = 0; i < mColumn; i++) {
            List<GameItem> row = new ArrayList<GameItem>();
            // column|row
            //record not null number
            for (int j = 0; j < mColumn; j++) {
                // get index
                int index = getIndexByAction(action, i, j);

                GameItem item = mGameItems[index];
                // record not null number
                if (item.getNumber() != 0) {
                    row.add(item);
                }
            }

            //extimate is moved or not
            for (int j = 0; j < mColumn && j < row.size(); j++) {
                int index = getIndexByAction(action, i, j);
                GameItem item = mGameItems[index];

                if (item.getNumber() != row.get(j).getNumber()) {
                    isMoveHappen = true;
                }
            }

            // merge same number
            mergeItem(row);

            // get the merged value
            for (int j = 0; j < mColumn; j++) {
                int index = getIndexByAction(action, i, j);
                if (row.size() > j) {
                    mGameItems[index].setNumber(row.get(j).getNumber());
                } else {
                    mGameItems[index].setNumber(0);
                }
            }

        }
        //renderer new number
        generateNum();

    }

    /**
     * according to Action,i,j get index
     *
     * @param action
     * @param i
     * @param j
     * @return
     */
    private int getIndexByAction(ACTION action, int i, int j) {
        int index = -1;
        switch (action) {
            case LEFT:
                index = i * mColumn + j;
                break;
            case RIGHT:
                index = i * mColumn + mColumn - j - 1;
                break;
            case UP:
                index = i + j * mColumn;
                break;
            case DOWM:
                index = i + (mColumn - 1 - j) * mColumn;
                break;
        }
        return index;
    }

    /**
     * merge same Items
     *
     * @param row
     */
    private void mergeItem(List<GameItem> row) {
        if (row.size() < 2)
            return;

        for (int j = 0; j < row.size() - 1; j++) {
            GameItem item1 = row.get(j);
            GameItem item2 = row.get(j + 1);

            if (item1.getNumber() == item2.getNumber()) {
                isMergeHappen = true;

                int val = item1.getNumber() + item2.getNumber();
                item1.setNumber(val);

                // increase score
                mScore += val;
                if (mGame2048Listener != null) {
                    mGame2048Listener.onScoreChange(mScore);
                }

                // move forward
                for (int k = j + 1; k < row.size() - 1; k++) {
                    row.get(k).setNumber(row.get(k + 1).getNumber());
                }

                row.get(row.size() - 1).setNumber(0);
                return;
            }

        }

    }

    /**
     * get the minimum number
     *
     * @param params
     * @return
     */
    private int min(int... params) {
        int min = params[0];
        for (int param : params) {
            if (min > param) {
                min = param;
            }
        }
        return min;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    /**
     * Measure the width and height of Layout, and set the width and height of Item. Here, ignore wrap_content and draw the square with the minimum value between width and height
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // Get the side length of the square
        int length = Math.min(getMeasuredHeight(), getMeasuredWidth());
        // Get the width of Item
        int childWidth = (length - mPadding * 2 - mMargin * (mColumn - 1))
                / mColumn;

        if (!once) {
            if (mGameItems == null) {
                mGameItems = new GameItem[mColumn * mColumn];
            }
            // place Item
            for (int i = 0; i < mGameItems.length; i++) {
                GameItem item = new GameItem(getContext());

                mGameItems[i] = item;
                item.setId(i + 1);
                RelativeLayout.LayoutParams lp = new LayoutParams(childWidth,
                        childWidth);
                // Set the horizontal margin,not the last one
                if ((i + 1) % mColumn != 0) {
                    lp.rightMargin = mMargin;
                }
                // not first line
                if (i % mColumn != 0) {
                    lp.addRule(RelativeLayout.RIGHT_OF,//
                            mGameItems[i - 1].getId());
                }
                // not first line，//Set the vertical margin, not the last line
                if ((i + 1) > mColumn) {
                    lp.topMargin = mMargin;
                    lp.addRule(RelativeLayout.BELOW,//
                            mGameItems[i - mColumn].getId());
                }
                addView(item, lp);
            }
            generateNum();
        }
        once = true;

        setMeasuredDimension(length, length);
    }

    /**
     * whather full of numbers or not
     *
     * @return
     */
    private boolean isFull() {
        // Check to see if all locations have Numbers
        for (int i = 0; i < mGameItems.length; i++) {
            if (mGameItems[i].getNumber() == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     *All current locations are checked with a number, and there is no identical number next to each other
     *
     * @return
     */
    private boolean checkOver() {
        // Check to see if all locations have Numbers
        if (!isFull()) {
            return false;
        }

        for (int i = 0; i < mColumn; i++) {
            for (int j = 0; j < mColumn; j++) {

                int index = i * mColumn + j;

                // current Item
                GameItem item = mGameItems[index];
                // right 
                if ((index + 1) % mColumn != 0) {
                    Log.e("TAG", "RIGHT");
                    // right Item
                    GameItem itemRight = mGameItems[index + 1];
                    if (item.getNumber() == itemRight.getNumber())
                        return false;
                }
                // bottom
                if ((index + mColumn) < mColumn * mColumn) {
                    Log.e("TAG", "DOWN");
                    GameItem itemBottom = mGameItems[index + mColumn];
                    if (item.getNumber() == itemBottom.getNumber())
                        return false;
                }
                // left
                if (index % mColumn != 0) {
                    Log.e("TAG", "LEFT");
                    GameItem itemLeft = mGameItems[index - 1];
                    if (itemLeft.getNumber() == item.getNumber())
                        return false;
                }
                // up
                if (index + 1 > mColumn) {
                    Log.e("TAG", "UP");
                    GameItem itemTop = mGameItems[index - mColumn];
                    if (item.getNumber() == itemTop.getNumber())
                        return false;
                }

            }

        }

        return true;

    }

    /**
     * generate new number
     */
    public void generateNum() {

        if (checkOver()) {
            Log.e("TAG", "GAME OVER");
            if (mGame2048Listener != null) {
                mGame2048Listener.onGameOver();
            }
            return;
        }

        if (!isFull()) {
            if (isMoveHappen || isMergeHappen) {
                Random random = new Random();
                int next = random.nextInt(16);
                GameItem item = mGameItems[next];

                while (item.getNumber() != 0) {
                    next = random.nextInt(16);
                    item = mGameItems[next];
                }

                item.setNumber(Math.random() > 0.75 ? 4 : 2);

                isMergeHappen = isMoveHappen = false;
            }

        }
    }

    /**
     * restart the game
     */
    public void restart() {
        for (GameItem item : mGameItems) {
            item.setNumber(0);
        }
        mScore = 0;
        if (mGame2048Listener != null) {
            mGame2048Listener.onScoreChange(mScore);
        }
        isMoveHappen = isMergeHappen = true;
        generateNum();
    }

    /**
     * Enumeration of directions of motion
     *
     * @author zhy
     */
    private enum ACTION {
        LEFT, RIGHT, UP, DOWM
    }

    public interface OnGame2048Listener {
        void onScoreChange(int score);

        void onGameOver();
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

        final int FLING_MIN_DISTANCE = 50;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            float x = e2.getX() - e1.getX();
            float y = e2.getY() - e1.getY();

            if (x > FLING_MIN_DISTANCE
                    && Math.abs(velocityX) > Math.abs(velocityY)) {
                action(ACTION.RIGHT);
                // Toast.makeText(getContext(), "toRight",
                // Toast.LENGTH_SHORT).show();

            } else if (x < -FLING_MIN_DISTANCE
                    && Math.abs(velocityX) > Math.abs(velocityY)) {
                action(ACTION.LEFT);
                // Toast.makeText(getContext(), "toLeft",
                // Toast.LENGTH_SHORT).show();

            } else if (y > FLING_MIN_DISTANCE
                    && Math.abs(velocityX) < Math.abs(velocityY)) {
                action(ACTION.DOWM);
                // Toast.makeText(getContext(), "toDown",
                // Toast.LENGTH_SHORT).show();

            } else if (y < -FLING_MIN_DISTANCE
                    && Math.abs(velocityX) < Math.abs(velocityY)) {
                action(ACTION.UP);
                // Toast.makeText(getContext(), "toUp",
                // Toast.LENGTH_SHORT).show();
            }
            return true;

        }

    }

}