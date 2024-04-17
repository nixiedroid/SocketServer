package com.nixiedroid.rpc.data.enums;

import com.nixiedroid.rpc.data.BytePackable;

import java.util.*;

public class PacketFlagsHolder implements BytePackable<PacketFlagsHolder> {

    private static EnumMap<PacketFlag,Integer> map = new EnumMap<PacketFlag,Integer>(PacketFlag.class);
    static {
        map.put(PacketFlag.FIRSTFRAG,1);
        map.put(PacketFlag.LASTFRAG,2); //0x02
        map.put(PacketFlag.PFC_PENDING_CANCEL,4); //0x04
        map.put(PacketFlag.RESERVED,8); //0x08
        map.put(PacketFlag.MULTIPLEX,16); //0x10
        map.put(PacketFlag.DIDNOTEXECUTE,32); //0x20
        map.put(PacketFlag.MAYBE,64); //0x40
        map.put(PacketFlag.OBJECTUUID,128); //0x80
    }

     public PacketFlagsHolder(PacketFlag... flags){
        this.flags.addAll(new ArrayList<PacketFlag>(Arrays.asList(flags)));
     }

     private Set<PacketFlag> flags = new HashSet<PacketFlag>();


    public PacketFlagsHolder deserialize(byte[] data,int start) {
        Set<PacketFlag> flags = new HashSet<PacketFlag>();
        for (PacketFlag fl: PacketFlag.values()) {
            checkAndAdd(data[start],fl,flags);
        }
        return this;
    }
    public void add(PacketFlag flag){
        flags.add(flag);
    }
    public void addAll(Set<PacketFlag> flags){
        this.flags.addAll(flags);
    }
    public Set<PacketFlag> get(){
        return flags;
    }
    public boolean checkFlag(PacketFlag flag){
        return flags.contains(flag);
    }

    private void checkAndAdd(byte b,PacketFlag fl,Set<PacketFlag> flags){
         if ((b & map.get(fl)) !=0) flags.add(fl);
    }

    public byte[] serialize() {
        byte b =0;
        for (PacketFlag flag: flags ) {
            b |= map.get(flag).byteValue();
        }
        return new byte[]{b};
    }

    public int size() {
        return 1;
    }

    public static enum PacketFlag{
        FIRSTFRAG,
        LASTFRAG,
        PFC_PENDING_CANCEL,
        RESERVED,
        MULTIPLEX,
        DIDNOTEXECUTE,
        MAYBE,
        OBJECTUUID;
    }
}
