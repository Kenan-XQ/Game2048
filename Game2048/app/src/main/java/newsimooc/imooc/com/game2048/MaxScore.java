package newsimooc.imooc.com.game2048;

import org.litepal.crud.DataSupport;

/**
 * Created by user on 2017/10/28.
 */

public class MaxScore extends DataSupport{
    private int maxscore;

    public void setMaxscore(int maxscore) {
        this.maxscore = maxscore;
    }

    public int getMaxscore() {
        return maxscore;
    }

}
