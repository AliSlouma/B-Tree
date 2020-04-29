package eg.edu.alexu.csd.filestructure.btree;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.management.RuntimeErrorException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SearchEngine implements ISearchEngine {

    ArrayList <ISearchResult> searchResults = new ArrayList<>();
    IBTree <String , String> bTree;


    public SearchEngine(int t) {

        bTree = new BTree<>(t);
    }

    public void indexWebPage(String filePath) {
        if(filePath == null || filePath.equals(""))
            throw new RuntimeErrorException(new Error());


        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document document = null;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(new File(filePath));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeErrorException(new Error());
        }

        document.getDocumentElement().normalize();
        Element root = document.getDocumentElement();

        NodeList nList = document.getElementsByTagName("doc");

        for (int temp = 0; temp < nList.getLength(); temp++)
        {
            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element eElement = (Element) node;
                bTree.insert(eElement.getAttribute("id"), eElement.getTextContent());

            }
        }
    }

    @Override
    public void indexDirectory(String directoryPath) {
        File folder = new File("res");
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
               // System.out.println(file.getName());
                indexWebPage(directoryPath+"\\"+file.getName());
            }
        }
    }

    @Override
    public void deleteWebPage(String filePath) {
        if(filePath == null || filePath.equals(""))
            throw new RuntimeErrorException(new Error());

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document document = null;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(new File(filePath));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeErrorException(new Error());
        }
        document.getDocumentElement().normalize();
        Element root = document.getDocumentElement();
        NodeList nList = document.getElementsByTagName("doc");

        for (int temp = 0; temp < nList.getLength(); temp++)
        {
            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element eElement = (Element) node;
                bTree.delete(eElement.getAttribute("id"));
            }
        }
    }

    @Override
    public List<ISearchResult> searchByWordWithRanking(String word){
        if(word == null)
            throw new RuntimeErrorException(new Error());
        if(word.equals("")){
            return new ArrayList<>();
        }
        List<String> keys = new ArrayList<>();
        List<String> vals = new ArrayList<>();
        traverseTreeInorder(bTree.getRoot(), keys, vals);
        for(int i=0;i<keys.size();i++){
            String in = vals.get(i);
            in = in.toLowerCase();
            in = in.replace('\n',' ');
            word =word.toLowerCase();
            int j = 0;
            Pattern p = Pattern.compile("\\s+"+word);
            Matcher m = p.matcher( in );
            while (m.find()) {
                j++;
            }
            if(j!=0){
                searchResults.add(new SearchResult(keys.get(i),j));
            }


        }
        return searchResults;
    }
    private void traverseTreeInorder(IBTreeNode<String, String> node, List<String> keys, List<String> vals) {
        int i;
        for (i = 0; i < node.getNumOfKeys(); i++)
        {

            if (!node.isLeaf())
                traverseTreeInorder(node.getChildren().get(i), keys, vals);
            keys.add(node.getKeys().get(i));
            vals.add(node.getValues().get(i));
        }
        if (!node.isLeaf())
            traverseTreeInorder(node.getChildren().get(i), keys, vals);
    }

    @Override
    public List<ISearchResult> searchByMultipleWordWithRanking(String sentence) {
        if(sentence == null)
            throw new RuntimeErrorException(new Error());
        if(sentence.equals("")){
            return new ArrayList<>();
        }
        ArrayList<ISearchResult> arrayList = new ArrayList<>();
        String[] arrOfStr = sentence.split("\\s+");

        arrayList = (ArrayList<ISearchResult>) searchByWordWithRanking(arrOfStr[0]);
        for(int i = 1; i < arrOfStr.length; i++) {
            List<ISearchResult> list = searchByWordWithRanking(arrOfStr[i]);
            List<ISearchResult> tempAns = new ArrayList<>();
            for(ISearchResult list1 : arrayList) {
                for(ISearchResult list2 : list) {
                    if(list1.getId().equals(list2.getId())) {
                        tempAns.add(new SearchResult(list1.getId(), Math.min(list1.getRank(), list2.getRank())));
                    }
                }
            }
            arrayList = (ArrayList<ISearchResult>) tempAns;
        }


        return arrayList;
    }
}
