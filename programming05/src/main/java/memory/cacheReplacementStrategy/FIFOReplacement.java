package memory.cacheReplacementStrategy;

import java.util.*;
import memory.Cache;

/**
 * 先进先出算法
 */
public class FIFOReplacement extends ReplacementStrategy {

    @Override
    public int isHit(int start, int end, char[] addrTag) {
        Cache cache = Cache.getCache();
        boolean flag = false;
        int targetline=start;
        for(int lineNO = start; lineNO<end; lineNO++) {
            if(!cache.getValidBit(lineNO)) {
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

    @Override
    public int writeCache(int start, int end, char[] addrTag, char[] input) {
        Long minTimeStamp = System.currentTimeMillis();  //java System模塊內置的timestamp
        int targetline = start;
        for(int lineNO=start; lineNO<end; lineNO++) {
            if(!Cache.getCache().getValidBit(lineNO)) {
                Cache.getCache().writeCache(input, lineNO, addrTag);
                return lineNO;
            }
            Long timestamp = Cache.getCache().getTimestamp(lineNO);
            if(timestamp < minTimeStamp) {
                targetline = lineNO;
                minTimeStamp = Cache.getCache().getTimestamp(targetline);
            }
        }

        Cache.getCache().writeCache(input, targetline, addrTag);
        return targetline;
    }

}
