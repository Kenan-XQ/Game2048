package newsimooc.imooc.com.game2048;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mGetScore;
    private TextView mMaxScore;
    private int score = 0;  //初始分数
    private int max_score = 0;  //最高分数
    private static MainActivity mainActivity = null;

    //一旦创建对象，就可以把mainActivity静态变量赋值
    public MainActivity() {
        mainActivity = this;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //绑定控件
        mGetScore = (TextView) findViewById(R.id.get_score);
        mMaxScore = (TextView) findViewById(R.id.max_score);

        LitePal.getDatabase();  //自动创建数据库
        List<MaxScore> maxScoreList = DataSupport.findAll(MaxScore.class);

        //判断有没有存入数据
        if (maxScoreList.size() == 0) {
            MaxScore maxScore = new MaxScore();
            maxScore.setMaxscore(0);
            maxScore.save();
            max_score = 0;
            //Toast.makeText(mainActivity, "成功存入第一个数据", Toast.LENGTH_SHORT).show();
        } else {
            //获取存入的最高分数
            max_score = maxScoreList.get(0).getMaxscore();
            mMaxScore.setText(max_score + "");
            //Toast.makeText(mainActivity, "已经有数据存入", Toast.LENGTH_SHORT).show();
        }

        showAlertDialog();

    }

    public void showAlertDialog() {
        new AlertDialog.Builder(this).setTitle("2048游戏2.0更新公告")
                .setMessage("1.在1.0的版本上进行了一些布局的调整。\n" +
                        "2.调整了部分数字的配色问题。\n" +
                        "3.增加了移动合并数字时的动画效果。\n" +
                        "4.增加了历史最高分的记录。\n" +
                        "5.修复了部分BUG。\n")
                .setCancelable(false)
                .setPositiveButton("我知道了!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();  //关闭对话框
                    }
                }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toast, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rule:
                toastRule();
                break;
            case R.id.contact:
                toastContact();
                break;
            case R.id.restart:  //重新开始游戏
                mGetScore.setText("0");
                List<MaxScore> maxScoreList = DataSupport.findAll(MaxScore.class);
                //如果最高分比数据库中的最高分大，那么就更新
                if (MainActivity.getMainActivity().getMax_score()
                        > maxScoreList.get(0).getMaxscore()) {
                    MaxScore maxScore = new MaxScore();
                    maxScore.setMaxscore(MainActivity.getMainActivity().getMax_score());
                    maxScore.updateAll("id = ?", "1");
                }
                GameView g = (GameView) findViewById(R.id.game_view);
                g.startGame();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 自定义Toast
     */
    public void toastRule() {
        View view = LayoutInflater.from(this).inflate(R.layout.toast_rule, null);
        Toast toast = new Toast(this);
        toast.setView(view);
        toast.setGravity(Gravity.TOP, 0, 200);
        toast.show();
    }

    public void toastContact() {
        View view = LayoutInflater.from(this).inflate(R.layout.toast_contact, null);
        Toast toast = new Toast(this);
        toast.setView(view);
        toast.setGravity(Gravity.TOP, 0, 200);
        toast.show();
    }

    /**
     * 获取最高分
     */
    public int getMax_score() {
        return max_score;
    }

    /**
     * 获取当前分数
     */
    public int getScore() {
        return score;
    }

    /**
     * 清除当前分数
     */
    public void clearScore() {
        score = 0;
    }

    /**
     * 添加分数
     */
    public void addScore(int num) {
        score += num;
    }

    /**
     * 显示分数
     */
    public void showScore() {
        mGetScore.setText(score + "");
    }

    /**
     * 显示最高分
     */
    public void showMaxScore() {
        mMaxScore.setText(max_score + "");
    }

    /**
     * 添加最高分
     */
    public void setMax_score(int max_score) {
        this.max_score = max_score;
    }

    /**
     * 退出
     */
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //按下了退出键
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(mainActivity, "再按100次退出程序!", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
