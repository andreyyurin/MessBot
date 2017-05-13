package hb.messbot;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import hb.messbot.fragments.DBHelper;

/**
 * Created by Andrey on 17.02.2017.
 */

public class CustAdapter extends
        RecyclerView.Adapter<RecyclerViewHolder> {

    private Context context;
    private ArrayList<String> sentences, answers;
    private final LayoutInflater mInflater;

    public CustAdapter(Context context, ArrayList<String> sentences, ArrayList<String> answers) {;
        // this.users = users;
        this.context = context;
        this.sentences = sentences;
        this.answers = answers;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getItemCount() {
        return (null != sentences ? sentences.size() : 0);
    }


    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        // final Data_Model model = arrayList.get(position);
        final SQLiteDatabase mSqLiteDatabase;
        DBHelper mDatabaseHelper;

        mDatabaseHelper = new DBHelper(context, "showdb.db", null, 1);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();

        final RecyclerViewHolder mainHolder = (RecyclerViewHolder) holder;// holder
        // mainHolder.imageview.getLayoutParams().height = mainHolder.imageview.getWidth();

        if(AllAct.flagsHave.get(position)==0) {

            final ArrayList<Integer> allads = new ArrayList<Integer>();


            if(position!=AllAct.flagsHave.size()-1) {
                for (int i = position + 1; i < AllAct.flagsHave.size(); i++) {
                    if (Objects.equals(AllAct.arrayBase.get(i), sentences.get(position))) {
                        AllAct.flagsHave.set(i, 1);
                        allads.add(i);

                        TextView value = new TextView(context);
                        value.setTextColor(context.getResources().getColor(R.color.bg_text1));
                        value.setText((allads.size()+1)+") Ответ на сообщение: "+AllAct.arrayBase2.get(i));
                        value.setTextSize(15);
                        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        lp2.gravity = Gravity.CENTER_VERTICAL;
                        lp2.weight = 1.0f;
                        value.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                        value.setLayoutParams(lp2);


                        //linearLayout.setWeightSum(1.0f);

                        ImageButton imgBtn = new ImageButton(context);
                        imgBtn.setImageResource(R.drawable.icclear);
                        imgBtn.setBackgroundResource(R.drawable.bgall);
                        imgBtn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.MATCH_PARENT
                        );
                        lp.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
                        lp.weight = 5.0f;
                        imgBtn.setLayoutParams(lp);
                        //linearLayout.addView(imgBtn);



                        View view = new View(context);
                        view.setBackgroundColor(context.getResources().getColor(R.color.colorBlack));

                        LinearLayout.LayoutParams layotParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics()));
                        layotParams.setMargins(
                                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()),
                                0,
                                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()),
                                0
                        );
                        view.setLayoutParams(layotParams);

                        final LinearLayout swipeLayout = new LinearLayout(context);
                        swipeLayout.setOrientation(LinearLayout.HORIZONTAL);


                        swipeLayout.addView(value);
                        swipeLayout.addView(imgBtn);

                        //swipeLayout.addView(linearLayout);



                        swipeLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, context.getResources().getDisplayMetrics())
                        ));

                        final LinearLayout offLayout = new LinearLayout(context);
                        offLayout.setOrientation(LinearLayout.VERTICAL);


                        offLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        ));

                        offLayout.addView(swipeLayout);
                        offLayout.addView(view);

                        final int finalI = i;

                        imgBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AllAct.deleteElement(finalI);

                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                    final Animator[] animator = {null};
                                    final Animator[] animator2 = {null};

                                    AllAct.linear.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            int cx = (AllAct.linear.getLeft() + AllAct.linear.getRight()) / 2;
                                            int cy = (AllAct.linear.getTop());

                                            int dx = Math.max(cx, AllAct.linear.getWidth() - cx);
                                            int dy = Math.max(cy, AllAct.linear.getHeight() - cy);

                                            float finalRadius = (float) Math.hypot(dx, dy);

                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                animator[0] = ViewAnimationUtils.createCircularReveal(AllAct.linear, cx, cy, finalRadius, 0);
                                            }

                                            animator[0].setInterpolator(new AccelerateDecelerateInterpolator());
                                            animator[0].setDuration(500);
                                            animator[0].start();
                                            animator[0].addListener(new AnimatorListenerAdapter() {
                                                @Override
                                                public void onAnimationEnd(Animator animation) {
                                                    super.onAnimationEnd(animation);
                                                    AllAct.linear.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
                                                    AllAct.linear.setVisibility(View.INVISIBLE);



                                                    swipeLayout.post(new Runnable() {
                                                        public void run() {
                                                            int cx = (offLayout.getLeft());
                                                            int cy = (offLayout.getTop() + offLayout.getBottom()) / 2;

                                                            int dx = Math.max(cx, offLayout.getWidth() - cx);
                                                            int dy = Math.max(cy, offLayout.getHeight() - cy);

                                                            float finalRadius = (float) Math.hypot(dx, dy);

                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                                animator2[0] = ViewAnimationUtils.createCircularReveal(offLayout, cx, cy, finalRadius, 0);
                                                            }

                                                            animator2[0].setInterpolator(new AccelerateDecelerateInterpolator());
                                                            animator2[0].setDuration(300);
                                                            animator2[0].start();
                                                            animator2[0].addListener(new AnimatorListenerAdapter() {
                                                                @Override
                                                                public void onAnimationEnd(Animator animation) {
                                                                    super.onAnimationEnd(animation);
                                                                    AllAct.show_base(context);
                                                                }
                                                            });


                                                        }
                                                    });
                                                }
                                            });


                                        }
                                    });

                                } else {
                                    AllAct.linear.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
                                    AllAct.linear.setVisibility(View.INVISIBLE);
                                    AllAct.show_base(context);
                                }
                            }
                        });



                       // swipeLayout.addDrag(SwipeLayout.DragEdge.Left, findViewById(R.id.bottom_wrapper));


                        mainHolder.linearForm.addView(offLayout);
                        //mainHolder.linearForm.addView(view);
                    }
                }
            }


            final String sent = sentences.get(position);
            String ans = answers.get(position);



            mainHolder.ans.setText("1) Ответ на сообщение: " + ans);
            mainHolder.sent.setText("Сообщение: " + sent);

            mainHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mainHolder.checkBox.isChecked()) {
                        for(int i = 0; i<allads.size(); i++)
                        {
                            AllAct.flags.set(allads.get(i), 1);
                        }
                        AllAct.flags.set(position, 1);
                    } else {
                        for(int i = 0; i<allads.size(); i++)
                        {
                            AllAct.flags.set(allads.get(i), 0);
                        }
                        AllAct.flags.set(position, 0);
                    }
                }
            });

            mainHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(allads.size()>0) {
                        for (int i = 0; i < allads.size(); i++) {
                            mSqLiteDatabase.delete("data", "text = '" + AllAct.arrayBase.get(allads.get(i)) + "' and answer = '"+ AllAct.arrayBase2.get(allads.get(i))+"'", null);
                            mSqLiteDatabase.delete("data", "answer = '" + AllAct.arrayBase2.get(allads.get(i)) + "' and text = '"+ AllAct.arrayBase.get(allads.get(i))+"'",null);
                            AllAct.arrayBase.remove(allads.get(i));
                            AllAct.flags.remove(allads.get(i));
                            AllAct.flagsHave.remove(allads.get(i));
                            AllAct.arrayBase2.remove(allads.get(i));
                        }
                    }

                    mSqLiteDatabase.delete("data", "text = '" + AllAct.arrayBase.get(position) + "' and answer = '"+ AllAct.arrayBase2.get(position)+"'", null);
                    mSqLiteDatabase.delete("data", "answer = '" + AllAct.arrayBase2.get(position) + "' and text = '"+ AllAct.arrayBase.get(position)+"'",null);
                    AllAct.arrayBase.remove(position);
                    AllAct.flags.remove(position);
                    AllAct.flagsHave.remove(position);
                    AllAct.arrayBase2.remove(position);

                    boolean fall = false;
                    if(AllAct.linear.getVisibility()==View.VISIBLE) {
                        fall = true;
                    }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        final Animator[] animator = {null};
                        AllAct.linear.post(new Runnable() {
                            @Override
                            public void run() {

                                int cx = (AllAct.linear.getLeft() + AllAct.linear.getRight()) / 2;
                                int cy = (AllAct.linear.getTop());

                                int dx = Math.max(cx, AllAct.linear.getWidth() - cx);
                                int dy = Math.max(cy, AllAct.linear.getHeight() - cy);

                                float finalRadius = (float) Math.hypot(dx, dy);

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    animator[0] = ViewAnimationUtils.createCircularReveal(AllAct.linear, cx, cy, finalRadius, 0);
                                }

                                animator[0].setInterpolator(new AccelerateDecelerateInterpolator());
                                    animator[0].setDuration(500);
                                    animator[0].start();
                                    animator[0].addListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            AllAct.linear.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
                                            AllAct.linear.setVisibility(View.INVISIBLE);
                                            AllAct.show_base(context);
                                        }
                                    });


                            }
                        });
                }else {
                    AllAct.linear.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
                    AllAct.linear.setVisibility(View.INVISIBLE);
                }



                 /*   if(fall) {
                        AllAct.linear.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        AllAct.linear.setVisibility(View.VISIBLE);
                    }*/
                }
            });

            mainHolder.btnAddAns.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    LayoutInflater li = LayoutInflater.from(context);
                    View promptsView = li.inflate(R.layout.addform, null);

                    final EditText addAns2 = (EditText) promptsView.findViewById(R.id.addAns);

                    new AlertDialog.Builder(context)
                            .setTitle("Добавить еще один ответ")
                            .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    String ans2 = addAns2.getText().toString();
                                    //Toast.makeText(context, ans2, Toast.LENGTH_SHORT).show();
                                    AllAct.arrayBase.add(sent);
                                    AllAct.flags.add(0);
                                    AllAct.flagsHave.add(0);
                                    AllAct.arrayBase2.add(ans2);

                                    ContentValues values = new ContentValues();
                                    values.put(DBHelper.TEXT_COLUMN, sent);
                                    values.put(DBHelper.ANSWER_COLUMN, ans2);
                                    mSqLiteDatabase.insert("data", null, values);
                                    AllAct.show_base(context);
                                }
                            })
                            .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            })
                            .setView(promptsView)
                            .show();

                }
            });




        }else{
            mainHolder.cardView.setVisibility(View.INVISIBLE);
            mainHolder.cardView.setLayoutParams(new CardView.LayoutParams(0, 0));
        }


        // bitmap

        // setting title

      /*  mainHolder.title.setText(model.getTitle());

        mainHolder.imageview.setImageBitmap(image);*/



    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.item_db, viewGroup, false);
        RecyclerViewHolder listHolder = new RecyclerViewHolder(mainGroup);
        return listHolder;

    }

}