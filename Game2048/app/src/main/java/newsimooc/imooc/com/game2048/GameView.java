package newsimooc.imooc.com.game2048;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.GridLayout;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/10/27.
 */

public class GameView extends GridLayout {

    //对应卡片数组
    Card[][] cards = new Card[4][4];
    //存放卡片的位置
    List<Point> emptyPointList = new ArrayList<>();
    //宽高
    private int sceneWidth, sceneHeight;
    private int cardWidth, cardHeight;
    //行数
    private static final int LINES = 4;

    public GameView(Context context) {
        super(context);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGameView();
    }


    /**
     * 初始化方法
     */
    private void initGameView() {
        setColumnCount(LINES);  //4列
        setBackgroundColor(0xff8E8E8E);  //设置背景颜色
        addCards();  //添加卡片

        setOnTouchListener(new OnTouchListener() {
            private float startX, startY, changeX, changeY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        changeX = event.getX() - startX;
                        changeY = event.getY() - startY;

                        //有横向移动趋势
                        if (Math.abs(changeX) > Math.abs(changeY)) {
                            if (changeX > 5) {  //向右
                                swipeRight();
                            } else if (changeX < -5) {  //向左
                                swipeLeft();
                            }
                        } else {
                            if (changeY > 5) {  //向下
                                swipeDown();
                            } else if (changeY < -5) {  //向上
                                swipeUp();
                            }
                        }
                        break;
                }
                return true;
            }
        });

        startGame();
    }


    public void startGame() {
        //清空分数
        if (MainActivity.getMainActivity() != null) {
            MainActivity.getMainActivity().clearScore();
        }

        for (int i=0; i<LINES; i++) {
            for (int j=0; j<LINES; j++) {
                cards[i][j].setNum(0);
            }
        }
        //随机添加两张卡片
        cardRandom();
        cardRandom();
        //清空分数
        MainActivity.getMainActivity().clearScore();
        //MainActivity.getMainActivity().showScore();
    }

    /**
     * 向上移动
     */
    private void swipeUp() {

        boolean move = false;
        for (int j=0; j<LINES; j++) {
            for (int i=0; i<LINES; i++) {
                for (int x=i+1; x<LINES; x++) {
                    if (cards[x][j].getNum() > 0) {
                        if (cards[i][j].getNum() <= 0) {
                            cards[i][j].setNum(cards[x][j].getNum());
                            cards[x][j].setNum(0);
                            cards[i][j].getCardTranslateAnimation(0, 0, 700, 0);
                            i --;
                            move = true;
                        } else if (cards[x][j].equals(cards[i][j])) {
                            cards[i][j].setNum(cards[i][j].getNum()*2);
                            MainActivity.getMainActivity().addScore(cards[i][j].getNum());
                            MainActivity.getMainActivity().showScore();
                            int score = MainActivity.getMainActivity().getScore();
                            int max_score = MainActivity.getMainActivity().getMax_score();
                            if (score > max_score) {
                                MainActivity.getMainActivity().setMax_score(score);
                                MainActivity.getMainActivity().showMaxScore();
                            }
                            cards[i][j].getCardAnimation();
                            cards[x][j].setNum(0);
                            //增加分数

                            move = true;
                        }
                        break;
                    }
                }
            }
        }
        if (move == true) {
            cardRandom();
            //检测游戏是否已经结束
            checkGameOver();
        }
    }

    /**
     * 向下移动
     */
    private void swipeDown() {
        boolean move = false;
        for (int j=0; j<LINES; j++) {
            for (int i=LINES-1; i>=0; i--) {
                for (int x=i-1; x>=0; x--) {
                    if (cards[x][j].getNum() > 0) {
                        if (cards[i][j].getNum() <= 0) {
                            cards[i][j].setNum(cards[x][j].getNum());
                            cards[x][j].setNum(0);
                            cards[i][j].getCardTranslateAnimation(0, 0, 700, 0);
                            i ++;
                            move = true;
                        } else if (cards[x][j].equals(cards[i][j])) {
                            cards[i][j].setNum(cards[i][j].getNum()*2);
                            MainActivity.getMainActivity().addScore(cards[i][j].getNum());
                            MainActivity.getMainActivity().showScore();
                            int score = MainActivity.getMainActivity().getScore();
                            int max_score = MainActivity.getMainActivity().getMax_score();
                            if (score > max_score) {
                                MainActivity.getMainActivity().setMax_score(score);
                                MainActivity.getMainActivity().showMaxScore();
                            }
                            cards[i][j].getCardAnimation();
                            cards[x][j].setNum(0);
                            //增加分数

                            move = true;
                        }
                        break;
                    }
                }
            }
        }
        if (move == true) {
            cardRandom();
            //检测游戏是否已经结束
            checkGameOver();
        }
    }

    /**
     * 向左移动
     */
    private void swipeLeft() {
        boolean move = false;
        for (int i=0; i<LINES; i++) {
            for (int j=0; j<LINES; j++) {
                for (int x=j+1; x<LINES; x++) {
                    if (cards[i][x].getNum() > 0) {
                        //如果这个卡片前面的一个是空的
                        if (cards[i][j].getNum() <= 0) {

                            cards[i][j].setNum(cards[i][x].getNum());
                            cards[i][x].setNum(0);
                            cards[i][j].getCardTranslateAnimation(700, 0, 0, 0);
                            j --;
                            move = true;  //判断确实进行过移动
                        } else if (cards[i][x].equals(cards[i][j])) {  //如果和前面那个相等

                            cards[i][j].setNum(cards[i][j].getNum()*2);
                            MainActivity.getMainActivity().addScore(cards[i][j].getNum());
                            MainActivity.getMainActivity().showScore();
                            int score = MainActivity.getMainActivity().getScore();
                            int max_score = MainActivity.getMainActivity().getMax_score();
                            if (score > max_score) {
                                MainActivity.getMainActivity().setMax_score(score);
                                MainActivity.getMainActivity().showMaxScore();
                            }
                            cards[i][j].getCardAnimation();
                            cards[i][x].setNum(0);
                            //cards[i][j].getCardTranslateAnimation();
                            //增加分数

                            move = true;
                        }
                        break;
                    }
                }
            }
        }

        //确实移动过
        if (move == true) {
            cardRandom();
            //检测游戏是否已经结束
            checkGameOver();
        }
    }

    /**
     * 向右移动
     */
    private void swipeRight() {
        boolean move = false;
        for (int i=0; i<LINES; i++) {
            for (int j=LINES-1; j>=0; j--) {
                for (int x=j-1; x>=0; x--) {
                    if (cards[i][x].getNum() > 0) {
                        if (cards[i][j].getNum() <= 0) {
                            cards[i][j].setNum(cards[i][x].getNum());
                            cards[i][x].setNum(0);
                            cards[i][j].getCardTranslateAnimation(700, 0, 0, 0);
                            j ++;
                            move = true;
                        } else if (cards[i][x].equals(cards[i][j])) {
                            cards[i][j].setNum(cards[i][j].getNum()*2);
                            MainActivity.getMainActivity().addScore(cards[i][j].getNum());
                            MainActivity.getMainActivity().showScore();
                            int score = MainActivity.getMainActivity().getScore();
                            int max_score = MainActivity.getMainActivity().getMax_score();
                            if (score > max_score) {
                                MainActivity.getMainActivity().setMax_score(score);
                                MainActivity.getMainActivity().showMaxScore();
                            }
                            cards[i][j].getCardAnimation();
                            cards[i][x].setNum(0);
                            //增加分数

                            move = true;
                        }
                        break;
                    }
                }
            }
        }
        if (move == true) {
            cardRandom();
            //检测游戏是否已经结束
            checkGameOver();
        }
    }

    /**
     * 添加卡片
     */
    private void addCards() {
        //int cnt = 1;
        getCardWidthOrHeight();
        for (int i=0; i<LINES; i++) {
            for (int j=0; j<LINES; j++) {
                Card card = new Card(getContext());
                addView(card, cardWidth, cardHeight);
                card.setNum(0);

                //cnt ++;
                cards[i][j] = card;
            }
        }
    }

    /**
     * 根据屏幕的宽高获取卡片的大小
     */
    private void getCardWidthOrHeight() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        //获取屏幕的宽高
        sceneWidth = dm.widthPixels;
        sceneHeight = dm.heightPixels;

        //获取卡片的宽高(宽高都是一样的)
        cardHeight = (Math.min(sceneHeight, sceneWidth) - 10) / LINES;
        cardWidth = (Math.min(sceneHeight, sceneWidth) - 10) / LINES;

    }

    /**
     * 获取随机位置出现的卡片
     */
    private void cardRandom() {
        emptyPointList.clear();
        for (int i=0; i<LINES; i++) {
            for (int j=0; j<LINES; j++) {
                //当前卡片是空的
                if (cards[i][j].getNum() == 0) {
                    emptyPointList.add(new Point(i, j));
                }
            }
        }

        //随机移除一个点  添上数据并上色
        Point point = emptyPointList.remove((int) (Math.random() * emptyPointList.size()));
        cards[point.x][point.y].setNum( Math.random()>0.4 ? 2 : 4 );
    }

    /**
     * 检测游戏是否已经结束
     */
    private void checkGameOver() {
        boolean flag = true;
        for (int i=0; i<LINES; i++) {
            for (int j=0; j<LINES; j++) {
                if (cards[i][j].getNum() == 0 ||
                        (i > 0 && cards[i][j].equals(cards[i-1][j])) ||
                        (i < 3 && cards[i][j].equals(cards[i+1][j])) ||
                        (j > 0 && cards[i][j].equals(cards[i][j-1])) ||
                        (j < 3 && cards[i][j].equals(cards[i][j+1])) ) {
                    flag = false;
                    break;
                }
            }
            //游戏没有结束
            if (!flag) {
                break;
            }
        }
        //游戏结束界面
        if (flag) {
            List<MaxScore> maxScoreList = DataSupport.findAll(MaxScore.class);
            //如果最高分比数据库中的最高分大，那么就更新
            if (MainActivity.getMainActivity().getMax_score()
                    > maxScoreList.get(0).getMaxscore()) {
                MaxScore maxScore = new MaxScore();
                maxScore.setMaxscore(MainActivity.getMainActivity().getMax_score());
                maxScore.updateAll("id = ?", "1");
            }
            new AlertDialog.Builder(getContext()).setTitle("结束界面")
                    .setMessage("游戏结束")
                    .setPositiveButton("重新开始", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startGame();
                        }
                    }).show();
        }
    }
}
