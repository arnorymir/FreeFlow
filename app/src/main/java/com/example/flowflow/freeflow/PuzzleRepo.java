package com.example.flowflow.freeflow;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 * Created by arnorymir on 20/09/14.
 */
public class PuzzleRepo {

    public List<Puzzle> mPuzzles;

    ///
    private static PuzzleRepo mInstance = new PuzzleRepo();

    public static PuzzleRepo getInstance() {
        return mInstance;
    }

    private PuzzleRepo(


    ) {
    }

}
