package com.wz.forkjoin;

import java.util.concurrent.RecursiveTask;

import static com.wz.forkjoin.WordCounter.occurrencesCount;

/**
 * DocumentSearchTask
 * wangzhen23
 * 2019/7/17.
 */
public class DocumentSearchTask extends RecursiveTask<Long> {
    private final Document document;
    private final String searchedWord;

    DocumentSearchTask(Document document, String searchedWord) {
        super();
        this.document = document;
        this.searchedWord = searchedWord;
    }

    @Override
    protected Long compute() {
        System.out.println(System.currentTimeMillis() + "--------------" + Thread.currentThread().getName());
        return occurrencesCount(document, searchedWord);
    }
}
