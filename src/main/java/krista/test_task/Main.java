package krista.test_task;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import krista.test_task.entities.Catalog;

public class Main {

    public static void main(String[] args){
        if(DBWorker.checkConnection()) return;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите путь до файла или каталога с файлами: ");
        String path = scanner.nextLine();

        ArrayList<File> filesList = FileUtils.getFilesList(path);

        if(filesList == null){
            System.out.println("Файл или каталог не найден");
            DBWorker.closeConnection();
            return;
        }

        for(File f: filesList){
            Catalog catalog = XMLParser.parseXML(f);
            if(catalog!=null){
                if(DBWorker.insertCatalog(catalog)){
                    System.out.println("Каталог " + catalog.uuid + " успешно добавлен в БД.");
                }
            }
        }

        DBWorker.closeConnection();
    }
}
