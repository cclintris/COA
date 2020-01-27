package memory.cacheMappingStrategy;

import memory.Cache;
import memory.Memory;
import transformer.Transformer;

/**
 * 4路-组相连映射 n=4,   14位标记 + 8位组号 + 10位块内地址
 * 256个组，每个组4行
 */
public class SetAssociativeMapping extends MappingStrategy{

    /**
     *
     * @param blockNO 内存数据块的块号
     * @return cache数据块号 22-bits  [前14位有效]
     */
    @Override
    public char[] getTag(int blockNO) {
        Transformer trans = new Transformer();
        char[] tag = trans.intToBinary(String.valueOf(blockNO)).substring(10).toCharArray();
        for(int i=14; i<22; i++) {
            tag[i]='0';
        }

        return tag;
    }

    /**
     *
     * @param blockNO 目标数据内存地址前22位int表示
     * @return -1 表示未命中
     */
    @Override
    public int map(int blockNO) {
        char[] tag = getTag(blockNO);
        int groupNO = getGroupNO(blockNO);

        //return replacementStrategy.isHit(groupNO*4, (groupNO*4)+4, tag);
        return replacementStrategy.isHit(groupNO, groupNO+4, tag);
    }

    public int getGroupNO(int blockNO) {
        //return blockNO % Integer.parseInt(new Transformer().binaryToInt("100000000"));
        return (blockNO % Integer.parseInt(new Transformer().binaryToInt("100000000")))*4;
    }

    @Override
    public int writeCache(int blockNO) {
        String eip = new Transformer().intToBinary(String.valueOf(blockNO)).substring(10)+"0000000000";
        char[] data = Memory.getMemory().read(eip, Cache.LINE_SIZE_B);
        char[] tag = getTag(blockNO);
        int groupNO = getGroupNO(blockNO);
        
        return replacementStrategy.writeCache(groupNO, groupNO+4, tag, data);
    }
}
