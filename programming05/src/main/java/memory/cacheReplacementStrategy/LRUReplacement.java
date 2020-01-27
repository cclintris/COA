package memory.cacheReplacementStrategy;

import java.util.*;
import memory.Cache;

/**
 * 最近最少用算法
 */
public class LRUReplacement extends ReplacementStrategy {

    /**
     *
     * @param start 起始位置
     * @param end 结束位置 闭区间
     */
    @Override
    public int isHit(int start, int end, char[] addrTag) {
        Cache cache = Cache.getCache();
        boolean flag = false;
        int targetline=start;
        for(int lineNO = start; lineNO<end; lineNO++) {
            if(!Cache.getCache().getValidBit(lineNO)) {
                continue;
            }
            char[] tagINline = cache.tagInLine(lineNO);
            if(Arrays.equals(tagINline, addrTag)) {
                targetline = lineNO;
                //cache.timeStampincrement(lineNO);
                flag = true;
                break;
            }
        }
    
        // 若都沒命中 return -1
        if(!flag) {
            return -1;
        }
        
        return targetline;
    }


    /**
     * 找到最小时间戳的行，替换
     * @param start 起始行
     * @param end 结束行 闭区间
     * @param addrTag tag
     * @param input  数据
     * @return
     */
    @Override
    public int writeCache(int start, int end, char[] addrTag, char[] input) {
        Long least = Cache.getCache().getTimestamp(start);
        int targetline = start;
        for(int lineNO=start; lineNO<end; lineNO++) {
            if(!Cache.getCache().getValidBit(lineNO)) {
                Cache.getCache().writeCache(input, lineNO, addrTag);
                return lineNO;
            }
            Long timestamp = Cache.getCache().getTimestamp(lineNO);
            if(timestamp < least) {
                targetline = lineNO;
                least = Cache.getCache().getTimestamp(targetline);
            }
        }

        Cache.getCache().writeCache(input, targetline, addrTag);
        return targetline;
    }

}





























