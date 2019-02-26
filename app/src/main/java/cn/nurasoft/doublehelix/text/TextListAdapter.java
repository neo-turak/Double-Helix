/*
 * Copyright (c) 2018.
 * Developed by Turak Nurahmat
 * Under the guide of  the University of Hertfordshire, Master of Computer Science final project team.
 * SID:15066679
 * License: Apache 2.0
 * All rights reserved!
 */

package cn.nurasoft.doublehelix.text;
/**
 * 
 * Modified: Nueraihemaiti Tureke
 * @Date 2017-07-07
 * version:4
 * description:Activate the new_text_activity layout
 */
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cn.nurasoft.doublehelix.R;

/**
 * Created by Miro on 2017/7/07.
 **/

public class TextListAdapter extends RecyclerView.Adapter<TextListAdapter.TextHolder> {

    private static ArrayList<String> AL_Texts = new ArrayList<>();
    private static ArrayList<String> date = new ArrayList<>();
    private final int MAX_LINE_COUNT = 3;
    private final int STATE_UNKNOWN = -1;
    private final int STATE_NOT_OVERFLOW = 1;//text lines could not overflow max line limit
    private final int STATE_COLLAPSED = 2;//text lines limit,after fold the texts
    private final int STATE_EXPANDED = 3;//expand the full texts
    private Activity mContent;
    private SparseIntArray mTextStateList;

    TextListAdapter(Activity context) {
        mContent = context;
        mTextStateList = new SparseIntArray();
    }

    public static void init(HashMap<String, String> hmap) {
        AL_Texts.clear();
        date.clear();
        Set set2 = hmap.entrySet();
        Iterator iterator2 = set2.iterator();
        while (iterator2.hasNext()) {
            Map.Entry mentry2 = (Map.Entry) iterator2.next();
            AL_Texts.add(mentry2.getValue().toString());
            date.add(mentry2.getKey().toString());
        }
    }

    @Override
    public TextHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new TextHolder(mContent.getLayoutInflater().inflate(R.layout.text_record_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final TextHolder holder, final int position) {


        holder.head.setText(position + 1 + "");//title text
        holder.date.setText(date.get(position));//set date to name;

        int state = mTextStateList.get(position, STATE_UNKNOWN);
//        if the text item is first time initialize, then get the texts line value
        if (state == STATE_UNKNOWN) {
            holder.content.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    //this function awake multiple times, so need to release after finish used every time
                    holder.content.getViewTreeObserver().removeOnPreDrawListener(this);
//                    holder.content.getViewTreeObserver().addOnPreDrawListener(null);
//                    if the shows texts greater than the limits....
                    if (holder.content.getLineCount() > MAX_LINE_COUNT) {
                        holder.content.setMaxLines(MAX_LINE_COUNT);//set the shows max lines.
                        holder.expandOrCollapse.setVisibility(View.VISIBLE);//set other texts to visible.
                        holder.expandOrCollapse.setText(R.string.ALL);//set all bottom to the hidden text
                        mTextStateList.put(position, STATE_COLLAPSED);
                    } else {
                        holder.expandOrCollapse.setVisibility(View.GONE);//hide the all texts
                        mTextStateList.put(position, STATE_NOT_OVERFLOW);//make the text do not over the limit
                    }
                    return true;
                }
            });

            holder.content.setMaxLines(Integer.MAX_VALUE);//for avoid error, set the line max value
            holder.content.setText(getContent(position));//use Util methods to get the texts
        } else {
//  if initialized before,save the status, do not need to get one more again
            switch (state) {
                case STATE_NOT_OVERFLOW:
                    holder.expandOrCollapse.setVisibility(View.GONE);
                    break;
                case STATE_COLLAPSED:
                    holder.content.setMaxLines(MAX_LINE_COUNT);
                    holder.expandOrCollapse.setVisibility(View.VISIBLE);
                    holder.expandOrCollapse.setText(R.string.ALL);
                    break;
                case STATE_EXPANDED:
                    holder.content.setMaxLines(Integer.MAX_VALUE);
                    holder.expandOrCollapse.setVisibility(View.VISIBLE);
                    holder.expandOrCollapse.setText(R.string.retract);
                    break;
            }
            holder.content.setText(getContent(position));
        }


//        listeners
        holder.expandOrCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state = mTextStateList.get(position, STATE_UNKNOWN);
                if (state == STATE_COLLAPSED) {
                    holder.content.setMaxLines(Integer.MAX_VALUE);
                    holder.expandOrCollapse.setText(R.string.retract);
                    mTextStateList.put(position, STATE_EXPANDED);
                } else if (state == STATE_EXPANDED) {
                    holder.content.setMaxLines(MAX_LINE_COUNT);
                    holder.expandOrCollapse.setText(R.string.ALL);
                    mTextStateList.put(position, STATE_COLLAPSED);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.e("Count", AL_Texts.size() + "");
        return AL_Texts.size();//all counts;
    }

    private String getContent(int position) {
        return AL_Texts.get(position % AL_Texts.size());
    }

    class TextHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView content;
        TextView head;
        TextView expandOrCollapse;

        TextHolder(View itemView) {
            super(itemView);
//            binding to xml
            head = itemView.findViewById(R.id.tv_head);
            date = itemView.findViewById(R.id.tv_date);
            content = itemView.findViewById(R.id.tv_content);
            expandOrCollapse = itemView.findViewById(R.id.tv_expand_or_collapse);
        }
    }

}