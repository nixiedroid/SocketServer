package com.nixiedroid.rpc.dynamic.stubs;
import com.nixiedroid.rpc.util.logger.Level;


public abstract class Settings {

    public abstract Level getLevel();
    public abstract int getServerPort();

    public abstract void setLevel(Level level);
    public abstract void setPort(int port);

}
