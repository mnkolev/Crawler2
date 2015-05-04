/**
 * Created by Mark on 10/21/2014.
 */

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

//import org.w3c.dom.Node;


public class Main {

    //public static DB db = new DB();
    public static Scanner kb=new Scanner(System.in);
    public static void main(String[] args) throws SQLException, IOException {
        int count=0;
        int i=1;

        System.out.println("Input job title");
        String title=kb.next();
        System.out.println("Input location");
        String location=kb.next();

        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            //for(int i=0;i<)

            //here I begin attempting connection to URL
            String urlString = ("http://api.indeed.com/ads/apisearch?publisher=8976072631363358&q=" + title + "&l=" + location + "%2C+va&sort=&radius=&st=&jt=&start="+i+"&limit=25&fromage=10&filter=&latlong=1&co=us&chnl=&userip=1.2.3.4&useragent=Google/%2F4.0%28Chrome%29&v=2");
            URL url = new URL(urlString);
            Document doc = dBuilder.parse(String.valueOf(url));

            processPage("http://www.indeed.com/");
            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            int total = Integer.parseInt((doc.getElementsByTagName("totalresults").item(0).getTextContent()));
            System.out.println("Total # of Jobs : " + total);

            //CREATING output XML here
            DocumentBuilderFactory docFactory=DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder=docFactory.newDocumentBuilder();
            Document expDoc = docBuilder.newDocument();

            for (i = 1; i <=total;) {


                NodeList nList = doc.getElementsByTagName("result");


                System.out.println("----------------------------");

                for (int temp = 0; temp < nList.getLength(); temp++) {

                    Node nNode = nList.item(temp);

                    System.out.println("\nCurrent Element :" + nNode.getNodeName());
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                        Element eElement = (Element) nNode;
                        System.out.println(count);

                        //here is where I create all the variables that I need from the indeed API
                        String JobTitle=eElement.getElementsByTagName("jobtitle").item(0).getTextContent();
                        String JobID=eElement.getElementsByTagName("jobkey").item(0).getTextContent();
                        String Company=eElement.getElementsByTagName("company").item(0).getTextContent();
                        String City=eElement.getElementsByTagName("city").item(0).getTextContent();
                        String Latitude=eElement.getElementsByTagName("latitude").item(0).getTextContent();
                        String Longitude=eElement.getElementsByTagName("longitude").item(0).getTextContent();
                        String JobInfo=eElement.getElementsByTagName("snippet").item(0).getTextContent();
                        String URL=eElement.getElementsByTagName("url").item(0).getTextContent();

                        //here is where I print out the variables
                        System.out.println("Job Title : " +JobTitle);
                        System.out.println("Job ID : " + JobID);
                        System.out.println("Company : " + Company);
                        System.out.println("City : " + City);
                        System.out.println("Latitude : " + Latitude);
                        System.out.println("Longitude : " + Longitude);
                        System.out.println("Job Description : " + JobInfo);
                        System.out.println("URL : " + URL);

                        //here is where I create the xml. Will transfer to a whole new method soon (version 2.0)
                         //DocumentBuilderFactory docFactory=DocumentBuilderFactory.newInstance();
                         //DocumentBuilder docBuilder=docFactory.newDocumentBuilder();

                        ArrayList<Element> users = null;
                        //create the root element here
                        Element rootElement = expDoc.createElement("response");
                        expDoc.appendChild(rootElement);

                        //result element created here
                        Element resultElement = expDoc.createElement("result");
                        rootElement.appendChild(resultElement);

                        Attr attr = expDoc.createAttribute("id");
                        attr.setValue(String.valueOf(count));
                        resultElement.setAttributeNode(attr);

                        //users.get(i).appendChild(resultElement);
                        //resultElement.appendChild((users.get(i)));

                        //all elements that are under result
                        Element jobtitleXML = expDoc.createElement("jobtitle");
                        jobtitleXML.appendChild(expDoc.createTextNode(JobTitle));
                        resultElement.appendChild(jobtitleXML);

                        Element jobkeyXML = expDoc.createElement("jobkey");
                        jobkeyXML.appendChild(expDoc.createTextNode(JobID));
                        resultElement.appendChild(jobkeyXML);

                        Element companyXML = expDoc.createElement("company");
                        companyXML.appendChild(expDoc.createTextNode(Company));
                        resultElement.appendChild(companyXML);

                        Element cityXML = expDoc.createElement("city");
                        cityXML.appendChild(expDoc.createTextNode(City));
                        resultElement.appendChild(cityXML);

                        Element latitudeXML = expDoc.createElement("latitude");
                        latitudeXML.appendChild(expDoc.createTextNode(Latitude));
                        resultElement.appendChild(latitudeXML);

                        Element longitudeXML = expDoc.createElement("longitude");
                        longitudeXML.appendChild(expDoc.createTextNode(Longitude));
                        resultElement.appendChild(longitudeXML);

                        Element snippetXML = expDoc.createElement("snippet");
                        snippetXML.appendChild(expDoc.createTextNode(JobInfo));
                        resultElement.appendChild(snippetXML);

                        Element urlXML = expDoc.createElement("url");
                        urlXML.appendChild(expDoc.createTextNode(URL));
                        resultElement.appendChild(urlXML);

                        //here is where I write to xml file
                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        Transformer transformer = transformerFactory.newTransformer();
                        DOMSource source = new DOMSource(expDoc);
                        StreamResult result = new StreamResult(new File("C:\\Users\\Mark\\IdeaProjects\\Crawler2\\output.xml"));

                        transformer.transform(source, result);

                        count++;


                    }

                }

                i +=25;
                String urlString2 = ("http://api.indeed.com/ads/apisearch?publisher=8976072631363358&q=" + title + "&l=" + location + "%2C+va&sort=&radius=&st=&jt=&start="+i+"&limit=25&fromage=10&filter=&latlong=1&co=us&chnl=&userip=1.2.3.4&useragent=Google/%2F4.0%28Chrome%29&v=2");
                URL url2 = new URL(urlString2);
                doc = dBuilder.parse(String.valueOf(url2));
            }
            }catch(Exception e){
                e.printStackTrace();
            }

        System.out.println("Total number of jobs printed out:"+count);
    }



    public static void processPage(String URL) throws SQLException, IOException {
        //check if the given URL is already in database
        String sql = "select * from Record where URL = '" + URL + "'";
        //ResultSet rs = db.runSql(sql);
        //if (rs.next()) {

        //} else {
            //store the URL to database to avoid parsing again (SQL STYLE)
            //sql = "INSERT INTO  `Crawler`.`Record` " + "(`URL`) VALUES " + "(?);";
            //PreparedStatement stmt = db.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
           // stmt.setString(1, URL);
            //stmt.execute();

            //get useful information

            //String urlTest="http://www.indeed.com/jobs?q="+title+"=&l"+location+"%2C+VA";
            //Document doc = Jsoup.connect(urlTest).get();
                //Document doc=Jsoup.parseBodyFragment("http://www.indeed.com/jobs?q=developer&l=Richmond%2C+VA");
            //Element body = doc.body();
            //if(doc.text().contains("developer")){
            //System.out.println(body);

            // Here is where I will be saving the data into a text file named jobs.txt
            try {

                //String content = String.valueOf(body);

                File file = new File("/Users/Mark/IdeaProjects/Crawler/src/main/java/jobs.txt");

                // if file doesnt exists, then create it
                if (!file.exists()) {
                    System.out.println("File Doesn't exist");
                    file.createNewFile();
                }

                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                //bw.write(content);
                bw.close();

                System.out.println("Done");

            } catch (IOException e) {
                e.printStackTrace();
            }

            //}

            //get all links and recursively call the processPage method
            //Elements questions = doc.select("a[href]");
            //for (Element link : questions) {
                //if(link.attr("href").contains("mit.edu"))
                //processPage(link.attr("abs:href"));
                //}
                String html = "http://www.indeed.com/jobs?q=developer&l=Richmond%2C+VA";
                //Document doc2 = Jsoup.parseBodyFragment(html);
                //Element body2 = doc2.body();

                //System.out.println(body2);
            //}



        }
    }
//}

