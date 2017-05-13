package hb.messbot;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Andrey on 17.02.2017.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout linearForm;
    public TextView sent;
    public TextView ans;
    public EditText getAdd;
    public CardView cardView;
    public CheckBox checkBox;
    public ImageButton btnDelete, btnAddAns;
    public LinearLayout layoutadd;

    public RecyclerViewHolder(View view) {
        super(view);
        // Find all views ids

        this.sent = (TextView) view
                .findViewById(R.id.textSent);
        this.ans = (TextView) view
                .findViewById(R.id.textAnswer);
        this.cardView = (CardView) view
                .findViewById(R.id.card_view);
        this.checkBox = (CheckBox) view
                .findViewById(R.id.checkbox);
        this.btnDelete = (ImageButton) view
                .findViewById(R.id.btn_del);
        this.btnAddAns = (ImageButton) view
                .findViewById(R.id.add_ans);
        this.linearForm = (LinearLayout) view
                .findViewById(R.id.item_linear);
    }
}
