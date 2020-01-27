package memory.cacheMappingStrategy;

import memory.Cache;
import memory.Memory;
import transformer.Transformer;

public class AssociativeMapping extends MappingStrategy {  // 全相联映射

    private int start=0;
    private int end = Cache.CACHE_SIZE_B/Cache.LINE_SIZE_B;

    /**
     * @param blockNO 内存数据块的块号
     * @return cache数据块号 22-bits  [前22位有效]
     */
    @Override
    public char[] getTag(int blockNO) {
        Transformer trans = new Transformer();
        char[] tag = trans.intToBinary(String.valueOf(blockNO)).substring(10).toCharArray();

        return tag;
    }

    @Override
    public int map(int blockNO) {
        char[] tag = getTag(blockNO);

        return replacementStrategy.isHit(start, end, tag);
    }

    @Override
    public int writeCache(int blockNO) {
        String eip = new Transformer().intToBinary(String.valueOf(blockNO)).substring(10)+"0000000000";
        char[] data = Memory.getMemory().read(eip, Cache.LINE_SIZE_B);
        char[] tag = getTag(blockNO);        

        return replacementStrategy.writeCache(start, end, tag, data);
    }
}
