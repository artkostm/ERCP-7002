package com.artkostm.core.lucene;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import com.artkostm.core.web.controller.Controller;
import com.artkostm.core.web.controller.Result;
import com.artkostm.core.web.controller.converter.Json;

public class LuceneController extends Controller
{
    //HTTP Method: GET
    //To search docs
    //returns json object with finded docs
    public static Result index() throws Exception
    {
        final String index = context().getPathParams().get("index");
        final String queryStr = context().getQueryParams().get("query").get(0);
        final Query q = new QueryParser(index, Indexer.analyzer).parse(queryStr);
        final int hitsPerPage = 100;
        final IndexReader reader = DirectoryReader.open(Indexer.directory);
        final IndexSearcher searcher = new IndexSearcher(reader);
        final TopDocs docs = searcher.search(q, hitsPerPage);
        final ScoreDoc[] hits = docs.scoreDocs;
        System.out.println("Found " + hits.length + " hits.");
        final List<Doc> docList = new ArrayList<>(hits.length);
        for(int i = 0; i < hits.length; ++i) 
        {
            final int docId = hits[i].doc;
            final Document d = searcher.doc(docId);
            final Doc doc = new Doc(docId);
            doc.setFileName(d.get("filename"));
            doc.setFilePath(d.get("filepath"));
            doc.setContent(printContent(d.get("filepath")));
            docList.add(doc);
        }
        return ok(Json.toJson(docList).toString()).asJson();
    }
    
    public static Result createIndex()
    {
        try 
        {
            Indexer.start("src/test/resources");
        } 
        catch (Exception e) 
        {
            return internalServerError(e.getMessage().getBytes());
        }
        finally
        {
            Indexer.close();
        }
        return ok();
    }
    
    public static class Doc
    {
        private final Integer id;
        private String fileName;
        private String filePath;
        private String content;
        
        public Doc(Integer id) 
        {
            this.id = id;
        }
        
        public String getFileName() 
        {
            return fileName;
        }
        
        public void setFileName(String fileName) 
        {
            this.fileName = fileName;
        }
        
        public String getFilePath() 
        {
            return filePath;
        }
        
        public void setFilePath(String filePath) 
        {
            this.filePath = filePath;
        }
        
        public String getContent() 
        {
            return content;
        }
        
        public void setContent(String content) 
        {
            this.content = content;
        }

        public Integer getId() 
        {
            return id;
        }
    }
    
    public static String printContent(final String filepath) throws Exception
    {
        File file = new File(filepath);
        FileReader reader = new FileReader(file);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; (i = reader.read()) > -1; )
        {
            sb.append((char)i);
        }
        reader.close();
        return sb.toString();
    }
}
