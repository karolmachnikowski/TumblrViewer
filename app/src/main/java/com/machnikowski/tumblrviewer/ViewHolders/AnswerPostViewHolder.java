package com.machnikowski.tumblrviewer.ViewHolders;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.machnikowski.tumblrviewer.R;

import org.json.JSONException;
import org.json.JSONObject;

public class AnswerPostViewHolder extends PostViewHolder {
    private TextView questionTextView;
    private TextView answerTextView;
    private String question = null;
    private String answer = null;

    public AnswerPostViewHolder(View view) {
        super(view);
        questionTextView = (TextView) view.findViewById(R.id.tv_answerpost_question);
        answerTextView = (TextView) view.findViewById(R.id.tv_answerpost_answer);

    }

    @Override
    public void bindData(JSONObject post) {
        if(post == null)
            return;
        try {
            getData(post);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (answer != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                answerTextView.setText(Html.fromHtml(answer, Html.FROM_HTML_MODE_LEGACY));
            } else {
                answerTextView.setText(Html.fromHtml(answer));
            }
            answerTextView.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            answerTextView.setVisibility(View.GONE);
        }

        if (question != null) {
            questionTextView.setText(question);
        } else {
            questionTextView.setVisibility(View.GONE);
        }
    }

    private void getData(JSONObject post) throws JSONException {
        if(!post.isNull("question"))
            question = post.getString("question");
        if(!post.isNull("answer"))
            answer = post.getString("answer");
    }

}