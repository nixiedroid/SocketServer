package com.nixiedroid.rpc;

public class Context {

    private Context() {




    }




    public static final Context i() {
        return Holder.getInstance();
    }

    private static class Holder {
        private static final Context INSTANCE = new Context();

        private static Context getInstance() {
            return INSTANCE;
        }
    }
}
