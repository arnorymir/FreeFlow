package com.example.flowflow.freeflow;
import java.util.List;

/**
 * Created by arnorymir on 18/09/14.
 */
public class Global {


    public List<Pack> mPacks;

    ///
    private static Global mInstance = new Global();

    public static Global getInstance() {
        return mInstance;
    }

    private Global() {



    }
}
