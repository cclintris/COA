package memory.cacheReplacementStrategy;

import memory.Cache;

import java.util.*;

/**
 * 最近不经常使用算法
 */
public class LFUReplacement extends ReplacementStrategy {

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
                //cache.visitedIncrement(lineNO);
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
        int min = Cache.getCache().getVisited(start);
        int targetlineNO = start;
        for(int lineNO = start; lineNO<end; lineNO++) {
            if(!Cache.getCache().getValidBit(lineNO)) {
                Cache.getCache().writeCache(input, lineNO, addrTag);
                return lineNO;
            }
            int visited = Cache.getCache().getVisited(lineNO); 
            if(visited < min) {
                targetlineNO = lineNO;
                min = Cache.getCache().getVisited(targetlineNO);
            }
        }

        Cache.getCache().writeCache(input, targetlineNO, addrTag);
        return targetlineNO;
    }

}
