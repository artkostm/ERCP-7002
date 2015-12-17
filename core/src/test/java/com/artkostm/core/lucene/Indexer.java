package com.artkostm.core.lucene;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Indexer
{
    protected static final IndexWriter writer = createWriter();
    
    private static final FieldType keywordFieldType = new FieldType();
    
    protected static Analyzer analyzer;
    protected static Directory directory;

    public static void start(final String dirName) throws Exception
    {
        final File root = new File(dirName);
        if (!root.exists()) 
        {
            System.err.println("Directory do not exist");
            return;
        }
        
        if (root.isDirectory())
        {
            recursiveRead(root);
        }
        else 
        {
            writer.addDocument(getDocument(root));
        }
    }
    
    private static void recursiveRead(final File root) throws IOException
    {
        final File[] files = root.listFiles();
        for (File file : files)
        {
            if (file.isDirectory())
            {
                recursiveRead(file);
            }
            else
            {
                writer.addDocument(getDocument(file));
                //System.out.println(file.getName());
            }
        }
    }
    
    private static Document getDocument(final File file) throws IOException
    {
        final Document document = new Document();

        final Field contentField = new Field("content", new FileReader(file), keywordFieldType);
        
        document.add(contentField);
        document.add(new StringField("filename", file.getName(), Field.Store.YES));
        document.add(new StringField("filepath", file.getCanonicalPath(), Field.Store.YES));

        return document;
     }   
    
    private static IndexWriter createWriter()
    {
        try
        {
            analyzer = new StandardAnalyzer();
            final IndexWriterConfig config = new IndexWriterConfig(analyzer);
            directory = FSDirectory.open(new File("src/test/resources/index").toPath());
            final IndexWriter writer = new IndexWriter(directory, config);
            return writer;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void close()
    {
        if (writer != null)
        {
            try
            {
                writer.close();
            }
            catch (IOException e)
            {}
        }
    }
    
    
//    private static final FileFilter filter = new FileFilter()
//    {
//        @Override
//        public boolean accept(File pathname)
//        {
//            return pathname.getName().toLowerCase().endsWith(".txt")  ||
//                    pathname.getName().toLowerCase().endsWith(".csv") ||
//                    pathname.getName().toLowerCase().endsWith(".log") ||
//                    pathname.getName().toLowerCase().endsWith(".xml") ||
//                    pathname.getName().toLowerCase().endsWith(".htm") ||
//                    pathname.getName().toLowerCase().endsWith(".js")  ||
//                    pathname.getName().toLowerCase().endsWith(".css") ||
//                    pathname.getName().toLowerCase().endsWith("_0");
//        }
//    };
    
    static
    {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                if (writer != null)
                {
                    try
                    {
                        writer.close();
                    }
                    catch (IOException e)
                    {}
                }
            }
        }));
        
//        keywordFieldType.setStored(true);
//        keywordFieldType.setIndexOptions(IndexOptions.DOCS);
//        keywordFieldType.setTokenized(false);
    }
}
