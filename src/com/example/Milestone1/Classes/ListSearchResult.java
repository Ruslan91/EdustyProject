package com.example.Milestone1.Classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Руслан on 21.02.14.
 */
public class ListSearchResult {
    public static void main(String[] args)  {
        List<SearchResult> resultList = new ArrayList<SearchResult>();
        resultList.add(new UserResult());
        resultList.add(new GroupResult());
    }
}
