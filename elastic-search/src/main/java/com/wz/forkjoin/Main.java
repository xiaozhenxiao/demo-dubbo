package com.wz.forkjoin;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

/**
 * Main
 * wangzhen23
 * 2019/7/18.
 */
public class Main {
    private static final ForkJoinPool forkJoinPool = new ForkJoinPool();

    private static Long countOccurrencesInParallel(Folder folder, String searchedWord) {
        return forkJoinPool.invoke(new FolderSearchTask(folder, searchedWord));
    }

    public static void main(String[] args) throws IOException {
        WordCounter wordCounter = new WordCounter();
        Folder folder = Folder.fromDirectory(new File("D:/forkjoin"));
//        System.out.println(wordCounter.countOccurrencesOnSingleThread(folder,args[1]));

        System.out.println(countOccurrencesInParallel(folder, "Slack"));
    }
}
