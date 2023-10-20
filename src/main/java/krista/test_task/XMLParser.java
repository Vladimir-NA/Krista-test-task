package krista.test_task;

import krista.test_task.entities.Catalog;
import krista.test_task.entities.Plant;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class XMLParser {

    public static Catalog parseXML(File file){
        Catalog catalog = null;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            Element catalogElement = doc.getDocumentElement();
            String uuid = catalogElement.getAttribute("uuid");
            Timestamp date = strDateToTimestamp(catalogElement.getAttribute("date"));
            if(date == null) return null;
            String company = catalogElement.getAttribute("company");
            NodeList plantList = catalogElement.getElementsByTagName("PLANT");

            ArrayList<Plant> plants = new ArrayList<>();
            for (int i = 0; i < plantList.getLength(); i++) {
                Node plantNode = plantList.item(i);

                if (plantNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element plantElement = (Element) plantNode;
                    try{
                        String common = plantElement.getElementsByTagName("COMMON").item(0).getTextContent();
                        String botanical = plantElement.getElementsByTagName("BOTANICAL").item(0).getTextContent();
                        Integer zone = Integer.valueOf(plantElement.getElementsByTagName("ZONE").item(0).getTextContent());
                        String light = plantElement.getElementsByTagName("LIGHT").item(0).getTextContent();
                        Double price = strPriceToDouble(plantElement.getElementsByTagName("PRICE").item(0).getTextContent());
                        Integer availability = Integer.valueOf(plantElement.getElementsByTagName("AVAILABILITY").item(0).getTextContent());

                        plants.add(new Plant(common, botanical, zone, light, price, availability));
                    }catch (Exception e){}
                }
            }
            catalog = new Catalog(uuid, date, company, plants);

        } catch (Exception e) {
            //e.printStackTrace();
        }

        return catalog;
    }

    private static Timestamp strDateToTimestamp(String dateStr){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date date = dateFormat.parse(dateStr);
            return new Timestamp(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Double strPriceToDouble(String price){
        String fixPrice = price.replace("$", "");
        return Double.parseDouble(fixPrice);
    }

}
