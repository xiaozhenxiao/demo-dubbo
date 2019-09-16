package com.wz.forkjoin;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Folder
 * wangzhen23
 * 2019/7/17.
 */
public class Folder {
        private final List<Folder> subFolders;
        private final List<Document> documents;

        Folder(List<Folder> subFolders, List<Document> documents) {
            this.subFolders = subFolders;
            this.documents = documents;
        }

        List<Folder> getSubFolders() {
            return this.subFolders;
        }

        List<Document> getDocuments() {
            return this.documents;
        }

        static Folder fromDirectory(File dir) throws IOException {
            List<Document> documents = new LinkedList<>();
            List<Folder> subFolders = new LinkedList<>();
            for (File entry : dir.listFiles()) {
                if (entry.isDirectory()) {
                    subFolders.add(Folder.fromDirectory(entry));
                } else {
                    documents.add(Document.fromFile(entry));
                }
            }
            return new Folder(subFolders, documents);
        }
}
