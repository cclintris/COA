package memory.cacheMappingStrategy;

import java.util.Arrays;

import memory.Cache;
import memory.Memory;
import transformer.Transformer;

/**
 * 直接映射 12位标记 + 10位行号 + 10位块内地址
 */
public class DirectMapping extends MappingStrategy{

    /**
     * @param blockNO 内存数据块的块号
     * @return cache数据块号 22-bits  [前12位有效]
     */
    @Override
    public char[] getTag(int blockNO) {
        Transformer trans = new Transformer();
        char[] tag = trans.intToBinary(String.valueOf(blockNO)).substring(10).toCharArray();

        for(int i=12; i<22; i++) {
            tag[i]='0';
        }

        return tag;
    }

    public int getLine(int blockNO) {
        Transformer trans = new Transformer();
        String line = trans.intToBinary(String.valueOf(blockNO)).substring(22);
        int deciline = Integer.parseInt(trans.binaryToInt(line));

        return deciline;
    }


    /**
     * 根据内存地址找到对应的行是否命中，直接映射不需要用到替换策略
     * @param blockNO
     * @return -1 表示未命中
     */
    // 22位塊號 
    // 取行號lineNO  找到cache的lineNO行  判定12位塊號是否相同 ? return lineNO : -1
    @Override
    public int map(int blockNO) {
        char[] tag = getTag(blockNO);
        int lineNO = getLine(blockNO);
        char[] tagInline = Cache.getCache().tagInLine(lineNO);

        if(Arrays.equals(tag, tagInline)) {
            return lineNO;
        }
 
        return -1;
    }

    /**
     * 在未命中情况下重写cache，直接映射不需要用到替换策略
     * @param blockNO
     * @return
     */
    @Override
    public int writeCache(int blockNO) {
        ///int lineNO = getLine(blockNO);
        int lineNO = blockNO % (Cache.CACHE_SIZE_B/Cache.LINE_SIZE_B);
        //String eip = new Transformer().intToBinary(String.valueOf(blockNO)).substring(10)+"0000000000";
        String eip = new Transformer().intToBinary(String.valueOf(blockNO)).substring(10)+"0000000000";
        char[] data = Memory.getMemory().read(eip, Cache.LINE_SIZE_B);
        Cache.getCache().writeCache(data, lineNO, getTag(blockNO));       
        return lineNO;
    }
}
