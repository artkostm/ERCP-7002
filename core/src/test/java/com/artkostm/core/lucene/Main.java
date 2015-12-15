package com.artkostm.core.lucene;

import java.io.File;
import java.io.FileReader;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        final long start = System.currentTimeMillis();
//        Indexer.start("C:\\Users\\Artsiom_Chuiko\\Desktop\\filesForTest");
//        Indexer.close();
        System.out.println("Time: " + (System.currentTimeMillis() - start) + " ms");
        String querystr = "filename: 000*";
        Query q = new QueryParser("filename", Indexer.analyzer).parse(querystr);
        int hitsPerPage = 100;
        IndexReader reader = DirectoryReader.open(Indexer.directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(q, hitsPerPage);
        ScoreDoc[] hits = docs.scoreDocs;
        System.out.println("Found " + hits.length + " hits.");
        for(int i = 0; i < hits.length; ++i) 
        {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println(d.get("filename") + "----------------------------" + d.get("filepath"));
            printContent(d.get("filepath"));
        }
    }
    
    public static void printContent(final String filepath) throws Exception
    {
        File file = new File(filepath);
        FileReader reader = new FileReader(file);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; (i = reader.read()) > -1; )
        {
            sb.append((char)i);
        }
        reader.close();
        System.out.println(sb);
    }
}
